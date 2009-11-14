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
package org.aoplib4j.modularity.pack.blacklist;

import org.aoplib4j.modularity.InjectedPkgBoundary;

/**
 * Aspect used in tests of package boundary violation.
 * The aspect injects the {@link InjectedPkgBoundary} into all the classes from the 
 * package "org.aoplib4j.modularity.pack.blacklist" excepting
 * the aspect himself, {@link BlackListPackageBoundaryViolationTest} class and 
 * package-info class.
 * 
 * @author Adrian Citu
 *
 */
public aspect PackageAnnotationIntroduction {
    
    declare @type:
        org.aoplib4j.modularity.pack.blacklist.* 
        && !org.aoplib4j.modularity.pack.blacklist.package*info
        && !org.aoplib4j.modularity.pack.blacklist.PackageAnnotationIntroduction
        && !org.aoplib4j.modularity.pack.blacklist.BlackListPackageBoundaryViolationTest:
        @InjectedPkgBoundary;
}
