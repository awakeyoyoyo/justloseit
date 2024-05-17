@echo off
echo generator proto begin!!!

set "folder_name=java"

if not exist %folder_name% (
    mkdir %folder_name%
    echo Folder %folder_name% has been created.
) else (
    echo The folder %folder_name% already exists.
    del /Q ".\%folder_name%\*.*"
)

for %%i in (*.proto) do (
    protoc.exe --proto_path=./ --java_out=./java %%i
    echo generator %%i Successfully!
)

echo generator proto end!!!

echo. 
@echo. & pause