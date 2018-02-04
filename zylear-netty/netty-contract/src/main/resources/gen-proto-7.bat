@echo off

SetLocal EnableDelayedExpansion   
set PROTO_BASE=%cd%\protobuf


ECHO "gonna generate project specific protos..."

SET CURRENT_PATH=%~dp0

set tmp=%CURRENT_PATH%\..\java\com\zylear
if exist %tmp% ((RD /s /q %tmp%) & (ECHO "%tmp% deleted"))

SET GEN_PATH=%CURRENT_PATH%\..\java
CD %CURRENT_PATH%
 
CD %PROTO_BASE%
FOR /D %%i IN ("%PROTO_BASE%\proto") DO (IF EXIST %%i/*.proto ("%PROTO_BASE%\1.7\protoc" -I %PROTO_BASE% %%i/*.proto --java_out=%GEN_PATH%) & echo %%i done)
EndLocal

PAUSE