@echo off
chcp 65001 > nul
echo Compiling Travel Expense Splitter...
javac -encoding UTF-8 SimpleTravelExpenseSplitter.java
if %errorlevel% == 0 (
    echo Compilation successful! Starting application...
    java SimpleTravelExpenseSplitter
) else (
    echo Compilation failed!
    pause
)