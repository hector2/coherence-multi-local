## Building a Real Java EE + Oracle Coherence Application with Maven

The repository is based on the 
[_Building a Real Application with Maven_][real-application-chapter] chapter of the 
[_Oracle� Fusion Middleware Developing Applications Using Continuous Integration_][ci-manual] manual.

The example describes how to build a web application that uses data stored in a Coherence cache. At the same time this repository 
includes a few differences:

1. `GAR` and `WAR` dependencies are configured within the top-level POM, as well as all employed plugins and dependencies. 

2. Oracle Coherence module, `ci-example-app-gar`, has not only the `gar` packaging type but are always included into POMs as 
a dependency with type `gar` (`<type>gar</type>`). Unfortunately, the _Oracle Coherence Plugin_ doesn't contain any 
_ArtifactHandler_ component definitions, so no `gar` files are included into classpath during compilation. Due to this issue, 
by default, the compilation of `WAR` module `ci-example-app-war` fails since classes defined in the `GAR` module can't be found. 
The `gar-to-classpath-plugin` maven plugin provides the following component definition in order to fix the issue.

```xml
    <component>
      <role>org.apache.maven.artifact.handler.ArtifactHandler</role>
      <role-hint>gar</role-hint>
      <implementation>org.apache.maven.artifact.handler.DefaultArtifactHandler</implementation>
      <configuration>
        <extension>gar</extension>
        <type>gar</type>
        <language>java</language>
        <addedToClasspath>true</addedToClasspath>
      </configuration>
    </component>
```

3. Due to a bug in the `gar-maven-plugin`, the processed `pof-config.xml` file won't be included into the result `EAR` archive. 
The postprocessor has to be disabled (`<generatePof>false</generatePof>`), and every needed POJO/POF class definition should 
be manually registered into the `pof-config.xml` file:

```xml
    <user-type>
      <type-id>1000</type-id>
      <class-name>ci.example.app.Person</class-name>
    </user-type>
```

### Prerequisites

- Minimum of Java 1.8
- Maven 3.5.0 or greater
- Oracle WebLogic Server and Coherence 12.2.1.2.0

### Getting started at the Command Line

The `oracle-fmw-ci-demo:gar-to-classpath-plugin` must be installed into the local Maven repository, otherwise numerous compilation 
errors will appear during building the `WAR` module.

To install the plugin do:

```
# cd gar-to-classpath-plugin
# mvn clean install
# cd ..
```

In order to use any provided by Oracle Maven plugins and dependencies, _Oracle Maven Repository_ has to be configured
as it described in the [official documentation][oracle-maven-repo-manual].

### Building the application

To build the application do:

```
# mvn clean package
```

### Deploying the application

Oracle WebLogic Server and Oracle Coherence 12.2.1.2.0 have to be installed on your environment. A domain with enabled 
_WebLogic Coherence Cluster Extension_ has to be created there.

To deploy the application to a running application server do:

```
# mvn clean verify -Duser=DEPLOYER_USER -Dpassword=PASSWORD
```

The Maven console should display lines like the following:

```
Task 3 initiated: [Deployer:149026]deploy application ci-example-app on AdminServer.
Task 3 completed: [Deployer:149026]deploy application ci-example-app on AdminServer.
Target state: deploy completed on Server AdminServer

Target Assignments:
+ ci-example-app  AdminServer
```

### Access the application

The application will be available at the following resource:

http://localhost:7001/ci-example-app/myservlet

[ci-manual]: https://docs.oracle.com/middleware/1221/core/MAVEN/toc.htm
[real-application-chapter]: https://docs.oracle.com/middleware/1221/core/MAVEN/real_app.htm#MAVEN8917
[oracle-maven-repo-manual]: http://docs.oracle.com/middleware/1221/core/MAVEN/config_maven_repo.htm#CACJADFE
