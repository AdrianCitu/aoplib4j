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

import com.google.code.aoplib4j.aspectj.modularity.ViolationInformation;

/**
 * Class instantiated by the framework containing the informations about the
 * boundary violation.
 * 
 * @author Adrian Citu
 *
 */
final class ViolationInformationImpl implements ViolationInformation {

    /**
     * called method.
     */
    private String calledMethodName = null;
    
    /**
     * called canonical name. 
     */
    private String calledClassName = null;
    
    /**
     * caller canonical name.
     */
    private String callerClassName = null;
    
    /**
     * @param cldMethodName the called method mane.
     * @param cldClassName the called class name.
     * @param clrClassName the caller class name.
     */
    ViolationInformationImpl(
            final String cldMethodName,
            final String cldClassName,
            final String clrClassName) {
        
        this.calledMethodName = cldMethodName;
        this.calledClassName = cldClassName;
        this.callerClassName = clrClassName;
    }
    
    /**
     * {@inheritDoc} 
     */
    public String getCalledMethodName() {
        return this.calledMethodName;
    }

    /**
     * {@inheritDoc} 
     */
    public String getCalledClassName() {
        return this.calledClassName;
    }

    /**
     * {@inheritDoc} 
     */
    public String getCallerClassName() {
        return this.callerClassName;
    }
    
 
}
