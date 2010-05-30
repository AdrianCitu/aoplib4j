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
package org.aoplib4j.modularity.clas;

import org.aoplib4j.modularity.Aoplib4jClassBoundary;

/**
 * Subclass of the {@link ClasCalledClass}; the parent class is annotated with 
 * {@link Aoplib4jClassBoundary} annotation.This class is used by the JUnit test to 
 * verify the behavior of subclasses in the case of boundary violations.
 * 
 * 
 * @author Adrian Citu
 *
 */
public class ChildOfCalledClass extends ClasCalledClass {

    @Override
    public void calledMethod() {
        super.calledMethod();
    }

    @Override
    public void callerCalledMethod() {
        super.callerCalledMethod();
    }

    
}
