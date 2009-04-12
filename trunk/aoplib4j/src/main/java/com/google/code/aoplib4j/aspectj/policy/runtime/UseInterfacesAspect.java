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
package com.google.code.aoplib4j.aspectj.policy.runtime;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect implementing the item 52 ("Refer to objects by their interfaces") 
 * from the Effective Java (2-end edition).
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class UseInterfacesAspect extends AbstractRuntimePolicyAspect {
    
    /**
     * The logger.
     */
    private Logger logger =
        Logger.getLogger(UseInterfacesAspect.class.getName());

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
    @Pointcut("execution(" 
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
    public void collectionsImplemReturnMethodPointcut() {
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
    @Pointcut("execution("
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
    public void mapsImplemReturnMethodPointcut() {
    }
    
    /**
     * If the object returned by the 
     * {@link #collectionsImplemReturnMethodPointcut()} or by the 
     * {@link #mapsImplemReturnMethodPointcut()}
     * is not an interface then an error message will be logged or a runtime 
     * exception will be thrown ({@link UnsupportedOperationException}) if the
     * {@link AbstractRuntimePolicyAspect#THROW_EXCEPTION_FOR_VIOLATIONS} 
     * system property is not null.
     * 
     * @param ret the returning object from the pointcut execution.
     * @param staticJp the static part of the Aspectj joinpoint
     */
    @AfterReturning(
            value = "collectionsImplemReturnMethodPointcut()" 
                    + " || mapsImplemReturnMethodPointcut()",
                    returning = "ret")
    public void implementationNotInterfaceReturnMethodPointcut(
            final Object ret,
            final JoinPoint.StaticPart staticJp) {

        String errorMessage = "Method " + staticJp.getSignature()
                + " should not return an obkect, it should return an "
                + "interface.(see Item 52 from \"Effective Java\"" + " book)";

        throwExceptionOrLogIt(errorMessage, logger, Level.SEVERE);
    }
}
