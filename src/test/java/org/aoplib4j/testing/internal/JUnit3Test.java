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
package org.aoplib4j.testing.internal;

import org.aoplib4j.testing.internal.JUnitAspect;

import junit.framework.TestCase;

/**
 * Unit test written with JUnit v3 used for testing the verification aspect.
 * 
 * @author Adrian Citu
 *
 */
public class JUnit3Test extends TestCase {

    private static final String ASSERT_VIOLATION_MESSAGE = 
        "AssertionError should be thrown";
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT, 
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT_VALUE);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        System.setProperty(
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT, 
                "false");
    }

  
    /**
     * test containing all the assert types. Normally this test should throw
     * an assertion failure but since the 
     * {@link JUnitAspect#AOPLIB4J_FAIL_INHIBIT} property have the value 
     * {@link JUnitAspect#AOPLIB4J_FAIL_INHIBIT_VALUE} the failures are 
     * inhibited.
     */
    public final void testAsserts() {
        assertEquals(ASSERT_VIOLATION_MESSAGE, false, true);
        assertFalse(ASSERT_VIOLATION_MESSAGE, true);
        assertTrue(ASSERT_VIOLATION_MESSAGE, false);
        assertSame(ASSERT_VIOLATION_MESSAGE, "1", "2");
        assertNotNull(ASSERT_VIOLATION_MESSAGE, null);
        
    }
    
    /**
     * Test that call an private method that contains the asserts.
     */
    public final void testAssertsFromPrivateMethod() {
        this.privateMethodWithAssert();
    }
    
    private void privateMethodWithAssert() {
        assertEquals(ASSERT_VIOLATION_MESSAGE, true, true);
        assertEquals(ASSERT_VIOLATION_MESSAGE, false, true);
        assertFalse(ASSERT_VIOLATION_MESSAGE, true);
        assertTrue(ASSERT_VIOLATION_MESSAGE, false);
        assertSame(ASSERT_VIOLATION_MESSAGE, "1", "2");
        assertNotNull(ASSERT_VIOLATION_MESSAGE, null);
    }
}
