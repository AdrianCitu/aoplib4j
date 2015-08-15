### Introduction ###
This page is a small tutorial about how to implement the [Observer pattern](http://en.wikipedia.org/wiki/Observer_pattern) using annotations.


### How to implement the Observer pattern with annotations (in 10 seconds) ###

  * Annotate the Subject classes with the `Aoplib4jSubject` annotation
  * Annotate the Observer classes with the `Aoplib4jObserver` annotation
  * Annotate (some of) the Subject methods with the `Aoplib4jRegisterObserver` annotation to be used for attaching Observers to the Subject.
  * Annotate (some of) the Subject methods with the `NotifyObservers` annotation to notify the attached Observers.
  * Annotate (some of) the Subject methods with the `Aoplib4jUnregisterObservers` annotation for removing the attached Observers.
  * For every Observer implement the behavior when an Observer is notified by a Subject by creating a class that inherits from the `ObserverCallback`.


### How to implement the Observer pattern with annotations (in 10 minutes) ###

For the implementation of the Observer pattern must answer to the following questions:
  1. Which objects are Subjects and which are Observers ?
  1. When should the Subject send a notification to its Observers ?
  1. What the Observer should do when notified ?
  1. When should the relationship begin and end ?

#### Which objects are Subjects and which are Observers ? ####
> The classes that will have the role of Subject must be annotated with the `Aoplib4jSubject` annotation and the classes that will have the role of Observer must be annotated with the `Observer` annotation.
Code example (from the JUnit tests):
```
@Aoplib4jSubject
public class Dealer {
....

@Aoplib4jObserver(callbackClass = ClientObserverCallBack.class)
public class Client {
......
```

#### When should the Subject send a notification to its Observers ? ####
The Subject will notify all the attached Observers when any method annotated with the `NotifyObservers` is called.

#### What the Observer should do when notified ? ####
The behavior of the Observer when is notified should be added into a class that inherits from the `ObserverCallback` class.
Here is the structure of the `ObserverCallback` class:

```
public abstract class ObserverCallback {

    /**
     * Default constructor.
     */
    public ObserverCallback() {

    }

    /**
     * Method called when a method annotated with the {@link NotifyObservers} 
     * annotation is executed on a class annotated with the {@link Subject}
     * annotation.
     * @see NotifyInformation
     * 
     * @param notifyInformation the object containing the notification 
     * information.
     */
    public abstract void notifyObserver(NotifyInformation notifyInformation);
```

The Observer can retrieve all the notification informations (the Subject method on which the notification was triggered, the method arguments, the Subject instance, the Observer instance) from the `NotifyInformation` interface.

```
public interface NotifyInformation {

    /**
     * @return the {@link Method} on which the notification was triggered.
     */
    Method getMethod();
    
    /**
     * @return the arguments of the method on which the notification was 
     * triggered.
     */
    Object[] getMethodArguments();
    
    /**
     * @return the instance on which the notification of the observers was 
     * triggered.
     */
    Object getSubject();
    
    /**
     * @return the instance of the observer that must be notified.
     */
    Object getObserver();
}
```

#### When should the relationship begin and end ? ####
For attaching the Observers to the Subject must annotate a method (or more) with the `Aoplib4jRegisterObserver` annotation. The annotated method must have a single parameter and this single parameter should be a class annotated with the `Observer` annotation.
The methods annotated with the `Aoplib4jRegisterObserver` having more that one parameter or having as parameter a class that is not annotated with `Observer` will be ignored by the framework.

Code example (from the JUnit tests):
```

@Aoplib4jSubject
public class Dealer {
...

    @Aoplib4jRegisterObserver
    public final void addClient(final Client o) {
..
```

To remove all the Observers attached to a Subject call any method on Subject annotated with the `Aoplib4jUnregisterObservers` annotation.


### The implementation:under the hood ###
Under the hood the AspectJ framework is used.

#### Which objects are Subjects and which are Observers ? ####
In the `org.aoplib4j.gof.observer.internal` package the framework contains an implementation of the Observer pattern. The Subject class is represented by the `GofSubject` interface and his implementation `GofSubjectImpl`; the Observer class is represented by the `GofObserver` interface and his implementation `GofObserver`.

Here are de definition of the `GofObserver` interface and `GofSubject` interface:
```
public interface GofSubject {

    /**
     * Add a new observer.
     *
     * @param o the new observer to be attached to the subject.
     */
    void addObserver(GofObserver o);

    /**
     * 
     * @return the number of attached observers.
     */
    int countObservers();

    /**
     * Removes all the observers attached to this subject.
     */
    void deleteObservers();

    /**
     * @return array of observers attached to the subject.
     */
    GofObserver[] getAllObservers();
}

public interface GofObserver {
}

```
So, under the hood (using the AspectJ mixins feature), all the classes annotated with `Aoplib4jSubject` annotation will automatically implement the `GofSubject` interface and will have injected as implementation the code from the `GofSubjectImpl`.

All the classes annotated with the `Observer` annotation will automatically implement the `GofObserver` interface and will have injected as implementation the code from the `GofObserverImpl`.

Let's see how to declare the mixins using @AspectJ:

```
@Aspect
final class ObserverAspect {
    
    /**
     * the classes annotated with the 
     * {@link org.aoplib4j.gof.observer.Aoplib4jSubject} annotation 
     * will automatically 
     * implement the {@link GofSubject} interface and the implementation
     * will be {@link GofSubjectImpl}.
     */
    @DeclareParents(
            value = "@org.aoplib4j.gof.observer.Aoplib4jSubject *",
            defaultImpl = GofSubjectImpl.class)
    private GofSubject subject = null;

    /**
     * the classes annotated with the {@link Observer} annotation will 
     * automatically 
     * implement the {@link GofObserver} interface.
     */
    @DeclareParents(
            value =
                "@org.aoplib4j.gof.observer.Aoplib4jObserver *")
    private GofObserver observer = null;
```

#### When should the Subject send a notification to its Observers ? ####
For the notification part, the Subject class must annotate methods with the `Aoplib4jRegisterObserver` annotation. Here is the @AspectJ pointcut that intercept the annotated methods.:
```
    @Pointcut("execution("
            + "@org.aoplib4j.gof.observer"
            + ".Aoplib4jRegisterObserver * * (..)) "
            + "&& this(sbj) && args(obs)")
    void addObserverPointcut(final GofSubject sbj, final GofObserver obs) {

    }
```

For adding a new `GofObserver` to the `GofObserver` the @AfterReturning advice is used (the advice will be executed only if the adviced method execution returns successfully):

```
    @AfterReturning("addObserverPointcut(sbj, obs)")
    public void addObserverAdvice(final GofSubject sbj, final GofObserver obs) {
        
        if (sbj != null && obs != null) {
            sbj.addObserver(obs);
        }
    }
```
#### What the Observer should do when notified ? ####
The Observers are notified by the call of a Subject method annotated with the `NotifyObservers` annotation. Here is the @Aspectj pointcut that intercepts the call of a `NotifyObservers` annotated method:
```
    @Pointcut("execution(@org.aoplib4j.gof.observer."
            + "NotifyObservers * * (..))"
            + " && this(sbj)")
    void notifyObserversPointcut(final GofSubject sbj) {

    }
```

The used advice is @AfterReturning. So, for every attached Observer retrieve the `ObserverCallback` class (from the `callbackClass` attribute of the `Aoplib4jObserver` annotation), and call the `ObserverCallback#notifyObserver(NotifyInformation)` method.

I think it's simpler just to show the code:
```

  @AfterReturning("notifyObserversPointcut(sbj)")
    public void notifyObserverAdvice(final GofSubject sbj,
            final JoinPoint thisJoinPoint)
        throws InstantiationException, IllegalAccessException {

        GofObserver[] obs = sbj.getAllObservers();

        for (GofObserver ob : obs) {
            Observer annotation =
                ob.getClass().getAnnotation(Observer.class);

            Class < ? extends ObserverCallback > callbackClass =
                annotation.callbackClass();

            ObserverCallback instance = callbackClass.newInstance();
            instance.notifyObserver(
                    this.cresteInstanceofNotifyInformation(thisJoinPoint, ob));
        }
    }

    /**
     * @param jp AspectJ join point.
     * @param obs instance of the observer.
     * @return a new instance of {@link NotifyInformation}.
     */
    private NotifyInformation cresteInstanceofNotifyInformation(
        final JoinPoint jp, 
        final GofObserver obs) {
            
            Method method =  ((MethodSignature) jp.getSignature()).getMethod();
            Object[] methodArguments = jp.getArgs();
            GofSubject sbj =  (GofSubject) jp.getTarget();
            
            return new NotifyInformationImpl(sbj, method, methodArguments, obs);
        }
```

#### When should the relationship begin and end ? ####
For attaching the Observers to Subject a method annotated with `@Aoplib4jRegisterObserver` should be called. Here is the pointcut and the advice:
```
   @Pointcut("execution("
            + "@org.aoplib4j.gof.observer"
            + ".Aoplib4jRegisterObserver * * (..)) "
            + "&& this(sbj) && args(obs)")
    void addObserverPointcut(final GofSubject sbj, final GofObserver obs) {

    }

  @AfterReturning("addObserverPointcut(sbj, obs)")
    public void addObserverAdvice(final GofSubject sbj, final GofObserver obs) {
        
        if (sbj != null && obs != null) {
            sbj.addObserver(obs);
        }
    }
```

For detach the Observers from the Subject a method annotated with `@Aoplib4jUnregisterObservers` should be called. Here is the pointcut and the advice:

```
   @Pointcut("execution(@org.aoplib4j.gof.observer."
            + "Aoplib4jUnregisterObservers * * (..))"
            + " && this(sbj)")
    void unregisterObserversPointcut(final GofSubject sbj) {
    }
    
    @AfterReturning("unregisterObserversPointcut(sbj)")
    public void unregisterObserversAdvice(final GofSubject sbj) {
        sbj.deleteObservers();
    }
```

And here is the complete code of the [ObserverAspect](http://code.google.com/p/aoplib4j/source/browse/trunk/aoplib4j/src/main/java/org/aoplib4j/gof/observer/internal/ObserverAspect.java).

### Enhancements ###
  1. It will be a good idea to add a callback also when an Observer is detached from his Subject(if the Observer wants to make some actions just before of being detached).
  1. For implementing the notification behavior the user must inherits from a class (`ObserverCallback`). It would be much better to just implement a Java interface. In the actual implementation the user must inherit from a class because the framework instantiate the callback class using the non-arguments constructor by introspection (see [Class.newInstance()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#newInstance())).