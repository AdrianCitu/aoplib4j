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

import org.aoplib4j.modularity.InjectedPkgBoundary;
import org.aoplib4j.modularity.pack.blacklist.BlackListPackageCalledClass;
import org.aoplib4j.modularity.pack.blacklist.forbiddenpackage.ForbiddenClass;
import org.aoplib4j.modularity.pack.whitelist.WhiteListPackageCalledClass;
import org.aoplib4j.modularity.pack.whitelist.allowedpackage.AllowedClass;

import junit.framework.TestCase;




/**
 * Class containing the tests for the packages boundary violation,
 * 
 * @author Adrian Citu
 *
 */
public class WhiteListPackageBoundaryViolationTest extends TestCase {

    private AllowedClass allowedClass = new AllowedClass();
    private ForbiddenClass forbiddenClass = new ForbiddenClass();
    
    /**
     * @throws java.lang.Exception
     */
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    public void tearDown() throws Exception {
    }

    /**
     * Test the calls of the {@link AllowedClass} to the 
     * {@link WhiteListPackageCalledClass}. The calls should be ok since the
     * {@link AllowedClass} package is in the list of allowed packages.
     */
    public final void testPackageCallOk() {
        allowedClass.voidMethod();
        AllowedClass.staticVoidMethod();
    }
    
    /**
     * Verify that the {@link WhiteListPackageCalledClass} 
     * and {@link BlackListPackageCalledClass} have attached the 
     * {@link InjectedPkgBoundary}; the {@link PackageAnnotationIntroduction} aspect should
     * inject this annotation.
     */
    public final void testPackageClassIsAnnotated() {
        assertNotNull(
                WhiteListPackageCalledClass.class.getAnnotation(
                        InjectedPkgBoundary.class));
    }
    
    /**
     * Test on which the caller and the called object are in the same package.
     * 
     */
    public final void testMethodsCalledFromTheSamePackage() {
        WhiteListPackageCalledClass cls = new WhiteListPackageCalledClass();
        
        //the callerCalledMethod calls a method on the same class.
        cls.callerCalledMethod();
        
        //the callerCalledStaticMethod calls a method on the same class/
        WhiteListPackageCalledClass.callerCalledStaticMethod();

        BlackListPackageCalledClass blk = new BlackListPackageCalledClass();
        
        //the callerCalledMethod calls a method on the same class.
        blk.callerCalledMethod();
        
        //the callerCalledStaticMethod calls a method on the same class/
        BlackListPackageCalledClass.callerCalledStaticMethod();
    }
}
