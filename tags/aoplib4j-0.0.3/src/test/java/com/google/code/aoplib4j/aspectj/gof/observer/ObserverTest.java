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

import com.google.code.aoplib4j.aspectj.gof.observer.client.Client;
import com.google.code.aoplib4j.aspectj.gof.observer.client.Dealer;
import com.google.code.aoplib4j.aspectj.gof.observer.client.Informer;
import com.google.code.aoplib4j.aspectj.gof.observer.internal.GofObserver;
import com.google.code.aoplib4j.aspectj.gof.observer.internal.GofSubject;

/**
 * @author Adrian Citu
 *
 */
public class ObserverTest extends TestCase {

    /**
     * the dealer.
     */
    private Dealer dealer = new Dealer("dealer");
    
    /**
     * a client.
     */
    private Client client1 = new Client("client1");
    
    /**
     * a client.
     */
    private Client client2 = new Client("client2");
    
    /**
     * a client.
     */
    private Client informer1 = new Informer("Informer");

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        dealer.addClient(client1);
        dealer.addClient(client2);
        
        //add a second time the same client(to check the issue#23). 
        dealer.addClient(client2);
        dealer.addClient(informer1);

    }

    /**
     * Test the AspectJ introduction.
     */
    public final void testDealerIsSubject() {

        assertTrue("Dealer instance is not a Subject",
                dealer instanceof GofSubject);
    }

    /**
     * Test the AspectJ introduction.
     */
    public final void testClientIsObserver() {
        assertTrue(
               "Client class is not an Observer",
                client1 instanceof GofObserver);
    }

    /**
    * Test the total number of observers attached by {@link #setUp()}; should
    * be 3, 2 clients and an informer.
    */
   public final void testClientCout() {
       assertEquals(3, ((GofSubject) dealer).countObservers());
   }

    /**
     * test the notification of the observers.
     */
    public final void testClientBuys() {
        assertFalse(client1.hasADose());

        dealer.sell();

        assertTrue("Client should have a dose because", client1.hasADose());
    }
    
    /**
     * Test the unregistration of observers.
     */
    public final void testUnregisterObservers() {
        assertEquals(3, ((GofSubject) dealer).countObservers());
        dealer.removeAllClients();
        
        assertEquals(0, ((GofSubject) dealer).countObservers());
        dealer.noDealingAnymore();
        
        assertEquals(0, ((GofSubject) dealer).countObservers());
    }
    
    public final void testTheFakeObserver() {
        FakeObserver fo = new FakeObserver();
        
        fo.methodWithNoParamer();
        FakeObserver.staticMethodWithNoParamer();
        //should raise no error;
        
    }
    
    
    /**
     * Add a null observer; should have no error.
     */
    public final void testAddNullClient() {
        dealer.removeAllClients();
        
        dealer.addClient(null);
        assertEquals(0, ((GofSubject) dealer).countObservers());
        
    }
    
    /**
     * Dealer contains {@link RegisterObserver} on wrong methods(methods
     * with multiple parameters, static method). Verifies that the call of this
     * method does not trigger the add of a new observer.
     */
    public final void testRegisterObserverAnotationOnWrongMethod() {
        
        dealer.removeAllClients();
        dealer.doSomethingWithClient(client1, 0);
        
        assertEquals(0, ((GofSubject) dealer).countObservers());
        
        Dealer.staticDoSomethingWithClient(client1, 0);
        
        assertEquals(0, ((GofSubject) dealer).countObservers());
    }
}
