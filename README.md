# ePSXe Android Debug Project

Проект для отладки и разработки Android-версии эмулятора PlayStation (ePSXe).

## 🚀 Быстрый старт

### Система контроля версий (Git)

Проект настроен с Git для удобного отката изменений:

#### Основные команды:

```bash
# Быстрое сохранение изменений
git-save.bat "Описание изменений"

# Откат к предыдущему состоянию  
git-rollback.bat

# Создание резервной копии
git-backup.bat

# Просмотр состояния проекта
git-status.bat

# Синхронизация с GitHub
git-sync.bat

# Получение изменений
git-pull.bat
```

### Структура проекта

```
ePSXe/
├── app/src/main/java/com/epsxe/ePSXe/
│   ├── ePSXe.java              # Основной класс эмулятора
│   ├── ePSXeViewGL.java        # OpenGL рендеринг
│   ├── SaveStateManager.java   # Менеджер сохранений (новый)
│   └── dialog/                 # Диалоговые окна
├── git-*.bat                   # Скрипты для работы с Git
└── README.md                   # Этот файл
```

## 🛠 Разработка

### Система сохранений

Добавлен `SaveStateManager` для управления состояниями эмулятора:

- Быстрые сохранения (Quick Save)
- Автоматическая очистка старых сохранений
- Список всех сохранений с метаданными
- Возможность отката к любому сохранению

### Откат изменений

#### Через Git:
1. **Мягкий откат** - сохраняет изменения: `git reset --soft HEAD~1`
2. **Жесткий откат** - удаляет изменения: `git reset --hard HEAD~1`
3. **Откат к конкретному коммиту**: `git reset --hard <commit-hash>`

#### Через скрипты:
- `git-rollback.bat` - интерактивный откат
- `git-backup.bat` - создание резервной ветки

## 📱 Сборка

```bash
# Отладочная сборка
./gradlew assembleDebug

# Релизная сборка  
./gradlew assembleRelease
```

## 🔧 Настройки

### Build Configuration
- **Target SDK**: 30
- **Min SDK**: 21
- **Java**: 1.8
- **NDK**: Поддержка ARM64, ARMv7, x86, x86_64

### Зависимости
- AndroidX
- Google Play Services
- Dropbox SDK
- OkHttp
- Jackson JSON

## 📋 Полезные команды

### Git команды:
```bash
# Просмотр истории
git log --oneline --graph

# Просмотр изменений
git diff HEAD~1

# Создание ветки для эксперимента
git checkout -b feature/new-feature

# Возврат к main ветке
git checkout main
```

### Gradle команды:
```bash
# Очистка проекта
./gradlew clean

# Запуск тестов
./gradlew test

# Проверка зависимостей
./gradlew dependencies
```

## 🐛 Отладка

### Логи эмулятора:
```bash
adb logcat | grep ePSXe
```

### Профилирование:
- GPU Profiler для OpenGL
- Memory Profiler для утечек памяти
- CPU Profiler для производительности

## 📞 Поддержка

При возникновении проблем:

1. Проверьте `git-status.bat` для состояния проекта
2. Используйте `git-rollback.bat` для отката
3. Создайте issue в репозитории с описанием проблемы

## 🔗 Ссылки

- [Репозиторий](https://github.com/Xellouey/epsxedebug)
- [Android Developer Guide](https://developer.android.com/)
- [Git Documentation](https://git-scm.com/doc)

---

**Автор**: ePSXe Development Team  
**Лицензия**: Proprietary