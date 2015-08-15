### Introduction ###
Modular programming is a software design technique that increases the extent to which software is composed from separate parts, called modules. Conceptually, modules represent a separation of concerns, and improve maintainability by enforcing logical boundaries between components(see [this](http://en.wikipedia.org/wiki/Modularity_(programming)) for a complete definition of the modular programming). `aoplib4j` offers the possibility to implement the modularity at the class level and at the package level.


### Modularity at the class level ###
In the case of the class level modularity, the class became a module and the goal is to create logical boundaries between classes(modules). In this case the logical boundaries are represented by method calls from other classes (modules).

#### How to implement the class modularity ####

For implementing the class modularity you must :
  1. Annotate your class(es) with the [org.aoplib4j.modularity.Aoplib4jClassBoundary](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/modularity/Aoplib4jClassBoundary.java)annotation
  1. Define the logical boundary by filling the annotation parameters (`Aoplib4jClassBoundary#classesList`, `Aoplib4jClassBoundary#classesListType` and `Aoplib4jClassBoundary#callbackClass`).
  1. Create your own callback class extending the `BoundaryViolationCallback` abstract class for handle the boundary violation and add it to the `Aoplib4jClassBoundary#callbackClass`.

Here is an example of `Aoplib4jClassBoundary` utilization (taken from the jUnit tests):
```
package org.aoplib4j.modularity.clas;

import org.aoplib4j.modularity.Aoplib4jClassBoundary;
import org.aoplib4j.modularity.ListType;
import org.aoplib4j.modularity.ThrowErrorBoundaryCallback;

@Aoplib4jClassBoundary(
        classesList={ForbiddenCallerClass.class}, 
        callbackClass=ThrowErrorBoundaryCallback.class,
        classesListType = ListType.BLACKLIST)
public class ClasCalledClass {
.....
```

So, in our example the `ClasCalledClass` forbids any call from the `ForbiddenCallerClass` class and in the case of a violation an instance of `ThrowErrorBoundaryCallback` is created.

The `Aoplib4jClassBoundary#classesList` parameter is a list of `java.lang.Class` objects representing the logical boundary.

The `Aoplib4jClassBoundary#classesListType` parameter is a Java enum representing the type `Aoplib4jClassBoundary#classesList` (a [blacklist](http://en.wikipedia.org/wiki/Blacklist_(computing)) or a [whitelist](http://en.wikipedia.org/wiki/Whitelist)). If the `Aoplib4jClassBoundary#classesListType` have the `ListType.WHITELIST` value then the `Aoplib4jClassBoundary#classesList` is the list of classes from which the annotated class can be called and (of course) if the `Aoplib4jClassBoundary#classesListType` have the `ListType.BLACKLIST` value the `Aoplib4jClassBoundary#classesList` is the list of classes from which the annotated class cannot be called.

The `Aoplib4jClassBoundary#callbackClass` parameter represents the callback class that will be executed when a boundary violation is detected. The `Aoplib4jClassBoundary#callbackClass` should be a class that extends the `BoundaryViolationCallback` abstract class. The structure of the `BoundaryViolationCallback`:
```
/**
 * Abstract class that must be extended for coding the behavior when a boundary
 * is violated.
 * @see LogViolationCallback
 * @see ViolationInformation
 * 
 * @author Adrian Citu
 *
 */
public abstract class BoundaryViolationCallback {

    /**
     * Default Constructor.
     */
    public BoundaryViolationCallback() {
        
    }
    
    /**
     * Method that will be executed by the framework when a boundary is
     * violated. The framework will create also an instance of 
     * {@link ViolationInformation}.
     * 
     * @param info object containing information about the boundary violation.
     */
    public abstract void boundaryViolation(final ViolationInformation info);
}
```

The `ViolationInformation` is an interface containing useful informations about the boundary violation: the caller class name, the called class name and the called method name. Here is the structure of the `ViolationInformation`:
```
/**
 * Interface containing the information about the boundary violation; what class
 * violated the boundary and what class and what method was
 * called).
 * 
 * @see Aoplib4jClassBoundary
 * 
 * @author Adrian Citu
 *
 */
public interface ViolationInformation {

    /**
     * @return the caller canonical name that violated the boundary.
     */
    String getCallerClassName();
    
    /**
     * @return the called class canonical name (marked with a 
     * {@link Aoplib4jClassBoundary}) annotation.
     */
    String getCalledClassName();
    
    /**
     * @return the called method name.
     */
    String getCalledMethodName();
    
}
```



#### Class modularity under the hood ####
For boundary checking at the class level the workflow is the following:

  * Intercept the call to all non private methods from a class annotated with the `Aoplib4jClassBoundary` annotation. The AspectJ pointcuts are:
    * non static methods
```
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
```
    * static methods
```
    @Pointcut("call(!private static * "
            + "(@org.aoplib4j.modularity.Aoplib4jClassBoundary *)."
            + "*(..))  && if()")
    public static boolean callOfStaticClassBoundaryPointcut(
            final JoinPoint.StaticPart jpsp) {

        Class< ? > calledClass = jpsp.getSignature().getDeclaringType();

        Aoplib4jClassBoundary boundary = getClassBoundaryAnnotation(calledClass);

        //advice is called on a subclass; the annotation is not inherited by
        //the subclasses.
        if (boundary == null) {
            return false;
        }
        
        return true;
    }
```
> As you can see in the two cases the `if()` pointcut is used in order to perform some compile-time checks and to avoid the advice injecting in some trivial useless cases (when the caller and called class are the same or the retrieved `Aoplib4jClassBoundary` annotation is null). For more informations about the use of `if` pointcut please check [if() pointcut expressions](http://www.eclipse.org/aspectj/doc/released/adk15notebook/ataspectj-pcadvice.html#pointcuts).

  * The @Before advice is used to check the boundary violation by using the information retrieved from the `Aoplib4jClassBoundary` annotation and the information given by the AspectJ framework.

  * If a boundary validation is detected then:
    * The framework creates an instance of `ViolationInformation` interface.
    * The framework creates an instance of the callback class (a class extending `BoundaryViolationCallback`) by introspection. The type of the class is retrieved from the `Aoplib4jClassBoundary#callbackClass` field.
    * The callback method `BoundaryViolationCallback#boundaryViolation(final ViolationInformation info)` is call to handle to boundary violation. **Note that the `Aoplib4jClassBoundary` have a default callback class, `org.aoplib4j.modularity.LogViolationCallback`**

Here is the complete code of the [ClassBoundaryAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/modularity/internal/ClassBoundaryAspect.java).


### Modularity at the package level ###
In the case of the package level modularity, the package became a module and the goal is to create logical boundaries between packages. In this case the logical boundaries are represented by method calls from other packages.


#### How to implement the package modularity ####
For implementing the class modularity you must :
  1. Annotate your package(es) with the [org.aoplib4j.modularity.Aoplib4jPackageBoundary](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/modularity/Aoplib4jPackageBoundary.java) annotation. For annotate a Java package it must create a file called package-info.java into the package (see [Declarative Programming in Java](http://www.onjava.com/pub/a/onjava/2004/04/21/declarative.html?page=3) for more details)
  1. Define the logical boundary by filling the annotation parameters (`Aoplib4jPackageBoundary#packagesList`, `Aoplib4jPackageBoundary#packagesListType` and `Aoplib4jPackageBoundary#callbackClass`).
  1. Create your own callback class extending the `BoundaryViolationCallback` abstract class for handle the boundary violation and add it to the `Aoplib4jPackageBoundary#callbackClass`.
  1. Annotate all your package classes with the `org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary` annotation or write an aspect that will do this for you.

Here is an example of `Aoplib4jPackageBoundary` utilization (taken from the JUnit tests):
```
//package-info.java
@Aoplib4jPackageBoundary(
        packagesList = {"org.aoplib4j.modularity.pack.blacklist.forbiddenpackage"}, 
        packagesListType = ListType.BLACKLIST,
        callbackClass=ThrowErrorBoundaryCallback.class)
package org.aoplib4j.modularity.pack.blacklist;

import org.aoplib4j.modularity.Aoplib4jPackageBoundary;
```
So, in our example all the calls from the package `org.aoplib4j.modularity.pack.blacklist.forbiddenpackage` are forbidden in all the classes from the `org.aoplib4j.modularity.pack.blacklist` package. In the case of a violation an instance of `ThrowErrorBoundaryCallback` is created.

As you can see in the previous example, for annotate a package it must create a `package-info.java` file which ill contains the package annotations and the javadoc for the package (the `package-info.java` file replaces the `package.html` file).

The `Aoplib4jPackageBoundary#packagesList` parameter is a list of `java.lang.String` objects representing the logical boundary.

The `Aoplib4jPackageBoundary#packagesListType` and `Aoplib4jPackageBoundary#callbackClass` have exactly the same behavior as in the case of `Aoplib4jClassBoundary` annotation.

The last step is to annotate all the classes of the annotated package with the `Aoplib4jInjectedPkgBoundary` annotation. This step is due to fact that AspectJ cannot handle pointcuts based on package annotations (something like: "calls of all the methods from all the classes from the package annotated with `@Aoplib4jPackageBoundary`" is not a valid pointcut definition).
I must admit that it is not a very clever solution to annotate by hand all the classes but in case you use `ajc` for compile your project you can write your own aspect that will automatically inject the annotation on all the desired classes.

Here is the aspect used by the JUnit tests (I didn't want to add the `Aoplib4jInjectedPkgBoundary` by hand):
```
package org.aoplib4j.modularity.pack.blacklist;

import org.aoplib4j.modularity.Aoplib4jInjectedPkgBoundary;

/**
 * Aspect used in tests of package boundary violation.
 * The aspect injects the {@link Aoplib4jInjectedPkgBoundary} into all the classes from   the 
 * package "org.aoplib4j.modularity.pack.blacklist" excepting
 * the aspect himself, {@link BlackListPackageBoundaryViolationTest} class and 
 * package-info class.
 * 
 * @author Adrian Citu
 *
 */
public aspect PackageAnnotationIntroduction {
    
    declare @type:
        org.aoplib4j.modularity.pack.blacklist.* 
        && !org.aoplib4j.modularity.pack.blacklist.package*info
        && !org.aoplib4j.modularity.pack.blacklist.PackageAnnotationIntroduction
        && !org.aoplib4j.modularity.pack.blacklist.BlackListPackageBoundaryViolationTest:
        @Aoplib4jInjectedPkgBoundary;
```


#### Package modularity under the hood ####
For boundary checking at the package level the workflow is the following:
  * Intercept the call to all non private methods from a class annotated with the `Aoplib4jInjectedPkgBoundary` annotation. The AspectJ pointcuts are:
    * non static methods
```
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
            LOGGER.log(Level.WARNING, "Cannot retrieve " + Aoplib4jPackageBoundary.class
                    + " annotation from package " + calledObjPackage
                    + "; The package is not annotated with the "
                    + Aoplib4jPackageBoundary.class);
            
            return false;
        }
        
        return true;
    }
```
    * static methods
```
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
            LOGGER.log(Level.WARNING, "Cannot retrieve " + Aoplib4jPackageBoundary.class
                    + " annotation from package " + calledPackage
                    + "; The package is not annotated with the "
                    + Aoplib4jPackageBoundary.class);
            return false;
        }
        
        return true;
    }
```

As in case of the class boundary the pointcuts use the `if()` pointcut for compile-time checking.

  * The @Before advice is used to check the boundary violation by using the information retrieved from the `Aoplib4jPackageBoundary` annotation and the information given by the AspectJ framework. The `Aoplib4jPackageBoundary` annotation is retrieved by introspection from the called class.

  * If a boundary validation is detected then:
    * The framework creates an instance of `ViolationInformation` interface.
    * The framework creates an instance of the callback class (a class extending `BoundaryViolationCallback`) by introspection. The type of the class is retrieved from the `Aoplib4jClassBoundary#callbackClass` field.
    * The callback method `BoundaryViolationCallback#boundaryViolation(final ViolationInformation info)` is call to handle to boundary violation. **Note that the `Aoplib4jClassBoundary` have a default callback class, `org.aoplib4j.modularity.LogViolationCallback`**

> Here is the complete code of the [PackageBoundaryAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/modularity/internal/PackageBoundaryAspect.java).

### Critics, enhancements and ... what i don't like about this solution ###

  * The user validation class must inherit from `BoundaryViolationCallback` class. It would be much better to just implement a Java interface. In the actual implementation the user must inherit from a class because the framework instantiate the validation class using the non-arguments constructor by introspection.
  * For every advised method the `Aoplib4jClassBoundary` or `Aoplib4jPackageBoundary` annotations are retrieved. A better idea would be  to have some kind of cache on which the annotations should be stored (but for instance it is just KISS).
  * Since static pointcuts cannot retrieve the caller object using `this` pointcut the caller information is computed using the stack trace of the current execution thread by creating a new `Throwable` object and calling `Throwable#getStackTrace()`. I know that this solution is not good at all but I didn't find another alternative.

> Here is the code of the method that computes the caller information based on the stack trace :
```
     /**
      * Method that computes the information about the caller class. The
      * information is stored into a {@link StackTraceElement} object.
      * 
      * The caller information is on the third position on the stack trace;
      * the first position contains the call to this method, and the 
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
```
**UPDATE**(24.05.2010) - The code of the method had been slightly modified (enhanced ?) due to the [bug#72](http://code.google.com/p/aoplib4j/issues/detail?id=75) or see the
[the diff](http://code.google.com/p/aoplib4j/source/diff?spec=svn151&r=151&format=side&path=/trunk/aoplib4j/src/main/java/org/aoplib4j/modularity/internal/AbstractBoundary.java).