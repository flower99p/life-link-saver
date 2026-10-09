# Life Link Saver - Android App

Aplikasi Android untuk menyimpan dan mengelola link/URL dengan interface yang indah dan menenangkan.

## Fitur
- 🔗 Simpan link/URL dengan mudah
- 📚 Kategorisasi link (Ide, Inspirasi, dll)
- 🎨 UI yang clean dan soft
- ✨ Animasi doodle yang menarik
- 💾 Penyimpanan data menggunakan Room Database
- 🎯 Navigasi bottom tab

## Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room Database
- **Architecture**: MVVM (ViewModel + Repository)

## Struktur Project
```
app/src/main/
├── java/com/example/lifelinksaver/
│   ├── data/
│   │   ├── db/
│   │   │   ├── AppDatabase.kt
│   │   │   ├── SavedLinkEntity.kt
│   │   │   └── SavedLinkDao.kt
│   │   └── repository/
│   │       └── LinkRepository.kt
│   ├── ui/
│   │   ├── components/
│   │   │   ├── TopStatusBar.kt
│   │   │   ├── CategoryCard.kt
│   │   │   ├── AnimatedDoodleScene.kt
│   │   │   ├── SaveLinkInput.kt
│   │   │   └── BottomNavigationBar.kt
│   │   └── theme/
│   │       └── Theme.kt
│   └── MainActivity.kt
└── res/
    └── values/
        └── themes.xml
```

## Persyaratan
- Android API 24+
- Android Studio Arctic Fox atau lebih baru
- Kotlin 1.9.24+

## Setup
1. Clone repository ini
2. Buka di Android Studio
3. Sync gradle files
4. Run pada emulator atau device

## Animasi
Aplikasi ini menampilkan doodle yang bergerak dengan animasi:
- **Bobbing Motion**: Doodle naik-turun perlahan
- **Blinking Eyes**: Mata berkedip secara natural
- **Floating Leaves**: Daun-daun kecil mengapung di sekitar

## Pengembangan Selanjutnya
- [ ] Share link ke aplikasi lain
- [ ] Import bookmark dari browser
- [ ] Pencarian link
- [ ] Tag untuk link
- [ ] Export/Backup data
- [ ] Dark mode
- [ ] Notifikasi pengingat

## License
MIT License