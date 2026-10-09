# Life Link Saver

Aplikasi Android untuk menyimpan link dengan desain calm, aesthetic, dan animasi doodle yang hidup.

## Fitur utama
- UI mengikuti konsep mockup dengan material 3 styling
- Input link untuk disimpan ke daftar lokal
- Animasi doodle bergerak dan mata berkedip
- Navigasi bawah yang soft dan nyaman dilihat
- Struktur project terpisah per komponen dan tema

## Teknologi
- Kotlin
- Jetpack Compose
- Material 3
- Room database (siap dikembangkan lebih lanjut)

## Cara menjalankan
1. Buka project di Android Studio
2. Tunggu proses sync Gradle selesai
3. Jalankan di emulator atau device

## Struktur utama
- `MainActivity.kt`
- `ui/theme/Theme.kt`
- `ui/components/AnimatedDoodleScene.kt`
- `ui/components/SaveLinkInput.kt`
- `ui/components/BottomNavigationBar.kt`
- `data/db/*` (Room setup)

## Catatan
Versi ini sudah menggunakan desain Material 3 dan animasi yang lebih hidup, sesuai kebutuhan UI seperti gambar referensi.

### Roadmap berikutnya
- Simpan link ke Room DB
- Tampil daftar link di halaman khusus
- Buka link di browser
- Edit dan hapus item
- Animasi yang lebih kompleks pada doodle
- Fitur search dan kategori

MIT License
