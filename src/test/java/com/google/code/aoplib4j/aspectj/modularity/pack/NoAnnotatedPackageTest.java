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
package com.google.code.aoplib4j.aspectj.modularity.pack;

import com.google.code.aoplib4j.aspectj.modularity.PackageBoundary;
import com.google.code.aoplib4j.aspectj.modularity.noannon.NoAnotatedPackageClass;

import junit.framework.TestCase;
//import NoPackageClass;

/**
 * Test case for testing the case of classes from a package not annotated 
 * with the {@link PackageBoundary}.
 *  
 * 
 * @author Adrian Citu
 *
 */
public class NoAnnotatedPackageTest extends TestCase {

    private NoAnotatedPackageClass napc = new NoAnotatedPackageClass();
    
    public final void testCallOnNonAnnotatedPackageClass() {
        napc.voidMethod();
        
        NoAnotatedPackageClass.staticVoidMethod();
    }
}
