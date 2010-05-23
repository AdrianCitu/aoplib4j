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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Root class for all the sequence diagram writers.
 * This class contains a writer that will be used to write the diagram.
 * 
 * @author Adrian Citu
 *
 */
public abstract class SequenceDiagramWriter {

    /**
     * the new line separator.
     */
    static final String NEW_LINE = System.getProperty("line.separator");
    
    /**
     * The writer to use for the diagram writing.
     */
    private Writer diagramWriter = null;
    
    /**
     * The diagram full path.
     */
    private String diagramFullPath = null;
    
    /**
     * Default constructor.
     * 
     */
    public SequenceDiagramWriter() {
    }
    
    /**
     *  Internal method used to create the diagram writer from the parameters
     *  of the {@link Aoplib4jSequenceDiagram} annotation.
     *  
     *  @param diagrFullPath the full path of the sequence diagram.
     */
    final void createWriter(final String diagrFullPath) {
        try {
            this.diagramFullPath = diagrFullPath;
            diagramWriter = 
                new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(diagrFullPath), "UTF8"));
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        } 
    }
    
    /**
     * @return the diagram full path.
     */
    public final String getDiagramFullPath() {
        return this.diagramFullPath;
    }

    /**
     * Write a string to the diagram on a new line.
     * 
     * @param str the string to write
     * @throws IOException if any I/O exception
     */
    final void writeLine(final String str) throws IOException {
        this.diagramWriter.write(str);
        this.diagramWriter.write(NEW_LINE);
        this.diagramWriter.flush();
    }
  
    /** 
     * It closes the writer used for writing the diagram.
     */
    final void closeWriter() {
            if (this.diagramWriter != null) {
                try {
                    this.diagramWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }   
            }
        }


    /**
     * Write the diagram starting from the root method which is passed
     * as parameter.
     * 
     * @param rootMethod the root method; the first method of the diagram.
     * 
     * @throws IOException I/O exception if any error writing.
     */
    public abstract void write(final SequenceMethod rootMethod) 
        throws IOException;
}
