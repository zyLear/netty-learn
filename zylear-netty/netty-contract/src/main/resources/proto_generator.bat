
set proto_path=%cd%

for %%i in (.\protobuf\*.proto) do .\protoc.exe --java_out=..\java %%i & echo %%i generate finish