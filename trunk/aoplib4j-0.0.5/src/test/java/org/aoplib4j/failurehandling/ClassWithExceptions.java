/*
 *  Copyright 2008-2008 the original author or authors.
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
package org.aoplib4j.failurehandling;

import org.aoplib4j.failurehandling.Aoplib4jRetryExecution;

/**
 * Class used by the failure handling tests.
 * 
 * @author Adrian Citu
 *
 */
public final class ClassWithExceptions {
    
    /**
     * counter for method calls.
     */
    private int methodExecutionCounter = 0;

    /**
     * counter for static method calls.
     */
    private static int staticMethodExecutionCounter = 0;
    
    private static int numberOfExecutionsBeforeOk = 0;
    
    /**
     * @throws TestingException the exception thrown by the method.
     */
    @Aoplib4jRetryExecution(exceptionToCatch = TestingException.class, maxRetry = 2)
    public void throwException() throws TestingException {
       methodExecutionCounter++;
       
       if (methodExecutionCounter == numberOfExecutionsBeforeOk) {
           return; 
        }
       
       throw new TestingException("Testing Exception");
       
    }
    
    /**
     * @param when
     * @throws TestingException the exception thrown by the method.
     */
    @Aoplib4jRetryExecution(exceptionToCatch = TestingException.class, maxRetry = 2)
    public static void throwExceptionStatic() throws TestingException {
        ClassWithExceptions.staticMethodExecutionCounter++;
        
        if (staticMethodExecutionCounter == numberOfExecutionsBeforeOk) {
            return; 
         }
        
        throw new TestingException("Testing Exception");
    }
    
    /**
     * @throws Testing2Exception the exception thrown by the method.
     */
    @Aoplib4jRetryExecution(exceptionToCatch=Testing2Exception.class, maxRetry=3)
    void throwExceptionPackageProtected() throws Testing2Exception {
        methodExecutionCounter++;
        
        if (this.methodExecutionCounter == numberOfExecutionsBeforeOk) {
           return; 
        }
        
        throw new Testing2Exception("Testing Exception");
    }
    
    /**
     * Clear the method counter.
     */
    public void clearMethodCounter() {
        this.methodExecutionCounter = 0;
    }
    
    /**
     * @return the counter representing the number of method calls.
     */
    public int getMethodCounter() {
        return this.methodExecutionCounter;
    }

    /**
     * Method that throws no exception.
     */
    public void noExceptionMethod() {
        
    }
    
    /**
     * Method that throws no exception.
     */
    public static void noExceptionStaticMethod() {
        
    }
    
    /**
     * Clear the static method counter.
     */
    public static void clearStaticMethodCounter() {
        ClassWithExceptions.staticMethodExecutionCounter = 0;
    }

    /**
     * @return the counter representing the number of static method calls.
     */
    public static int getStaticMethodCounter() {
        return ClassWithExceptions.staticMethodExecutionCounter;
    }
    
    /**
     * Set the number of executions before the method executions is ok.
     * @param nr the new value of the number of executions.  
     */
    public static void setNumbersOfExecutionsBeforeOk(final int nr) {
        numberOfExecutionsBeforeOk = nr;
    }
}
