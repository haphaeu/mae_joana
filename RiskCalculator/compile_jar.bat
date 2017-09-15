javac -target 1.5 -source 1.5 RiskCalculator.java
jar cfm RiskCalculator.jar RiskCalculatorManifest.mf RiskCalculator*.class
pause