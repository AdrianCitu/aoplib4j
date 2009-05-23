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
import org.aspectj.lang.reflect.MethodSignature;

import com.google.code.aoplib4j.aspectj.modularity.BoundaryViolationCallback;
import com.google.code.aoplib4j.aspectj.modularity.ClassBoundary;
import com.google.code.aoplib4j.aspectj.modularity.PackageBoundary;
import com.google.code.aoplib4j.aspectj.modularity.ViolationInformation;

/**
 * Abstract class containing the common behavior for the package modularity
 * and class modularity aspects.
 * 
 * @author Adrian Citu
 *
 */
abstract class AbstractBoundary {

    /**
     * the logger to use.
     */
    private static final Logger LOGGER = Logger
            .getLogger(AbstractBoundary.class.getName());
    
    /**
     * 
     * Method that will create  a callback class using information from the
     * {@link ClassBoundary} or {@link PackageBoundary} and execute the 
     * callback by calling 
     * {@link BoundaryViolationCallback#boundaryViolation(ViolationInformation)}
     * .
     * 
     * @param calledClass the class of called object.
     * @param classBoundary the class boundary annotation (if applicable)
     * @param pkgBoundary the package boundary annotation (if applicable)
     * @param calledMethod the called method
     * @param callerSte the caller stacke trace.
     */
     void createAndExecuteCallback(final Class< ? > calledClass,
            final ClassBoundary classBoundary, 
            final PackageBoundary pkgBoundary,
            final Method calledMethod,
            final StackTraceElement callerSte) {
        try {
            BoundaryViolationCallback cllbck = 
                createCallBackInstance(classBoundary, pkgBoundary);

            ViolationInformation bv = new ViolationInformationImpl(
                    calledMethod.getName(), 
                    calledClass.getCanonicalName(),
                    callerSte.getClassName());

            cllbck.boundaryViolation(bv);
        } catch (InstantiationException e) {
            LOGGER.log(Level.WARNING, "Creation of call back failed", e);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Creation of call back failed", e);
        }
    }
     
     /**
      * Creates an instance of boundary violation callback using the information
      * from the {@link ClassBoundary#callbackClass()} or from the
      * {@link PackageBoundary#callbackClass()}.
      * 
      * 
      * @see BoundaryViolationCallback
      * 
      * @param classBoundary
      *            the {@link ClassBoundary} annotation.
      * @param pkgBoundary the {@link PackageBoundary} annotation.
      * 
      * @return instance of a {@link BoundaryViolationCallback}.
      * 
      * @throws IllegalAccessException
      *             if the instantiation fails (introspection is used)
      * @throws InstantiationException
      *             if the instantiation fails (introspection is used)
      */
     private BoundaryViolationCallback createCallBackInstance(
             final ClassBoundary classBoundary,
             final PackageBoundary pkgBoundary)
             throws InstantiationException, IllegalAccessException {
         
         Class< ? extends BoundaryViolationCallback> callBackClass = null;
         
         if (classBoundary == null) {
             callBackClass = pkgBoundary.callbackClass();
         } else {
             callBackClass = classBoundary.callbackClass();
         }
             

         LOGGER.info("Creating a boundary callback instance of " 
                 + callBackClass);

         return callBackClass.newInstance();

     }
     
     /**
      * Method that computes the information about the caller class. The
      * information is stored into a {@link StackTraceElement} object.
      * 
      * The caller information is on the third position on the stack trace;
      * the first position contains the call to this method, and the 
      * second one to the
      * {@link 
      * #callOfStaticClassBoundaryAdvice(org.aspectj.lang.JoinPoint.StaticPart)}
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
      StackTraceElement getCallerInformation(
             final String calledClassName, final String calledMethodName) {

         StackTraceElement[] stes = new Throwable().getStackTrace();

         return stes[2];
     }

     /**
      * Method that will create  a callback class using information from the
      * {@link ClassBoundary} or {@link PackageBoundary} and execute the 
      * callback by calling 
      *{@link BoundaryViolationCallback#boundaryViolation(ViolationInformation)}
      * .
      * 
      * @param calledObj the called object
      * @param callerObj the caller object
      * @param jp aspectj joinpoint
      * @param pkgBoundary package boundary annotation (if applicable)
      * @param classBoundary class boundary annotation (if applicable)
      */
      void createAndExecuteCallback(
              final Object calledObj,
              final Object callerObj, 
              final JoinPoint jp,
              final PackageBoundary pkgBoundary, 
              final ClassBoundary classBoundary) {
          
          try {
              BoundaryViolationCallback cllbck = 
                  createCallBackInstance(classBoundary, pkgBoundary);

              Method calledMethod = ((MethodSignature) jp.getSignature())
              .getMethod();

              ViolationInformation bv = new ViolationInformationImpl(
                      calledMethod.getName(), 
                      calledObj.getClass().getCanonicalName(), 
                      callerObj.getClass().getCanonicalName());

              cllbck.boundaryViolation(bv);
          } catch (InstantiationException e) {
              LOGGER.log(Level.WARNING, "Creation of call back failed", e);
          } catch (IllegalAccessException e) {
              LOGGER.log(Level.WARNING, "Creation of call back failed", e);
          }
      }
}
