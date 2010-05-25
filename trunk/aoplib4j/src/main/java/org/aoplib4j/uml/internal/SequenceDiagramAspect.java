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
package org.aoplib4j.uml.internal;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.aoplib4j.failurehandling.internal.RetryExecutionAspect;
import org.aoplib4j.uml.Aoplib4jSequenceDiagram;
import org.aoplib4j.uml.SequenceDiagram;
import org.aoplib4j.uml.SequenceDiagramWriter;
import org.aoplib4j.uml.SequenceMethod;
import org.aspectj.lang.Aspects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect that implements the creation of a sequence diagram. The aspect use
 * the <code>percflow</code> association; an aspect instance will be created for
 * every method annotated with the {@link Aoplib4jSequenceDiagram}.
 * For every annotated method, the aspect will create(write) a sequence diagram
 * using as writer an instance of the class passed as parameter to
 * {@link Aoplib4jSequenceDiagram#diagramWriter()}
 * 
 * @see #sequenceDiagramMethodStartPointcut(Aoplib4jSequenceDiagram)
 * @see Aoplib4jSequenceDiagram
 * 
 * @author Adrian Citu
 **/
@Aspect("percflow( " 
        + "execution("
        + "@org.aoplib4j.uml.Aoplib4jSequenceDiagram * * (..)))")
/*
 * WARNINNG the pointcut of the percflow SHOULD BE THE SAME as the 
 * sequenceDiagramMethodStartPointcut; the only reason that I didn't use 
 * directly the sequenceDiagramMethodStartPointcut due to a OutOfMemoryError 
 * "Java heap space" exception.
 */
public final class SequenceDiagramAspect {

    /**
     *  Objects containing aspect instances; this is useful and used in the 
     *  case of nested annotations (method annotated containing their flow
     *  another annotated methods).
     *  
     */
    private static final ThreadLocal<List<SequenceDiagramAspect>> THREAD_LOCAL 
        = new ThreadLocal<List<SequenceDiagramAspect>>(); 
    
    static {
        THREAD_LOCAL.set(new Vector<SequenceDiagramAspect>());
    }
    
    /**
     * Boolean which is true if the aspect execution is inside a sequence
     * diagram, false otherwise. 
     * This lag is used by the {@link #validSequenceDiagramPointcut()}
     * which is an <code>if</code> pointcut to see if a method is executed
     * inside the diagram or outside.
     */
    private boolean insideSequenceDiagram = false;
    
    /**
     *  Sequence diagram object attached; contains all the information
     *  concerning the diagram(path, writer).
     */
    private SequenceDiagram seqDiagram = null;
        
    /**
     * the logger to use.
     */
    private static final Logger LOGGER = 
        Logger.getLogger(RetryExecutionAspect.class.getName());

    /**
     * the name under which a constructor will appear in the diagrams.
     */
    private static final String CONSTRUCTOR_METHOD_NAME = "constructor";

    /**
     * Pointcut representing the execution of the methods annotated with the
     * {@link Aoplib4jSequenceDiagram} annotation. The pointcut represents
     * the starting point of the sequencediagram.
     * 
     * <pre>
     * AspectJ pointcut:
     * 
     * execution(@org.aoplib4j.uml.Aoplib4jSequenceDiagram * * (..))
     *      && @annotation(seqAnnot)
     * </pre>
     * 
     * @param seqAnnot
     *            the annotation of the method (it will be used to retrieve
     *            the parameters).
     */
    @Pointcut("execution("
            + "@org.aoplib4j.uml.Aoplib4jSequenceDiagram * * (..))"
            + " && @annotation(seqAnnot)")
    /*
     * WARNINNG the pointcut of the percflow SHOULD BE THE SAME as the 
     * sequenceDiagramMethodStartPointcut; the only reason that I didn't use 
     * directly the sequenceDiagramMethodStartPointcut due to a OutOfMemoryError
     * "Java heap space" exception.
     */        
    public void sequenceDiagramMethodStartPointcut(
            final Aoplib4jSequenceDiagram seqAnnot) {
    }
    
    /**
     * Pointcut used to distinguish if a(nother) pointcut is a valid pointcut
     * for the writing of the diagram.
     * 
     * A pointcut is valid if :
     * <lu>
     *      <li>the pointcut is inside of the control flow of the 
     *    {@link #sequenceDiagramMethodStartPointcut(Aoplib4jSequenceDiagram)}
     *    pointcut, so the 
     *        {@link SequenceDiagramAspect#insideSequenceDiagram} is true.
     *      </li>
     *      <pre>(AND)</pre>
     *      <li> the current depth of the diagram is smaller or equal than 
     *      the maximum diagram depth.
     *      </li>
     * </lu>
     * The pointcut is represented by a static boolean method 
     * because of the use of the <code>if()</code> pointcut (see 7.3.2 from
     * AspectJ in action 2 ed.)
     * 
     * <pre>
     * AspectJ pointcut:
     *  if()
     * </pre>
     * @return true if the program flow is in the control flow of the 
     * {@link #sequenceDiagramMethodStartPointcut(Aoplib4jSequenceDiagram)} 
     * false otherwise.
     */
    @Pointcut("if()")
    public static boolean validSequenceDiagramPointcut() {
        
       boolean aspectAttached = Aspects.hasAspect(SequenceDiagramAspect.class);
       
       if (aspectAttached) {
           SequenceDiagramAspect instance = 
               Aspects.aspectOf(SequenceDiagramAspect.class);
           
           // instance.maxDiagramDepth - 1 because the next executed method will
           //increase the actual diagram depth with 1.
           
           return (instance.insideSequenceDiagram
               && instance.seqDiagram.getDiagramDepth() 
                   <= instance.seqDiagram.getMaxDiagramDepth() - 1);
       } else {
           return false;
       }
 
    }

    /**
     *  Pointcut containing the execution of all the methods and constructors 
     *  inside of the sequence diagram. To know if the pointcut is inside the 
     *  sequence diagram the {@link #validSequenceDiagramPointcut()} is used.
     *  
     *  <pre>
     *   AspectJ pointcut:
     *   validSequenceDiagramPointcut()
     *      && (execution(* *.*(..)) || execution(*.new(..)))
     *      && !within (org.aoplib4j.uml.internal.SequenceDiagramAspect.*) 
     *      && !within (org.aoplib4j.uml.SequenceDiagramWriter+)
     *      && !within (org.aoplib4j.uml.SequenceDiagram)
     *      && !within (org.aoplib4j.uml.SequenceMethod)
     *      && !within (org.aoplib4j.*.internal.*)
     *  </pre>
     *  
     *  @see #validSequenceDiagramPointcut()
     */
    @Pointcut("validSequenceDiagramPointcut()"
            + "&& (execution(* *.*(..)) || execution(*.new(..)))" 
            + "&& !within (org.aoplib4j.uml.internal.SequenceDiagramAspect)"
            + "&& !within (org.aoplib4j.uml.SequenceDiagramWriter+)"
            + "&& !within (org.aoplib4j.uml.SequenceDiagram)"
            + "&& !within (org.aoplib4j.uml.SequenceMethod)"
            + "&& !within (org.aoplib4j.*.internal.*)"
            )    
    public void sequenceMethodsPointcut() {

    }
 
    /**
     * Method executed before the execution of any method belonging to a 
     * sequence diagram.
     * 
     * The method do not use the <code>this</code> instance, instead retrieves
     * the aspect instances from the local thread object ({@link #THREAD_LOCAL})
     * because the behavior of this method can/should be applied to ALL the 
     * method of ALL the aspects.
     * 
     * For every aspect instance a new {@link SequenceMethod} is created and
     * attached to the {@link SequenceDiagram} of every aspect instance.
     * 
     * @param jp AspectJ join point.
     */
    private void sequenceMethodBefore(final JoinPoint jp) {
        
        Signature sig = jp.getSignature();
        
        List<SequenceDiagramAspect> aspectInstances = 
            SequenceDiagramAspect.THREAD_LOCAL.get();
        
        for (SequenceDiagramAspect aspect : aspectInstances) {
            SequenceMethod seqMet = 
                aspect.createSequenceMethodInstance(
                        sig, aspect.seqDiagram.getActiveMethod());
            
            aspect.seqDiagram.setActiveMethod(seqMet);
            //change the diagram depth
            aspect.seqDiagram.increaseDiagramDepth();
        }
    }
    

    /**
     * Method executed after the execution of any method belonging to a 
     * sequence diagram.
     * 
     * The method do not use the <code>this</code> instance, instead retrieves
     * the aspect instances from the local thread object ({@link #THREAD_LOCAL})
     * because the behavior of this method can/should be applied to ALL the 
     * method of ALL the aspects.
     * 
     * For every aspect instance, the active method is changed; the active
     * method became the parent of the actual active method and the depth
     * of the diagram is decreased.
     * 
     * @param jp AspectJ join point.
     */
    private void sequenceMethodAfter(final JoinPoint jp) {
              
        List<SequenceDiagramAspect> aspectInstances = 
            SequenceDiagramAspect.THREAD_LOCAL.get();
        
        for (SequenceDiagramAspect aspect : aspectInstances) {
                        
            aspect.seqDiagram.setActiveMethod(
                    aspect.seqDiagram.getActiveMethod().getParent());
            
            //change the diagram depth
            aspect.seqDiagram.decreaseDiagramDepth();
        }
    }  
    
    /**
     * Advice executed around the methods inside the sequence diagram (all the 
     * methods beside the annotated method/root method).
     * 
     * This method calls the {@link #sequenceMethodBefore(JoinPoint)},
     * {@link ProceedingJoinPoint#proceed()} and 
     * {@link #sequenceMethodAfter(JoinPoint)}.
     * 
     * 
     * @param pjp the AspectJ proceeding pointcut.
     * @return the result of the advice execution. 
     * @throws Throwable exception thrown by the advice execution. 
     */
    @Around("sequenceMethodsPointcut()")
    public Object sequenceMethodsAroundAdvice(final ProceedingJoinPoint pjp) 
        throws Throwable {
        
        this.sequenceMethodBefore(pjp);
        
        Object returnValue = pjp.proceed();
        
        this.sequenceMethodAfter(pjp);
        
        return returnValue;
        
    }
   

    /**
     * Method that will create for the current aspect instance a new diagram
     * and the attached writer. This method should only be called by the
     *  {@link #sequenceMethodStartAroundAdvice(
     *      Aoplib4jSequenceDiagram, ProceedingJoinPoint)}
     *      
     * 
     * @param seqAnnot the annotation.
     * @param pjp object created by AspectJ framework providing access 
     *  to the around advice.
     * @throws InstantiationException if the creation of diagram writer fails.
     * @throws IllegalAccessException if the creation of diagram writer fails.
     */
    private void sequenceMethodStartBeforeForThis(
            final Aoplib4jSequenceDiagram seqAnnot,
            final ProceedingJoinPoint pjp) 
        throws InstantiationException, IllegalAccessException {
        
        JoinPoint.StaticPart jpsp = pjp.getStaticPart();
        Signature sig = pjp.getSignature();
        
        LOGGER.info("DIAGRAM START " + jpsp.getSignature().getName());

        //create the root method and attached it to the aspect diagram.
        SequenceMethod rootMethod = this.createSequenceMethodInstance(sig);

        //create the sequence diagram for this aspect instance.
        this.seqDiagram = new SequenceDiagram(seqAnnot.diagramDepth(), 
                this.createWriterInstance(seqAnnot, jpsp),
                this.getDiagramFullPath(seqAnnot, jpsp),
                rootMethod);
        
        SequenceDiagramAspect.addAspectToThread(this);
        
        this.insideSequenceDiagram = true;
    }
    
    
    /**
     * @param pjp object created by AspectJ framework providing access 
     *  to the around advice.
     *  
     *  This method should only be called by the
     *  {@link #sequenceMethodStartAroundAdvice(
     *      Aoplib4jSequenceDiagram, ProceedingJoinPoint)}
     */
    private void sequenceMethodStartAfterForAllInstances(
            final ProceedingJoinPoint pjp) {
        
        SequenceDiagramAspect.removeAspectFromThread(this);
        
        //if nested annotations, this method can be a inner method
        //of another aspect
        this.sequenceMethodAfter(pjp);
    }
    
    /**
     * Method called at the end of the <code>sequenceMethodStartAroundAdvice
     * </code>. The method call the 
     * {@link SequenceDiagramWriter#write(SequenceMethod)} and close the 
     * writer attached to the diagram writer.
     * 
     * This method should only be called by the
     *  {@link #sequenceMethodStartAroundAdvice(
     *      Aoplib4jSequenceDiagram, ProceedingJoinPoint)}
     *      
     * @throws IOException if the diagram writing fails.
     */
    private void sequenceMethodStartAfterForThis() throws IOException {
        
        this.seqDiagram.write();
        this.seqDiagram.closeWriter();
        
        this.insideSequenceDiagram = false;
    }
    
    /**
     *  Method executed around the first method (root method) of the sequence
     *  diagram.
     *  
     *  The method flow is:
     *  <lu>
     *      <li>
     *       call 
     *   {@link #sequenceMethodBefore(JoinPoint)}
     *      </li>
     *      
     *      <li>
     *      call 
     *      {@link #sequenceMethodStartBeforeForThis(
     *          Aoplib4jSequenceDiagram, ProceedingJoinPoint)}
     *      </li>
     *      
     *      <li>
     *          call {@link ProceedingJoinPoint#proceed()}
     *      </li>
     *      
     *      <li>
     *      call {@link #sequenceMethodStartAfterForThis()}
     *      </li>
     *      
     *      <li>
     *       call
     *     {@link #sequenceMethodStartAfterForAllInstances(ProceedingJoinPoint)}
     *      </li>
     *  </lu>
     *  
     *  
     * @param seqAnnot the annotation.
     * @param pjp object created by AspectJ framework providing access 
     *  to the around advice. 
     * @return the result of the joinpoint execution.
     * @throws Throwable the exception thrown by the joinpoint execution;since
     * the aspect cannot treat it, just throw it further.  
     */
    @Around("sequenceDiagramMethodStartPointcut(seqAnnot)")
    public Object sequenceMethodStartAroundAdvice(
            final Aoplib4jSequenceDiagram seqAnnot,
            final ProceedingJoinPoint pjp) throws Throwable {

        //for the case of inner annotations; handle this method for the 
        //other aspects as a normal inner method.
        this.sequenceMethodBefore(pjp);
        
        sequenceMethodStartBeforeForThis(seqAnnot, pjp);
        
        Object returnValue = pjp.proceed();
        
               
        sequenceMethodStartAfterForThis();
        sequenceMethodStartAfterForAllInstances(pjp);
        
        LOGGER.info("DIAGRAM END " + pjp.getSignature().getName());
        
        return returnValue;
    }
    
    /**
     * Create {@link SequenceMethod} object by retrieving all the needed 
     * informations from the {@link Signature} object (method name, method
     * return value, method parameters).
     * 
     * @param sig AspectJ signature
     * @param parent the parent method; optional parameter.
     * @return a new instance of sequence method.
     */
    private SequenceMethod createSequenceMethodInstance(final Signature sig,
            final SequenceMethod ... parent) {
        
        String className = sig.getDeclaringTypeName();
        String methodName = CONSTRUCTOR_METHOD_NAME;
        Class< ? > returnType = null;
        Class< ? >[] parameterTypes = {};
        String[] parameterNames = {};
        
        boolean[] isStaticAndConstructor = {false, false};
        
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            
            methodName = msig.getMethod().getName();
            isStaticAndConstructor[0] = Modifier.isStatic(msig.getModifiers());
            
            returnType = msig.getReturnType();
            
            if (msig.getParameterTypes() != null) {
                parameterTypes = msig.getParameterTypes();
            }
            
            if (msig.getParameterNames() != null) {
                parameterNames = msig.getParameterNames();  
            }
            
        } else if (sig instanceof ConstructorSignature) {
            ConstructorSignature msig = (ConstructorSignature) sig;

            isStaticAndConstructor[1] = true;
            
            if (msig.getParameterTypes() != null) {
                parameterTypes = msig.getParameterTypes();
            }
            
            if (msig.getParameterNames() != null) {
                parameterNames = msig.getParameterNames();  
            }
        }
        
        if (parent != null && parent.length != 0) {
            return new SequenceMethodImpl(className, methodName, 
                    isStaticAndConstructor, 
                    returnType, parameterTypes, parameterNames, parent[0]);   
        } else {
            return new SequenceMethodImpl(className, methodName, 
                    isStaticAndConstructor, 
                    returnType, parameterTypes, parameterNames);
        }
    }
    
    /**
     * Method that creates an instance of {@link SequenceDiagramWriter}. The
     * needed information are the writer class (retrieved from the 
     * {@link Aoplib4jSequenceDiagram#diagramWriter()}) and the diagram full
     * path  {@link Aoplib4jSequenceDiagram#diagramFullPath()}).
     * 
     * If the diagram full path have the default value (empty string "") then
     * a new default pat is computed and will be 
     * java.io.tmpdir/className.methodName().txt
     * 
     * @param seqAnnot annotation to retrieve the class to instantiate.
     * @param jpsp AspectJ static join point.
     * 
     * @return instance of the class retrieved from the 
     * {@link Aoplib4jSequenceDiagram#diagramWriter()}.
     * 
     * @throws IllegalAccessException if the instantiation fails 
     *          (introspection is used)
     * @throws InstantiationException if the instantiation fails 
     *          (introspection is used)
     */
    private SequenceDiagramWriter createWriterInstance(
            final Aoplib4jSequenceDiagram seqAnnot, final StaticPart jpsp)
        throws InstantiationException, IllegalAccessException {
        
        SequenceDiagramWriter returnValue = null; 
        
        Class < ? extends SequenceDiagramWriter > writter = 
            seqAnnot.diagramWriter();
        
        LOGGER.info("Creating a sequence diagram writer instance of " 
                + writter);
        
        
        returnValue = writter.newInstance();
       
        
        return returnValue;
    }
    
    /**
     * Compute the diagram full path from the annotation. If the 
     * {@link Aoplib4jSequenceDiagram#diagramFullPath()} is "" (default value)
     * then the new diagram path will be java.io.tmpdir/ClassName.MethodName.txt
     * 
     * @param seqAnnot the annotation
     * @param jpsp the AspectJ static join point.
     * @return the full path where the diagram will be written.
     */
    private String getDiagramFullPath(final Aoplib4jSequenceDiagram seqAnnot,
            final StaticPart jpsp) {
        
        
        String diagramFullPath = seqAnnot.diagramFullPath();
        
        //should compute the diagram full path; it will be 
        //java.io.tmpdir/ClassName.MethodName.txt
        if ("".equals(diagramFullPath)) {
            String tmpDir = SequenceDiagramAspect.getJavaIoTmpDir();
            diagramFullPath = 
                tmpDir + jpsp.getSignature().toShortString() + ".txt";
        }
        
        LOGGER.info("The diagram will be written in: " + diagramFullPath);
        
        return diagramFullPath;
        
    }
    
    /**
     * On some plateform, the temporary directory returned by java.io.tmpdir do
     * not include a trailing slash. That is: <br>
     * Win NT --> C:\TEMP\ <br>
     * Win XP --> C:\TEMP <br>
     * Solaris --> /var/tmp/ <br>
     * Linux --> /var/tmp
     * 
     * @return returns value of "java.io.tmpdir" system property with a
     *         System.getProperty("file.separator") character at the and despite
     *         the platform(*NIX, Win, etc..)
     */

    private static String getJavaIoTmpDir() {

        String returnValue = System.getProperty("java.io.tmpdir");

        if (!(returnValue.endsWith("/") || returnValue.endsWith("\\"))) {
            returnValue = returnValue + System.getProperty("file.separator");
        }
        return returnValue;
    }
        
    /**
     * Add a new aspect instance to the local thread variable.
     * @param asp the new instance.
     */
    private static void addAspectToThread(final SequenceDiagramAspect asp) {
        THREAD_LOCAL.get().add(asp);
    }
    
    /**
     * Remove the aspect instance from the local thread variable.
     * @param asp the instance to remove.
     */
    private static void removeAspectFromThread(
            final SequenceDiagramAspect asp) {
        THREAD_LOCAL.get().remove(asp);
    }
}
