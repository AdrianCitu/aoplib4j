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

import org.junit.Ignore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test written with TestNG used for testing the verification aspect.
 * 
 * @author Adrian Citu
 *
 */
@Ignore
public class TestNGTest {

    private static final String ASSERT_VIOLATION_MESSAGE = 
        "AssertionError should be thrown";
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty(
                AbstractTestingAspect.AOPLIB4J_FAIL_INHIBIT, 
                AbstractTestingAspect.AOPLIB4J_FAIL_INHIBIT_VALUE);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.setProperty(
                AbstractTestingAspect.AOPLIB4J_FAIL_INHIBIT, 
                "false");
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeMethod
    public void setUp() throws Exception {
        //nothing to do
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterMethod
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
        assertEquals(false, true, ASSERT_VIOLATION_MESSAGE);
        assertFalse(true, ASSERT_VIOLATION_MESSAGE);
        assertTrue(false, ASSERT_VIOLATION_MESSAGE);
        assertSame("1", "2", ASSERT_VIOLATION_MESSAGE);
        assertNotNull(null, ASSERT_VIOLATION_MESSAGE);
        
    }
    
    /**
     * Test that call an private method that contains the asserts.
     */
    @Test
    public final void ssertsTestFromPrivateMethod() {
        this.privateMethodWithAssert();
    }
    
    private void privateMethodWithAssert() {
        assertEquals(true, true, ASSERT_VIOLATION_MESSAGE);
        assertEquals(false, true, ASSERT_VIOLATION_MESSAGE);
        assertFalse(true, ASSERT_VIOLATION_MESSAGE);
        assertTrue(false, ASSERT_VIOLATION_MESSAGE);
        assertSame("1", "2", ASSERT_VIOLATION_MESSAGE);
        assertNotNull(null, ASSERT_VIOLATION_MESSAGE);
    }


}
