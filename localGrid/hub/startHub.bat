@echo off 
setlocal 
  title Hub 
  java -jar ../selenium.jar -role hub --debug -host localhost 
endlocal 
pause 
