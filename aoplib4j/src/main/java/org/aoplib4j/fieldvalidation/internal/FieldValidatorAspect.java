/*
 *  Copyright 2008 the original author or authors.
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
package org.aoplib4j.fieldvalidation.internal;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.aoplib4j.fieldvalidation.FieldInformation;
import org.aoplib4j.fieldvalidation.FieldValidator;
import org.aoplib4j.fieldvalidation.Aoplib4jValidate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.FieldSignature;


/**
 * Aspect implementing the mechanism of field validation.
 * 
 * @author Adrian Citu
 *
 */
@Aspect
public final class FieldValidatorAspect {

    /**
     * the logger to use.
     */
    private static final Logger LOGGER = 
        Logger.getLogger(FieldValidatorAspect.class.getName());
    
    /**
     * Pointcut for the assignment of a non-static fields annotated with the 
     * {@link Aoplib4jValidate}.
     * 
     * <pre>
     * AspectJ pointcut:
     * set(@org.aoplib4j.fieldvalidation.Aoplib4jValidate * *.*)
     *  && @annotation(validateAnnot) 
     *  && args(newValue) 
     *  && this(instance)
     * </pre>
     * @param validateAnnot the field {@link Aoplib4jValidate} annotation.
     * @param newValue the new value to be assigned to the field.
     * @param instance the instance on which the intercepted field is present. 
     */
    @Pointcut(
            "set(@org.aoplib4j.fieldvalidation.Aoplib4jValidate "
            + "* *.*)" 
            + "&& @annotation(validateAnnot) " 
            + "&& args(newValue) && this(instance)")
    public void validateNonStaticFieldPointcut(
            final Aoplib4jValidate validateAnnot, 
            final Object newValue, 
            final Object instance) {
    }
    
    /**
     * Pointcut for the assignment of a static fields annotated with the 
     * {@link Aoplib4jValidate}.
     * <pre>
     * AspectJ pointcut:
     * set(@org.aoplib4j.fieldvalidation.Aoplib4jValidate 
     *      static * *.*)
     *  && @annotation(validateAnnot) 
     *  && args(newValue)
     * </pre>
     * @param validateAnnot the field {@link Aoplib4jValidate} annotation.
     * @param newValue the new value to be assigned to the field.
     */
    @Pointcut(
            "set(@org.aoplib4j.fieldvalidation.Aoplib4jValidate "
            + "static * *.*)"
            + "&& @annotation(validateAnnot) " 
            + "&& args(newValue)")
    public void validateStaticFieldPointcut(
            final Aoplib4jValidate validateAnnot, 
            final Object newValue) {
    }
    
    /**
     * Advice executed before the assignment of a new value to a field annotated
     * with {@link Aoplib4jValidate} annotation.
     *  
     * @param validateAnnot the field {@link Aoplib4jValidate} annotation.
     * @param newValue the new value to be assigned to the field.
     * @param jpsp AspectJ joinpoint static part.
     * @param instance the instance on which the intercepted field is present.
     * @throws Exception  exception thrown by execution of 
     * {@link FieldValidator#validate(FieldInformation)}.
     * 
     */
    @Before("validateNonStaticFieldPointcut(validateAnnot, newValue, instance)")
    public void validateFieldAdvice(
            final Aoplib4jValidate validateAnnot, 
            final Object newValue, 
            final JoinPoint.StaticPart jpsp, 
            final Object instance) throws Exception {
        
        this.validateField(validateAnnot, newValue, jpsp, instance); 
    }

    /**
     * @param validateAnnot the field {@link Aoplib4jValidate} annotation.
     * @param newValue the new value to be assigned to the field.
     * @param jpsp AspectJ joinpoint static part.
     * @param instance the instance on which the intercepted field is present.
     * @throws Exception  exception thrown by execution of 
     * {@link FieldValidator#validate(FieldInformation)}.
     */
    private void validateField(final Aoplib4jValidate validateAnnot,
            final Object newValue, final JoinPoint.StaticPart jpsp,
            final Object instance) throws Exception {
        FieldValidator validatorInstance = null;
        Object oldValue = null;
        
        String fieldName = getFieldName(jpsp);
        
        LOGGER.info("Validating field: " + fieldName);
        
        try {
            validatorInstance = createValidatorInstance(validateAnnot);    
        } catch (InstantiationException e) {
            LOGGER.severe("Error creating validator: " + e);
            
            //don't want to interfere with the current execution.
            return;
        } catch (IllegalAccessException e) {
            LOGGER.severe("Error creating validator: " + e);
            
           //don't want to interfere with the current execution.
           return;
        }  catch (RuntimeException e) {
            LOGGER.severe("Error creating validator: " + e);
            
            //don't want to interfere with the current execution.
            return;
        }
        
        try {
             oldValue = getFieldValue(jpsp, instance);  
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Error retrieving field actual value: " + e);
            
            //don't want to interfere with the current execution.
            return;
        } catch (IllegalAccessException e) {
            LOGGER.severe("Error retrieving field actual value: " + e);
            
            //don't want to interfere with the current execution.
            return;
        } catch (RuntimeException e) {
            LOGGER.severe("Error retrieving field actual value: " + e);
            
            //don't want to interfere with the current execution.
            return;
        }
        
        
        boolean staticField = fieldIsStatic(instance);
        
        LOGGER.info("The field is static:" + staticField);
        
        FieldInformation fldInfp = 
            new FieldInformationImpl(
                    oldValue, newValue, staticField, fieldName);
        
        validatorInstance.validate(fldInfp);
    }

    /**
     * Advice executed before the assignment of a new value to a static field 
     * annotated with {@link Aoplib4jValidate} annotation.
     * 
     * @param validateAnnot the field {@link Aoplib4jValidate} annotation.
     * @param newValue the new value to be assigned to the field.
     * @param jpsp AspectJ joinpoint static part.
     * @throws Exception  exception thrown by execution of 
     * {@link FieldValidator#validate(FieldInformation)}.
     */
    @Before("validateStaticFieldPointcut(validateAnnot, newValue)")
    public void validateStaticFieldAdvice(
            final Aoplib4jValidate validateAnnot, 
            final Object newValue, 
            final JoinPoint.StaticPart jpsp) throws Exception {
        
        this.validateField(validateAnnot, newValue, jpsp, null);
    }
    
    /**
     * @param validateAnnot annotation to retrieve the class to instantiate.
     * 
     * @return instance of the class retrieved from the 
     * {@link Aoplib4jValidate#validationClass()}.
     * 
     * @throws IllegalAccessException if the instantiation fails 
     *          (introspection is used)
     * @throws InstantiationException if the instantiation fails 
     *          (introspection is used)
     */
    private FieldValidator createValidatorInstance(
            final Aoplib4jValidate validateAnnot)
        throws InstantiationException, IllegalAccessException {
        
        //TODO use some kind of cache.
        //TODO find a better solution than creating a new instance at every call
        Class<? extends FieldValidator> validatorClass = 
            validateAnnot.validationClass();
        
        LOGGER.info("Creating a validator instance of " + validatorClass);
        return validatorClass.newInstance();
    }

    /**
     * Retrieve the value of the annotated field.
     * 
     * @param jp AspectJ joinpoint static part.
     * 
     * @param classInstance the instance of the class; used if the field is not
     * static.
     * @return the value of the field before the 
     * @throws IllegalAccessException
     *     if cannot retrieve the field value by introspection
     */
    private Object getFieldValue(
            final JoinPoint.StaticPart jp, final Object classInstance)
            throws IllegalAccessException {

        Object returnValue = null;
        
        Field field =  ((FieldSignature) jp.getSignature()).getField();
        
        LOGGER.info("Retrieve actual value for the field " + field);
        boolean wasAccessible = field.isAccessible();
        
        try {
            if (!wasAccessible) {
                field.setAccessible(true); 
            }
            returnValue = field.get(classInstance);
        } finally {
            if (!wasAccessible) {
                field.setAccessible(false);
            }
        }
        
        return returnValue;
    }
    
    /**
     * @param jp AspectJ joinpoint static part.
     * @return the name of the field.
     */
    private String getFieldName(final JoinPoint.StaticPart jp) {
        Field field =  ((FieldSignature) jp.getSignature()).getField();
        
        return field.getName();
    }
    
    /**
     * If the aspect is called on a static field the instance reference is null.
     * @param instance the instance on which the aspect is executing.
     * 
     * @return true if the aspect is not executed on a instance object.
     */
    private boolean fieldIsStatic(final Object instance) {
        return instance == null;
    }
}


