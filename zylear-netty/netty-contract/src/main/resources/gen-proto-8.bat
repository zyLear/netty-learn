@echo off

SetLocal EnableDelayedExpansion   
set PROTO_BASE=%cd%\protobuf


ECHO "gonna generate project specific protos..."

SET CURRENT_PATH=%~dp0

set tmp=%CURRENT_PATH%..\java\com\zylear
echo %tmp%
if exist %tmp% (RD /s /q %tmp% & ECHO "%tmp% deleted" )
echo 1

SET GEN_PATH=%CURRENT_PATH%..\java
echo %GEN_PATH%
CD %CURRENT_PATH%
 
CD %PROTO_BASE%
FOR /D %%i IN ("%PROTO_BASE%\proto") DO (IF EXIST %%i/*.proto ("%PROTO_BASE%\1.8\protoc" -I %PROTO_BASE% %%i/*.proto --java_out=%GEN_PATH%) & echo %%i done )
EndLocal

PAUSE