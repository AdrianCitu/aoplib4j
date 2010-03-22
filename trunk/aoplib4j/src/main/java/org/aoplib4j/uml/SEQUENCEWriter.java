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
import java.io.StringWriter;


/**
 * Writer for the SEQUENCE project.
 * (http://www.zanthan.com/itymbi/archives/cat_sequence.html). 
 * 
 * @author Adrian Citu
 *
 */
public final class SEQUENCEWriter extends SequenceDiagramWriter {
    



    /** 
     * Write the diagram for in the SEQUENCE format.
     * 
     * @param root the root method of the diagram.
     * @throws IOException if any writing error.
     */
    @Override
    public void write(final SequenceMethod root) throws IOException {
        
        StringWriter strWrit = new StringWriter();
        
        strWrit
            .append(root.getClassName().substring(
                root.getClassName().lastIndexOf(".") + 1))
            .append(".");
        
        if (root.isStatic()) {
            strWrit.append("[static]");
        }
        
        strWrit
            .append(root.getMethodName())
            .append("(");
        
        String[] parametersNames = root.getParameterNames();
        Class< ? >[] parametersTypes = root.getParameterTypes();
        
        for (int i = 0; i < parametersNames.length - 1; i++) {
            strWrit
                .append(parametersTypes[i].getCanonicalName())
                .append(" ")
                .append(parametersNames[i])
                .append(",");
        }
        
        if (parametersNames.length != 0) {
            strWrit
            .append(parametersTypes[parametersNames.length - 1]
                                    .getCanonicalName())
            .append(" ")
            .append(parametersNames[parametersNames.length - 1]);   
        }
        strWrit.append(")");
        
        strWrit.append(" -> ");
        
        if (root.getReturnType() != null) {
            strWrit.append(root.getReturnType().getSimpleName());
        } else {
            strWrit.append("void");
        }
            
        
        if (root.hasChilds()) {
           strWrit.append("{");
           
           this.writeLine(strWrit.toString());
           strWrit = new StringWriter();
           
           for (SequenceMethod children : root.getChilds()) {
               this.write(children);
           }
           
           strWrit.append("}");
        } else {
            strWrit.append(";");
        }
        this.writeLine(strWrit.toString());
        
    }
}
