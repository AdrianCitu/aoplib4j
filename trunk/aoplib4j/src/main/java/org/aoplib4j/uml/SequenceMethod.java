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

import java.util.ArrayList;
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
public final class SequenceMethod {

    
    /**
     * The class name of which the methods is attached to.
     */
    private String className = null;
    
    /**
     * The method name; default value is <code>new</code> (constructor).
     */
    private String methodName = "new";
    
    /**
     * The return type of the method. 
     */
    private Class< ? > returnType = null;
    
    /**
     * The types of the method parameters.
     */
    private Class< ? >[] parameterTypes = null;
    
    /**
     * The names of the method parameters.
     */
    private String[] parameterNames = null;
    
    
    /**
     * The method is static or not.
     */
    private boolean staticMethod = false;
    
    /**
     * The method is a constructor or not.
     */
    private boolean constructor = false;
    
    /**
     * The "childs" of the method; all the methods that the method called.
     */
    private List<SequenceMethod> childs = new ArrayList<SequenceMethod>();
    
    /**
     * The "parent" method; the caller of this method; is null for the
     * first method of the diagram.
     */
    private SequenceMethod parent = null;
    

    /**
     * @param clsName the class name on which the method is attached.
     * @param metdName the method name
     * @param stsOrCons stsOrCons[0] is the method static, stsOrCons[1] is the
     * method a constructor (made an array and not 2 parameters because of 
     * Checkstyle rule; not proud of the solution)
     * @param retType return type of the method.
     * @param paramTypes parameter types of the method
     * @param paramNames parameter names of the method
     * @param caller the caller method.
     */
    public SequenceMethod(final String clsName, final String metdName, 
            final boolean[] stsOrCons, final Class< ? > retType,
            final Class< ? >[] paramTypes, final String[] paramNames,
            final SequenceMethod ... caller) {
        super();
        
        this.className = clsName;
        this.methodName = metdName;
        this.staticMethod = stsOrCons[0];
        this.constructor = stsOrCons[1];
        this.returnType = retType;
        this.parameterTypes = paramTypes;
        this.parameterNames = paramNames;
        
        if (caller != null && caller.length != 0) {
            this.parent = caller[0]; 
            this.parent.setChild(this);
        }
    }
    
    /**
     * @return the (full) class name of which this method is attached to.
     * 
     * @see #getSimpleClassName()
     */
    public String getClassName() {
        return this.className;
    }
    /**
     * @return the method name.
     */
    public String getMethodName() {
        return this.methodName;
    }
    /**
     * @return the method return type.
     */
    public Class< ? > getReturnType() {
        return this.returnType;
    }
    /**
     * @return method parameters types.
     */
    public Class< ? >[] getParameterTypes() {
        return this.parameterTypes;
    }
    /**
     * @return method parameter names.
     */
    public String[] getParameterNames() {
        return this.parameterNames;
    }

    /**
     * @return the collection of children.
     */
    public List<SequenceMethod> getChildren() {
        return this.childs;
    }

    /**
     * Add a new child.
     * @param child the child to add.
     */
    private void setChild(final SequenceMethod child) {
        this.childs.add(child);
    }

    /**
     * @return the parent (the caller) of this method.
     */
    public SequenceMethod getParent() {
        return this.parent;
    } 
    
    /**
     * @return true if the node have children attached, false otherwise.
     */
    public boolean haveChildren() {
        return this.childs.size() != 0;
    }
    
    /**
     * @return true if the method is static, false otherwise.
     */
    public boolean isStatic() {
        return this.staticMethod;
    }
    
    /**
     * @return true if the method is a constructor, false otherwise
     */
    public boolean isConstructor() {
        return this.constructor;
    }
    
    /**
     * @return the simple (no packages) class name; returns null if the class
     * name is null.
     */
    public String getSimpleClassName() {
        String fullClassName = this.getClassName();
        
        if (fullClassName != null) {
            return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        } else {
            return null;
        }
    }
}
