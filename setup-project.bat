@echo off
REM Скрипт для первоначальной настройки проекта

echo ==========================================
echo      НАСТРОЙКА ПРОЕКТА ePSXe DEBUG
echo ==========================================
echo.

echo 1. Проверяем Git...
git --version
if %ERRORLEVEL% NEQ 0 (
    echo ОШИБКА: Git не установлен!
    echo Скачайте с https://git-scm.com/
    pause
    exit /b 1
)

echo.
echo 2. Настраиваем Git алиасы...
git config alias.save "!f() { git add . && git commit -m \"$1\"; }; f"
git config alias.undo "reset --soft HEAD~1"
git config alias.hard-undo "reset --hard HEAD~1"
git config alias.tree "log --oneline --graph --all"
git config alias.st "status --short"

echo.
echo 3. Проверяем Java...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ПРЕДУПРЕЖДЕНИЕ: Java не найдена в PATH
)

echo.
echo 4. Проверяем Android SDK...
if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
    echo Android SDK найден: %ANDROID_HOME%
) else (
    echo ПРЕДУПРЕЖДЕНИЕ: Android SDK не настроен
    echo Установите переменную ANDROID_HOME
)

echo.
echo 5. Создаем рабочие директории...
if not exist "backups" mkdir backups
if not exist "logs" mkdir logs
if not exist "temp" mkdir temp

echo.
echo 6. Настройка завершена!
echo.
echo Доступные команды:
echo   git-save.bat "описание"     - Сохранить изменения
echo   git-rollback.bat            - Откатить изменения  
echo   git-sync.bat                - Синхронизация с GitHub
echo   git-status.bat              - Состояние проекта
echo   git-backup.bat              - Создать резервную копию
echo.
echo Репозиторий: https://github.com/Xellouey/epsxedebug
echo.
pause