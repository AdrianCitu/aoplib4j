/*
 *  Copyright 2008-2009 the original author or authors.
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
package com.google.code.aoplib4j.aspectj.modularity.clas;

import junit.framework.TestCase;


/**
 * Test case for the boundary validation aspect.
 *  
 * @author Adrian Citu
 *
 */
public class ClassBoundaryViolationTest extends TestCase {

    private OkCallerClass ok = new OkCallerClass();
    private ForbiddenCallerClass fb = new ForbiddenCallerClass();
    private ClasCalledClass cc = new ClasCalledClass();
    private ChildOfCalledClass childOfCc = new ChildOfCalledClass();
    private ChildOfForbiddenCallerClass childOfFb = 
        new ChildOfForbiddenCallerClass();
    
    /**
     * Test for check the violation of the call boundary. The test use the 
     * {@link ForbiddenCallerClass} class that does not have the right to
     * call the methods from the {@link ClasCalledClass}.
     */
    @SuppressWarnings(value={"all"})
    public final void testViolateBoundary() {
        try {
            fb.voidMethod();
            
            fail("An IllegalAccessError should be thrown because the"
                    + fb.getClass().getName() 
                    + " violated the call boundary.");
        } catch (IllegalAccessError iae) {
            //nothing to do
        }
        
        try {
            ForbiddenCallerClass.staticVoidMethod();
            
            fail("An IllegalAccessError should be thrown because the"
                    + fb.getClass().getName() 
                    + " violated the call boundary.");
        } catch (IllegalAccessError iae) {
            //nothing to do
        }
    }
    
    /**
     * Test for check the violation of the call boundary. The test use the 
     * {@link OkCallerClass} that have the right to call the methods from
     * the {@link ClasCalledClass}.
     */
    public final void testBoundaryOk() {
        ok.voidMethod();
        OkCallerClass.staticVoidMethod();
    }
    
    
    /**
     * Test for check the violation of the call boundary. The tests call
     * methods from the {@link ClasCalledClass} that calls methods from the
     * {@link ClasCalledClass}.
     */
    public final void testBoundaryOkOnSameClass() {
        cc.callerCalledMethod();
        
        ClasCalledClass.callerCalledStaticMethod();
    }
    
    /**
     * Test for check the violation of the call boundary. The tests call
     * methods from the {@link ChildOfCalledClass} that is a child of the 
     * {@link ClasCalledClass}.
     */
    public final void testBoundaryOkOnChildClass() {
        childOfCc.calledMethod();
        
        childOfCc.callerCalledMethod();
        
        ChildOfCalledClass.callerCalledStaticMethod();
    }
    
    /**
     * This test is written to focus a problem into the class boundary 
     * implementation. The non overridden calls to methods (static or not) are
     * seen by the JVM as calls to the parent class. For this reason in the 
     * aspect it is impossible to distinguish between the call
     * {@link ChildOfForbiddenCallerClass#staticVoidMethod()} and
     *  the call to {@link ForbiddenCallerClass#staticVoidMethod()}.
     *  
     */
    @SuppressWarnings(value={"all"})
    public final void testBoundaryNokOnStaticNonOverriddenMethod() {
        
        try {
            //this is a call to a inherit non overridden method
            ChildOfForbiddenCallerClass.staticVoidMethod();
            fail("Call to a inherit non overridden method");
        } catch (IllegalAccessError e) {
            //nothing to do
        }
    }
    
    /**
     * Test for check the violation of the call boundary. The tests call
     * methods from the {@link ChildOfForbiddenCallerClass} that is a child of 
     * the {@link ForbiddenCallerClass}.
     */
    public final void testBoundaryOkOnChildOfForbiddenClass() {
        childOfFb.childMethod();
        
        //this is a call to a inherit non overridden method
        childOfFb.voidMethod();
        
        ChildOfForbiddenCallerClass.staticChildVoidMethod();
    }
    
}
