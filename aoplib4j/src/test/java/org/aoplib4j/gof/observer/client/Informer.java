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
 * A class representing an informer.
 *
 * @author Adrian Citu
 *
 */
@Aoplib4jObserver(callbackClass = InformerObserverCallBack.class)
public final class Informer extends Client {

    public Informer(final String name) {
        super("Informer code name:" + name);
    }

    public void buy() {
        super.buy();
    }
}
