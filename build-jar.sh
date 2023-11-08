
rm -rf *.class
javac -d . *.java
jar cvf CLI_Calculator.jar CalculatorApp.class
jar cvmf manifest.mf CLI_Calculator.jar *.class
rm -rf *.class
