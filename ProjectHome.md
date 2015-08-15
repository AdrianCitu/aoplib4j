The goal of aoplib4j is to offer a library of aspects. For instance the project supports only AspectJ aspects but maybe in the future will support another Java AOP framework (JBoss AOP).
The aoplib4j contains the following aspects:
  * Aspect for implementing the Observer pattern using annotations (see [ObserverPatternHowTo](http://code.google.com/p/aoplib4j/wiki/ObserverPatternHowTo)).
  * Aspect for validating classes fields using annotations (see [FieldValidationHowTo](http://code.google.com/p/aoplib4j/wiki/FieldValidationHowTo))
  * Aspect for re-executing methods (using annotations) if an exception is thrown by the method execution (see [FailureHandlingHowTo](http://code.google.com/p/aoplib4j/wiki/FailureHandlingHowTo)).
  * Aspects for applying some policy enforcement rules (see [PolicyEnforcementHowTo](http://code.google.com/p/aoplib4j/wiki/PolicyEnforcementHowTo))
  * Aspects for implementing modularity at the class and package level (see [ModularityHowTo](http://code.google.com/p/aoplib4j/wiki/ModularityHowTo))
  * Aspect for transform test assertions to verifies (see [TransformAssertToVerifyForTests](http://code.google.com/p/aoplib4j/wiki/TransformAssertToVerifyForTests))
  * Aspect for automatically generate a UML Sequence Diagram (see [UMLSequenceDiagramHowTo](http://code.google.com/p/aoplib4j/wiki/UMLSequenceDiagramHowTo))


Project news:

  * **30-05-2010 version 0.0.5 out.**Added `SequenceDiagramAspect` to automatically generate UML Sequence diagrams plus some [fixed issues](http://code.google.com/p/aoplib4j/issues/list?can=1&q=milestone%3DRelease0.5&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

  * 29-11-2009 version 0.0.4 out. Added aspects for JUnit(`JUnitAspect`) and TestNG(`TestNGAspect`) testing frameworks to transform assertions to verifies plus some [fixed issues](http://code.google.com/p/aoplib4j/issues/list?can=1&q=milestone%3DRelease0.4&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

  * 30-05-2009 version 0.0.3 out. Added aspects (`ClassBoundaryAspect`, `PackageBoundaryAspect`) for implementing modularity at the class and package level and some [fixed issues](http://code.google.com/p/aoplib4j/issues/list?can=1&q=milestone%3DRelease0.3&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

  * 25-04-2009 version 0.0.2 out. Added the policy enforcement aspects (`NoSysOutAspect`, `OnlyPrivateFieldsAspect`, etc...) and filed validation aspect (`FieldValidatorAspect`). Here is the complete list of [fixed issues](http://code.google.com/p/aoplib4j/issues/list?can=1&q=milestone%3DRelease0.2&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

  * 24-01-2009 version 0.0.1 out. Added the aspects for the Observer pattern (`ObserverAspect`) and field validation aspect. The complete list of all the [fixed issues](http://code.google.com/p/aoplib4j/issues/list?can=1&q=milestone%3DRelease0.1&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

In case you want to know more :
  * The [javadoc](http://aoplib4j.googlecode.com/svn/trunk/aoplib4j/to-add-to-web-site/javadoc/index.html) of the latest build
  * The [coverage reports](http://aoplib4j.googlecode.com/svn/trunk/aoplib4j/to-add-to-web-site/coverage-reports/index.html) of the latest build made by [Cobertura](http://cobertura.sourceforge.net/)
  * Some [metrics](http://code.google.com/p/aoplib4j/wiki/ProjectAnalysys) about the project computed by [ohloh.net](http://www.ohloh.net)