@echo off

SetLocal EnableDelayedExpansion

ECHO "gonna generate project specific protos..."

set tmp=..\java\com\zylear
echo %tmp%
if exist %tmp% ((RD /s /q %tmp%) & ECHO "%tmp% deleted" )

for %%i in (.\protobuf\proto\*.proto) do (.\protobuf\1.8\protoc.exe --java_out=..\java %%i & echo %%i generate finish)

