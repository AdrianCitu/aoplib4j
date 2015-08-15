### Introduction ###
Policy enforcement is a mechanism for ensuring that system components follow certain
programming practices, comply with specified rules. The `aoplib4j` contains a few aspects dedicated to the policy enforcement.



### The aop4lib policy enforcement rules ###
  * Don't use `System.out.print*(...)`.
  * Use only private fields in your classes.
  * Return interfaces of an object from a method, not a implementation class.
  * Return empty arrays or collections from a method, not nulls.


### Don't use `System.out.print*(...)` ###
I think it's not a good idea to use `System.out.print*(...)` because it cannot be visible into the log files and (eventually) will be visible in production to the console.

This rule is applied at compile time so it must compile the code with the `ajc` compiler. If a `java.io.PrintStream.print*(...)` is found, the compiler will raise a warning.
Example (taken from the JUnit tests)
```
[iajc] warning at System.out.println("I buy !");
[iajc] ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[iajc] /src/test/java/com/google/code/aoplib4j/aspectj/gof/observer/client/Client.java:38:0::0 
"Don\'t use System.out.print*(..). Use a logger API."
[iajc] 	see also: com/google/code/aoplib4j/aspectj/policy/compiletime/NoSysOutAspect.java::0
 
```

#### How it works under the hood ####
AspectJ provides a static crosscutting mechanism to declare weave-time errors and warnings
based on certain usage patterns. In our case the `@DeclareWarning` construction is used which declares a weave-time warning:
```
    @DeclareWarning(
            "call(* java.io.PrintStream.print*(..))")
    private static final String NO_SYS_OUT =  
        "Don't use System.out.print*(..). Use a logger API.";
```
Here is the complete code of the [NoSysOutAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/policy/compiletime/NoSysOutAspect.java).

### Use only private fields in your classes ###
It's always a good practice to not expose directly the non final fields of your classes if the class accessible outside his package.This fields sould be exposed by providing accessor methods. Joshua Bloch in his "Effective Java (2-end edition)" book explains very well why [it should not use public fields.](http://books.google.be/books?id=ka2VUBqHiWkC&printsec=frontcover&dq=effective+java+item+43&hl=fr#PPA71,M1) This rule, as the first one, is applied at compile time so the output of the compiler would be:
```
[iajc] warning at public String publicString = null;
[iajc] ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[iajc] /src/test/java/com/google/code/aoplib4j/aspectj/policy/compiletime/CompileTimePolicy.java:43:0::0 
"Don\'t use non final public, protected or default fields. Make all non final fields private and use set/get methods."
[iajc] 	see also: com/google/code/aoplib4j/aspectj/policy/compiletime/OnlyPrivateFieldsAspect.java::0

```

#### How it works under the hood ####
Under the hood the same mechanism of weave-time warning is used. Here is the AspectJ pointcut:
```
    @DeclareWarning(
           "get(!private !final * *) || set(!private !final * *)")
    private static final String ONLY_PRIVATE_FIELDS = 
        "Don't use non final public, protected or default fields." 
        + " Make all non final fields private and use set/get methods.";

```
Here is the complete code of the [OnlyPrivateFieldsAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/policy/compiletime/OnlyPrivateFieldsAspect.java).

### Return interfaces of an object from a method, not a implementation class. ###
This is also an "advice" taken from the "Effective Java"(Item 52).If appropriate interface type exists, then parameters, return values variables and fields should declared using the interface type. Basically code like this should be avoid :
```
  //don't return a implementation and don't use as parameter an implementation
  public HashMap<String,String>  someMethod(Vector<String> param) {
  .....
  }
```
and replaced by:
```
  public Map<String,String>  someMethod(List<String> param) {
  ...
  }
```

The `aoplib4j` offer a limited implementation for this rule. Firstly, it verifies only the return values of the methods (the same rule should apply of class fields and method parameters) and it verifies only the sub-classes of the `java.util.Collection` and `java.util.Map`; the [issue#35](http://code.google.com/p/aoplib4j/issues/detail?id=35) has been created for a more general implementation. This rule is also a compile time warning and the `ajc` message warning will be:
```
[iajc] warning at public Vector<String> returnVectorNotInterface() {
[iajc] ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[iajc] /src/test/java/com/google/code/aoplib4j/aspectj/policy/compiletime/CompileTimePolicy.java:67:0::0 
"Return the Collection interface or another interface not the real implementation"
[iajc] 	see also: com/google/code/aoplib4j/aspectj/policy/compiletime/UseInterfacesAspect.java::0
```

#### How it works under the hood ####
Here are the two pointcuts used
Pointcut capturing return values of subclasses of `java.util.Collection`:
```
   @DeclareWarning(
            "execution(" 
            + "(java.util.TreeSet+"
            + " || java.util.Vector+"
            + " || java.util.concurrent.SynchronousQueue+"
            + " || java.util.Stack+"
            + " || javax.management.relation.RoleUnresolvedList+"
            + " || javax.management.relation.RoleList+"
            + " || java.util.PriorityQueue+"
            + " || java.util.concurrent.PriorityBlockingQueue+"
            + " || java.util.LinkedList+"
            + " || java.util.LinkedHashSet+"
            + " || java.util.concurrent.LinkedBlockingQueue+"
            + " || java.util.HashSet+"
            + " || java.util.concurrent.DelayQueue+"
            + " || java.util.concurrent.CopyOnWriteArraySet+"
            + " || java.util.concurrent.CopyOnWriteArrayList+"
            + " || java.util.concurrent.ConcurrentLinkedQueue+"
            + " || java.beans.beancontext.BeanContextSupport+"
            + " || java.beans.beancontext.BeanContextServicesSupport+"
            + " || java.util.ArrayList+"
            + " || java.util.concurrent.ArrayBlockingQueue+"
            + ")"
            + " *..*.*(..))")
    private static final String NO_IMPL_COLLECTION = 
        "Return the Collection interface or another interface not the real " 
        + "implementation";
```

Pointcut capturing return values of subclasses of `java.util.Map`:
```
   @DeclareWarning(
            "execution("
            + "(java.util.HashMap+"
            + " || java.util.Hashtable+" 
            + " || java.util.concurrent.ConcurrentHashMap+"
            + " || java.util.WeakHashMap+"
            + " || java.util.TreeMap+"
            + " || java.awt.RenderingHints+"
            + " || java.util.Properties+"
            + " || java.util.LinkedHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.EnumMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + " || java.util.IdentityHashMap+"
            + ")"
            + " *..*.*(..))")
    private static final String NO_IMPL_MAPS = 
        "Return the Map interface or another interface not the real " 
        + "implementation";
```

Here is the complete code of the [UseInterfacesAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/policy/compiletime/UseInterfacesAspect.java).

### Return empty arrays or collections from a method, not nulls. ###
This is also taken from "Effective Java 2-end edition" (Item 43).
This rule is applied at runtime so, the code should be executed in order to verify the return values of the methods. The default behavior if a violation of the rule is found will be to log an error with the [SEVERE level](http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/Level.html#SEVERE) (as you see the `java.util.logging` is used). Another possible behavior is to throw an [java.lang.UnsupportedOperationException](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/UnsupportedOperationException.html) by executing the code with the system variable `THROW_EXCEPTION_FOR_VIOLATIONS` (the value of the constant `org.aoplib4j.policy.runtime.AbstractRuntimePolicyAspect#THROW_EXCEPTION_FOR_VIOLATIONS`) set to `true`.

#### How it works under the hood ####
The AspectJ pointcut that will intercept all the methods having as return values a collection or map or an array is:
```
    @Pointcut("execution("
    		+ "(java.util.Map+"
            + " || java.util.Collection+"
            + " || java.lang.Object[]+)"
            + " *..*.*(..)) ")
    public void notNullCollectionOrArrayPointcut() {
    }
```

For retrieving the returned value of the pointcut the `@AfterReturning` advice is used.
```
  @AfterReturning(value = "notNullCollectionOrArrayPointcut()", 
                    returning = "ret")
    public void notNullCollectionOrArrayAdvice(final Object ret,
            final JoinPoint.StaticPart staticJp) {
        
        if (ret == null) {
            String errorMessage = "Method " + staticJp.getSignature() 
            + " should not return null value, it should return an " 
            + "empty Collection/Map/Array.(see Item 43 from \"Effective Java\""
            + " second edition book)";
            
            throwExceptionOrLogIt(errorMessage, logger, Level.SEVERE);
        }
    }

```

As you can see this rule have a major drawback; the faulty code must be executed in order to be found.
Here is the complete code of the [ReturnEmptyCollectionsAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/policy/runtime/ReturnEmptyCollectionsAspect.java).