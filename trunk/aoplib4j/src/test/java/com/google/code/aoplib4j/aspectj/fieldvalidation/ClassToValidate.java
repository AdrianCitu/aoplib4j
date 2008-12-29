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
package com.google.code.aoplib4j.aspectj.fieldvalidation;

/**
 * Class used by the jUnit tests for the {@link Validate} annotation.
 *  
 * @author Adrian Citu
 *
 */
public class ClassToValidate {

    @Validate(validationClass=StringValidator.class)
    private String str;
    
    private Integer integer = null;
    
    private int i= 0;

    @Validate(validationClass=StringValidator.class)
    private static String staticStr;
    
    private static Integer staticInteger;
    
    private int staticI= 0;

    public String getStr() {
        return this.str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Integer getInteger() {
        return this.integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public static String getStaticStr() {
        return staticStr;
    }

    public static void setStaticStr(String staticStr) {
        ClassToValidate.staticStr = staticStr;
    }

    public static Integer getStaticInteger() {
        return staticInteger;
    }

    public static void setStaticInteger(Integer staticInteger) {
        ClassToValidate.staticInteger = staticInteger;
    }

    public int getStaticI() {
        return this.staticI;
    }

    public void setStaticI(int staticI) {
        this.staticI = staticI;
    }
}
