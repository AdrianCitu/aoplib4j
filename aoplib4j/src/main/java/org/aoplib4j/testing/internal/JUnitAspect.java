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

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect implementation for the JUnit(3 and 4) framework.
 *  
 * For every executed test, the aspect will intercept any JUnit assertion 
 * violation and will throw them at the end of the test execution 
 * (before tearDown).
 * 
 * @author Adrian Citu
 *
 */
//@Aspect("percflow(executionOfJUnit3TestMethodsPointcut()" 
//        + " || executionOfJUnit4TestMethodsPointcut())")
@Aspect
public final class JUnitAspect extends AbstractTestingAspect {    
    
    /**
     * {@inheritDoc}
     * Pointcut representing the call of all the JUnit (3 or 4) assert* 
     *  methods.
     * <pre>
     *  AspectJ pointcut:
     *  call(static public void junit.framework.Assert+.assert*(..))
     *  || call(static public void org.junit.Assert+.assert*(..))
     * </pre>
     */
    @Pointcut("call(static public void junit.framework.Assert+.assert*(..))"
            + " || call(static public void org.junit.Assert+.assert*(..))")
    @Override
    public void assertCallPointcut() {
        
    }
    
    /**
     * {@inheritDoc}
     * 
     * Pointcut representing the execution of any JUni4 or JUnit3 test method.
     * 
     * <pre>
     * AspectJ pointcut:
     *  execution(public void junit.framework.TestCase+.test*())
     *   || execution(@org.junit.Test * * ())
     * </pre>
     * 
     * @see AbstractTestingAspect#executionOfTestMethodPointcut()
     */
    @Pointcut("execution(public void junit.framework.TestCase+.test*())"
            + " || execution(@org.junit.Test * * ())")
    @Override        
    public void executionOfTestMethodPointcut() {
        
    }
    
    /**
     * {@inheritDoc}
     * 
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
     */
    @Around("assertCallPointcut()")
    @Override
    public void assertCallAdvice(final ProceedingJoinPoint pjp) 
        throws Throwable {
        
        try {
            pjp.proceed();
        } catch (AssertionFailedError junit3Error) {            
            storeErrorInformation(junit3Error);
            
            LOGGER.info("AssertionFailedError " 
                    + pjp.getSignature().toLongString());
        } catch (AssertionError junit4Error) {
            storeErrorInformation(junit4Error);
            
            LOGGER.info("AssertionFailedError " 
                    + pjp.getSignature().toLongString());
        }

    }

    /**
     * {@inheritDoc}
     * Implementation using the JUnit {@link Assert#fail(String)}.
     * 
     * @see org.aoplib4j.testing.internal.AbstractTestingAspect#assertFail(
     * java.lang.String)
     */
    @Override
    public void assertFail(final String message) {
        Assert.fail(message);
    }
    
}
