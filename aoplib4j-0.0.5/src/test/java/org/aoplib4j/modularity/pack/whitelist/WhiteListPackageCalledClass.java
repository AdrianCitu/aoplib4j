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
package org.aoplib4j.modularity.pack.whitelist;

import org.aoplib4j.modularity.Aoplib4jClassBoundary;
import org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary;
import org.aoplib4j.modularity.ListType;
import org.aoplib4j.modularity.Aoplib4jPackageBoundary;
import org.aoplib4j.modularity.ThrowErrorBoundaryCallback;
import org.aoplib4j.modularity.clas.ForbiddenCallerClass;


/**
 * Target class; annotated with the {@link Aoplib4jPackageBoundary} annotation.  
 * This class forbidden the calls from the {@link ForbiddenCallerClass} class.
 *  
 * @author Adrian Citu
 *
 */
@Aoplib4jClassBoundary( 
        callbackClass=ThrowErrorBoundaryCallback.class,
        classesList = {String.class}, 
        classesListType = ListType.BLACKLIST)
@Aoplib4jInjectedPkgBoundary
public class WhiteListPackageCalledClass {

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
        WhiteListPackageCalledClass.calledStaticMethod();
    }
}
