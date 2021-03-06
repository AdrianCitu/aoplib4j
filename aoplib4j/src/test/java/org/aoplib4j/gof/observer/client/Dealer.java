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

import java.util.Vector;

import org.aoplib4j.gof.observer.NotifyObservers;
import org.aoplib4j.gof.observer.Aoplib4jRegisterObserver;
import org.aoplib4j.gof.observer.Aoplib4jSubject;
import org.aoplib4j.gof.observer.Aoplib4jUnregisterObservers;


/**
 * A drug dealer class.
 *
 * @author Adrian Citu
 *
 */
@Aoplib4jSubject
public class Dealer {

    /**
     *
     */
    private String name = null;

    /**
     * @param name
     */
    public Dealer(String name) {
        this.name = name;
    }

    /**
     *
     */
    private Vector<Client> clients = new Vector<Client> ();

    /**
     *
     */
    @NotifyObservers
    public final void sell() {
        System.out.println("I sell !!");
    }

    /**
     * @param o a client to add
     */
    @Aoplib4jRegisterObserver
    public final void addClient(final Client o) {
        clients.add(o);
    }

    /**
     * @param o a client
     * @param i param.
     */
    @Aoplib4jRegisterObserver
    public final void doSomethingWithClient(final Client o, final int i) {
        //do nothing.
    }

    /**
     * @param o a client
     * @param i param.
     */
    @Aoplib4jRegisterObserver
    public static final void staticDoSomethingWithClient(
            final Client o, final int i) {
        //do nothing.
    }
    
    /**
     * @param o
     * @return
     */
    public final boolean removeClient(Object o) {
        return clients.remove(o);
    }

    /**
     * 
     */
    @Aoplib4jUnregisterObservers
    public void removeAllClients() {
        clients.removeAllElements();
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }
    
    @Aoplib4jUnregisterObservers
    public void noDealingAnymore() {
        
    }

}
