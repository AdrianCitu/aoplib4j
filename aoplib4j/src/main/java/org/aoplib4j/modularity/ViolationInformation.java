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
package org.aoplib4j.modularity;



/**
 * Interface containing the information about the boundary violation; what class
 * violated the boundary and what class and what method was
 * called).
 * 
 * @see ClassBoundary
 * 
 * @author Adrian Citu
 *
 */
public interface ViolationInformation {

    /**
     * @return the caller canonical name that violated the boundary.
     */
    String getCallerClassName();
    
    /**
     * @return the called class canonical name (marked with a 
     * {@link ClassBoundary}) annotation.
     */
    String getCalledClassName();
    
    /**
     * @return the called method name.
     */
    String getCalledMethodName();
    
}
