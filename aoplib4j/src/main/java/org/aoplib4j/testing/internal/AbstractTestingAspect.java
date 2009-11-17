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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Abstract aspect representing the base for all the testing aspects.
 * The basic idea is to intercept the execution of every tests and in every
 * test to intercept all the assertion failures (if an assertion fail, 
 * the framework will intercept it and the test will continue to execute). 
 * The failures are stored and are thrown at the end of the test execution. 
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public abstract class AbstractTestingAspect {
    
    
    /**
     * the logger to use.
     */
     static final Logger LOGGER = Logger
            .getLogger(AbstractTestingAspect.class.getName());
    
    /**
     * System variable used for the tests to inhibit the fail of the test
     * containing assertion failures.
     * 
     * THIS VARIABLE SHOULD BE USED ONLY FOR/BY THE (our)UNIT TESTS.
     */
     static final String AOPLIB4J_FAIL_INHIBIT = "doNotThrowException";
    
    /**
     * The value of the {@link #AOPLIB4J_FAIL_INHIBIT} system variable must
     * have for inhibiting the test failure.
     * 
     * THIS VARIABLE SHOULD BE USED ONLY FOR/BY THE (our)UNIT TESTS.
     */
     static final String AOPLIB4J_FAIL_INHIBIT_VALUE = "true";
    
    /**
     * buffer containing the stack trace of failed assertions.
     */
    private StringBuffer failedAssestionsStackTrace = new StringBuffer("");
    
    /**
     * number of failed assertions.
     */
    private int numberOfFailedAssertions = 0;
    
    /**
     * Abstract pointcut representing the call of a assert method. 
     */
    @Pointcut
    public abstract void assertCallPointcut();
    
    /**
     * Pointcut representing the execution of a test method. Every child aspect
     * should write the pointcut proper to his own way of executing tests.
     */
    @Pointcut
    public abstract void executionOfTestMethodPointcut();
    
    /**
     * Store the error stack trace as a String into 
     * {@link #failedAssestionsStackTrace} and updates by one the number of 
     * failed assertions.
     * 
     * @param error the error thrown by the testing framework 
     * (TestNG, JUnit, etc).
     */
     final void storeErrorInformation(final Error error) {
        failedAssestionsStackTrace.append(errorToString(error));
        numberOfFailedAssertions++;

    }
     
     /**
      * @param error the error for which the stack trace should be transformed 
      * in string.
      * 
      * @return string representation of the error stack trace.
      */
     private static String errorToString(final Error error) {
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw);
         error.printStackTrace(pw);
         
         return sw.toString();
     }
     
     /**
      * Advice executed after (<code>AfterReturning</code>) the 
      * {@link #executionOfTestMethodPointcut()} pointcut. 
      * If the executed test had at least one failed assert then the method
      * {@link #assertFail(String)} is executed having as message the failed
      * assertions stack traces. 
      */
     @AfterReturning("executionOfTestMethodPointcut()")
     public final void executionOfTestMethodAdvice() {       
         if (numberOfFailedAssertions != 0) {
             try {
                 String verificationErrorString = 
                     failedAssestionsStackTrace.toString();
                 
                 StringBuffer failMessage = new StringBuffer()
                     .append("Total number of failed assertions is ")
                     .append(numberOfFailedAssertions)
                     .append(System.getProperty("line.separator"))
                     .append(verificationErrorString);
                 
                 // inhibit the failure of the test; this is used only by
                 //our unit tests.
                 if (!AOPLIB4J_FAIL_INHIBIT_VALUE.equals(
                         System.getProperty(AOPLIB4J_FAIL_INHIBIT))) {
                    
                     //Assert.fail(failMessage.toString());
                     assertFail(failMessage.toString());
                 }
                 
             } finally {
                 //clean the variables for the following execution
                 failedAssestionsStackTrace = new StringBuffer();
                 numberOfFailedAssertions = 0;
             }
         }
     }
     
     /**
     * Abstract method representing the advice for the 
     * {@link #assertCallPointcut()}. Normally the child aspects should use
     * the <code>Around</code> advice to implement the behavior arround the 
     * call of the assert methods.
     *  
     * @param pjp object created by the AspectJ framework.
     * @throws Throwable the exception that can be thrown.
     * 
     */
    public abstract void assertCallAdvice(final ProceedingJoinPoint pjp) 
        throws Throwable; 
    
    /**
     * Fails a test with the given message. Every child aspect should use the 
     * proper assert class or another mechanism to mark a test failure. 
     * 
     * @param message the assertion error message.
     */
    public abstract void assertFail(final String message);

}
