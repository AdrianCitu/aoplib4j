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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.testng.Assert;

/**
 * Aspect implementation for the TestNG V5.* framework.
 *  
 * The aspect that will intercept any assertion violation and will
 * throw them at the end of the test.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class TestNGAspect extends AbstractTestingAspect {
    
    
    
    /**
     *  {@inheritDoc}
     *  
     *  Pointcut representing the call of all the TestNG assert* 
     *  methods.
     *  <pre>
     *  AspectJ pointcut:
     *  call(static public void org.testng.Assert+.assert*(..))"
     *  </pre>
     */
    @Pointcut("call(static public void org.testng.Assert+.assert*(..))")
    @Override
    public void assertCallPointcut() {
        
    }


    /**
     * {@inheritDoc}
     * 
     * Pointcut representing the execution of any TestNG test method.
     * 
     * <pre>
     * AspectJ pointcut:
     *  execution(@org.testng.annotations.Test * * ())
     * </pre>
     * 
     * @see org.aoplib4j.testing.internal.AbstractTestingAspect#
     *  executionOfTestMethodPointcut()
     */
    @Pointcut("execution(@org.testng.annotations.Test * * ())")
    @Override
    public void executionOfTestMethodPointcut() {  
    }
    
    /**
     * {@inheritDoc}
     * 
     * Advice that executed around the call of assert methods.
     * The advice executes the assertion and catch the {@link AssertionError}
     *  (if any) and computes and keep the error stack trace for later use.
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
        } catch (AssertionError testNGError) {
            storeErrorInformation(testNGError);
            
            LOGGER.info("AssertionFailedError " 
                    + pjp.getSignature().toLongString());
        }
    }


    /**
     * {@inheritDoc}
     * Implementation using the TestNG {@link Assert#fail()}.
     * 
     * @see org.aoplib4j.testing.internal.AbstractTestingAspect#
     *  assertFail(java.lang.String)
     */
    @Override
    public void assertFail(final String message) {
        Assert.fail(message);
        
    }
    
}
