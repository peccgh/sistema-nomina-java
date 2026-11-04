@echo off
title Compilador - Sistema de Nomina
echo Compilando codigo fuente...
cd /d "%~dp0src"
javac -cp ".;lib\mssql-jdbc-13.6.0.jre11.jar" Main.java PruebaDriver.java model\*.java service\*.java view\*.java utils\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Hubo un fallo en la compilacion. Revisa los errores arriba.
) else (
    echo.
    echo [EXITO] Todos los archivos se compilaron correctamente sin abrir la aplicacion.
)

pause