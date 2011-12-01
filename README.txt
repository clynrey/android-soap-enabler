Q: How to build Android SOAP Enabler ?

A: Using Apache Maven 2, you can build the entire project with the following command :
$ mvn clean install
See http://maven.apache.org/ for more  information about Apache Maven 2
Android SOAP Enabler was build with Apache Maven 3.0.2

Q: How to tests Android SOAP Enabler capabilities ?

A: Launch the tests module, it's the server side test projet, using the following command :
$ mvn tomcat:run
Once the tomcat servlet container is running, you can launch the integration test cases
from each module using the following command :
$ mvn integration-test
See http://mojo.codehaus.org/maven-tomcat-plugin/ for more informations about the Tomcat Maven Plugin.
