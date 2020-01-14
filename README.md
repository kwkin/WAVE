# WAVE
The following README contains build instructions,

## Group Information
Group: Earth, Wind, and Audio (Earth Force)

**Members:** 
1) Eric D. Goicochea, UF-ID: 6414-8523
2) Bridget L. Homer, UF-ID: 6963-3242
3) Kenneth W. King, UF-ID: 0901-3401


### Build Requirements
Java version 1.8 is required. The particular version of java tested is 1.8.0_191, but any Java 1.8 version should work.

All files required to build the application are packaged in the submission. No additional files are required to be downloaded, but three open source projects were included in the submission. 
- **lib/jmatio.jar** - Reads .mat files. This is used to read the .mat files provided in the CIPIC database. 

https://www.mathworks.com/matlabcentral/fileexchange/10759-jmatio-matlab-s-mat-file-i-o-in-java
- **Java Wav File IO** - The two java files contained in this directory are used for reading and writing .wav files. 

http://www.labbookpages.co.uk/audio/javaWavFiles.html
- **Apache Commons Math 3.6** -  

http://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/util/MathArrays.html

## How to Run
There are two ways different methods of building depending on what OS you are on/what tools you use. 

### Unix
Using a terminal, navigate to the "HW4" directory containing this README.md file. Enter the following commands:

```
find -name "*.java" > source.txt
javac -d ./ -cp "lib/commons-math3-3.6.1.jar:lib/gluegen-rt.jar:lib/jamtio.jar:lib/jfoenix-8.0.8.jar:lib/jogl-all.jar:lib/worldwind.jar:lib/worldwindx.jar" @source.txt
find -name "*.class" > class.txt
jar cfm HW4.jar manifest.txt @class.txt
java -jar HW4.jar
```

3D audio should be played back to the user after the execution of the above steps. 

### IDE (Eclipse)
To run the program in an IDE, load the hw4 directory as a java project, add **lib/jamtio.jar** and **lib/commons-math3-3.6.1.jar** files and to the build path, and run the program. For the Eclipse IDE, this process is:

1) Select File -> New -> Project
2) Selected Java Project
3) Uncheck "Use default location"
4) Select browse next to the "location" textbox and navigate to the "hw4" directory. This directory contains this README.md file. 
5) Select "Finish"
6) Add all jar files located in the lib directory to the build path by right-clicking the "hw4" folder in eclipse, and selecting "Build Path -> Configure Build Path". Select "Add External JARs..." and select the two jar files file.
7) Select the "Apply and Close" Button
8) Run the program (main located in HW4.java)