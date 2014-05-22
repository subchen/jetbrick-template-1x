@echo off

set JAVA_HOME=C:\dev\jdk1.6.0_41
set JAVA_OPTS=-Xms256m -Xmx512m
set PATH=%JAVA_HOME%\bin;%PATH%

set ANT_HOME=C:\dev\apache-ant-1.8.4
set PATH=%ANT_HOME%\bin;%PATH%

:: gpg path
set PATH=C:\dev\git-1.8.1\bin;%PATH%

:: switch pwd
cd /d %~dp0

call ant -buildfile build.xml deploy

pause &
