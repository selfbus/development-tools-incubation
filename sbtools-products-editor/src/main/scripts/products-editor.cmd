@echo off
setlocal

cd libs
set CLASSPATH=%CLASSPATH%;.
java -jar sbtools-products-editor-0.2-SNAPSHOT.jar org.selfbus.sbtools.prodedit.ProdEdit

if %ERRORLEVEL% neq 0 (
   echo.
   echo Please note: this application requires Java 7
   echo (This notice might not be related to the problem above)
   echo.
   pause
)
