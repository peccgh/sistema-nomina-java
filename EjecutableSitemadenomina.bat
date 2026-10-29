@echo off
title Sistema de Nomina - Azure SQL
echo Iniciando el sistema de nomina...
cd /d "%~dp0src"
java -cp ".;lib\mssql-jdbc-13.6.0.jre11.jar" Main
pause