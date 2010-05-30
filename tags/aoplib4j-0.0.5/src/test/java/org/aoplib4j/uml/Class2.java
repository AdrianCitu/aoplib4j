/*
 *  Copyright 2008-2010 the original author or authors.
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
package org.aoplib4j.uml;

/**
 * @author Adrian Citu
 *
 */
public class Class2 {

    
    public void method1Class2() {
        System.out.println("method 1, class 2");
        
        this.method2Class2();
        
        Class3 cls3 = new Class3();
        cls3.method1Class3();
    }
    
    private Integer method2Class2() {
        return null;
    }
}
