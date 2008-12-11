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

package com.google.code.aoplib4j.aspectj.gof.observer.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

import com.google.code.aoplib4j.aspectj.gof.observer.Observer;
import com.google.code.aoplib4j.aspectj.gof.observer.ObserverCallback;
import com.google.code.aoplib4j.aspectj.gof.observer.Subject;

/**
 * @author Adrian Citu
 *
 */
@Aspect
public class ObserverAspect {

    @SuppressWarnings("unused")
    @DeclareParents(
            value = "@com.google.code.aoplib4j.aspectj.gof.observer.annotation.Subject *",
            defaultImpl = SubjectImpl.class)
    private Subject subject = null;

    @SuppressWarnings("unused")
    @DeclareParents(
            value =
                "@com.google.code.aoplib4j.aspectj.gof.observer.annotation.Observer *",
            defaultImpl = ObserverImpl.class)
    private Observer observer = null;

    @Pointcut("execution(@com.google.code.aoplib4j.aspectj.gof.observer.annotation.AddAsObserver * * " 
            + "(..)) && this(sbj) && args(obs)")
    void addObserverPointcut(Subject sbj, Observer obs){

    }

    @AfterReturning("addObserverPointcut(sbj, obs)")
    public void addObserverAdvice(final Subject sbj, final Observer obs) {
        sbj.addObserver(obs);
    }

    @Pointcut("execution(@com.google.code.aoplib4j.aspectj.gof.observer" 
            + ".annotation.NotifyObservers * * " 
            + "(..)) && this(sbj)")
    void notifyObserversPointcut(final Subject sbj){

    }

    @AfterReturning("notifyObserversPointcut(sbj)")
    public void addObserverAdvice(final Subject sbj,
            final JoinPoint thisJoinPoint)
        throws InstantiationException, IllegalAccessException {

        Observer[] obs = sbj.getAllObservers();

        for (Observer ob : obs) {
            com.google.code.aoplib4j.aspectj.gof.observer.annotation.Observer
            annotation =
                ob.getClass().getAnnotation(
                        com.google.code.aoplib4j.aspectj.gof
                            .observer.annotation.Observer.class);

            Class < ? extends ObserverCallback > callbackClass =
                annotation.callbackClass();

            ObserverCallback instance = callbackClass.newInstance();
            instance.update(new NotifyInformation(thisJoinPoint, ob));
        }
    }
}
