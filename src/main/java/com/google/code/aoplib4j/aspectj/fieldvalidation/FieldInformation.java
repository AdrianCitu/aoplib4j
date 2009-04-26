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
package com.google.code.aoplib4j.aspectj.fieldvalidation;

/**
 * Interface containing the informations concerning
 * the assignment of the new value to a field that must be validated.
 * 
 * @see FieldValidator
 * 
 * @author Adrian Citu
 *
 */
public interface FieldInformation {

    /**
     * @return the field actual value.
     */
    Object getFieldValue();
    
    /**
     * @return the new value that should be assigned to the field.
     */
    Object getNewValueToAssign();
    
    /**
     * @return the field name.
     */
    String getFieldName();
    
    /**
     * @return true if the field is static, false otherwise.
     */
    boolean isStaticField();
}
