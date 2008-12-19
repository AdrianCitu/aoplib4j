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

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
    private static Logger logger = Logger.getLogger(RetryExecutionAspect.class
            .getName());

    /**
     * Pointcut representing the execution of the methods annotated with the
     * {@link RetryExecution} annotation.
     * 
     * @param retryAnnotation
     *            the annotation of the method.
     */
    @Pointcut("execution("
            + "@com.google.code.aoplib4j.aspectj.failurehandling"
            + ".RetryExecution * * (..)) && @annotation(retryAnnotation)")
    void retryExecutionPointcut(final RetryExecution retryAnnotation) {
    }

    /**
     * Advice executed around the annotated method. 
     * @see RetryExecution.
     * 
     * @param retryAnnotation
     *            the annotation of the method.
     * @param pjp
     *            object created by AspectJ framework providing access to the
     *            around advice.
     * @return the result of the method intercepted by the advice.
     * @throws Throwable
     *             any exception thrown by the execution of the intercepted
     *             method.
     */
    @Around("retryExecutionPointcut(retryAnnotation)")
    public Object retryExecutionAdvice(final RetryExecution retryAnnotation,
            final ProceedingJoinPoint pjp) throws Throwable {

        /* retrieve the exception class to catch */
        final Class< ? extends Throwable> exceptionToCatch = retryAnnotation
                .exceptionToCatch();

        /* the maximum number of method re-execution */
        final int maxRetry = retryAnnotation.maxRetry();

        /* the waiting time between 2 consecutive executions */
        final long waitTime = retryAnnotation.waitTime();

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

    }
}