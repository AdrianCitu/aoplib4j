/*
 *  Copyright 2008-2008 the original author or authors.
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
package com.google.code.aoplib4j.aspectj.gof.observer;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.gof.observer.internal.GofObserver;
import com.google.code.aoplib4j.aspectj.gof.observer.internal.GofSubject;


/**
 * Object automatically created by the framework at the execution of the 
 * methods annotated with {@link NotifyObservers} and passed as parameter 
 * to the method {@link ObserverCallback#notifyObserver(NotifyInformation)}.
 * 
 * @author Adrian Citu
 *
 */
public final class NotifyInformation {

    /**
     * the {@link JoinPoint} delegate.
     */
    private JoinPoint delegate = null;

    /**
     * the instance of the observer that must be notified.
     */
    private GofObserver observer = null;

    /**
     * The default constructor.
     *
     * @param jp Aspect JoinPoint containing the execution environment.
     * @param obs the observer
     */
    public NotifyInformation(final JoinPoint jp, final GofObserver obs) {
        this.delegate = jp;
        this.observer = obs;
    }

    /**
     * @return the {@link Method} on which the notification was triggered.
     */
    public Method getMethod() {
        return ((MethodSignature) this.delegate.getSignature()).getMethod();
    }
    
    /**
     * @return the arguments of the method on which the notification was 
     * triggered.
     */
    public Object[] getMethodArguments() {
        return this.delegate.getArgs();
    }
    
    /**
     * @return the instance on which the notification of the observers was 
     * triggered.
     */
    public GofSubject getSubject() {
        return (GofSubject) this.delegate.getTarget();
    }
    
    /**
     * @return the instance of the observer that must be notified.
     */
    public GofObserver getObserver() {
        return this.observer;
    }
    
    /**
     * @return the AspectJ {@link JoinPoint} created at the execution of the 
     * method annotated with {@link NotifyObservers}. Use this if you need 
     */
    public JoinPoint getEecutionJoinPoint() {
        return this.delegate;
    }
}
