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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on methods for handling execution failure.
 * If the method execution throws an instance of {@link #exceptionToCatch()}
 * the method is re-executed until the execution succeeds or the number 
 * of re-executions is{@link #maxRetry()}.
 * If {@link #waitTime()} is bigger than zero then the current thread will
 * wait(sleep) {@link #waitTime()} milliseconds between two consecutive 
 * executions.  
 * 
 * @author Adrian Citu
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Aoplib4jRetryExecution {
    
    /**
     * The number of times the method should be re-executed if the execution
     * throws an instance of {@link #exceptionToCatch()}.
     */
    int maxRetry();
    
    /**
     *
     * The exception to catch that will trigger a re-execution.
     */
    Class<? extends Throwable> exceptionToCatch();
    
    /**
     * The time to wait between 2 executions.
     */
    long waitTime() default 0;
}
