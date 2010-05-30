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
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.aoplib4j.TestHelperClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


/**
 * Parameterized JUnit test for the sequence writers; Every writer use his
 * own {@link ActorInterface} implementation. 
 * @author Adrian Citu
 *
 */
@RunWith(value = Parameterized.class)
public final class SequenceDiagramTest {
    

    private String tmpDir = TestHelperClass.getJavaIoTmpDir();
    
    private ActorInterface actor = null;
    
    private String expectedDiagramRelativePath = null;
    
    /**
     * @return a matrix of 2 rows; the first column contains the instances of
     * the {@link ActorInterface} class and the second column contains the 
     * relative path where the expected diagrams files are stored.   
     */
    @Parameters
    public static List< Object[] > parameters() {
        return Arrays.asList(
            new Object[][] {
                    {new org.aoplib4j.uml.codetodiagram.ActorClass(), 
                        "/org/aoplib4j/uml/codetodiagram/" },
                    {new org.aoplib4j.uml.SEQUENCE.ActorClass(), 
                        "/org/aoplib4j/uml/SEQUENCE/" },
                    {new org.aoplib4j.uml.png.ActorClass(), 
                        "/org/aoplib4j/uml/SEQUENCE/" }
           }
        );
    }
    
    /**
     * Unit test constructor.
     * @param ac the actor implementation to use.
     * @param diagrPath the diagram relative path.
     */
    public SequenceDiagramTest(ActorInterface ac, String diagrPath) {
        this.actor = ac;
        this.expectedDiagramRelativePath = diagrPath;
    }
    
    
    /**
     * The test call a method annotated with the {@link Aoplib4jSequenceDiagram}
     * but no parameter {@link Aoplib4jSequenceDiagram#diagramFullPath()} 
     * is done so the default path is use.
     * 
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     */
    @Test
    public final void testVerifyDiagramWrittenInDefaultFolder() 
        throws FileNotFoundException, IOException, URISyntaxException, 
        SecurityException, IllegalArgumentException, NoSuchMethodException, 
        IllegalAccessException, InvocationTargetException {

        TestHelperClass.executeMethodByIntrospection(
                this.actor, 
                "staticMethodWithDefaultDiagramPath", 
                new Class[] {int.class},
                new Object[] {1});
        
        actor.actorMethodWithParams("str");
        
        //the diagram name contains .. because of the method parameters.
        String actualDiagram = tmpDir 
            + "ActorClass.actorMethodWithParams(..).txt"; 
        
        String expectedDiagram = 
               expectedDiagramRelativePath 
                   + "ActorClass.actorMethodWithParams(..).txt";
        
        this.genericTest(expectedDiagram, actualDiagram);  

    }

    /**
     * Test that create a diagram for the {@link ActorClass#staticMethod()} and
     * compares it with the expected diagram.
     * 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * 
     */
    @Test
    public final void testActorClassStaticMethod() 
    throws FileNotFoundException, IOException, URISyntaxException,
    SecurityException, IllegalArgumentException, NoSuchMethodException, 
    IllegalAccessException, InvocationTargetException {
        
        TestHelperClass.executeMethodByIntrospection(
                this.actor, 
                "staticMethodWithDefaultDiagramPath", 
                new Class[] {int.class},
                new Object[] {1});
        
        TestHelperClass.executeMethodByIntrospection(
                this.actor, 
                "staticMethod", 
                new Class[] {},
                new Object[] {});
        
        String actualDiagram = tmpDir 
            + "ActorClass.staticMethod().txt"; 
        
        String expectedDiagram = 
            expectedDiagramRelativePath + "ActorClass.staticMethod().txt";
        
        this.genericTest(expectedDiagram, actualDiagram);        
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
        
        actor.actorMethod();
        
        String actualDiagram = tmpDir 
            + "ActorClass.actorMethod().txt"; 
        
        String expectedDiagram = 
            expectedDiagramRelativePath + "ActorClass.actorMethod().txt";
        
        this.genericTest(expectedDiagram, actualDiagram); 
        
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
    
        actor.anotherActorMethod();
        
        String actualDiagram = tmpDir 
            + "ActorClass.anotherActorMethod().txt"; 
    
        String expectedDiagram = 
            expectedDiagramRelativePath + "ActorClass.anotherActorMethod().txt";
    
        this.genericTest(expectedDiagram, actualDiagram ); 
        
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
        
        actor.methodWithNestedAnnotation("");
        
        String actualDiagram = tmpDir 
        + "ActorClass.methodWithNestedAnnotation(..).txt"; 

        String expectedDiagram = 
            expectedDiagramRelativePath 
                + "ActorClass.methodWithNestedAnnotation(..).txt";

        this.genericTest(expectedDiagram, actualDiagram); 

        actualDiagram = tmpDir 
        + "ActorClass.methodWithNestedAnnotation2(..).txt"; 

        expectedDiagram = 
            expectedDiagramRelativePath + 
                "ActorClass.methodWithNestedAnnotation2(..).txt";

        this.genericTest(expectedDiagram, actualDiagram); 
    }
    
    /**
     * @param expectedDiagram the expected diagram
     * @param actualDiagram the actual diagram
     * 
     */
    private void genericTest(final String expectedDiagram, 
            final String actualDiagram) 
        throws FileNotFoundException, IOException, URISyntaxException {
        
        try {
            
            boolean diagramExist = (new File(actualDiagram)).exists();
            assertTrue(diagramExist);
            
            pngDiagramExist(actualDiagram);
            
            String expectedContent =
              TestHelperClass.getFileContentFromTestRessources(expectedDiagram);

            String actualContent = 
                TestHelperClass.inputStreamToString(
                        new FileInputStream(actualDiagram));
            
            assertEquals(expectedContent, actualContent);
            
            } finally {
                new File(actualDiagram).delete(); 
            }
    }
    
    /**
     * Verify if for the <code>actualDiagram</code> the png diagram exist.
     * @param actualDiagram the actual diagram
     */
    private final void pngDiagramExist (final String actualDiagram) {
        
        if (actor instanceof org.aoplib4j.uml.png.ActorClass) {
            
                String pngFileName = actualDiagram.replaceAll(".txt", ".png");
            
                boolean pngExist = (new File(pngFileName)).exists();
                
                if(pngExist) {
                    new File(pngFileName).delete(); 
                }
                assertTrue(pngExist);
                
        }
        
    }
}
