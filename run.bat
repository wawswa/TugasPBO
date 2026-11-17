@echo off
cd /d "%~dp0"
if not exist "target\penilaian-akademik-1.0.0.jar" (
    echo Jar belum ada. Jalankan: mvn clean package
    exit /b 1
)
java -jar "target\penilaian-akademik-1.0.0.jar" %*
