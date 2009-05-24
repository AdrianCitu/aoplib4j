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
package com.google.code.aoplib4j.aspectj.modularity.internal;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.modularity.ClassBoundary;
import com.google.code.aoplib4j.aspectj.modularity.ListType;

/**
 * Aspect implementing the modularity at the class level.
 * 
 * @see ClassBoundary
 *  
 * @author Adrian Citu
 *
 */
@Aspect
public final class ClassBoundaryAspect extends AbstractBoundary {

    /**
     * Pointcut intercepting the call of methods non private and
     * non static to the classes annotated with the {@link ClassBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *   call(!private !static * 
     *   (@com.google.code.aoplib4j.aspectj.modularity.ClassBoundary *).*(..)) 
     *   && target(calledObj) && this(callerObj)
     * </pre>
     * 
     * @param calledObj instance of the called object.
     * @param callerObj instance of the caller object.
     */
    @Pointcut("call(!private !static * "
            + "(@com.google.code.aoplib4j.aspectj.modularity.ClassBoundary *)."
            + "*(..)) " + "&& target(calledObj) && this(callerObj)")
    public void callOfClassBoundaryPointcut(final Object calledObj,
            final Object callerObj) {

    }

    /**
     * Pointcut intercepting the call of methods non private and
     * static to the classes annotated with the {@link ClassBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *    call(!private static * 
     *    (@com.google.code.aoplib4j.aspectj.modularity.ClassBoundary *).*(..)) 
     * </pre>
     */
    @Pointcut("call(!private static * "
            + "(@com.google.code.aoplib4j.aspectj.modularity.ClassBoundary *)."
            + "*(..)) ")
    public void callOfStaticClassBoundaryPointcut() {

    }

    /**
     * Advice for the execution of the
     * {@link #callOfClassBoundaryPointcut(Object, Object)} pointcut.
     * 
     * The advice compute the called class informations (using the 
     * <code>calledObj</code> parameter), the caller class
     * informations (using the <code>callerObj</code> parameter) and then 
     * checks if the caller class is present or not into
     * the list of forbidden classes.
     * 
     * If the caller is present into the list of forbidden classes, the
     * framework creates an instance of 
     * {@link 
     * com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback}
     * (using
     * as implementation class taken from {@link ClassBoundary#callbackClass()}
     * ) and call the callback method 
     * {@link 
     * com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback
     * #boundaryViolation(
     * com.google.code.aoplib4j.aspectj.modularity.ViolationInformation)}
     * 
     * @param calledObj the instance of the called object
     * @param callerObj the instance of the caller object
     * @param jp the AspectJ join point.
     */
    @Before("callOfClassBoundaryPointcut(calledObj, callerObj)")
    public void callOfClassBoundaryAdvice(final Object calledObj,
            final Object callerObj, final JoinPoint jp) {

        //the caller and called are the same instance
        if (calledObj == callerObj) {
            return;
        }
        
        ClassBoundary boundary = getClassBoundaryAnnotation(calledObj
                .getClass());

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return;
        }

        if (classBoundaryViolated(
                callerObj.getClass().getCanonicalName(), boundary)) {
            createAndExecuteCallback(
                    calledObj, callerObj, jp, null, boundary);
        }

    }

    /**
     * Advice executed before the execution of a static method found on an 
     * annotated class.
     * The advice retrieve the called informations using the
     * {@link org.aspectj.lang.JoinPoint.StaticPart}
     * object. Sincestatic pointcuts cannot retrieve the caller object
     * using <code>this</code> pointcut the caller information is computed 
     * using the 
     * stack trace of the current execution thread by creating a new
     * {@link Throwable} object and calling {@link Throwable#getStackTrace()}.
     * (see {@link #getCallerInformation(String, String)}).
     * After the caller and called classes are computed a violation check is 
     * made See
     * {@link #classBoundaryViolated(
     * com.google.code.aoplib4j.aspectj.modularity.PackageBoundary, String)} 
     * for more 
     * information about the boundary violation check.
     * 
     * 
     * In the case of a violation, the framework creates an instance of 
     * {@link 
     * com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback} 
     * (using
     * as implementation class taken from 
     * {@link 
     * com.google.code.aoplib4j.aspectj.modularity.PackageBoundary#
     * callbackClass()}) and call the callback method 
     * {@link 
     * com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback
     * #boundaryViolation(
     * com.google.code.aoplib4j.aspectj.modularity.ViolationInformation)}
     *
     * @param jpsp AspectJ join point static part.
     * 
     * @see {@link #classBoundaryViolated(String, Class[])} 
     */
    @Before("callOfStaticClassBoundaryPointcut()")
    public void callOfStaticClassBoundaryAdvice(
            final JoinPoint.StaticPart jpsp) {

        Class< ? > calledClass = jpsp.getSignature().getDeclaringType();

        ClassBoundary boundary = getClassBoundaryAnnotation(calledClass);

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return;
        }

        Method calledMethod = ((MethodSignature) jpsp.getSignature())
                .getMethod();

        StackTraceElement callerSte = this.getCallerInformation(calledClass
                .getCanonicalName(), calledMethod.getName());

        if (classBoundaryViolated(
                callerSte.getClassName(), boundary)) {

            createAndExecuteCallback(calledClass, boundary, null, calledMethod,
                    callerSte);
        }
    }
    
    /**
     * Method that returns the {@link ClassBoundary} annotation from the object
     * passed as parameter or null if the class is not annotated.
     * 
     * @param calledClass
     *            the class from which the annotation should be retrieved.
     * @return attached {@link ClassBoundary} or null.
     */
    private ClassBoundary getClassBoundaryAnnotation(
            final Class< ? > calledClass) {
        
        return calledClass.getAnnotation(ClassBoundary.class);
    }
    
    /**
     * Verifies the violation of class boundary by retrieving the information
     * from the {@link ClassBoundary} annotation (the classes list, the
     * list type) and comparing with the class name passed as parameter.
     * 
     * @param classBoundary {@link ClassBoundary} annotation.
     * @param callerClassName caller class name.
     * @return true if a boundary violation; false otherwise.
     */
    private boolean classBoundaryViolated(final String callerClassName,
            final ClassBoundary classBoundary) {
                
        ListType listType = classBoundary.classesListType();
        Class< ? >[] classesList = classBoundary.classesList();
        
        String[] classesNamesList = new String[classesList.length];
        
        for (int i = 0; i < classesList.length; i++) {
            classesNamesList[i] = classesList[i].getCanonicalName();
        }
        
        return searchStringIntoArray(
                callerClassName, listType, classesNamesList);
    }
}
