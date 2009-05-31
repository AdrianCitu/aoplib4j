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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to put on fields to validate. The framework will automatically
 * instantiate instances of {@link FieldValidator} class and will call 
 * {@link FieldValidator#validate(FieldInformation)} method.
 * 
 *  see {@link FieldValidator}
 * @author Adrian Citu
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {
    
    /**
     * The validator class. The framework will call 
     * {@link FieldValidator#validate(FieldInformation)} method for validating
     * the annotated field.
     */
    Class < ? extends FieldValidator > validationClass();
}
