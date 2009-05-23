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
package com.google.code.aoplib4j.aspectj.modularity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation for the boundary violation. This class only logs 
 * the violation using the Java logging API.
 * 
 * @author Adrian Citu
 *
 */
class LogViolationCallback extends BoundaryViolationCallback {

    /**
     * The logger used to log the boundary violations.
     */
    private static final Logger LOGGER =
        Logger.getLogger(LogViolationCallback.class.getName());
    
    /**
     * The method will extract the information from the 
     * {@link ViolationInformation} and will log it.
     * 
     * {@inheritDoc}
     */
    @Override
    public void boundaryViolation(final ViolationInformation info) {
        StringBuffer errorMessage = new StringBuffer();
        errorMessage.append("It is forbidden to call the method ")
        .append(info.getCalledClassName())
        .append("#")
        .append(info.getCalledMethodName())
        .append(" from the class ")
        .append(info.getCallerClassName());
        
        LOGGER.log(Level.WARNING, errorMessage.toString());
    }

}
