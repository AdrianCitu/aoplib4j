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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark the starting method/constructor of the sequence 
 * diagram.
 *  
 * @author Adrian Citu
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Aoplib4jSequenceDiagram {

    /**
     * The default sequence diagram depth.
     */
     int DEFAULT_DIAGRAM_DEPTH = 5;
    
    /**
     * Writer implementation used to write the diagram.
     */
    Class<? extends SequenceDiagramWriter> diagramWriter();
    
    /**
     * The maximum number of participants in the diagram; default to 
     * {@value #DEFAULT_DIAGRAM_DEPTH}.
     * 
     */
    int diagramDepth() default DEFAULT_DIAGRAM_DEPTH;
    
    /**
     * The diagram full path; the default value will be
     * java.io.tmpdir/ClassName.MethodName.txt (this value will be computed
     * at runtime).
     */
    String diagramFullPath() default "";
}
