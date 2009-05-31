/*
 *  Copyright 2008 the original author or authors.
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
package com.google.code.aoplib4j.aspectj.fieldvalidation;

import junit.framework.TestCase;


/**
 * Test case for testing the {@link Validate} annotation.
 * 
 * @author Adrian Citu
 *
 */
public class FieldValidationTest extends TestCase {
    
    private ClassToValidate ctv = null;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctv = new ClassToValidate();
    }

    /**
     * Test {@link StringValidator}; the test should not pass because the input
     * string is null.
     */
    @SuppressWarnings(value={"all"})
    public final void testSetStringAsNull() {
        
        try {
            ctv.setStr(null);  
            fail("the method call should throw a null pointer exception.");
        } catch (NullPointerException e) {
            //nothing to do.
        } 
    }
    
    /**
     * Test {@link StringValidator}; the test should not pass because the input
     * string is null.
     */
    @SuppressWarnings(value={"all"})
    public final void testSetStaticStringAsNull() {
        
        try {
            ClassToValidate.setStaticStr(null);
            fail("the method call should throw a null pointer exception.");
        } catch (NullPointerException e) {
            //nothing to do.
        } 
    }
    
    /**
     * Test for {@link StringValidator}; the validation should pass.
     */
    public final void testSetStringOk() {
        ClassToValidate.setStaticStr("str");
        ctv.setStr("str");
    }
}
