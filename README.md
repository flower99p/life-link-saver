# Life Link Saver

🔗 Aplikasi Android untuk menyimpan link dengan desain aesthetic, animasi doodle yang hidup, dan navigasi yang smooth.

## 🎯 Fitur Utama

- **Save Link** dengan thumbnail otomatis (og:image / thumbnail YouTube)
- **Material 3 + Monet**: warna dinamis mengikuti wallpaper (Android 12+), dark mode otomatis
- **Cari & filter** berdasarkan kategori (Ide, Inspirasi, Belajar, Lainnya)
- **Share ke aplikasi**: bagikan link dari browser/aplikasi lain langsung ke Life Link Saver
- **Judul & catatan** opsional, buka di browser, unduh media publik yang dapat diakses langsung ke penyimpanan internal aplikasi, hapus link
- Unduhan dari media sosial hanya tersedia jika platform menyediakan tautan media publik; konten privat atau yang dibatasi platform tidak dapat diunduh

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design**: Material 3
- **Database**: Room (siap dikembangkan)
- **Animation**: Compose Animation API

## 🚀 Cara Menjalankan

1. Buka project di Android Studio
2. Tunggu Gradle sync selesai
3. Jalankan app di emulator/device

## 🔄 CI/CD

Project juga dilengkapi workflow GitHub Actions untuk build debug APK secara otomatis.

## 📄 License

MIT

## API unduhan (universalDownloader)

Untuk mengunduh dari media sosial, jalankan/deploy [universalDownloader](https://github.com/milancodess/universalDownloader) lalu build dengan:

`./gradlew assembleDebug -PdownloaderApiUrl=https://alamat-api-anda`

Aplikasi memanggil `/api/{platform}/download?url=...`; jika tidak diset, aplikasi hanya mengunduh tautan media langsung.
