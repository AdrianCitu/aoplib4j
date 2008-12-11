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

package com.google.code.aoplib4j.aspectj.gof.observer.client;

import java.util.Vector;

import com.google.code.aoplib4j.aspectj.gof.observer.annotation.AddAsObserver;
import com.google.code.aoplib4j.aspectj.gof.observer.annotation.NotifyObservers;
import com.google.code.aoplib4j.aspectj.gof.observer.annotation.Subject;

/**
 * A drug dealer class.
 *
 * @author Adrian Citu
 *
 */
@Subject
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
     * @return true if added or false otherwise.
     */
    @AddAsObserver
    public final boolean addClient(final Client o) {
        return clients.add(o);
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
    public void removeAllClients() {
        clients.removeAllElements();
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

}
