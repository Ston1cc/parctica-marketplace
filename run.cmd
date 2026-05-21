@echo off
SET JAVA_HOME=C:\Program Files\Java\jdk-25
SET PATH=%USERPROFILE%\tools\apache-maven-3.9.6\bin;%PATH%

echo Starting Marketplace App...
cd /d "%~dp0"
mvn javafx:run
pause
