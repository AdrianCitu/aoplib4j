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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to mark the classes on which the class boundary calls should be 
 * applied.
 * 
 * This annotation should be put on the classes to mark from which classes
 * the calls of the annotated class are forbidden.
 * 
 * @author Adrian Citu
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassBoundary {

    /**
     * The classes on which the calls of the annotated class are
     * forbidden.
     */
    Class< ? >[] forbiddenClasses();
    
    /**
     * The class the will be instantiated by the framework when a 
     * boundary violation is detected. The default callback class is 
     * {@link LogViolationCallback}.
     * 
     * @see LogViolationCallback
     */
    Class < ? extends BoundaryViolationCallback > callbackClass() 
        default LogViolationCallback.class;
    
}
