# 9. Task Management System in a Team

TaskSync is a task management system in a team created to streamline the process of designing, creating, and finalizing projects. As the application's tagline "Sync Your Task, Sync Your Success" suggests, proper task management and insight into employee statistics will enable drawing appropriate conclusions that will accelerate teamwork and shed light on previously unseen problems resulting from inadequate task management in the team.

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)

## Installation

1. **Install IntelliJ IDEA**
   - An IDE designed for creating projects in Java and its libraries.
   - [Installation Instructions](https://www.jetbrains.com/help/idea/getting-started.html)

2. **Download and Install JDK**
   - Go to [Oracle](https://www.oracle.com/java/technologies/downloads/) and download the appropriate JDK version for your operating system.
   - Install JDK following the installer instructions.

3. **Download JavaFX Package (ver. 22.0.1)**
   - [Download JavaFX](https://gluonhq.com/products/javafx/)

4. **Download MySQL Connector/J**
   - Go to [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) and download the JAR file.
   - Add the JAR file to the `libs` directory in your project (if you don't have a `libs` directory, create one).
   - Add the JAR file to the project configuration in IntelliJ IDEA:
     - Go to `File -> Project Structure` (or press `Ctrl + Alt + Shift + S`).
     - In the `Platform Settings` section, select `Libraries`.
     - Click the plus icon (+) and select `Java`.
     - Point to the path of the JAR file you copied to the `libs` directory.
     - Click `OK` and make sure the library is added to the modules that require it.

5. **Download and Install XAMPP**
   - Go to [Apache Friends](https://www.apachefriends.org/index.html).
   - Choose the version for your operating system and follow the installer instructions.

6. **XAMPP Configuration**
   - Run XAMPP Control Panel and start the Apache and MySQL services.
   - Check the server's functionality by opening a browser and entering `http://localhost`.
   - To access phpMyAdmin, enter `http://localhost/phpmyadmin` in the browser's address bar.

7. **Import the Database**
   - Import the database from the tasksync.sql file located in the repository.

8. **Configure the Database Connection**
   - In the `DBUtils.java` file, enter the database connection details:
     ```java
     DB_URL = "jdbc:mysql://localhost:3306/tasksync";
     DB_USER = "database_username";
     DB_PASSWORD = "database_user_password";
     ```

After successfully following the instructions, the application should start correctly.

## Usage
After launching the application, the login view will appear, from which you can directly go to the account creation subpage. <br />
<img src="https://github.com/dazby0/TaskSync/assets/91540933/1bf50030-c37e-496d-bb81-9dfb84f29264" alt="Log In View" width="450" height="350">
<img src="https://github.com/dazby0/TaskSync/assets/91540933/79d300ef-674a-40a6-b353-2f934f713dc6" alt="Sign Up View" width="450" height="350">



### Manager's View
- **Main Page:** List of all tasks created by the manager, containing title, status, assigned person, assignment date, time spent on the task, and a button to view task details.
<img src="https://github.com/dazby0/TaskSync/assets/91540933/8faf2827-99d2-4f63-a330-ce816c735709" alt="Main View" width="450" height="350">
<img src="https://github.com/dazby0/TaskSync/assets/91540933/ea84a52a-a384-4a13-bbbf-30a721ebc29f" alt="Task Info" width="450" height="350">

- **Navigation:** Buttons to subpages:
  - **Add Task:** Add a new task and assign it to a user. <br />
    <img src="https://github.com/dazby0/TaskSync/assets/91540933/dbff0b84-e849-4020-8ce8-833fb6fa26a3" alt="Add Task View" width="450" height="350"> <br />
  - **Teams:** View all manager's teams and create new teams. <br />
    <img src="https://github.com/dazby0/TaskSync/assets/91540933/c61041ba-b56e-4e6b-8014-17663a9193ae" alt="Teams View" width="400" height="311">
    <img src="https://github.com/dazby0/TaskSync/assets/91540933/b1ec6f29-d6b8-4cde-9a7e-d17bd0e1a8bd" alt="Teams View" width="400" height="311"> <br />
  - **Reports:** Reports showing the average task completion time by employees and teams.
    <img src="https://github.com/dazby0/TaskSync/assets/91540933/3bef62f1-3e53-47db-9f6b-09db013dab39" alt="Teams View" width="450" height="350">

### Employee's View
- **Main Page:** List of assigned tasks with the ability to view task details, start and stop tasks with a timer showing the current time spent on the task. <br />
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/00aadd5c-c179-49cb-a7cb-3fc785d00a77" alt="Teams View" width="450" height="350"> <br />
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/1cf904b9-22e0-4ecf-b84e-9df94d545144" alt="Teams View" width="280" height="218">
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/7bcf8853-dbdf-4e29-8e9a-407ea5114ffe" alt="Teams View" width="280" height="218">
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/15c50da7-dff6-47b1-8e2f-1a146eabd693" alt="Teams View" width="280" height="218"> <br />

- **Teams:** Checking membership in teams and viewing colleagues.
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/b5d3d3db-a4ac-4036-86ea-638bef6213f9" alt="Teams View" width="450" height="350"> <br />
  
- **Reports:** Average task completion time by the logged-in employee and the teams they belong to.
  <img src="https://github.com/dazby0/TaskSync/assets/91540933/af7f3ad2-bf56-44b3-bf93-7e89f33ff96b" alt="Teams View" width="450" height="350">

