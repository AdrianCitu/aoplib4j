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
 * Writer for the <code>SEQUENCE</code> project.
 * (http://www.zanthan.com/itymbi/archives/cat_sequence.html).
 * 
 * This class is a concrete class from the Template Method Pattern
 * (http://en.wikipedia.org/wiki/Template_method_pattern).
 * 
 * @author Adrian Citu
 * 
 */
public final class SEQUENCEWriter extends DFSDiagramWriter {

    /**
     * buffer on which the temporary content is written.
     */
    private StringBuffer strWrit = new StringBuffer();

    /**
     * Method called after all the children methods are written.
     * 
     * @param meth the method to write.
     * 
     * @throws IOException if any writing error.
     */
    @Override
    public void writeMethodAfterChilds(final SequenceMethod meth) 
        throws IOException {
        
        if (meth.haveChildren()) {
            this.strWrit.append("}");
            this.writeLine(strWrit.toString());
            this.strWrit = new StringBuffer();
        }
    }

    /**
     * Method that will write the SEQUENCE diagram part for a method.
     * Example of result for a method that does have children 
     * (the methods calls another methods)
     * <pre>
     * ActorClass.actorMethod() -> void{ 
     * </pre>
     * 
     * Example of result for a constructor that do not call another methods 
     * inside.
     * <pre>
     * Class1.constructor(java.lang.String str) -> void;
     * </pre>
     * 
     * @param meth the method to write.
     * 
     * @throws IOException if any writing error.
     */
    @Override
    public void writeMethodBeforeChilds(final SequenceMethod meth) 
        throws IOException {

        strWrit.append(
                meth.getClassName().substring(
                        meth.getClassName().lastIndexOf(".") + 1)).append(".");

        if (meth.isStatic()) {
            strWrit.append("[static]");
        }

        strWrit.append(meth.getMethodName()).append("(");

        String[] parametersNames = meth.getParameterNames();
        Class< ? >[] parametersTypes = meth.getParameterTypes();

        for (int i = 0; i < parametersNames.length - 1; i++) {
            strWrit.append(parametersTypes[i].getCanonicalName()).append(" ")
                    .append(parametersNames[i]).append(",");
        }

        if (parametersNames.length != 0) {
            strWrit.append(
                    parametersTypes[parametersNames.length - 1]
                            .getCanonicalName()).append(" ").append(
                    parametersNames[parametersNames.length - 1]);
        }
        
        strWrit.append(")");
        strWrit.append(" -> ");

        if (meth.getReturnType() != null) {
            strWrit.append(meth.getReturnType().getSimpleName());
        } else {
            strWrit.append("void");
        }

        if (meth.haveChildren()) {
            strWrit.append("{");

        } else {
            strWrit.append(";");
        }

        this.writeLine(strWrit.toString());
        this.strWrit = new StringBuffer();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFooter(final SequenceMethod meth) throws IOException {
        //nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeHeader(final SequenceMethod meth) throws IOException {
        //nothing to do;
    }
}
