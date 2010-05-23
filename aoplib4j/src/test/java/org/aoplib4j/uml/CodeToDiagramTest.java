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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.aoplib4j.TestHelperClass;
import org.junit.Test;
import org.aoplib4j.uml.codetodiagram.ActorClass;
/**
 * Class for testing the {@link CodeToDiagramWriter} writer.
 * @author Adrian Citu
 *
 */
public class CodeToDiagramTest extends UmlTestsRoot {

    private ActorClass codeActor = new ActorClass();
    
    private String tmpDir = TestHelperClass.getJavaIoTmpDir();
    
    /**
     * The test call a method annotated with the {@link Aoplib4jSequenceDiagram}
     * but no parameter {@link Aoplib4jSequenceDiagram#diagramFullPath()} 
     * is done so the default path is use.
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public final void testVerifyDiagramWrittenInDefaultFolder() 
        throws FileNotFoundException, IOException, URISyntaxException {

        ActorClass.staticMethodWithDefaultDiagramPath(1);
        
        codeActor.actorMethodWithParams("str");
        
        
        
        //the diagram name contains .. because of the method parameters.
        String expectedDiagramPath = tmpDir 
            + "ActorClass.actorMethodWithParams(..).txt"; 
        
        String actualDiagramPath = 
            "/codetodiagram/ActorClass.actorMethodWithParams(..).txt";
        
        this.genericTest(expectedDiagramPath, actualDiagramPath);
        

    }
    
   private void genericTest(final String expectedDiagramPath, 
           final String actualDiagramPath) 
       throws FileNotFoundException, IOException, URISyntaxException {
       
       try {
           
           boolean diagramExist = (new File(expectedDiagramPath)).exists();
           
           assertTrue(diagramExist);
           
           String expectedContent = 
               TestHelperClass.inputStreamToString(
                       new FileInputStream(expectedDiagramPath));
               
           
           String actualContent = 
               TestHelperClass.getTestFileContents(PACKAGE + actualDiagramPath);
           
           assertEquals(expectedContent, actualContent);
           
           } finally {
               new File(expectedDiagramPath).delete(); 
           }
   }

    /**
     * Test that create a diagram for the {@link ActorClass#staticMethod()} and
     * compares it with the expected diagram.
     * 
     */
    @Test
    public final void testActorClassStatiMethod() 
    throws FileNotFoundException, IOException, URISyntaxException {
        
        ActorClass.staticMethodWithDefaultDiagramPath(2);
        
        ActorClass.staticMethod();
        
        String expectedDiagramPath = tmpDir 
            + "ActorClass.staticMethod().txt"; 
        
        String actualDiagramPath = 
            "/codetodiagram/ActorClass.staticMethod().txt";
        
        this.genericTest(expectedDiagramPath, actualDiagramPath);        
    }
    
    /**
     * Test that create a diagram for the {@link ActorClass#actorMethod()} and
     * compares it with the expected diagram. The diagram have the depth 1,
     * so only the first method  
     * 
     * 
     */
    @Test
    public final void testActorClassActorMethodethodWithDepth1() 
    throws FileNotFoundException, IOException, URISyntaxException {
        codeActor.actorMethod();
        
        String expectedDiagramPath = tmpDir 
            + "ActorClass.actorMethod().txt"; 
        
        String actualDiagramPath = 
            "/codetodiagram/ActorClass.actorMethod().txt";
        
        this.genericTest(expectedDiagramPath, actualDiagramPath); 
        
    }
    
    /**
     * Test that create a diagram for the 
     * {@link ActorClass#anotherActorMethod()} and
     * compares it with the expected diagram. The diagram have the depth 0,
     * so only the first method  
     * 
     * 
     */
    @Test
    public final void testActorClassActorMethodethodWithDepth0() 
    throws FileNotFoundException, IOException, URISyntaxException {
    
        codeActor.anotherActorMethod();
        
        String expectedDiagramPath = tmpDir 
            + "ActorClass.anotherActorMethod().txt"; 
    
        String actualDiagramPath = 
            "/codetodiagram/ActorClass.anotherActorMethod().txt";
    
        this.genericTest(expectedDiagramPath, actualDiagramPath); 
        
    }
    
    /**
     * The {@link ActorClass#methodWithNestedAnnotation()} is a (annotated)
     * method that will call another annotated method 
     * ({@link ActorClass#methodWithNestedAnnotation2()}); The tests verifies 
     * that the 2 sequence methods are correctly created.
     */
    @Test
    public final void testNestedAnnotations()
    throws FileNotFoundException, IOException, URISyntaxException {
        
        codeActor.methodWithNestedAnnotation("");
        
        String expectedDiagramPath = tmpDir 
        + "ActorClass.methodWithNestedAnnotation(..).txt"; 

        String actualDiagramPath = 
            "/codetodiagram/ActorClass.methodWithNestedAnnotation(..).txt";

        this.genericTest(expectedDiagramPath, actualDiagramPath); 

        expectedDiagramPath = tmpDir 
        + "ActorClass.methodWithNestedAnnotation2(..).txt"; 

        actualDiagramPath = 
            "/codetodiagram/ActorClass.methodWithNestedAnnotation2(..).txt";

        this.genericTest(expectedDiagramPath, actualDiagramPath); 
    }
    
}
