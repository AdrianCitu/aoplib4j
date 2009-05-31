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
package com.google.code.aoplib4j.aspectj.gof.observer;



/**
 * Abstract class that must be implemented to code the behavior
 * of an {@link Observer} when is notified by a {@link Subject}.
 *  
 * @author Adrian Citu
 *
 */
public abstract class ObserverCallback {

    /**
     * Default constructor.
     */
    public ObserverCallback() {

    }

    /**
     * Method called when a method annotated with the {@link NotifyObservers} 
     * annotation is executed on a class annotated with the {@link Subject}
     * annotation.
     * @see NotifyInformation
     * 
     * @param notifyInformation the object containing the notification 
     * information.
     */
    public abstract void notifyObserver(NotifyInformation notifyInformation);
}

