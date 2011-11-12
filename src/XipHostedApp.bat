@ECHO %1
@ECHO %2
@ECHO %3
@ECHO %4
for /f %%i in ("%0") do set curpath=%%~dpi
cd /d %curpath%
set PATH=.;..\resources;%XIP_LIB_PATH%;%JAVA_JRE_PATH%;%JAVA_JRE_PATH%\server\bin;%PATH%
%JAVA_JRE_PATH%\java -Xms256m -Xmx1024m -cp ..\..\XIPApp\bin;..\lib\pixelmed.jar;..\lib\DicomUtil.jar;..\lib\mime-util.jar; XipHostedApp %1 %2 %3 %4
PAUSE
REM EXIT