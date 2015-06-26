rem Seem to need JAVA_HOME to point to the JRE 
set SAVED_JAVA_HOME=%JAVA_HOME%
set JAVA_HOME=%SAVED_JAVA_HOME%\jre

rem The JPOX enhancer should work for Kodo too
REM May leave forever:
rem call ant enhance-wombat-openaccess

rem call ant enhance-wombat-jpox
rem call ant enhance-supersix-jpox

call ant enhance-wombat-kodo
rem call ant enhance-supersix-kodo

rem To stop jre being strapped as the end if repeatedly call 
set JAVA_HOME=%SAVED_JAVA_HOME%

pause