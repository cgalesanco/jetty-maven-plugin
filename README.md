jetty-maven-plugin
==================

This is a fork Eclipse's jetty-maven-plugin (org.eclipse.jetty:jetty-maven-plugin:9.0.3.v20130506 exactly) to honor &lt;webResources> configuration for the maven-war-plugin.

The Problem
-----------

When developing web apps in Maven, it's a common practice to store the Javascritp sources under src/main/js. This requires configuring the maven WAR plugin accordingly:

```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <configuration>
      <webResources>
        <resource>
          <directory>src/main/js</directory>
          <targetPath>js</targetPath>
        </resource>
      </webResources>
    </configuration>
  </plugin>
```

In this scenario you have two options to use Jetty plugin for rapid develop-test cycle:

* Configure the resource plugin to copy the Javascript files in the `generate-resources` fase to the proper web-app folder.
* Use jetty:run-exploded instead of jetty:run

Both options prevent Jetty to reflect Javascript code changes without additional user actions, and the latest can take a considerable amount of time for large-size projects.

My Solution
-----------
Modify the run goal in the jetty-maven-plugin to read and honor the &lt;webResources> section. Jetty resources are now
the source files themselves, so that any change in the Javascript files will be inmediatly refelectedby Jetty.

The developer only needs to use the "forked" version of the Jetty plugin

```xml
  <plugin>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>9.0.3.JSE</version>
  </plugin>
```

Configuration
-------------
Filtered web resources (those configured with &lt;filtering>true&lt;/filtering>) are not included by default. You can
control this behavior using the `includeFilteredWarResources` configuration property

```xml
  <plugin>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>9.0.3.JSE</version>
    <includeFilteredWarResources>true</includeFilteredWarResources>
  </plugin>
```

Did you like it?
----------------
I've posted a [patch](https://bugs.eclipse.org/bugs/show_bug.cgi?id=407621) with this solution to the Jetty project at Eclipse. Comments supporting the enhancement are welcome.
Meanwhile I've uploaded the modified plugin to my personal Maven repository. You can grab it from there tweaking your POM like this:

```xml
  <repositories>
    <repository>
      <id>desarrollo-agil</id>
      <url>http://demo.desarrolloagil.es/nexus/content/repositories/releases/</url>
    </repository>
  </repositories>

  <build>
    ...
    <plugins>
      ...
      <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.0.3.JSE</version>
      </plugin>
    </plugins>
  </build>
```
