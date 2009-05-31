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
 * Aspect implementing the item 52 ("Refer to objects by their interfaces") 
 * from the Effective Java (2-end edition).
 * This is a partial implementation since it handles only the return value for
 * the {@link java.util.Collection} and {@link java.util.Map} interfaces.
 * 
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class UseInterfacesAspect {
    
    /**
     * Default constructor. This is an utility class so don't want to make 
     * instances of this class.
     */
    private UseInterfacesAspect() {
        
    }
    
    /**
     * Pointcut containing the execution of the methods returning 
     * implementations of {@link java.util.Map} and not an interface.
     * 
     *  <pre>
     *  execution(
     *      (
     *          java.util.HashMap+
     *          || java.util.Hashtable+ 
     *          || java.util.concurrent.ConcurrentHashMap+ 
     *          || java.util.WeakHashMap+ 
     *          || java.util.TreeMap+ 
     *          || java.awt.RenderingHints+ 
     *          || java.util.Properties+ 
     *          || java.util.LinkedHashMap+ 
     *          || java.util.IdentityHashMap+ 
     *          || java.util.EnumMap+ 
     *          || java.util.IdentityHashMap+ 
     *          || java.util.IdentityHashMap+ 
     *          || java.util.IdentityHashMap+ 
     *          || java.util.IdentityHashMap+ 
     *          || java.util.IdentityHashMap+
     *       ) 
     *       *..*.*(..)
     *     )
     *  </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
            "execution("
            + "(java.util.HashMap+"
            + " || java.util.Hashtable+" 
            + " || java.util.concurrent.ConcurrentHashMap+"
            + " || java.util.WeakHashMap+"
            + " || java.util.TreeMap+"
            + " || java.awt.RenderingHints+"
            + " || java.util.Properties+"
            + " || java.util.LinkedHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.EnumMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + ")"
            + " *..*.*(..))")
    private static final String NO_IMPL_MAPS = 
        "Return the Map interface or another interface not the real " 
        + "implementation";
    
    
    /**
     * Pointcut containing the execution of the methods that returns an 
     * implementation of {@link java.util.Collection} and not an interface.
     * 
     * <pre>
     * AspectJ pointcut:
     * execution(
     *            (
     *              java.util.TreeSet+ 
     *              || java.util.Vector+ 
     *              || java.util.concurrent.SynchronousQueue+ 
     *              || java.util.Stack+ 
     *              || javax.management.relation.RoleUnresolvedList+ 
     *              || javax.management.relation.RoleList+ 
     *              || java.util.PriorityQueue+ 
     *              || java.util.concurrent.PriorityBlockingQueue+ 
     *              || java.util.LinkedList+ 
     *              || java.util.LinkedHashSet+ 
     *              || java.util.concurrent.LinkedBlockingQueue+ 
     *              || java.util.HashSet+ 
     *              || java.util.concurrent.DelayQueue+ 
     *              || java.util.concurrent.CopyOnWriteArraySet+ 
     *              || java.util.concurrent.CopyOnWriteArrayList+ 
     *              || java.util.concurrent.ConcurrentLinkedQueue+ 
     *              || java.beans.beancontext.BeanContextSupport+ 
     *              || java.beans.beancontext.BeanContextServicesSupport+ 
     *              || java.util.ArrayList+ 
     *              || java.util.concurrent.ArrayBlockingQueue+ 
     *            ) 
     *           *..*.*(..)
     *         )
     * </pre>
     */
    @SuppressWarnings("unused")
    @DeclareWarning(
            "execution(" 
            + "(java.util.TreeSet+"
            + " || java.util.Vector+"
            + " || java.util.concurrent.SynchronousQueue+"
            + " || java.util.Stack+"
            + " || javax.management.relation.RoleUnresolvedList+"
            + " || javax.management.relation.RoleList+"
            + " || java.util.PriorityQueue+"
            + " || java.util.concurrent.PriorityBlockingQueue+"
            + " || java.util.LinkedList+"
            + " || java.util.LinkedHashSet+"
            + " || java.util.concurrent.LinkedBlockingQueue+"
            + " || java.util.HashSet+"
            + " || java.util.concurrent.DelayQueue+"
            + " || java.util.concurrent.CopyOnWriteArraySet+"
            + " || java.util.concurrent.CopyOnWriteArrayList+"
            + " || java.util.concurrent.ConcurrentLinkedQueue+"
            + " || java.beans.beancontext.BeanContextSupport+"
            + " || java.beans.beancontext.BeanContextServicesSupport+"
            + " || java.util.ArrayList+"
            + " || java.util.concurrent.ArrayBlockingQueue+"
            + ")"
            + " *..*.*(..))")
    private static final String NO_IMPL_COLLECTION = 
        "Return the Collection interface or another interface not the real " 
        + "implementation";
}
