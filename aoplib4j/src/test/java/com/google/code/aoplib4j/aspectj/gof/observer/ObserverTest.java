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

import junit.framework.TestCase;

import com.google.code.aoplib4j.aspectj.gof.observer.Observer;
import com.google.code.aoplib4j.aspectj.gof.observer.Subject;
import com.google.code.aoplib4j.aspectj.gof.observer.client.Client;
import com.google.code.aoplib4j.aspectj.gof.observer.client.Dealer;
import com.google.code.aoplib4j.aspectj.gof.observer.client.Informer;

/**
 * @author Adrian Citu
 *
 */
public class ObserverTest extends TestCase {

    private Dealer dealer = new Dealer("dealer");
    private Client client1 = new Client("client1");
    private Client client2 = new Client("client2");
    private Client informer1 = new Informer("Informer");

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dealer.addClient(client1);
        dealer.addClient(client2);
        dealer.addClient(informer1);

    }

    /**
     *
     */
    public final void testDealerIsSubject() {

        assertTrue("Dealer instance is not a Subject",
                dealer instanceof Subject);
    }

    /**
     *
     */
    public final void testClientIsObserver() {
        assertTrue(
               "Client class is not an Observer",
                client1 instanceof Observer);
    }

    /**
    *
    */
   public final void testClientCout() {
       assertEquals(3, ((Subject) dealer).countObservers());
   }

    /**
     *
     */
    public final void testClientBuys() {
        assertFalse(client1.hasADose());

        dealer.sell();

        assertTrue("Client should have a dose because", client1.hasADose());
    }

    /**
     *
     */
/*    public final void testPoliceRazia() {

        int initialNumber = ((Subject) dealer).countObservers();
        client1.catchByPolice();
        int afterNumber = ((Subject) dealer).countObservers();
        assertEquals(afterNumber, initialNumber + 1);

    }*/
}
