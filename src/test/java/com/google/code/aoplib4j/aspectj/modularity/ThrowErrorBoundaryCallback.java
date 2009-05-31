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

import com.google.code.aoplib4j.aspectj.modularity.ViolationInformation;
import com.google.code.aoplib4j.aspectj.modularity.LogViolationCallback;

/**
 * Implementation of the boundary violation that throws an exception.
 * @author Adrian Citu
 *
 */
public class ThrowErrorBoundaryCallback extends BoundaryViolationCallback {

    private LogViolationCallback lvc = new LogViolationCallback();
    /**
     * {@inheritDoc}
     */
    @Override
    public void boundaryViolation(ViolationInformation info) {    
        lvc.boundaryViolation(info);
        throw new IllegalAccessError("Class or Package Boundary Violation");
    }

}
