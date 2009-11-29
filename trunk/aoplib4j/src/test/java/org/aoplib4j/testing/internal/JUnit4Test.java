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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test written with JUnit v4 used for testing the verification aspect.
 * 
 * @author Adrian Citu
 *
 */
public class JUnit4Test {

    private static final String ASSERT_VIOLATION_MESSAGE = 
        "AssertionError should be thrown";
    
    /**
     * Inhibit the throws af the assertion failures.
     * @see AbstractTestingAspect#AOPLIB4J_FAIL_INHIBIT
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty(
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT, 
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT_VALUE);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.setProperty(
                JUnitAspect.AOPLIB4J_FAIL_INHIBIT, 
                "false");
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        //nothing to do
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        //nothing to do
    } 
  
    /**
     * test containing all the assert types. Normally this test should throw
     * an assertion failure but since the 
     * {@link JUnitAspect#AOPLIB4J_FAIL_INHIBIT} property have the value 
     * {@link JUnitAspect#AOPLIB4J_FAIL_INHIBIT_VALUE} the failures are 
     * inhibited.
     */
    @Test
    public final void assertsTest() {
        assertEquals(ASSERT_VIOLATION_MESSAGE, false, true);
        assertFalse(ASSERT_VIOLATION_MESSAGE, true);
        assertTrue(ASSERT_VIOLATION_MESSAGE, false);
        assertSame(ASSERT_VIOLATION_MESSAGE, "1", "2");
        assertNotNull(ASSERT_VIOLATION_MESSAGE, null);
        
    }
    
    /**
     * Test that call an private method that contains the asserts.
     */
    @Test
    public final void ssertsTestFromPrivateMethod() {
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

    /**
     * The test should throw a NullPointerException since the <code>str</code>
     * string is null.
     */
    @Test(expected= NullPointerException.class)
    public final void testThatThrowsNullPointerException() {
        String str = null;
        
        assertNotNull(ASSERT_VIOLATION_MESSAGE, str);
        
        str.length();
        
    }
    
    /**
     * The test should throw a NullPointerException since the <code>str</code>
     * string is null.
     */
    @Test(expected= NullPointerException.class)
    public final void testThatThrowsNullPointerExceptionAndNoAsserts() {
        String str = null;        
        str.length();
    }

}
