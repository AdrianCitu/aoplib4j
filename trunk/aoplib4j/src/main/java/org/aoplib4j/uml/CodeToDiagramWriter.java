/*
 *  Copyright 2008-2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.aoplib4j.uml;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
  * Writer for the <code>CODETODIAGRAM</code> project.
 * (http://www.assembla.com/wiki/show/codetodiagram).
 * 
 * A CODETODIAGRAM sequence diagram is a xml file containing 2 sections :
 * 
 * <ul>
 * <li> 
 *  the actors section
 *  <pre>
 *  &lt;actors&gt;
 *   &lt;actor id="user" type="user"&gt; User&lt;/actor&gt;
 *   &lt;actor id="view" type="boundary"&gt;View&lt;/actor&gt;
 *  &lt;/actors&gt;
 *  </pre>
 * </li>
 * <li>
 * the messages section
 * <pre>
 * &lt;messages&gt;
 *  &lt;message type="call" from="user" to="view" text="request()"/&gt;
 *  &lt;message type="call" from="view" to="controller" text="handleEvnt()"/&gt;
 *  &lt;message type="return" from="view"  to="user"  text="notifyUser()"/&gt;
 * &lt;/messages&gt;
 * </pre>
 * </li>
 * </ul>
 * It is possible to generate the uml diagram from the XML file using this url:
 * http://www.thiagomata.com/codetodiagram/svn/public/caller.php
 * 
 * @author Adrian Citu
 *
 */
public final class CodeToDiagramWriter extends DFSDiagramWriter {

    /**
     * Constant used for generate the xml diagram.
     */
    private static final String QUOTE_LT = "\"/>";


    /**
     * Constant used for generate the xml diagram.
     */
    private static final String QUOT_SPACE = "\" ";


    /**
     * Constant used for generate the xml diagram.
     */
    private static final String TO_ATTR = "to=\"";


    /**
     * Constant used for generate the xml diagram.
     */
    private static final String FROM_ATTR = "from=\"";


    /**
     * Constant used for generate the xml diagram.
     */
    private static final String TYPE_ATTR = "type=\"";
    

    /**
     * Private enumeration representing the type attribute of the message tag. 
     * @author Adrian Citu
     *
     */
    private enum MessageType {
        /**
         * the call message type.
         */
        callType, 
        /**
         * the return message type.
         */
        returnType;
    }

    /**
     * The header of the (xml) diagram file.
     */
    private static final String HEADER = "<?xml version=\"1.0\""
        + "encoding=\"UTF-8\"?>" + NEW_LINE + "<sequence>";
    
    
    /**
     * The footer of the (xml) diagram file. 
     */
    private static final String FOOTER = NEW_LINE + "</sequence>";
    

    /**
     * Buffer for the  <code>actors</code> section.
     */
    private StringBuffer actorsBuffer = 
        new StringBuffer("<actors>").append(NEW_LINE);
    
    /**
     * Set containing defined actors (represented by the full class name). 
     * ex: key=foo.Foo; value=Foo.  
     */
    private Set<String> definedActors = new HashSet<String>();
    
    /**
     * Buffer for the <code>messages</code> section.
     */
    private StringBuffer messagesBuffer = 
        new StringBuffer("<messages>").append(NEW_LINE);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter(final SequenceMethod meth) 
        throws IOException {
        
        this.actorsBuffer.append("</actors>");
        this.writeLine(this.actorsBuffer.toString());
        
        this.messagesBuffer.append("</messages>");
        this.writeLine(this.messagesBuffer.toString());
        
        this.writeLine(FOOTER);

    }

 
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader(final SequenceMethod meth) 
        throws IOException {
        this.writeLine(HEADER);

    }


    /**
     * {@inheritDoc}
     */
    @Override   
    public void writeMethodAfterChildren(final SequenceMethod meth) 
        throws IOException {
        
        //fill the messagesBuffer with the messages of type "return"     
        this.writeToMessageBuffer(meth, MessageType.returnType);
        
    }


    /**
     * @param meth the method to use for compute the actor.
     */
    private void computeActorAndMessageFromMethod(final SequenceMethod meth) {
     
        String actorIdAttr = meth.getClassName();

        //fill the actorsBuffer
        if (!this.definedActors.contains(actorIdAttr)) {
            this.definedActors.add(actorIdAttr);
            String actorTypeAttr = "system";
            String actorContent = meth.getSimpleClassName();

            // the first method of the diagram will have the type "user"
            if (meth.getParent() == null) {
                actorTypeAttr = "user";
            }

            this.actorsBuffer
                .append("<actor ")
                .append("id=\"").append(actorIdAttr).append(QUOT_SPACE)
                .append(TYPE_ATTR).append(actorTypeAttr).append("\">")
                .append(actorContent)
                .append("</actor>")
                .append(NEW_LINE);
        
        }
        
        //fill the messagesBuffer with the messages of type "call"     
        this.writeToMessageBuffer(meth, MessageType.callType);
        
    }
    
    /**
     *  Write a message to the {@link #messagesBuffer}.
     *  
     *  If the message type is a call then the line will be:
     *  <pre>
     *  &lt;message type="call" 
     *      from="meth.getParent().getClassName()" 
     *      to="meth.getClassName()" 
     *      text="meth.getMethodName()"
     *  /&gt;
     *  </pre>
     *  
     *  If the message type is a return then the to and from attributes are
     *  reversed:
     *  <pre>
     *  &lt;message type="return" 
     *      from="meth.getClassName()" 
     *      to="meth.getParent().getClassName()" 
     *      text="meth.getMethodName()"
     *  /&gt;
     *  </pre>
     * @param meth the method for compute the message.
     * @param type the message type 
     */
    private void writeToMessageBuffer(
            final SequenceMethod meth, final MessageType type) {
        
        if (meth.getParent() == null) {
            return;
        }
        
        this.messagesBuffer.append("<message ");
        
        if (MessageType.callType.equals(type)) {
            this.messagesBuffer
            .append(TYPE_ATTR).append("call").append(QUOT_SPACE)
            .append(FROM_ATTR).append(meth.getParent().getClassName())
            .append(QUOT_SPACE)
            .append(TO_ATTR).append(meth.getClassName()).append(QUOT_SPACE);
        }
        
        //for "returnType", To and From ar reversed
        if (MessageType.returnType.equals(type)) {

            this.messagesBuffer
            .append(TYPE_ATTR).append("return").append(QUOT_SPACE)
            .append(FROM_ATTR).append(meth.getClassName()).append(QUOT_SPACE)
            .append(TO_ATTR).append(meth.getParent().getClassName())
            .append(QUOT_SPACE);           
        }
        
        this.messagesBuffer
        .append("text=\"").append(meth.getMethodName()).append("(");
        
        Class<?>[] paramTypes = meth.getParameterTypes();
        
        for (int i = 0; i < paramTypes.length; i++) {
            this.messagesBuffer.append(paramTypes[i].toString());
            if (i < paramTypes.length - 1) {
                this.messagesBuffer.append(",");
            }
        }

        this.messagesBuffer
        .append(")")
        .append(QUOTE_LT).append(NEW_LINE);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeMethodBeforeChildren(final SequenceMethod meth) 
        throws IOException {
        
        this.computeActorAndMessageFromMethod(meth);

    }
    
    

}
