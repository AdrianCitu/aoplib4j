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
package org.aoplib4j.policy.runtime;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect implementing the item 43 ("Return empty arrays or collections, 
 * not nulls") from the Effective Java (2-end edition).
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class ReturnEmptyCollectionsAspect 
    extends AbstractRuntimePolicyAspect {
    
    /**
     * The logger.
     */
    private static final Logger LOGGER =
        Logger.getLogger(ReturnEmptyCollectionsAspect.class.getName());
    
    /**
     * Pointcut containing the execution of all methods returning a 
     * {@link java.util.Map}
     * an {@link java.util.Collections} or an array of Objects.
     * 
     * <pre>
     * AspectJ pointcut:
     * 
     * execution(
     *      (
     *          java.util.Map+ 
     *          || java.util.Collection+ 
     *          || java.lang.Object[]+
     *      )  *..*.*(..)
     * ) 
     * </pre>
     */
    @Pointcut("execution("
    		+ "(java.util.Map+"
            + " || java.util.Collection+"
            + " || java.lang.Object[]+)"
            + " *..*.*(..)) ")
    public void notNullCollectionOrArrayPointcut() {
    }
    
    /**
     * If the object returned by the {@link #notNullCollectionOrArrayPointcut()}
     * is null then an error message will be logged or a runtime exception
     * will be thrown ({@link UnsupportedOperationException}) if the
     * {@link AbstractRuntimePolicyAspect#THROW_EXCEPTION_FOR_VIOLATIONS} 
     * system property is not null.
     * 
     * @param ret the returning object from the pointcut execution.
     * @param staticJp the static part of the Aspectj joinpoint
     */
    @AfterReturning(value = "notNullCollectionOrArrayPointcut()", 
                    returning = "ret")
    public void notNullCollectionOrArrayAdvice(final Object ret,
            final JoinPoint.StaticPart staticJp) {
        
        if (ret == null) {
            String errorMessage = "Method " + staticJp.getSignature() 
            + " should not return null value, it should return an " 
            + "empty Collection/Map/Array.(see Item 43 from \"Effective Java\""
            + " second edition book)";
            
            throwExceptionOrLogIt(errorMessage, LOGGER, Level.SEVERE);
        }
    }
    

}
