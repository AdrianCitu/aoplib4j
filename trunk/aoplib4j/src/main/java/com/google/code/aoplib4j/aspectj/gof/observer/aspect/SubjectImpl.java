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

package com.google.code.aoplib4j.aspectj.gof.observer.aspect;

import java.util.Vector;

import com.google.code.aoplib4j.aspectj.gof.observer.Observer;
import com.google.code.aoplib4j.aspectj.gof.observer.Subject;

/**
 * @author Adrian Citu
 *
 */
public class SubjectImpl implements Subject {
    
    private Vector<Observer> obs = new Vector<Observer>();

    /* (non-Javadoc)
     * @see org.aoplibrary.aspectj.gof.observer.Subject#addObserver(org.aoplibrary.aspectj.gof.observer.Observer)
     */
    public void addObserver(Observer o) {
        obs.add(o);
    }

    /* (non-Javadoc)
     * @see org.aoplibrary.aspectj.gof.observer.Subject#countObservers()
     */
    public int countObservers() {
        return obs.size();
    }

    /* (non-Javadoc)
     * @see org.aoplibrary.aspectj.gof.observer.Subject#deleteObservers()
     */
    public void deleteObservers() {
        obs.clear();
    }

    /* (non-Javadoc)
     * @see org.aoplibrary.aspectj.gof.observer.Subject#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return obs.equals(obj);
    }

    /* (non-Javadoc)
     * @see org.aoplibrary.aspectj.gof.observer.Subject#hashCode()
     */
    public int hashCode() {
        return obs.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return obs.toString();
    }
    
    public Observer[] getAllObservers() {
        return this.obs.toArray(new Observer[this.obs.size()]);
    }
}
