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
 * Aspect that will find the use of <code>System.out.print*</code> and will
 * raise a compile warning.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class NoSysOutAspect {

    /**
     * Default constructor. This is an utility class so don't want to make 
     * instances of this class.
     */
    private NoSysOutAspect() {
        
    }
    
    /**
     * Poincut representing the calls to the {@link java.io.PrintStream} print
     * methods.
     *
     * <pre>
     * AspectJ pointcut:
     * call(* java.io.PrintStream.print*(..))
     * </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
            "call(* java.io.PrintStream.print*(..))")
    private static final String NO_SYS_OUT =  
        "Don't use System.out.print*(..). Use a logger API.";
    
}
