/*
 *  Copyright 2008-2010 the original author or authors.
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
package org.aoplib4j.uml;

import java.util.List;

/**
 * Class representing a method of a sequence diagram. This class is a node of
 * a tree (represented by the {@link SequenceDiagram}).
 * 
 * A sequence method contains all the necessary informations concerning a
 * method: method name, method class, method is static/nonStatic/constructor,
 * return type of the method, parameters types, parameter names, methods that
 * this method calls and the method that called this method.
 * 
 * @author Adrian Citu
 *
 */
public interface SequenceMethod {

    /**
     * @return the (full) class name of which this method is attached to.
     * 
     * @see #getSimpleClassName()
     */
    String getClassName();

    /**
     * @return the method name.
     */
     String getMethodName();

    /**
     * @return the method return type.
     */
    Class<?> getReturnType();

    /**
     * @return method parameters types.
     */
    Class<?>[] getParameterTypes();

    /**
     * @return method parameter names.
     */
    String[] getParameterNames();

    /**
     * @return the collection of children.
     */
    List<SequenceMethod> getChildren();

    /**
     * @return the parent (the caller) of this method.
     */
    SequenceMethod getParent();

    /**
     * @return true if the node have children attached, false otherwise.
     */
    boolean haveChildren();

    /**
     * @return true if the method is static, false otherwise.
     */
    boolean isStatic();

    /**
     * @return true if the method is a constructor, false otherwise
     */
    boolean isConstructor();

    /**
     * @return the simple (no package) class name; returns null if the class
     * name is null.
     */
    String getSimpleClassName();

}
