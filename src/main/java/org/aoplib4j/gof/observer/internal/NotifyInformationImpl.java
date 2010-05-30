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
package org.aoplib4j.gof.observer.internal;

import java.lang.reflect.Method;

import org.aoplib4j.gof.observer.NotifyInformation;


/**
* Object automatically created by the framework at the execution of the 
* methods annotated with 
* {@link org.aoplib4j.gof.observer.NotifyObservers} 
* and passed as parameter 
* to the method {@link 
* org.aoplib4j.gof.observer.ObserverCallback#notifyObserver(
* NotifyInformation)}.
* 
* @author Adrian Citu
*
*/
final class NotifyInformationImpl implements NotifyInformation {

   /**
    * The instance on which the notification of the observers was 
    * triggered.
    */
   private GofSubject subject = null;
   
   /**
    * The {@link Method} on which the notification was triggered.
    */
   private Method method = null;
   
   /**
    * The arguments of the method on which the notification was 
    * triggered.
    */
   private Object[] methodArguments = null;
   
   /**
    * the instance of the observer that must be notified.
    */
   private GofObserver observer = null;

   /**
    * The default constructor.
    * 
    * @param sbj the instance on which the notification of the observers was 
    * triggered.
    * @param metd The method on which the notification was triggered.
    * @param arguments the arguments of the method on which the notification 
    * was triggered.
    * @param obs the instance of the observer that must be notified.
    */
    NotifyInformationImpl(final GofSubject sbj, 
           final Method metd, 
           final Object[] arguments,
           final GofObserver obs) {
       
       this.subject = sbj;
       this.method = metd;
       this.methodArguments = arguments;
       this.observer = obs;
   }

   /**
    * @return the {@link Method} on which the notification was triggered.
    */
   public Method getMethod() {
       return this.method;
   }
   
   /**
    * @return the arguments of the method on which the notification was 
    * triggered.
    */
   public Object[] getMethodArguments() {
       return this.methodArguments;
   }
   
   /**
    * @return the instance on which the notification of the observers was 
    * triggered.
    */
   public Object getSubject() {
       return this.subject;
   }
   
   /**
    * @return the instance of the observer that must be notified.
    */
   public Object getObserver() {
       return this.observer;
   }
}

