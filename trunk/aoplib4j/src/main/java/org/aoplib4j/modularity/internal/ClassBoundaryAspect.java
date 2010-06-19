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
package org.aoplib4j.modularity.internal;

import java.lang.reflect.Method;

import org.aoplib4j.modularity.Aoplib4jClassBoundary;
import org.aoplib4j.modularity.ListType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * Aspect implementing the modularity at the class level.
 * 
 * @see Aoplib4jClassBoundary
 *  
 * @author Adrian Citu
 *
 */
@Aspect
public final class ClassBoundaryAspect extends AbstractBoundary {

    /**
     * Pointcut intercepting the call of methods non private and
     * non static to the classes annotated with the 
     * {@link Aoplib4jClassBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *   call(!private !static * 
     *   (@org.aoplib4j.modularity.Aoplib4jClassBoundary *).*(..)) 
     *   && target(calledObj) && this(callerObj) && if()
     * </pre>
     * 
     * The pointcut is represented by a static boolean method 
     * because of the use of the <code>if()</code> pointcut (see 7.3.2 from
     * AspectJ in action 2 ed.)
     * 
     * @param calledObj instance of the called object.
     * @param callerObj instance of the caller object.
     * 
     * @return false if calledObject == callerObject (the call is made on the 
     * class instance) or if the retrieved {@link Aoplib4jClassBoundary} 
     * annotation
     * from the called class is null.
     */
    @Pointcut("call(!private !static * "
            + "(@org.aoplib4j.modularity.Aoplib4jClassBoundary *)."
            + "*(..)) " + "&& target(calledObj) && this(callerObj) && if()")
    public static boolean callOfClassBoundaryPointcut(final Object calledObj,
            final Object callerObj) {

        //the caller and called are the same instance
        if (calledObj == callerObj) {
            return false;
        }
        
        Aoplib4jClassBoundary boundary = getClassBoundaryAnnotation(calledObj
                .getClass());

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return false;
        }
        
        return true;
    }

    /**
     * Pointcut intercepting the call of methods non private and
     * static to the classes annotated with the {@link Aoplib4jClassBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *    call(!private static * 
     *    (@org.aoplib4j.modularity.Aoplib4jClassBoundary *).*(..))
     *    && if() 
     * </pre>
     * 
     * The pointcut is represented by a static boolean method 
     * because of the use of the <code>if()</code> pointcut (see 7.3.2 from
     * AspectJ in action 2 ed.)
     * 
     * @param jpsp AspectJ join point static part.
     * 
     * @return false if the {@link Aoplib4jClassBoundary} annotation retrieved 
     * from
     * the calledClass is null true otherwise.
     */
    @Pointcut("call(!private static * "
            + "(@org.aoplib4j.modularity.Aoplib4jClassBoundary *)."
            + "*(..))  && if()")
    public static boolean callOfStaticClassBoundaryPointcut(
            final JoinPoint.StaticPart jpsp) {

        Class<?> calledClass = jpsp.getSignature().getDeclaringType();

        Aoplib4jClassBoundary boundary = 
            getClassBoundaryAnnotation(calledClass);

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return false;
        }
        
        return true;
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
     * org.aoplib4j.modularity.BoundaryViolationCallback}
     * (using
     * as implementation class taken from 
     * {@link Aoplib4jClassBoundary#callbackClass()}
     * ) and call the callback method 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback
     * #boundaryViolation(
     * org.aoplib4j.modularity.ViolationInformation)}
     * 
     * @param calledObj the instance of the called object
     * @param callerObj the instance of the caller object
     * @param jp the AspectJ join point.
     */
    @Before("callOfClassBoundaryPointcut(calledObj, callerObj)")
    public void callOfClassBoundaryAdvice(final Object calledObj,
            final Object callerObj, final JoinPoint jp) {
        
        Aoplib4jClassBoundary boundary = getClassBoundaryAnnotation(calledObj
                .getClass());

        if (boundary != null
                && classBoundaryViolated(
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
     * object. Since static pointcuts cannot retrieve the caller object
     * using <code>this</code> pointcut the caller information is computed 
     * using the 
     * stack trace of the current execution thread by creating a new
     * {@link Throwable} object and calling {@link Throwable#getStackTrace()}.
     * (see {@link #getCallerInformation(String, String)}).
     * After the caller and called classes are computed a violation check is 
     * made.
     * 
     * In the case of a violation, the framework creates an instance of 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback} 
     * (using
     * as implementation class taken from 
     * {@link 
     * org.aoplib4j.modularity.Aoplib4jClassBoundary#callbackClass(
     * )}) 
     * and call the callback method
     * {@link
     * org.aoplib4j.modularity.BoundaryViolationCallback
     * #boundaryViolation(
     * org.aoplib4j.modularity.ViolationInformation)}
     *
     * @param jpsp AspectJ join point static part.
     * 
     * @see 
     *  ClassBoundaryAspect#classBoundaryViolated(String, Aoplib4jClassBoundary)
     * @see  
     * org.aoplib4j.modularity.BoundaryViolationCallback
     */
    @Before("callOfStaticClassBoundaryPointcut(jpsp)")
    public void callOfStaticClassBoundaryAdvice(
            final JoinPoint.StaticPart jpsp) {

        Class<?> calledClass = jpsp.getSignature().getDeclaringType();

        Aoplib4jClassBoundary boundary = 
            getClassBoundaryAnnotation(calledClass);

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
     * Method that returns the {@link Aoplib4jClassBoundary} annotation from the
     * object
     * passed as parameter or null if the class is not annotated.
     * 
     * @param calledClass
     *            the class from which the annotation should be retrieved.
     * @return attached {@link Aoplib4jClassBoundary} or null.
     */
    private static Aoplib4jClassBoundary getClassBoundaryAnnotation(
            final Class<?> calledClass) {
        
        return calledClass.getAnnotation(Aoplib4jClassBoundary.class);
    }
    
    /**
     * Verifies the violation of class boundary by retrieving the information
     * from the {@link Aoplib4jClassBoundary} annotation (the classes list, the
     * list type) and comparing with the class name passed as parameter.
     * 
     * @param classBoundary {@link Aoplib4jClassBoundary} annotation.
     * @param callerClassName caller class name.
     * @return true if a boundary violation; false otherwise.
     */
    private boolean classBoundaryViolated(final String callerClassName,
            final Aoplib4jClassBoundary classBoundary) {
                
        ListType listType = classBoundary.classesListType();
        Class<?>[] classesList = classBoundary.classesList();
        
        String[] classesNamesList = new String[classesList.length];
        
        for (int i = 0; i < classesList.length; i++) {
            classesNamesList[i] = classesList[i].getCanonicalName();
        }
        
        return searchStringIntoArray(
                callerClassName, listType, classesNamesList);
    }
}
