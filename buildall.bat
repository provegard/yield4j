@echo off
setlocal enableextensions

if "%JAVA_HOME%x" == "x" (
	echo JAVA_HOME is not set. Cannot continue.
	exit /B
)

set OUTDIR=buildall
set LOGFILE=%OUTDIR%\buildall.log
if exist %OUTDIR% (
	rmdir /S /Q %OUTDIR%
)
mkdir %OUTDIR%

echo Log file is %LOGFILE%

for %%i in ("%JAVA_HOME%\..") do set ROOT=%%~fi
echo Building for all JDKs found in %ROOT% >>%LOGFILE%
echo ===================================================== >>%LOGFILE%

for /D %%i in ("%ROOT%\jdk*") do call :jdk %%~fi

goto :eof

:jdk
set JAVA_HOME=%*

for /D %%i in (%JAVA_HOME%) do set jdk=%%~ni%%~xi
echo.
echo Building for JDK %jdk%...

echo. >>%LOGFILE%
echo Building for JDK %jdk%... >>%LOGFILE%
echo ====================================== >>%LOGFILE%

rem Timeout to avoid locked files when doing mvn clean
timeout 2 /NOBREAK

echo JAVA_HOME = %JAVA_HOME%
mkdir "%OUTDIR%\%jdk%"

verify >nul
cmd /C "mvn clean package" >>%LOGFILE% 2>&1
if ERRORLEVEL 1 goto :mvnerr

echo Success! Copying JAR file...
copy target\yield4j*.jar "%OUTDIR%\%jdk%"
goto :eof

:mvnerr
echo Build failed! Check %LOGFILE% for details.
