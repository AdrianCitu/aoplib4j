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

/**
 * This class represents the AbstractClass from the Template Method Pattern
 * (http://en.wikipedia.org/wiki/Template_method_pattern). The algorithm
 * implemented by this class is Depth-first search (DFS)
 * (http://en.wikipedia.org/wiki/Depth-first_search) on the sequence methods.
 * (since a {@link SequenceMethod} is a tree structure).
 * 
 * Every subclass must implement the way of writing the diagrams which can vary
 * for every diagram writer used.
 * 
 * @author Adrian Citu
 *
 */
public abstract class DFSDiagramWriter extends SequenceDiagramWriter {


    /**
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    @Override
    public final void write(final SequenceMethod meth) throws IOException {
        
        this.writeHeader(meth);
        this.writeMethod(meth);
        this.writeFooter(meth);
    }
    

    /**
     * Last method called the end for the diagram writing.
     * @param meth the root method
     * @throws IOException if any exception when writing.
     */
    public abstract void writeFooter(SequenceMethod meth) throws IOException;

    /**
     * Method called only one time at the beginning of the diagram writing.
     *  
     * @param meth the root method.
     * @throws IOException if any exception when writing.
     */
    public abstract void writeHeader(SequenceMethod meth) throws IOException;

    /**
     *  Write the content for a method. The workflow is :
     *  <ul>
     *  <li>
     *      call {@link #writeMethodBeforeChilds(SequenceMethod)}
     *  </li>
     *  <li>
     *      for every child method method call 
     *      {@link #writeMethod(SequenceMethod)}
     *  </li>
     *  
     *  <li>
     *      call {@link #writeMethodAfterChilds(SequenceMethod)}
     *  </li>
     *  </ul>
     *  
     * @param meth the method to write.
     * @throws IOException if any exception when writing.
     */
    private void writeMethod(final SequenceMethod meth) throws IOException {
        
        this.writeMethodBeforeChilds(meth);
        
        for (SequenceMethod children : meth.getChildren()) {
            this.writeMethod(children);
        }
        
        this.writeMethodAfterChilds(meth);
    }
    
    /**
     * Method called for writing a method called BEFORE the call of the 
     * {@link #write(SequenceMethod)} on the children.
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    public abstract void writeMethodBeforeChilds(final SequenceMethod meth) 
        throws IOException;
    
    /**
     * Method called for writing a method called AFTER the call of the 
     * {@link #write(SequenceMethod)} on the children.
     * 
     * @param meth the method to write
     * @throws IOException if any problem writing.
     */
    public abstract void writeMethodAfterChilds(final SequenceMethod meth) 
        throws IOException;

}
