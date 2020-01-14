# WAVE
The Weather Auditory and Visual Environment seeks to integrate 3D audio and visuals of weather on a virtual globe. Weather is presented in an interactive wy using overlays and positional audio which varies on weather conditions and intensity.

![Desktop Application Sample](/img/sample1.png)
![Panel Features](/img/sample2.png)

## Group Information
The 

Group: Earth, Wind, and Audio

**Members:** 
1) Eric D. Goicochea, UF-ID: 6414-8523
2) Bridget L. Homer, UF-ID: 6963-3242
3) Kenneth W. King, UF-ID: 0901-3401


## Dependencies
The application was developed using Java version 1.8. The application is depenent upon three open source projects:
- **lib/jmatio.jar** - Reads .mat files. This is used to read the .mat files provided in the CIPIC database. 

https://www.mathworks.com/matlabcentral/fileexchange/10759-jmatio-matlab-s-mat-file-i-o-in-java
- **Java Wav File IO** - The two java files contained in this directory are used for reading and writing .wav files. 

http://www.labbookpages.co.uk/audio/javaWavFiles.html
- **Apache Commons Math 3.6** -  

http://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/util/MathArrays.html

## Datasets
The humidity and land temperature datasets are provided by Nasa Earth Observations (NEO). The rain dataset is provided by Nasa Precipitation Measurment Missions (PMM). The wind dataset is converted from the National Centers for Environmental Information (NOAA). The lightning dataset originates from the National Space Science Technology Center (NSSTC) and provided by the Google Earth Blog.

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
