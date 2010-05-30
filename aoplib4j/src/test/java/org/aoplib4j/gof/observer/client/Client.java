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

package org.aoplib4j.gof.observer.client;

import org.aoplib4j.gof.observer.Aoplib4jObserver;

/**
 * A drug dealer client.
 *
 * @author Adrian Citu
 *
 */
@Aoplib4jObserver(callbackClass = ClientObserverCallBack.class)
public class Client {

    private String name = null;
    
    private boolean dose = false;
    
    public Client(String name) {
        this.name = name;
    }
    
    public void buy() {
        System.out.println("I buy !");
        dose = true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean hasADose() {
        return dose;
    }
    
    public void catchByPolice() {
        System.out.println("Catch by the police !");
    }
}
