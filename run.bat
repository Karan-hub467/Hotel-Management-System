@echo off
cd /d "%~dp0"
java -cp "out;lib\mysql-connector-j.jar;database" com.hotel.ui.MainFrame
