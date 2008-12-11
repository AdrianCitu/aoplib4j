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
package com.google.code.aoplib4j.aspectj.gof.observer.client;

import com.google.code.aoplib4j.aspectj.gof.observer.ObserverCallback;
import com.google.code.aoplib4j.aspectj.gof.observer.aspect.NotifyInformation;

/**
 * @author Adrian Citu
 *
 */
public class ClientObserverCallBack extends ObserverCallback {

    /* (non-Javadoc)
     * @see com.google.code.aoplib4j.aspectj.gof.observer.ObserverCallback#update(com.google.code.aoplib4j.aspectj.gof.observer.SubjectInformation)
     */
    public void update(NotifyInformation si) {
        
        if(si.getMethod().getName().equals("sell")) {
          ((Client)si.getObserver()).buy();  
        }
    }

}
