@ECHO OFF

REM A simple wrapper so that bamboo can execute powershell commands

powershell -command "%1" < NUL