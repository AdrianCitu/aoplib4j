/*
 *  Copyright 2008-2010 the original author or authors.
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
package org.aoplib4j.uml;


/**
 * Interface used by the {@link SequenceDiagramTest}. For every test of a writer
 * a new class that implements this interface must be created.
 * @see SequenceDiagramTest
 *  
 * @author Adrian Citu
 *
 */
public interface ActorInterface {

    public abstract void actorMethod();

    public abstract void anotherActorMethod();

    public abstract void actorMethodWithParams(String str);

    public abstract void nonAnnotatedMethod();

    public abstract void methodWithNestedAnnotation(String str);

    public abstract void methodWithNestedAnnotation2(String str);

}