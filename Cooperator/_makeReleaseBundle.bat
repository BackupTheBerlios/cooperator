@echo off
echo ==== Release-Bundel-Maker v1.0 ====
echo Achtung: der pfad zu jar.exe und javadoc.exe muss evt. angepasst werden
echo Standartmäßig gesetzt ist c:\Programme\j2sdk\bin
echo drücken sie Ctrl+Break um abzubrechen
set path=%PATH%;c:\programme\j2sdk\bin
pause

echo ===== Komplettes Paket kopieren =====
echo.
xcopy . c:\release\complete /S /E /I /Q
del c:\release\complete\src\mk.bat
del c:\release\complete\*.bat


echo ===== Source-Paket kopieren =====
echo.
xcopy .\src c:\release\src\src /S /E /I /Q
xcopy .\web c:\release\src\web /S /E /I /Q
xcopy .\WEB-INF c:\release\src\WEB-INF /I
xcopy .\WEB-INF\classes\A*.* c:\release\src\WEB-INF\classes\ /I
xcopy .\WEB-INF\lib c:\release\src\WEB-INF\lib /S /E /I /Q
del c:\release\src\src\mk.bat
del c:\release\src\*.bat

@echo ===== Binary-Paket kopieren =====
echo.
xcopy .\web c:\release\bin\web /S /E /I /Q
xcopy .\WEB-INF c:\release\bin\WEB-INF /S /E /I /Q
xcopy .\*.* c:\release\bin
del c:\release\bin\*.bat

c:
cd \
cd release

echo ===== komplettes Paket in jar paken =====
echo.
cd complete
jar cf ..\complete.jar *.*
cd ..

echo ===== Source-Paket in jar paken =====
echo.
cd src
jar cf ..\src.jar *.*
cd ..

echo ===== Bin-Paket für Tomcat in war paken =====
echo.
cd bin
jar cf ..\Cooperator.war *.*
cd ..

echo ===== JavaDoc erstellen =====
echo.
cd src\src
@rem copy "D:\Uni\4. SS05\SWT-Praktikum\Web\Aufgabe3\WEB-INF\lib\struts.jar" "C:\programme\j2sdk\jre\lib\ext"
javadoc -d c:\release\JavaDoc -subpackages *.*
@rem del c:\programme\j2sdk\jre\lib\ext\struts.jar
cd ..\..

echo ===== JavaDoc komprimieren =====
echo.
cd JavaDoc
jar cMf ..\javadoc.zip *.*
cd..

pause