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

import org.aoplib4j.modularity.BoundaryViolationCallback;
import org.aoplib4j.modularity.Aoplib4jClassBoundary;
import org.aoplib4j.modularity.ListType;
import org.aoplib4j.modularity.Aoplib4jPackageBoundary;
import org.aoplib4j.modularity.ViolationInformation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;


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
     * {@link Aoplib4jClassBoundary} or {@link Aoplib4jPackageBoundary} and 
     * execute the 
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
            final Aoplib4jClassBoundary classBoundary, 
            final Aoplib4jPackageBoundary pkgBoundary,
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
      * from the {@link Aoplib4jClassBoundary#callbackClass()} or from the
      * {@link Aoplib4jPackageBoundary#callbackClass()}.
      * 
      * 
      * @see BoundaryViolationCallback
      * 
      * @param classBoundary
      *            the {@link Aoplib4jClassBoundary} annotation.
      * @param pkgBoundary the {@link Aoplib4jPackageBoundary} annotation.
      * 
      * @return instance of a {@link BoundaryViolationCallback}.
      * 
      * @throws IllegalAccessException
      *             if the instantiation fails (introspection is used)
      * @throws InstantiationException
      *             if the instantiation fails (introspection is used)
      */
     private BoundaryViolationCallback createCallBackInstance(
             final Aoplib4jClassBoundary classBoundary,
             final Aoplib4jPackageBoundary pkgBoundary)
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
      * Normally the caller information is on the third position on 
      * the stack trace but the stack trace can be modified by the weaving of 
      * an aspect (see http://code.google.com/p/aoplib4j/issues/detail?id=75).
      * So the method returns the first entry from the stacktrace that do not 
      * have as package name 
      * <code>org.aoplib4j.modularity.internal</code>.
      * ;the first position contains the call to this method, and the 
      * second one to the
      * {@link 
      * ClassBoundaryAspect#callOfStaticClassBoundaryAdvice(
      * org.aspectj.lang.JoinPoint.StaticPart)}
      * 
      *or
      *{@link
      *PackageBoundaryAspect#callOfStaticPackageBoundaryAdvice(
      * org.aspectj.lang.JoinPoint.StaticPart)}
      * 
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

         String packageName = this.getClass().getPackage().getName();
          
         StackTraceElement[] stes = new Throwable().getStackTrace();

         for (StackTraceElement stackTraceElement : stes) {
            if (!stackTraceElement.getClassName().startsWith(packageName)) {
                return stackTraceElement;
            }
        }
         //should never happen;
         //better that sending a null.
         return stes[2];
     }

     /**
      * Method that will create  a callback class using information from the
      * {@link Aoplib4jClassBoundary} or {@link Aoplib4jPackageBoundary} 
      * and execute the
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
              final Aoplib4jPackageBoundary pkgBoundary, 
              final Aoplib4jClassBoundary classBoundary) {
          
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
      /**
       * @param stringTolook the string to look for
       * @param listType the type of list
       * @param stringArray the array of string on which the search should be 
       * done
       * @return true if the <code>stringArray</code> is a 
       * {@link ListType#BLACKLIST} and the 
       * <code>stringToLook</code> is present into the array. 
       * false if the <code>stringArray</code> is a 
       * {@link ListType#WHITELIST} and the 
       * <code>stringToLook</code> is present into the array. 
       */
       boolean searchStringIntoArray(final String stringTolook,
              final ListType listType, final String[] stringArray) {
          
          
          if (stringTolook == null || stringArray == null || listType == null) {
              LOGGER.log(Level.WARNING, 
                      "One of the following parameters was null:"
                      + " stringToLook:" + stringTolook
                      + ", listType:" + listType
                      + ", stringArray:" + stringArray);
              return false;
          }
          
          if (ListType.BLACKLIST.equals(listType)) {
              for (String cls : stringArray) {
                  if (stringTolook.equals(cls)) {
                      return true;
                  }
              }
              
              return false;
          //WHITELIST    
          } else {
              for (String cls : stringArray) {
                  if (stringTolook.equals(cls)) {
                      return false;
                  }
              }   
              return true;    
          }
      }      
}
