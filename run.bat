@echo off
:: Enable UTF-8 encoding in Command Prompt to support box-drawing characters and emojis
chcp 65001 >nul

:: Ensure working directory is the script's directory
cd /d "%~dp0"

:: Auto-build JAR if it does not already exist
if not exist "Lab 04 Nyare.jar" (
    echo Building executable JAR...
    if not exist out\classes mkdir out\classes
    javac -d out\classes src\*.java
    if errorlevel 1 (
        echo [ERROR] Compilation failed!
        pause
        exit /b 1
    )
    jar cfe "Lab 04 Nyare.jar" Main -C out\classes .
    if errorlevel 1 (
        echo [ERROR] Packaging JAR failed!
        pause
        exit /b 1
    )
)

:: Clear the screen and execute the JAR
cls
java -Dfile.encoding=UTF-8 -jar "Lab 04 Nyare.jar"
pause
