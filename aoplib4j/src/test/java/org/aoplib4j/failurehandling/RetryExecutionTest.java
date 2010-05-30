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
package org.aoplib4j.failurehandling;

import org.aoplib4j.failurehandling.Aoplib4jRetryExecution;

import junit.framework.TestCase;


/**
 * JUnit test for the handling failure aspect.
 *  
 * @author Adrian Citu
 *
 */
public class RetryExecutionTest extends TestCase {
    
    /**
     * Instance used by the tests.
     */
    private ClassWithExceptions cwex = null;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        cwex = new ClassWithExceptions();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ClassWithExceptions.setNumbersOfExecutionsBeforeOk(0);
        ClassWithExceptions.clearStaticMethodCounter();
    }
    
    /**
     * The executed method are annotated and throws an exception first time
     * when are executed. The tests are made on static and not static methods.
     * @throws Testing2Exception 
     *
     */
    public final void testExecutionAndExceptionSecondTime() 
        throws Testing2Exception  {
        
        ClassWithExceptions.setNumbersOfExecutionsBeforeOk(2);
        
        try {
            cwex.throwExceptionPackageProtected();
        } catch (Testing2Exception e) {
            assertEquals(3, cwex.getMethodCounter());
        }
    }
    
    /**
     * The executed methods are annotated with {@link Aoplib4jRetryExecution} 
     * but no error is thrown at the execution.
     * The tests are made on static and not static methods.
     */
    public final void testExecutionAndNoException() {
        cwex.noExceptionMethod();
        ClassWithExceptions.noExceptionStaticMethod();
    }
    
    /**
     * The executed methods are annotated {@link Aoplib4jRetryExecution} and 
     * an error is thrown at the execution.
     * The tests are made on static and not static methods.
     */
    @SuppressWarnings(value={"all"})
    public final void testExecutionAndException() {
        try {
            cwex.throwException();
            fail("This method call should throw exception !");
        } catch (TestingException e) {
            //nothing to do; it is normal
        }
        
        try {
            ClassWithExceptions.throwExceptionStatic();
            fail("This method call should throw exception !");
        } catch (TestingException e) {
          //nothing to do; it is normal
        }
    }
    
    /**
     * The executed method are annotated and throws an exception first time
     * when are executed. The tests are made on static and not static methods.
     *
     */
    public final void testExecutionAndExceptionFirstTime() 
        throws TestingException {
        
        ClassWithExceptions.setNumbersOfExecutionsBeforeOk(1);
        
        try {
            cwex.throwException();
        } catch (TestingException e) {
            assertEquals(2, cwex.getMethodCounter());
        }
        
        try {
            ClassWithExceptions.throwExceptionStatic(); 
        } catch (TestingException e) {
            assertEquals(2, ClassWithExceptions.getStaticMethodCounter());
        }
        
    }
}
