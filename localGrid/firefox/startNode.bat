@echo off 
setlocal 
  title Firefox Node 
  java -Dwebdriver.gecko.driver=./geckodriver.exe -jar ../selenium.jar -role node -maxSession 10 -port 6002 -host localhost -hub http://localhost:4444/grid/register  -browser browserName=firefox,version=70,platform=WINDOWS,maxInstances=10 --debug  
  endlocal  
pause 
