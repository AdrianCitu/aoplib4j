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
package org.aoplib4j.policy.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Calss used by the runtime policy tests.
 * 
 * @author Adrian Citu
 *
 */
class TestedClass {

    public static Map<String, String> returnStaticNullMap() {
        return null;
    }

    public Map<String, String> returnNullMap() {
        return null;
    }
    
    public static BlockingQueue<String> returnStaticNullCollection() {
        return null;
    }
    
    public Collection<String> returnNullCollection() {
        return null;
    }

    public static String[] returnStaticNullArray() {
        return null;
    }

    public String[] returnNotNullArray() {
        return new String[]{};
    }
    
    public Collection<String> returnNotNullCollection() {
        return new Vector<String>();
    }
    
    public Map<String,String> returnInterfaceMap() {
        return new HashMap<String,String>();
    }

}
