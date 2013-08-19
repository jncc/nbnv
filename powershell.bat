@ECHO OFF

REM A simple wrapper so that bamboo can execute powershell commands

powershell.exe -command "%1" < NUL