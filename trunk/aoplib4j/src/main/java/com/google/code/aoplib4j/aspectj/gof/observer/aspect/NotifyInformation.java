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
package com.google.code.aoplib4j.aspectj.gof.observer.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.gof.observer.Observer;
import com.google.code.aoplib4j.aspectj.gof.observer.Subject;

/**
 * @author Adrian Citu
 *
 */
public final class NotifyInformation {

    /**
     *
     */
    private JoinPoint delegate = null;

    /**
     *
     */
    private Observer observer = null;

    /**
     * The default constructor.
     *
     * @param jp Aspect JoinPoint containing the execution environment.
     * @param obs the observer
     */
    NotifyInformation(final JoinPoint jp, final Observer obs) {
        this.delegate = jp;
        this.observer = obs;
    }

    /**
     * @return
     */
    public Method getMethod() {
        return ((MethodSignature)this.delegate.getSignature()).getMethod();
    }

    /**
     * @return
     */
    public Subject getSubject() {
        return (Subject) this.delegate.getTarget();
    }

    /**
     * @return
     */
    public JoinPoint getExecutionJoinPoint() {
        return this.delegate;
    }
    
    public Observer getObserver() {
        return this.observer;
    }
}
