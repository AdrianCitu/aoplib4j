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
package org.aoplib4j.uml.png;

import org.aoplib4j.uml.ActorInterface;
import org.aoplib4j.uml.Aoplib4jSequenceDiagram;
import org.aoplib4j.uml.Class1;
import org.aoplib4j.uml.PngWriter;


/**
 * Class containing methods annotated with the {@link Aoplib4jSequenceDiagram}
 * annotation and used for testing the {@link PngWriter} writer.
 * @author Adrian Citu
 *
 */
public class ActorClass implements ActorInterface {
    
    /**
     * @param args
     */
    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class,
            diagramDepth = 30)
    public static void main(String[] args) {        
        ActorClass ac = new ActorClass();
        ac.actorMethodWithParams("");
        ac.nonAnnotatedMethod();
        
        staticMethod();
        
        ac.actorMethodWithParams("");
        
    }
    

    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class)
    public static void staticMethod() {
        ActorClass ac = new ActorClass();
        ac.actorMethodWithParams("");
        ac.nonAnnotatedMethod();
        
        ac.actorMethodWithParams("");
    }
    
    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class)
    public static void staticMethodWithDefaultDiagramPath(int integer) {
        
    }
    

    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class,
            diagramDepth = 1)
    public void actorMethod() {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        ActorClass.staticPrivateMethod(1);
        
    }

    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class, 
            diagramDepth = 0)
    public void anotherActorMethod() {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        ActorClass.staticPrivateMethod(1);
        
    }
    
    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class,     
            diagramDepth = 10)
    public void actorMethodWithParams(String str) {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        ActorClass.staticPrivateMethod(1);
        
    }
    
    public void nonAnnotatedMethod() {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
    }
    
    private static void staticPrivateMethod(Integer in) {
        
        
    }
    
    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class,
            diagramDepth = 20)
    public void methodWithNestedAnnotation(String str) {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        
        this.methodWithNestedAnnotation2("");
        
    }
    
    
    @Aoplib4jSequenceDiagram(diagramWriter = PngWriter.class)
    public void methodWithNestedAnnotation2(String str) {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        
    }

}
