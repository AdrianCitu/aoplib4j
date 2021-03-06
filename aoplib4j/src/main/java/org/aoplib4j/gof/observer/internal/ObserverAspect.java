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

package org.aoplib4j.gof.observer.internal;

import java.lang.reflect.Method;

import org.aoplib4j.gof.observer.NotifyInformation;
import org.aoplib4j.gof.observer.Aoplib4jObserver;
import org.aoplib4j.gof.observer.ObserverCallback;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * The AspectJ aspect implementing the Observer pattern.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class ObserverAspect {
    
    /**
     * the classes annotated with the 
     * {@link org.aoplib4j.gof.observer.Aoplib4jSubject} annotation 
     * will automatically 
     * implement the {@link GofSubject} interface and the implementation
     * will be {@link GofSubjectImpl}.
     */
    @SuppressWarnings("unused")
    @DeclareParents(
            value = "@org.aoplib4j.gof.observer.Aoplib4jSubject *",
            defaultImpl = GofSubjectImpl.class)
    private GofSubject subject = null;

    /**
     * the classes annotated with the {@link Aoplib4jObserver} annotation will 
     * automatically 
     * implement the {@link GofObserver} interface.
     */
    @SuppressWarnings("unused")
    @DeclareParents(
            value =
                "@org.aoplib4j.gof.observer.Aoplib4jObserver *")
    private GofObserver observer = null;

    /**
     * AspectJ pointcut represented by the call of a method annotated with 
     * {@link org.aoplib4j.gof.observer.Aoplib4jRegisterObserver}
     * and having as unique parameter a class annotated
     * with the {@link Aoplib4jObserver} annotation.
     * 
     * <pre>
     * AspectJ pointcut:
     * execution(@org.aoplib4j.gof.observer
     *  .RegisterObserver * * (..))
     *  && this(sbj) && args(obs)
     * 
     * </pre>
     * 
     * @param sbj the instance of {@link GofObserver} on which the annotated
     * method is called.
     * @param obs the instance of {@link GofObserver} that is the parameter 
     * passed to the annotated method.
     */
    @Pointcut("execution("
            + "@org.aoplib4j.gof.observer"
            + ".Aoplib4jRegisterObserver * * (..)) "
            + "&& this(sbj) && args(obs)")
    public void addObserverPointcut(
            final GofSubject sbj, final GofObserver obs) {

    }

    /**
     * AspectJ advice for the pointcut 
     * {@link #addObserverPointcut(GofSubject, GofObserver)}. The advice
     * will add the obs object as a observer of the sbj object calling
     * {@link GofSubject#addObserver(GofObserver)}.
     * 
     * @param sbj instance of {@link GofSubject}.
     * @param obs instance of {@link GofObserver}.
     */
    @AfterReturning("addObserverPointcut(sbj, obs)")
    public void addObserverAdvice(final GofSubject sbj, final GofObserver obs) {
        
        if (sbj != null && obs != null) {
            sbj.addObserver(obs);
        }
    }

    /**
     * Pointcut representing the execution of the method annotated with the
     * {@link org.aoplib4j.gof.observer.NotifyObservers} 
     * annotation.
     * 
     * <pre>
     * AspectJ pointcut:
     * execution(@org.aoplib4j.gof.observer
     *  .NotifyObservers * * (..))
     * && this(sbj)
     * </pre>
     * @param sbj instance of {@link GofSubject}
     */
    @Pointcut("execution(@org.aoplib4j.gof.observer."
            + "NotifyObservers * * (..))"
            + " && this(sbj)")
    public void notifyObserversPointcut(final GofSubject sbj) {

    }

    /**
     * Advice for the pointcut {@link #notifyObserversPointcut(GofSubject)}.
     * For every {@link GofObserver} the advice will instantiate a
     * class extending {@link ObserverCallback} and call the method
     * {@link ObserverCallback#notifyObserver(NotifyInformation)}.
     * The class name extending the {@link ObserverCallback} is retrieved by
     * {@link Aoplib4jObserver#callbackClass()}.
     * 
     * @param sbj instance of {@link GofSubject}.
     * @param thisJoinPoint AspectJ {@link JoinPoint}.
     * 
     * @throws InstantiationException thrown when creating the 
     * {@link ObserverCallback} instance by introspection.
     * 
     * @throws IllegalAccessException thrown when creating the 
     * {@link ObserverCallback} instance by introspection.
     */
    @AfterReturning("notifyObserversPointcut(sbj)")
    public void notifyObserverAdvice(final GofSubject sbj,
            final JoinPoint thisJoinPoint)
        throws InstantiationException, IllegalAccessException {

        GofObserver[] obs = sbj.getAllObservers();

        for (GofObserver ob : obs) {
            Aoplib4jObserver annotation =
                ob.getClass().getAnnotation(Aoplib4jObserver.class);

            Class<? extends ObserverCallback> callbackClass =
                annotation.callbackClass();

            ObserverCallback instance = callbackClass.newInstance();
            instance.notifyObserver(
                    this.cresteInstanceofNotifyInformation(thisJoinPoint, ob));
        }
    }
    
    /**
     * Pointcut representing the execution of the method annotated with the
     * {@link org.aoplib4j.gof.observer.Aoplib4jUnregisterObservers}
     * annotation.
     * 
     * @param sbj instance of {@link GofSubject}
     */
    @Pointcut("execution(@org.aoplib4j.gof.observer."
            + "Aoplib4jUnregisterObservers * * (..))"
            + " && this(sbj)")
    public void unregisterObserversPointcut(final GofSubject sbj) {
    }
    
    /**
     * Advice for the pointcut {@link #unregisterObserversPointcut(GofSubject)}.
     * All the observers attached to the sbj will be removed.
     * 
     * @param sbj instance of {@link GofSubject}.
     */
    @AfterReturning("unregisterObserversPointcut(sbj)")
    public void unregisterObserversAdvice(final GofSubject sbj) {
        sbj.deleteObservers();
    }
    
    /**
     * @param jp AspectJ join point.
     * @param obs instance of the observer.
     * @return a new instance of {@link NotifyInformation}.
     */
    private NotifyInformation cresteInstanceofNotifyInformation(
        final JoinPoint jp, 
        final GofObserver obs) {
            
            Method method =  ((MethodSignature) jp.getSignature()).getMethod();
            Object[] methodArguments = jp.getArgs();
            GofSubject sbj =  (GofSubject) jp.getTarget();
            
            return new NotifyInformationImpl(sbj, method, methodArguments, obs);
        }
}
