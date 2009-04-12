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
package com.google.code.aoplib4j.aspectj.policy.compiletime;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareWarning;

/**
 * Aspect containing compiling time policy enforcement. The classes should be 
 * compiled (weaved) with the AspectJ weaver (ajc). 
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class CompileTimePolicyAspect {
    
    /**
     * Default constructor; Just to fix the CheckStyle warning.
     */
    private CompileTimePolicyAspect() {
        
    }
    
    /**
     * Poincut representing the access to the {@link System#out} field.
     *
     * <pre>
     * AspectJ pointcut:
     * get(public static final java.io.PrintStream java.lang.System.out)
     * </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
            "get(public static final java.io.PrintStream java.lang.System.out)")
    private static final String NO_SYS_OUT =  "No use of System.out allowed";

   /**
     * Pointcut representing all the non-final public fields. It's not a good 
     * idea to expose the fields of a class. 
     * <pre>
     * AspectJ pointcut:
     * get(public !final * *) || set(public * *)
     * </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
           "get(public !final * *) || set(public * *)")
    private static final String NO_PUBLIC_FIELDS = "Don't use public fields";
}
