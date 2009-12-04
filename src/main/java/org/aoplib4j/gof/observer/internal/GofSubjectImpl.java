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

package org.aoplib4j.gof.observer.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Implementation of the {@link GofSubject} interface. The content of this class
 * will be introduced by AspectJ into the classes annotated with the 
 * {@link org.aoplib4j.gof.observer.Subject}
 * annotation.
 * 
 * @author Adrian Citu
 *
 */
public final class GofSubjectImpl implements GofSubject {

    /**
     * set of attached observers.
     */
    private Set < GofObserver > observers = 
        Collections.synchronizedSet(new HashSet< GofObserver >());
    
    /**
     * {@inheritDoc}
     */
    public void addObserver(final GofObserver o) {
        observers.add(o);
    }

    /**
     * {@inheritDoc}
     */
    public int countObservers() {
        return observers.size();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteObservers() {
        observers.clear();
    }


    /**
     * {@inheritDoc}
     */
    public GofObserver[] getAllObservers() {
        return this.observers.toArray(new GofObserver[this.observers.size()]);
    }
}

