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


/**
 * Interface containing the notification informations.
 * Instances implementing this interface will be  created automatically by 
 * the framework at the execution of the methods annotated with 
 * {@link NotifyObservers} and passed as parameter 
 * to the method {@link ObserverCallback#notifyObserver(NotifyInformation)}.
 * 
 * see {@link ObserverCallback}.
 * 
 * @author Adrian Citu
 *
 */
public interface NotifyInformation {

    /**
     * @return the {@link Method} on which the notification was triggered.
     */
    Method getMethod();
    
    /**
     * @return the arguments of the method on which the notification was 
     * triggered.
     */
    Object[] getMethodArguments();
    
    /**
     * @return the instance on which the notification of the observers was 
     * triggered.
     */
    Object getSubject();
    
    /**
     * @return the instance of the observer that must be notified.
     */
    Object getObserver();
}
