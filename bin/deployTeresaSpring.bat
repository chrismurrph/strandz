rem Use after have compiled, 
rem Will deploy the service on local tomcat and restart tomcat
rem Note that there is a same-named .sh file that resides on the server

call ant -f ..\spring\teresa\build.xml deploy

pause
