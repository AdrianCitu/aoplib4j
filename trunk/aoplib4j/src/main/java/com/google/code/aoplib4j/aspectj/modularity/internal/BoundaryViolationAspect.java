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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback;
import com.google.code.aoplib4j.aspectj.modularity.ClassBoundary;
import com.google.code.aoplib4j.aspectj.modularity.ViolationInformation;

/**
 * Aspect implementing the modularity functionality.
 * 
 * @author Adrian Citu
 * 
 */
@Aspect
final class BoundaryViolationAspect {

    /**
     * the logger to use.
     */
    private static final Logger LOGGER = Logger
            .getLogger(BoundaryViolationAspect.class.getName());

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
     * framework creates an instance of {@link BoundaryViolationCallback} (using
     * as implementation class taken from {@link ClassBoundary#callbackClass()}
     * ) and call the callback method 
     * {@link BoundaryViolationCallback#classViolation(ViolationInformation)}
     * 
     * @param calledObj the instance of the called object
     * @param callerObj the instance of the caller object
     * @param jp the AspectJ join point.
     */
    @Before("callOfClassBoundaryPointcut(calledObj, callerObj)")
    public void callOfClassBoundaryAdvice(final Object calledObj,
            final Object callerObj, final JoinPoint jp) {

        ClassBoundary boundary = getClassBoundaryAnnotation(calledObj
                .getClass());

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return;
        }

        Class< ? >[] forbiddenClasses = boundary.forbiddenClasses();

        if (boundaryViolated(callerObj.getClass().getCanonicalName(),
                forbiddenClasses)) {

            try {
                BoundaryViolationCallback cllbck = 
                    createCallBackInstance(boundary);

                Method calledMethod = ((MethodSignature) jp.getSignature())
                        .getMethod();

                ViolationInformation bv = new ViolationInformationImpl(
                        calledMethod.getName(), 
                        calledObj.getClass().getCanonicalName(), 
                        callerObj.getClass().getCanonicalName());

                cllbck.classViolation(bv);

            } catch (InstantiationException e) {
                LOGGER.log(Level.WARNING, "Creation of call back failed", e);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.WARNING, "Creation of call back failed", e);
            }
        }

    }

    /**
     * Advice executed before the execution of a static method found on an 
     * annotated class.
     * The advice retrieve the called informations using the {@link StaticPart}
     * object. Sincestatic pointcuts cannot retrieve the caller object
     * using <code>this</code> pointcut this information is computing using the 
     * stack trace of the current execution thread by creating a new
     * {@link Throwable} object and calling {@link Throwable#getStackTrace()}.
     * 
     *
     * @param jpsp AspectJ join point static part.
     */
    @Before("callOfStaticClassBoundaryPointcut()")
    public void callOfStaticClassBoundaryAdvice(
            final JoinPoint.StaticPart jpsp) {

        Class< ? > calledClass = jpsp.getSignature().getDeclaringType();

        ClassBoundary boundary = getClassBoundaryAnnotation(calledClass);

        if (boundary == null) {
            return;
        }

        Method calledMethod = ((MethodSignature) jpsp.getSignature())
                .getMethod();

        Class< ? >[] forbiddenClasses = boundary.forbiddenClasses();

        StackTraceElement callerSte = this.getCallerInformation(calledClass
                .getCanonicalName(), calledMethod.getName());

        if (boundaryViolated(callerSte.getClassName(), forbiddenClasses)) {

            try {
                BoundaryViolationCallback cllbck = 
                    createCallBackInstance(boundary);

                ViolationInformation bv = new ViolationInformationImpl(
                        calledMethod.getName(), 
                        calledClass.getCanonicalName(),
                        callerSte.getClassName());

                cllbck.classViolation(bv);
            } catch (InstantiationException e) {
                LOGGER.log(Level.WARNING, "Creation of call back failed", e);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.WARNING, "Creation of call back failed", e);
            }
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
     * Method that computes the information about the caller class. The
     * information is stored into a {@link StackTraceElement} object.
     * 
     * The caller information is on the third position on the stack trace;
     * the first position contains the call to this method, and the 
     * second one to the
     * {@link #callOfStaticClassBoundaryAdvice(StaticPart)}.
     * 
     * This method will return faulty results when a child class calls an
     * inherited non overridden parent method. In this case the stack trace
     * contains a call to the parent method directly.
     * 
     * <pre>
     *  Example:
     *  P - parent class 
     *  P#method - a method of the parent
     *  C - child class; it does not override the &quot;method&quot; method
     *  
     *  On the stack trace a call to C#method is in fact written as a call to
     *  P#method.
     * 
     * </pre>
     * 
     * @param calledClassName
     *            the canonical name of the called class name
     * @param calledMethodName
     *            the name of the called method.
     * @return {@link StackTraceElement} represented the caller method.
     */
    private StackTraceElement getCallerInformation(
            final String calledClassName, final String calledMethodName) {

        StackTraceElement[] stes = new Throwable().getStackTrace();

        return stes[2];
    }

    /**
     * Creates an instance of boundary violation callback using the information
     * from the {@link ClassBoundary#callbackClass()}.
     * 
     * @see BoundaryViolationCallback
     * 
     * @param boundaryAnnotation
     *            the {@link ClassBoundary} annotation.
     * 
     * @return instance of a {@link BoundaryViolationCallback}.
     * 
     * @throws IllegalAccessException
     *             if the instantiation fails (introspection is used)
     * @throws InstantiationException
     *             if the instantiation fails (introspection is used)
     */
    private BoundaryViolationCallback createCallBackInstance(
            final ClassBoundary boundaryAnnotation)
            throws InstantiationException, IllegalAccessException {

        Class< ? extends BoundaryViolationCallback> callBackClass = 
            boundaryAnnotation.callbackClass();

        LOGGER.info("Creating a boundary callback instance of " 
                + callBackClass);

        return callBackClass.newInstance();

    }

    /**
     * Verifies a boundary violation by comparing the caller class with the
     * array of forbidden classes.
     * 
     * @param callerClassName
     *            the caller class canonical name.
     * @param forbiddenClasses
     *            array of the forbidden classes.
     * 
     * @return true if a violation (if the target class is in into the array of
     *         forbidden classes), false otherwise.
     */
    private boolean boundaryViolated(final String callerClassName,
            final Class< ? >[] forbiddenClasses) {
        boolean returnValue = false;

        for (Class< ? > forbiddenClass : forbiddenClasses) {
            if (callerClassName.equals(forbiddenClass.getCanonicalName())) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }
}
