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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.aoplib4j.TestHelperClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for test the {@link SEQUENCEWriter} writer.
 * 
 * @author Adrian Citu
 *
 */
public class SEQUENCETest extends UmlTestsRoot {

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
        
        String expectedContent = 
            TestHelperClass.getTestFileContents(SEQUENCETest.PACKAGE
                    + "/expectedStaticMethod.seq");
        
        String generatedContent = TestHelperClass.inputStreamToString(
                new FileInputStream(ActorClass.STATIC_METHOD_DIAGRAM_PATH));
        
        assertEquals(expectedContent, generatedContent);
        
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
        ac.actorMethod();
        
        String expectedContent = 
            TestHelperClass.getTestFileContents(SEQUENCETest.PACKAGE
                    + "/expectedActorMethodDepth1.seq");
        
        String generatedContent = TestHelperClass.inputStreamToString(
                new FileInputStream(ActorClass.ACTOR_METHOD_DIAGRAM_PATH_1));
        
        assertEquals(expectedContent, generatedContent);
        
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
    
        ac.anotherActorMethod();
        
        String expectedContent = 
            TestHelperClass.getTestFileContents(SEQUENCETest.PACKAGE
                    + "/expectedAnotherActorMethodDepth0.seq");
        
        String generatedContent = TestHelperClass.inputStreamToString(
                new FileInputStream(ActorClass.ANOTHER_METHOD_DIAGRAM_PATH_0));
        
        assertEquals(expectedContent, generatedContent);
        
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
        ac.methodWithNestedAnnotation("");
        
        String expectedContent = 
            TestHelperClass.getTestFileContents(SEQUENCETest.PACKAGE
                    + "/expectedMethodWithNestedAnnotation.seq");
        
        String generatedContent = TestHelperClass.inputStreamToString(
               new FileInputStream(ActorClass.METHOD_DIAGRAM_WITH_NESTED_ANNO));
        
        assertEquals(expectedContent, generatedContent);
        
        String expectedNestedContent = 
            TestHelperClass.getTestFileContents(SEQUENCETest.PACKAGE
                    + "/expectedMethodWithNestedAnnotation2.seq");
        
        String generatedNestedContent = TestHelperClass.inputStreamToString(
              new FileInputStream(ActorClass.METHOD_DIAGRAM_WITH_NESTED_ANNO2));
        
        assertEquals(expectedNestedContent, generatedNestedContent);

    }
    
    /**
     * The test call a method annotated with the {@link Aoplib4jSequenceDiagram}
     * but no parameter {@link Aoplib4jSequenceDiagram#diagramFullPath()} 
     * is done so the default path is use.
     */
    @Test
    public final void testVerifyDiagramWrittenInDefaultFolder() {

        ActorClass.staticMethodWithDefaultDiagramPath(1);
        
        String tmpDir = TestHelperClass.getJavaIoTmpDir();
        
        //the diagram name contains .. because of the method parameters.
        String expectdDiagramPath = tmpDir 
            + "ActorClass.staticMethodWithDefaultDiagramPath(..).txt"; 
        
        try {
            
        boolean diagramExist = (new File(expectdDiagramPath)).exists();
        
        assertTrue(diagramExist);
        
        } finally {
            new File(expectdDiagramPath).delete(); 
        }
    }
    
}
