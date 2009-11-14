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

import org.aoplib4j.modularity.ClassBoundary;
import org.aoplib4j.modularity.ListType;
import org.aoplib4j.modularity.ThrowErrorBoundaryCallback;



/**
 * Target class; annotated with the {@link ClassBoundary} annotation.  
 * This class forbidden the calls from the {@link ForbiddenCallerClass} class.
 *  
 * @author Adrian Citu
 *
 */
@ClassBoundary(
        classesList={ForbiddenCallerClass.class}, 
        callbackClass=ThrowErrorBoundaryCallback.class,
        classesListType = ListType.BLACKLIST)
public class ClasCalledClass {

    /**
     * A normal method.
     */
    public void calledMethod() {
        
    }
    
    /**
     * A normal static method.
     */
    public static void calledStaticMethod() {
        
    }
    
    /**
     * Method that calls the {@link #calledMethod()}; This is done to test 
     * the the boundary inside the own class. 
     */
    public void callerCalledMethod() {
        this.calledMethod();
    }
    
    /**
     * Method that calls the {@link #calledStaticMethod()}; 
     * This is done to test the the boundary inside the own class. 
     */
    public static void callerCalledStaticMethod() {
        ClasCalledClass.calledStaticMethod();
    }
}
