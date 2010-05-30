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
package org.aoplib4j.modularity.pack.whitelist.allowedpackage;

import org.aoplib4j.modularity.clas.ClasCalledClass;
import org.aoplib4j.modularity.pack.whitelist.WhiteListPackageCalledClass;

/**
 * Class used for the package boundary.The class is a part of the allowed 
 * package which means that it can call methods from the {@link ClasCalledClass}
 * class.
 * @author Adrian Citu
 *
 */
public class AllowedClass {

    
    private WhiteListPackageCalledClass called = 
        new WhiteListPackageCalledClass();
    
    public void voidMethod() {
        called.calledMethod();
    }
    
    public static void staticVoidMethod() {
        WhiteListPackageCalledClass.calledStaticMethod();
    }
    
}
