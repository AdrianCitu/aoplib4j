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
package com.google.code.aoplib4j.aspectj.modularity.pack.whitelist.forbiddenpackage;

import com.google.code.aoplib4j.aspectj.modularity.clas.ClasCalledClass;
import com.google.code.aoplib4j.aspectj.modularity.pack.whitelist.WhiteListPackageCalledClass;

/**
 * Class used for the package boundary. The class is a part of the forbidden 
 * package which means that it cannot call methods from the 
 * {@link ClasCalledClass} class.
 * @author Adrian Citu
 *
 */
public class ForbiddenClass {

    private WhiteListPackageCalledClass called = new WhiteListPackageCalledClass();
    
    public void voidMethod() {
        called.calledMethod();
    }
    
    public static void staticVoidMethod() {
        WhiteListPackageCalledClass.calledStaticMethod();
    }
}
