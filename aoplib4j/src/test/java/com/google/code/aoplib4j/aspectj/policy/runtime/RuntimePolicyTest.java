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
package com.google.code.aoplib4j.aspectj.policy.runtime;

import junit.framework.TestCase;

/**
 * Tests for the runtime policies.
 * 
 * @author Adrian Citu
 *
 */
public class RuntimePolicyTest  extends TestCase {

    
    /**
     * instance of {@link TestedClass} used for testing purposes.
     */
    private static final TestedClass INSTANCE = new TestedClass();
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                AbstractRuntimePolicyAspect.THROW_EXCEPTION_FOR_VIOLATIONS, "Yes");
    }


    
    /**
     * Test that verifies the methods that should returns Collections, Maps or
     * Arrays does return an empty collection and not a null value. 
     */
    public final void testRuntimePolicyForReturnOfNullCollections() {
        
        /* correct calls; no exception is raised */
        INSTANCE.returnNotNullArray();
        
        /* correct calls; no exception is raised */
        INSTANCE.returnInterfaceMap();
        
        String errorMsg = "UnsupportedOperationException should be thrown"
            + " because the method returns a null value";
        
        try {
            INSTANCE.returnNullCollection();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
            //nothing to do
        }
        
        try {
            INSTANCE.returnNullMap();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
        
        try {
            TestedClass.returnStaticNullCollection();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
        
        try {
            TestedClass.returnStaticNullMap();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
        
        try {
            TestedClass.returnStaticNullArray();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
        
        //should not thrown error since the return value is not null.
        INSTANCE.returnNotNullCollection();
    }

    /**
     * Test that verifies the methods should return interfaces and not 
     * implementations of interfaces. 
     */
    public final void testRuntimePolicyForReturnInterfacesNotObjects() {
 
        /* correct calls; no exception is raised*/
        INSTANCE.returnInterfaceCollection();
        
        /* correct calls; no exception is raised*/
        INSTANCE.returnInterfaceMap();
        
        String errorMsg = "UnsupportedOperationException should be thrown"
            + " because the method returns an implementation not an interface.";
        
        try {
            INSTANCE.returnVectorNotInterface();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
        
        try {
            INSTANCE.returnHashMapNotInterface();
            fail(errorMsg);
        } catch (UnsupportedOperationException e) {
          //nothing to do
        }
    }
}
