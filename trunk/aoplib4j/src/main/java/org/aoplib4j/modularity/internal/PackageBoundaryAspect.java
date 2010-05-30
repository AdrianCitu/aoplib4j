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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aoplib4j.modularity.ListType;
import org.aoplib4j.modularity.Aoplib4jPackageBoundary;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * Aspect implementing the modularity at the package level.
 * 
 * @see Aoplib4jPackageBoundary
 * 
 * @author Adrian Citu
 *
 */
@Aspect
@DeclarePrecedence(
  "org.aoplib4j.modularity.internal.PackageBoundaryAspect," 
 + "org.aoplib4j.modularity.internal.ClassBoundaryAspect")
public final class PackageBoundaryAspect extends AbstractBoundary {

    /**
     * the logger to use.
     */
    private static final Logger LOGGER = Logger
            .getLogger(PackageBoundaryAspect.class.getName());
    
    
    /**
     * Pointcut intercepting the call of methods non private and
     * non static to the classes annotated with the 
     * {@link 
     * org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *   call(!private !static * 
     *   (@org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary *)
     *      .*(..)) 
     *   && target(calledObj) && this(callerObj) && if()
     * </pre>
     * 
     * The pointcut is represented by a static boolean method 
     * because of the use of the <code>if()</code> pointcut (see 7.3.2 from
     * AspectJ in action 2 ed.)
     * 
     * @param calledObj instance of the called object.
     * @param callerObj instance of the caller object.
     * @return false if the callerObj and calledObj have are on the same package
     * or if the callerObj and calledObj are the same instance, true otherwise.
     */
    @Pointcut("call(!private !static * "
   + "(@org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary *)."
   + "*(..)) " + "&& target(calledObj) && this(callerObj) && if()")
    public static boolean callOfPackageBoundaryPointcut(final Object calledObj,
            final Object callerObj) {

        //caller and called are the same instance
        if (calledObj == callerObj) {
            return false;
        }
        
        Package calledObjPackage = calledObj.getClass().getPackage();
        
        Package callerObjPackage = callerObj.getClass().getPackage();
        
        //the caller and called are in the same package.
        if (calledObjPackage.equals(callerObjPackage)) {
            return false;
        }
        
        Aoplib4jPackageBoundary pkgBoundary = 
            calledObjPackage.getAnnotation(Aoplib4jPackageBoundary.class);
        
        //should never happen unless the aspectj weaver is buggy
        if (pkgBoundary == null) {
            LOGGER.log(Level.WARNING, "Cannot retrieve " 
                    + Aoplib4jPackageBoundary.class
                    + " annotation from package " + calledObjPackage
                    + "; The package is not annotated with the "
                    + Aoplib4jPackageBoundary.class);
            
            return false;
        }
        
        return true;
    }

    /**
     * Advice for the execution of the
     * this{@link #callOfPackageBoundaryPointcut(Object, Object)} pointcut.
     * 
     * The advice retrieve the package of the caller object 
     * (using <code>callerObj</code> parameter) and the package of the called 
     * object (using the <code>calledObj</code> parameter) and then do a 
     * violation check. See
     * {@link #packageBoundaryViolated(Aoplib4jPackageBoundary, String)} for 
     * more 
     * information about the boundary violation check.
     * 
     * 
     * In the case of a violation, the framework creates an instance of 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback} 
     * (using
     * as implementation class taken from 
     * {@link Aoplib4jPackageBoundary#callbackClass()}) and call the callback 
     * method 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback
     *  #boundaryViolation(
     *  org.aoplib4j.modularity.ViolationInformation)}
     * .
     * 
     * @param calledObj the called object.
     * @param callerObj the caller object.
     * @param jp aspectj joinpoint object. 
     */
    @Before("callOfPackageBoundaryPointcut(calledObj, callerObj)")
    public void callOfPackageBoundaryAdvice(final Object calledObj,
            final Object callerObj, final JoinPoint jp) {
        
        
        Package calledObjPackage = calledObj.getClass().getPackage();
        
        Package callerObjPackage = callerObj.getClass().getPackage();
        
        Aoplib4jPackageBoundary pkgBoundary = 
            calledObjPackage.getAnnotation(Aoplib4jPackageBoundary.class);
        
        if (packageBoundaryViolated(pkgBoundary, callerObjPackage.getName())) {
            createAndExecuteCallback(
                    calledObj, callerObj, jp, pkgBoundary, null);
        }
    }
 
    /**
     * Pointcut intercepting the call of methods non private and
     * static to the classes annotated with the 
     * {@link org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary}
     * annotations.
     * 
     * <pre>
     *  AspectJ pointcut:
     *    call(!private static * 
     *    (@org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary *)
     *      .*(..)) 
     * </pre>
     * The pointcut is represented by a static boolean method 
     * because of the use of the <code>if()</code> pointcut (see 7.3.2 from
     * AspectJ in action 2 ed.)
     * 
     * @param jpsp AspectJ join point static part.
     * @return false if the {@link Aoplib4jPackageBoundary} annotation 
     * retrieved from
     * the called class package is null, true otherwise.
     */
    @Pointcut("call(!private static * "
       + "(@org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary *)."
       + "*(..)) && if()")
    public static boolean callOfStaticPackageBoundaryPointcut(
            final JoinPoint.StaticPart jpsp) {

        Class< ? > calledClass = jpsp.getSignature().getDeclaringType();
        Package calledPackage = calledClass.getPackage();
                
        Aoplib4jPackageBoundary pkgBoundary = 
            calledPackage.getAnnotation(Aoplib4jPackageBoundary.class);
        
        //should never happen unless the aspectj weaver is buggy
        if (pkgBoundary == null) {
            LOGGER.log(Level.WARNING, "Cannot retrieve " 
                    + Aoplib4jPackageBoundary.class
                    + " annotation from package " + calledPackage
                    + "; The package is not annotated with the "
                    + Aoplib4jPackageBoundary.class);
            return false;
        }
        
        return true;
    }
    
    /**
     * Advice executed before of the 
     * {@link #callOfStaticPackageBoundaryPointcut(
     * org.aspectj.lang.JoinPoint.StaticPart)} pointcut.
     * 
     * The advice retrieve the called informations using the
     * {@link org.aspectj.lang.JoinPoint.StaticPart}
     * object and the package called object . 
     * Since static pointcuts cannot retrieve the caller object
     * using <code>this</code> pointcut, the caller object information is 
     * computed using the stack trace of the current execution thread 
     * (by creating a new {@link Throwable} object and calling 
     * {@link Throwable#getStackTrace()}).
     * 
     * After having the packages of the caller and the called object a violation
     * check is made.See
     * {@link #packageBoundaryViolated(Aoplib4jPackageBoundary, String)} 
     * for more 
     * information about the boundary violation check.
     * 
     * 
     * In the case of a violation, the framework creates an instance of 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback} 
     * (using
     * as implementation class taken from 
     * {@link Aoplib4jPackageBoundary#callbackClass()}) and call the 
     * callback method 
     * {@link 
     * org.aoplib4j.modularity.BoundaryViolationCallback
     *  #boundaryViolation(
     *  org.aoplib4j.modularity.ViolationInformation)}
     * 
     * @param jpsp AspectJ join point static part.
     * 
     * @see 
     *PackageBoundaryAspect#packageBoundaryViolated(
     *  Aoplib4jPackageBoundary, String)
     */
    @Before("callOfStaticPackageBoundaryPointcut(jpsp)")
    public void callOfStaticPackageBoundaryAdvice(
            final JoinPoint.StaticPart jpsp) {
        

        Class< ? > calledClass = jpsp.getSignature().getDeclaringType();
        Package calledPackage = calledClass.getPackage();
                
        Aoplib4jPackageBoundary pkgBoundary = 
            calledPackage.getAnnotation(Aoplib4jPackageBoundary.class);

        Method calledMethod = 
            ((MethodSignature) jpsp.getSignature()).getMethod();
        
        StackTraceElement callerSte = this.getCallerInformation(calledClass
                .getCanonicalName(), calledMethod.getName());
        
        String callerClassName = callerSte.getClassName();
        
        String callerPkgName = 
            callerClassName.substring(0, callerClassName.lastIndexOf('.'));
        
        //caller and called are in the same package so just return.
        if (calledPackage.getName().equals(callerPkgName)) {
            return;
        }
        
        if (packageBoundaryViolated(pkgBoundary, callerPkgName)) {
            
            createAndExecuteCallback(calledClass, null, pkgBoundary, 
                    calledMethod, callerSte);
        }
    }
    
    /**
     * Verifies the violation of package boundary by retrieving the information
     * from the {@link Aoplib4jPackageBoundary} annotation (the packages list, 
     * the
     * list type) and comparing with the package name of the caller class.
     * 
     * @param pkgBoundary {@link Aoplib4jPackageBoundary} annotation.
     * @param callerObjPackage caller object package.
     * @return true if a boundary violation; false otherwise.
     */
    private boolean packageBoundaryViolated(
            final Aoplib4jPackageBoundary pkgBoundary,
            final String callerObjPackage) {
                
        String[] packagesList = pkgBoundary.packagesList();
        ListType listType = 
            pkgBoundary.packagesListType();
        
        return searchStringIntoArray(callerObjPackage, listType, packagesList);
    }
}
