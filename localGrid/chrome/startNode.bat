@echo off 
setlocal 
  title Chrome Node  
  java -Dwebdriver.chrome.driver=./chromedriver.exe -jar ../selenium.jar -role node -maxSession 30 -port 6001 -host localhost -hub http://localhost:4444/grid/register -browser browserName=chrome,version=78,platform=WINDOWS,maxInstances=30 --debug 
  endlocal  
pause 
