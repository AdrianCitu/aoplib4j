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
package org.aoplib4j.policy.compiletime;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareWarning;

/**
 * Aspect implementing the Item 14 (In public classes, use accessor methods, 
 * not public fields) from the Effective Java (2-end edition) book.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class OnlyPrivateFieldsAspect {

    /**
     * Default constructor. This is an utility class so don't want to make 
     * instances of this class.
     */
    private OnlyPrivateFieldsAspect() {
        
    }
    
    /**
     * Pointcut representing all the non-final no private fields. 
     * It's not a good idea to expose the fields of a class. 
     * <pre>
     * AspectJ pointcut:
     * get(!private !final * *) || set(!private !final * *)
     * </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
           "get(!private !final * *) || set(!private !final * *)")
    private static final String ONLY_PRIVATE_FIELDS = 
        "Don't use non final public, protected or default fields." 
        + " Make all non final fields private and use set/get methods.";
    
}
