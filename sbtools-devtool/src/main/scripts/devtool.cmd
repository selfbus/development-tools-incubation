@echo off
setlocal

cd libs
set CLASSPATH=%CLASSPATH%;.
java -jar sbtools-devtool-0.2-SNAPSHOT.jar

if %ERRORLEVEL% neq 0 (
   echo.
   echo Please note: this application requires Java 7
   echo (This notice might not be related to your problem above)
   echo.
   pause
)
