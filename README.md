Java Lab Projects

Author: Garv Verma

About This Repository

This repository contains my Java lab activities. The work covers Swing and the MVC pattern, TCP socket programming, Java RMI, JDBC database connectivity, Servlets, and a Spring Boot web application.

Each activity lives in its own folder with its own source code. They are separate programs, not one application, so each one is built and run on its own. Setup steps are given below.

Activities
#	Folder	What it does	Main concepts
1	MVC_calc	Swing calculator (add, subtract, multiply, divide) split into Cmodel, Cviews and Ccontroller	MVC pattern, Swing, event handling
2	RMI	Remote calculator: a server publishes Calculator and a client calls add and subtract on it	Remote, UnicastRemoteObject, Naming
3	ServerClient	Client and server exchange one message over TCP on port 5000	Socket, ServerSocket, I/O streams
4	ShopSphere	ShopNest online store: product catalogue, cart, cash-on-delivery checkout and an admin panel	Spring Boot 3, Thymeleaf, JPA, H2, Maven
5	java_jdbc	SectionB.java: Swing student registration form that saves records to MySQL	JDBC, PreparedStatement, Swing
6	servlet	Maven web app with hello, login and home servlets and a session-checking filter	Jakarta Servlet 6.1, sessions, filters
7	student-registration	StudentRegistration.java: student registration form that saves records to MySQL	JDBC, Swing, form handling
8	To be added		
Repository Structure
Java-Lab/
├── MVC_calc/
├── RMI/
├── ServerClient/
├── ShopSphere/
├── java_jdbc/
│   ├── SectionB.java
│   └── jdbc/
├── servlet/
│   ├── pom.xml
│   └── src/
└── student-registration/
    ├── StudentRegistration.java
    └── Jdbcproject/
Requirements
JDK 17 or later
Git
Apache Maven (for servlet and ShopSphere)
MySQL server and the MySQL Connector/J jar (for java_jdbc and student-registration)
Apache Tomcat 10.1 or later (for servlet; it uses the jakarta.* packages)
Getting Started
bash
git clone https://github.com/garvverma-work/Java-Lab.git
cd Java-Lab

Then open the folder of the activity you want and follow its steps below.

MVC_calc
bash
cd MVC_calc
javac *.java
java Main
RMI

Open three terminals in the RMI folder.

bash
javac *.java
rmiregistry          # terminal 1: keep running
java Server          # terminal 2
java Client          # terminal 3
ServerClient
bash
cd ServerClient
javac Server.java Client.java
java Server          # terminal 1
java Client          # terminal 2
java_jdbc and student-registration
Start MySQL and create the database the program connects to (Git for SectionB.java, collegeJava for StudentRegistration.java).
Create a student table with the columns id, name, gender and branch.
Set your own MySQL username and password in the source file.
Download MySQL Connector/J (the .jar is not stored in this repository), then compile and run:
bash
javac SectionB.java
java -cp ".:path/to/mysql-connector-j.jar" SectionB

On Windows, use ; instead of : in the classpath.

servlet
bash
cd servlet
mvn clean package

Copy target/servlet-1.0-SNAPSHOT.war into Tomcat's webapps folder, start Tomcat, and open http://localhost:8080/servlet-1.0-SNAPSHOT/. The login is hard-coded in LoginServlet.java for demonstration only.

ShopSphere

Follow ShopSphere/README.md. In short:

bash
cd ShopSphere
mvn spring-boot:run

Then open http://localhost:8080.

Notes
There is no single command that runs every activity.
Database passwords in the code are for local practice only. Do not reuse real passwords.
Compiled files (.class, target/) and jar files are not committed.
The README describes the repository. It does not replace a demonstration of the working code.

Maintained by Garv Verma
