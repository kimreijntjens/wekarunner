# README #



### WekaProgram purpose ###

A program to calculate the expected values of a dataset while using machine learning. The program uses a simple logistic model that is tuned to be best fitting for the default dataset. You can run the program with a new instance that needs to be classified or let the program run with the default dataset. 


## usage

de application can be started by calling the jar file on the command line
command line:

java build/libs/Wekaprogram-1.0-SNAPSHOT.jar

then there is a option for using your own arff file containing data with instances that need to be classified

-f <data.arff> 

java -jar build/libs/Wekaprogram-1.0-SNAPSHOT.jar -f (your own file)
 
 
if no file is chosen the program will be run with a example data file that is located in testdata folder 
'testdata/data1_short.arff'


this is the file containing the unknown, yet to be classified instances.
And a file that contains the instances to be classified.


## Dependencies

| Project              | Home Page                                           |
|----------------------|-----------------------------------------------------|
| java 14.0            | <https://docs.oracle.com/en/java/javase/14/>        |
| apace CLI  1.4       | <https://commons.apache.org/proper/commons-cli/>    |
| weka API  3.8.4      | <https://waikato.github.io/weka-wiki/using_the_api/>|

further dependencies can be found in the build.gradle


### Who do I talk to? ###

for questions contact:
k.reijntjens@st.hanze.nl
