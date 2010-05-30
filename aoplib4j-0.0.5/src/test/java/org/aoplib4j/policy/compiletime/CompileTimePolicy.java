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
package org.aoplib4j.policy.compiletime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A pseudo-test class; The compiler warnings cannot be retrieved/tested
 * programmatically; so the only possible test is to compile this class
 * and to look to the compiler warnings.
 * 
 * @author Adrian Citu
 *
 */
public class CompileTimePolicy {

    
    /* no warning should be printed */
    protected final String protectedFinalString = null;
    
    /* no warning should be printed */
    public final String publicFinalString = null;

    /* no warning should be printed */
    final String defaultFinalString = null;
    
    /* a warning SHOULD be printed */
    public String publicString = null;
    
    /* a warning SHOULD be printed */
    protected String protectedString = null;
    
    /* a warning SHOULD be printed */
    String defaultString = null;
    
    /* no warning should be printed */
    @SuppressWarnings("unused")
    private String privateString = null;
    
    /* no warning should be printed */
    public enum Rank { 
        DEUCE, THREE, FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    
    /* no warning should be printed */
    public Collection<String> returnInterfaceCollection() {
        return new Vector<String>();
    }
    
    /* a warning SHOULD be printed */
    public Vector<String> returnVectorNotInterface() {
        return new Vector<String>();
    }
    
    /* a warning SHOULD be printed */
    public HashMap<String,String> returnHashMapNotInterface() {
        return new HashMap<String,String>();
    }
    
    /* no warning should be printed */
    public Map<String,String> returnInterfaceMap() {
        return new HashMap<String,String>();
    }
}
