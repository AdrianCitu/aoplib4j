/*
 *  Copyright 2008-2008 the original author or authors.
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
package com.google.code.aoplib4j.aspectj.fieldvalidation;

/**
 * Validator for the {@link String} objects (used for testing purposes).
 * 
 * @author Adrian Citu
 *
 */
public class StringValidator extends FieldValidator {

    /* (non-Javadoc)
     * @see com.google.code.aoplib4j.aspectj.fieldvalidation.FieldValidator#validate(com.google.code.aoplib4j.aspectj.fieldvalidation.FieldInformation)
     */
    @Override
    public void validate(FieldInformation fldInfo) throws Exception {
        if (fldInfo.getNewValueToAssign() == null) {
            throw new NullPointerException("null field");
        }

    }

}
