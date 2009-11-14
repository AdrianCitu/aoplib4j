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
package org.aoplib4j.test.junit.internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect that will intercept any JUnit assertion violation and will
 * throw them at the end of the test (before tearDown).
 * 
 * @author Adrian Citu
 *
 */
//@Aspect("percflow(executionOfJUnit3TestMethodsPointcut()" 
//        + " || executionOfJUnit4TestMethodsPointcut())")
@Aspect
public final class VerifyAspect {
    
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
     * the logger to use.
     */
    private static final Logger LOGGER = Logger
            .getLogger(VerifyAspect.class.getName());
    
    
    /**
     *  Pointcut representing the call of all the JUnit (3 or 4) assert* 
     *  methods.
     *  <pre>
     *  AspectJ pointcut:
     *  call(static public void junit.framework.Assert+.assert*(..))
     *  </pre>
     */
    @Pointcut("call(static public void junit.framework.Assert+.assert*(..))"
    		+ " || call(static public void org.junit.Assert+.assert*(..))")
    public void assertCallPointcut() {
        
    }

    /**
     *  Pointcut representing the execution of any JUni3 test method.
     *  <pre>
     *  AspectJ pointcut:
     *  execution(public void junit.framework.TestCase+.test*())
     *  </pre>
     */
    @Pointcut("execution(public void junit.framework.TestCase+.test*())")
    public void executionOfJUnit3TestMethodsPointcut() {
    }

    /**
     *  Pointcut representing the execution of any JUni4 test method.
     *  <pre>
     *  AspectJ pointcut:
     *  execution(@org.junit.Test * * ())
     *  </pre>
     */
    @Pointcut("execution(@org.junit.Test * * ())")
    public void executionOfJUnit4TestMethodsPointcut() {  
    }
    
    /**
     * Advice that executed around the call of assert methods.
     * The advice executes the assertion and catch the 
     * {@link AssertionFailedError}(for JUnit3) or {@link AssertionError}
     * (for JUnit4) if any and computes and keep the error stack trace for later
     *  use.
     * 
     * @param pjp object created by the AspectJ framework.
     * @throws Throwable the exception that can be thrown by the 
     * {@link ProceedingJoinPoint#proceed()} execution.
     * 
     * @see {@link #executionOfJUnit3TestMethodsAdvice()}.
     * @see {@link #executionOfJUnit4TestMethodsAdvice()}.
     */
    @Around("assertCallPointcut()")
    public void assertCallAdvice(final ProceedingJoinPoint pjp) 
        throws Throwable {
        
        try {
            pjp.proceed();
        } catch (AssertionFailedError junit3Error) {            
            storeJUnitErrorInformation(junit3Error);
            
            LOGGER.info("AssertionFailedError " 
                    + pjp.getSignature().toLongString());
        } catch (AssertionError junit4Error) {
            storeJUnitErrorInformation(junit4Error);
            
            LOGGER.info("AssertionFailedError " 
                    + pjp.getSignature().toLongString());
        }

    }
    
    /**
     * Advice executed after (ArroundReturning) the 
     * {@link #executionOfJUnit3TestMethodsPointcut()} or 
     * {@link #executionOfJUnit4TestMethodsPointcut()} pointcuts. 
     * If the executed
     * JUnit test had at least one failed assert then an 
     * {@link Assert#fail(String)} is executed having as message the failed
     * assertions stack traces. 
     */
    @AfterReturning("executionOfJUnit3TestMethodsPointcut() "
    		 + "|| executionOfJUnit4TestMethodsPointcut()")
    public void executionOfJUnitTestMethodsAdvice() {       
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
                   
                    Assert.fail(failMessage.toString());
                }
                
            } finally {
                //clean the variables for the following execution
                failedAssestionsStackTrace = new StringBuffer();
                numberOfFailedAssertions = 0;
            }
        }
    }

    /**
     * @param error the error for which the stack trace should be transformed 
     * in string.
     * 
     * @return string representation of the error stack trace.
     */
    private static String junitErrorToString(final Error error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        
        return sw.toString();
    }
    

    /**
     * Store the error stack trace as a String into 
     * {@link #failedAssestionsStackTrace} and updates by one the number of 
     * failed assertions.
     * 
     * @param junitError the error thrown by JUnit.
     */
    private void storeJUnitErrorInformation(final Error junitError) {
        failedAssestionsStackTrace.append(junitErrorToString(junitError));
        numberOfFailedAssertions++;

    }
}
