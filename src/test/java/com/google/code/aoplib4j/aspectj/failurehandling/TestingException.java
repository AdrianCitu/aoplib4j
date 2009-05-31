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
package com.google.code.aoplib4j.aspectj.failurehandling;

/**
 * Exception used for testing purposes.
 * 
 * @author Adrian Citu
 *
 */
public class TestingException extends Exception {

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 1486860741799606755L;

    /**
     * @param message the error message.
     */
    public TestingException(final String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}
