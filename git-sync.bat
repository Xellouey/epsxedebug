@echo off
REM Скрипт для синхронизации с удаленным репозиторием

echo Синхронизация с GitHub...
echo.

echo 1. Получаем последние изменения...
git fetch origin

echo.
echo 2. Проверяем статус...
git status

echo.
echo 3. Добавляем все изменения...
git add .

echo.
set /p commit_msg="Введите описание изменений (или Enter для автосообщения): "

if "%commit_msg%"=="" (
    set commit_msg=Auto-sync %date% %time%
)

echo 4. Создаем коммит...
git commit -m "%commit_msg%"

echo.
echo 5. Отправляем на GitHub...
git push origin main

echo.
echo Синхронизация завершена!
echo Репозиторий: https://github.com/Xellouey/epsxedebug