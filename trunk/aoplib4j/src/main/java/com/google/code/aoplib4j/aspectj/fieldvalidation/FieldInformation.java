/*
 *  Copyright 2008 the original author or authors.
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

import java.util.logging.Logger;


/**
 * Class instantiated by the framework containing the informations concerning
 * the assignment of the new value to a field that must be validated.
 * 
 * @see FieldValidator
 * 
 * @author Adrian Citu
 *
 */
public final class FieldInformation {
    
    /**
     * the logger to use.
     */
    private static Logger logger = Logger.getLogger(
            FieldInformation.class.getName());
    
    /**
     * the field initial value.
     */
    private Object fieldValue = null;
    
    /**
     * the field new value.
     */
    private Object fieldNewValue = null;
    
    /**
     * the field is static or not.
     */
    private boolean staticField = false;
    
    /**
     * the field name.
     */
    private String fieldName = null;
    
    /**
     * Default constructor.
     * 
     * @param fldValue the actual value of the field.
     * @param valueToAssign the new value to assign to the field.
     * @param staticFld true if the field is static.
     * @param fldName field name.
     */
    public FieldInformation(final Object fldValue, final Object valueToAssign,
            final boolean staticFld, final String fldName) {
        super();
        logger.info("Creating instance of " + FieldInformation.class.getName());
        
        this.fieldValue = fldValue;
        this.fieldNewValue = valueToAssign;
        this.staticField = staticFld;
        this.fieldName = fldName;
        
        logger.info("Field Name" + this.fieldName);
        logger.info("Field is static" + this.staticField);
        logger.info("Field value" + this.fieldValue);
        logger.info("Value to asign" + this.fieldNewValue);
        
    }
    
    /**
     * @return the field actual value.
     */
    public Object getFieldValue() {
        return this.fieldValue;
    }
    
    /**
     * @return the new value that should be assigned to the field.
     */
    public Object getNewValueToAssign() {
        return this.fieldNewValue;
    }
    
    /**
     * @return the field name.
     */
    public String getFieldName() {
        return this.fieldName;
    }
    
    /**
     * @return true if the field is static, false otherwise.
     */
    public boolean isStaticField() {
        return this.staticField;
    }
}
