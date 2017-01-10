# StabilityMeasureWithReflection

## Project Description
Java application that uses reflection to analyse an arbitrary Java Application Archive (JAR) and calculates the positional stability of each of the component classes in its object graph using the Positional Stability formula: 

...I = Ce/Ca+Ce 
---> Stability = (no. of efferent values/no. of afferent values + no. of efferent values)

## Project Features
* User can import Jar files
* Program ensures only Jar files are added by assessing file and enabling the import button only for valid files.
* Output shows each class in the Jar along with each the classes that have afferent and efferent coupling to the class
* Output also shows the calculated stability for each class

## Running the project
The src folder contains the required java files required to run this program.  These can be downloaded and compiled in an IDE of choice or from the cmd.  The java classes in this project are:

* AppSummary.java
* AppWindow.java
* Dependable.java
* DependencyChecker.java
* JarReader.java
* ListBuilder.java
* Runner.java
* Stability.java
* TypeSummaryTableModel.java

When running the program:

1. A GUI will appear on screen with option buttons to Browse for a Jar or to close the program.  All other buttons are disabled at the start.
2. Click the Browse button and you will be able to check your machine for a Jar file.  Please be aware that files without a .jar extension will be imported into this program.
3. Once a valid Jar file has been selected, the Import button will become enabled.  Click this to import the file to carry out the Positional Stability analysis on the selected Jar file.
4. Click the Show Stability Details to view the analysis results.
5. To carry out analysis on another Jar file, simply click the Browse button and select another file.


