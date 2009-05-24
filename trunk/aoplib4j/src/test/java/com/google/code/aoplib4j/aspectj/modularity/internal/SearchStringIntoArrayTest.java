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
package com.google.code.aoplib4j.aspectj.modularity.internal;

import com.google.code.aoplib4j.aspectj.modularity.ListType;
import com.google.code.aoplib4j.aspectj.modularity.internal.ClassBoundaryAspect;

import junit.framework.TestCase;

/**
 * Test for the {
 * @link AbstractBoundary#searchStringIntoArray(String, ListType, String[])}.
 * 
 * @author Adrian Citu
 *
 */
public class SearchStringIntoArrayTest extends TestCase {

    private ClassBoundaryAspect cba = new ClassBoundaryAspect();
    
    /**
     * Test the method call with null values.
     */
    public final void testSearchStringIntoArrayWithNullValues() {
        assertFalse(cba.searchStringIntoArray(
                null, 
                ListType.BLACKLIST, 
                new String[] {"1"}));
        
        assertFalse(cba.searchStringIntoArray("1", ListType.WHITELIST, null));
        
        assertFalse(cba.searchStringIntoArray("1", null, new String[]{"2"}));
    }
    
    /**
     * Test the method with a blacklist.
     */
    public final void testBlackListType() {
        String toLook = "A";
        
        String[] stringArray = {"A", "B", "C", "D"};
        
        assertTrue(cba.searchStringIntoArray(
                toLook, ListType.BLACKLIST, stringArray));
        
        
        toLook = "x";
        
        assertFalse(cba.searchStringIntoArray(
                toLook, ListType.BLACKLIST, stringArray));
    }
    
    /**
     * Test the method with a white list.
     */
    public final void testWhiteListType() {
        String toLook = "A";
        
        String[] stringArray = {"A", "B", "C", "D"};
        
        assertFalse(cba.searchStringIntoArray(
                toLook, ListType.WHITELIST, stringArray));
        
        
        toLook = "x";
        
        assertTrue(cba.searchStringIntoArray(
                toLook, ListType.WHITELIST, stringArray));       
    }
}
