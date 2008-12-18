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
package com.google.code.aoplib4j.aspectj.failurehandling.internal;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.failurehandling.RetryExecution;

/**
 * Aspect that handles the method execution exceptions.
 *  
 * @author Adrian Citu
 *
 */
@Aspect
final class RetryExecutionAspect {
    
    /**
     * the logger to use.
     */
    private static Logger logger = 
        Logger.getLogger(RetryExecutionAspect.class.getName());
    
    /**
     * Pointcut representing the execution of the methods annotated with the 
     * {@link RetryExecution} annotation.
     */
    @Pointcut("execution("
            + "@com.google.code.aoplib4j.aspectj.failurehandling"
            + ".RetryExecution * * (..)) ")
    void retryExecutionPointcut() {
    }
    
    /**
     * @param pjp object created by AspectJ framework providing access to the 
     * around advice. 
     * @return the result of the method intercepted by the advice.
     * @throws Throwable any exception thrown by the execution of the 
     * intercepted method.
     */
    @Around("retryExecutionPointcut()")
    public Object retryExecutionAdvice(final ProceedingJoinPoint pjp) 
        throws Throwable {
        
        RetryExecution re = getMethodAnnotation(pjp);
        
        if (re != null) {
            /*retrieve the exception class to catch*/
            final Class< ? extends Throwable > exceptionToCatch = 
                re.exceptionToCatch();
            
            /*the maximum number of method re-execution*/
            final int maxRetry = re.maxRetry();
            
            /*the waiting time between 2 consecutive executions */
            final long waitTime = re.waitTime();
            
            int counter = 0;
            
            while (true) {
                try {
                    
                    return pjp.proceed();
                } catch (Throwable e) {
                    logger.info("Catched the exception " + e);
                    
                    if (exceptionToCatch.equals(e.getClass())) {
                        if (++counter > maxRetry) {
                            throw e;
                        } else {
                            if (waitTime > 0) {
                                Thread.sleep(waitTime);  
                            }
                            logger.info("The method will be re-executed " 
                                    + (maxRetry - counter) + " times");
                        }
                    } else {
                        logger.info("Throw this exception because it's not " 
                                + "treated by the aspect:" + e);
                        throw e;
                    }
                } 
            }
        } else {
            return pjp.proceed();
        }
   
        
    }

    /**
     * 
     * @param pjp object created by AspectJ framework representing the execution
     * joinpoint.
     * @return annotation attached to the method retrieved from the 
     * {@link ProceedingJoinPoint}.
     */
    private RetryExecution getMethodAnnotation(final ProceedingJoinPoint pjp) {
        RetryExecution returnValue = null;
        
        Signature sgn = pjp.getSignature();
        
        if (sgn instanceof MethodSignature) {
            
            Method method = ((MethodSignature) sgn).getMethod();
            logger.info("Executing method :" + method.toGenericString());
            returnValue = method.getAnnotation(RetryExecution.class);
        }
        
        return returnValue;
    }
}
