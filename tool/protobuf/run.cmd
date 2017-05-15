set DES_DIR=%~dp0\..\..\example\src\main\java
set CMD_PATH=%~dp0

%~d0
cd %CMD_PATH%

dir template /B > list.txt

FOR /f %%i IN (list.txt) DO protoc --java_out=%DES_DIR% template/%%i

del list.txt

pause