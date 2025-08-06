@echo off
REM Скрипт для получения изменений из репозитория

echo Получаем последние изменения из GitHub...
echo.

echo Текущее состояние:
git status --short

echo.
echo Получаем изменения...
git pull origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Успешно обновлено!
    echo.
    echo Последние коммиты:
    git log --oneline -5
) else (
    echo.
    echo Возникли конфликты! Необходимо их разрешить.
    echo Используйте: git status для просмотра конфликтов
)

echo.
pause