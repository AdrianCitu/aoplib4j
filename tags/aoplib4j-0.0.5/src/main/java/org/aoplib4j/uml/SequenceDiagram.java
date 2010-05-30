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
 * Class representing a sequence diagram. An instance of this object is created
 * by the <code>AopLib4j</code> framework for every annotated method.
 * 
 * A sequence diagram contains a writer used to write the diagram, the maximum
 * depth of the diagram, 
 * 
 * A sequence diagram is a tree having as nodes instances of 
 * {@link SequenceMethod} class. 
 * 
 * @see SequenceDiagramWriter#write(SequenceMethod) 
 * @see SequenceMethod
 * 
 * @author Adrian Citu
 *
 */
public final class SequenceDiagram {

    /**
     *  Method that is executed by the aspect at a certain time.
     */
    private SequenceMethod activeMethod = null;
    
    /**
     * The writer to use for the diagram creation. 
     */
    private SequenceDiagramWriter diagramWriter = null;
    
    /**
     * the actual diagram depth.
     */
    private int diagramDepth = 0;
    
    /**
     * the maximal diagram depth; taken from the annotation parameter
     * ({@link Aoplib4jSequenceDiagram#diagramDepth()}).
     */
    private int maxDiagramDepth = 0;
    
    /**
     * The root method of the diagram.
     */
    private SequenceMethod rootMethod = null;

 
    /**
     * Default constructor.
     * 
     * @param maxDepth the maximum depth that the diagram can have.
     * @param writer the writer to use for writing the diagram.
     * @param diagramPath the diagram full path.
     * @param rootMetd root method of this diagram.
     */
    public SequenceDiagram(
            final int maxDepth, final SequenceDiagramWriter writer,
            final String diagramPath, final SequenceMethod rootMetd) {
        super();
        this.maxDiagramDepth = maxDepth;
        this.diagramWriter = writer;
        this.diagramWriter.setDiagramPath(diagramPath);
        this.rootMethod = rootMetd;
        
        this.setActiveMethod(rootMetd);
    }

    /**
     * @return the actual diagram depth.
     */
    public int getDiagramDepth() {
        return this.diagramDepth;
    }

    /**
     * increase the actual diagram depth by 1.
     */
    public void increaseDiagramDepth() {
        this.diagramDepth++;
    }

    /**
     * decrease the diagram depth by 1.
     */
    public void decreaseDiagramDepth() {
        this.diagramDepth--;
    }
    
    /**
     * @return the maximum depth that the giagram can have.
     */
    public int getMaxDiagramDepth() {
        return this.maxDiagramDepth;
    }

    /**
     * @return the active method of the diagram.
     */
    public SequenceMethod getActiveMethod() {
        return this.activeMethod;
    }

    /**
     * Set the new active method for the diagram.
     * @param activeMeth the new active method.
     */
    public void setActiveMethod(final SequenceMethod activeMeth) {
        this.activeMethod = activeMeth;
    }

    /**
     * Write to the attached writer.
     * @throws IOException  if any error when writing.
     */
    public void write() throws IOException {
        this.diagramWriter.write(this.rootMethod);
    }

    /**
     * Closed the writer of the attached diagram writer.
     */
    public void closeWriter() {
        this.diagramWriter.closeWriter();
    }
    
    
    
}
