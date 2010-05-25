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
package org.aoplib4j.uml.internal;

import java.util.ArrayList;
import java.util.List;

import org.aoplib4j.uml.SequenceMethod;

/**
 * Implementation of {@link SequenceMethod} interface.
 * 
 * @author Adrian Citu
 *
 */
final class SequenceMethodImpl implements SequenceMethod {

    
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
    private SequenceMethodImpl parent = null;
    

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
     SequenceMethodImpl(final String clsName, final String metdName, 
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
            this.parent = (SequenceMethodImpl) caller[0]; 
            this.parent.setChild(this);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String getClassName() {
        return this.className;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getMethodName() {
        return this.methodName;
    }
    
    /**
     * {@inheritDoc}
     */
    public Class< ? > getReturnType() {
        return this.returnType;
    }

    /**
     * {@inheritDoc}
     */
    public Class< ? >[] getParameterTypes() {
        return this.parameterTypes;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public boolean haveChildren() {
        return this.childs.size() != 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isStatic() {
        return this.staticMethod;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isConstructor() {
        return this.constructor;
    }
    
    /**
     * {@inheritDoc}
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
