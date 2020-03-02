# Assignment_web_data
Brief introduction to the app: [Introduction](Introduction.md)

## How to run the app:
### Requirements:
* [Netbeans](https://netbeans.apache.org/download/nb100/nb100.html): The bin zip contains both Windows and Linux executables.
* [JavaSE-11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html): Or other versions
* [Tomcat](https://tomcat.apache.org/download-90.cgi)

### Steps:
* Clone the repository
* Go to [src/main/webapp/WEB-INF/web.xml](src/main/webapp/WEB-INF/web.xml) and modify pagesPath parameter to the absolute path for the [data folder](/src/main/java/com/assignment/assignment_web_data/data/) of this project <br> (e.g. \<param-value> **C:/Programs/Assignment_web_data/src/main/java/com/assignment/assignment_web_data/data/** \</param-value>).
* Open Netbeans
* Go to tools>plugins and install Java EE Base plugin
* Open services (Window>Services) and on Servers add a new Tomcat server
* Now you can Run the project
