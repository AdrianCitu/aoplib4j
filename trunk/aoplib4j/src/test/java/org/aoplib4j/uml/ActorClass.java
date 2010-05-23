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


/**
 * Class containing methods annotated with the {@link Aoplib4jSequenceDiagram}
 * annotation and used for testing the {@link SEQUENCEWriter} writer.
 * @author Adrian Citu
 *
 */
public class ActorClass {
    
    /**
     * Path of the diagram for {@link #staticMethod()} method.
     * Warning ! the path should be the same as the one from the annotation.
     */
    public static final String STATIC_METHOD_DIAGRAM_PATH = 
       SEQUENCETest.DIAGRAMS_FOLDER + "/staticMethod.seq";

    /**
     * Path of the diagram for {@link #actorMethodWithParams(String)} method.
     * Warning ! the path should be the same as the one from the annotation.
     */
    public static final String ACTOR_METHOD_DIAGRAM_PATH_1 = 
        SEQUENCETest.DIAGRAMS_FOLDER + "/actorMethodDepth1.seq";
    
    /**
     * Path of the diagram for {@link #actorMethodWithParams(String)} method.
     * Warning ! the path should be the same as the one from the annotation.
     */
    public static final String ANOTHER_METHOD_DIAGRAM_PATH_0 = 
        SEQUENCETest.DIAGRAMS_FOLDER + "/anotherActorMethodDepth0.seq";

    /**
     * Path of the diagram for {@link #methodWithNestedAnnotation2(String)}
     * method.
     * Warning ! the path should be the same as the one from the annotation.
     */
    public static final String METHOD_DIAGRAM_WITH_NESTED_ANNO2 = 
       SEQUENCETest.DIAGRAMS_FOLDER + "/methodWithNestedAnnotation2.seq";
    
    /**
     * Path of the diagram for {@link #methodWithNestedAnnotation(String)} method.
     * Warning ! the path should be the same as the one from the annotation.
     */
    public static final String METHOD_DIAGRAM_WITH_NESTED_ANNO = 
        SEQUENCETest.DIAGRAMS_FOLDER + "/methodWithNestedAnnotation.seq";
    
    
    
    /**
     * @param args
     */
    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/main.seq", diagramDepth = 30)
    public static void main(String[] args) {        
        ActorClass ac = new ActorClass();
        ac.actorMethodWithParams("");
        ac.nonAnnotatedMethod();
        
        staticMethod();
        
        ac.actorMethodWithParams("");
        
    }
    

    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/staticMethod.seq")
    public static void staticMethod() {
        ActorClass ac = new ActorClass();
        ac.actorMethodWithParams("");
        ac.nonAnnotatedMethod();
        
        ac.actorMethodWithParams("");
    }
    
    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class)
    public static void staticMethodWithDefaultDiagramPath(int integer) {
        
    }
    

    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/actorMethodDepth1.seq", 
            diagramDepth = 1)
    public void actorMethod() {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        ActorClass.staticPrivateMethod(1);
        
    }

    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/anotherActorMethodDepth0.seq", 
            diagramDepth = 0)
    public void anotherActorMethod() {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        ActorClass.staticPrivateMethod(1);
        
    }
    
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
    
    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/methodWithNestedAnnotation.seq",
            diagramDepth = 20)
    public void methodWithNestedAnnotation(String str) {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        
        this.methodWithNestedAnnotation2("");
        
    }
    
    
    @Aoplib4jSequenceDiagram(diagramWriter = SEQUENCEWriter.class, 
            diagramFullPath = "/aoplib4jtests/methodWithNestedAnnotation2.seq")
    public void methodWithNestedAnnotation2(String str) {
        Class1 cl1 = new Class1("");
        cl1.method1Class1();
        
    }

}
