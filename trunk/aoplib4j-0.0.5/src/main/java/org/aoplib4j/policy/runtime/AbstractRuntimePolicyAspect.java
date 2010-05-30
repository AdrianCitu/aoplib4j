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

import org.aspectj.lang.annotation.Aspect;

/**
 * Abstract class containing the common behavior for all the runtime policies
 * aspects. Another goal of this class is to contain the aspect precedence 
 * between multiple runtime policies aspects.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
abstract class AbstractRuntimePolicyAspect {

    
    /**
     * Default constructor.
     */
    protected AbstractRuntimePolicyAspect() {
        
    }
    
    /**
     * Name of the system variable to set if an exception should be thrown for
     * a policy violation. Normally this system value is used only by the JUnit 
     * tests.
     */
    public static final String THROW_EXCEPTION_FOR_VIOLATIONS = 
        "THROW_EXCEPTION_FOR_VIOLATIONS";
    
    
    /**
     * @return false if the system property 
     * {@link AbstractRuntimePolicyAspect#THROW_EXCEPTION_FOR_VIOLATIONS} 
     * is null, true otherwise.
     */
    private boolean throwExceptionActivated() {
        return System.getProperty(
            AbstractRuntimePolicyAspect.THROW_EXCEPTION_FOR_VIOLATIONS) != null;
    }
    
    /**
     * Method that will throw an {@link UnsupportedOperationException} if the
     *  {@link AbstractRuntimePolicyAspect#THROW_EXCEPTION_FOR_VIOLATIONS} 
     *  exists (using as message the <code>message</code> parameter) 
     *  or the message is logged using the <code>logger</code> and the 
     *  <code>level</code>.
     *  
     * @param message the exception or message to log.
     * @param logger the logger to use 
     * @param level the logger level
     */
    void throwExceptionOrLogIt(
            final String message, 
            final Logger logger, 
            final Level level) {
        
        if (throwExceptionActivated()) {
            throw new UnsupportedOperationException(message);
        } else {
            logger.log(level, message, new Throwable());   
        }
    }
}
