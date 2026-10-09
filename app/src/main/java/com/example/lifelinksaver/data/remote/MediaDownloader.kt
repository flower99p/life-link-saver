package com.example.lifelinksaver.data.remote

import com.example.lifelinksaver.BuildConfig
import java.io.File
import java.net.URLEncoder
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaDownloader {
    private const val MAX_BYTES = 250L * 1024 * 1024
    private const val MAX_HTML_BYTES = 2 * 1024 * 1024
    private const val MAX_REDIRECTS = 5
    private val mediaExtensions = setOf("mp4", "webm", "mov", "m4v", "jpg", "jpeg", "png", "gif", "webp", "mp3", "m4a")

    suspend fun download(directory: File, url: String, title: String): File =
        withContext(Dispatchers.IO) {
            val mediaUrl = resolveViaApi(url) ?: resolveMediaUrl(url)
            val connection = openConnection(mediaUrl)
            try {
                val contentType = connection.contentType?.substringBefore(';')?.lowercase(Locale.ROOT).orEmpty()
                val extension = extensionFrom(mediaUrl, contentType)
                if (contentType == "text/html" || contentType == "application/xhtml+xml" ||
                    !isMedia(contentType, extension)
                ) {
                    throw IllegalArgumentException(
                        "Tautan ini bukan media langsung atau media sosial membatasi akses unduhan"
                    )
                }

                val length = connection.contentLengthLong
                if (length > MAX_BYTES) throw IllegalArgumentException("Ukuran media melebihi batas 250 MB")
                directory.mkdirs()
                val baseName = title.replace(Regex("[^A-Za-z0-9._-]"), "_")
                    .trim('.', '_', '-')
                    .take(80)
                    .ifBlank { "media" }
                val destination = File(directory, "downloads").apply { mkdirs() }
                val output = File(destination, "$baseName-${System.currentTimeMillis()}.$extension")
                val temporary = File(destination, "${output.name}.part")
                try {
                    connection.inputStream.use { input ->
                        temporary.outputStream().buffered().use { stream ->
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                            var total = 0L
                            while (true) {
                                val count = input.read(buffer)
                                if (count < 0) break
                                total += count
                                if (total > MAX_BYTES) {
                                    throw IllegalArgumentException("Ukuran media melebihi batas 250 MB")
                                }
                                stream.write(buffer, 0, count)
                            }
                        }
                    }
                    if (!temporary.renameTo(output)) throw IllegalStateException("Tidak dapat menyimpan media")
                    output
                } finally {
                    temporary.delete()
                }
            } finally {
                connection.disconnect()
            }
        }

    private fun platformFor(url: String): String? {
        val host = runCatching { URL(url).host.lowercase(Locale.ROOT) }.getOrNull() ?: return null
        fun has(vararg names: String) = names.any { host == it || host.endsWith(".$it") }
        return when {
            has("instagram.com", "facebook.com", "fb.watch") -> "meta"
            has("tiktok.com") -> "tiktok"
            has("youtube.com", "youtu.be") -> "youtube"
            has("twitter.com", "x.com") -> "twitter"
            has("threads.net", "threads.com") -> "threads"
            has("reddit.com") -> "reddit"
            has("pinterest.com", "pin.it") -> "pinterest"
            has("linkedin.com") -> "linkedin"
            has("bsky.app") -> "bluesky"
            has("capcut.com") -> "capcut"
            has("dailymotion.com", "dai.ly") -> "dailymotion"
            has("douyin.com") -> "douyin"
            has("kuaishou.com") -> "kuaishou"
            has("snapchat.com") -> "snapchat"
            has("soundcloud.com") -> "soundcloud"
            has("spotify.com") -> "spotify"
            has("terabox.com") -> "terabox"
            has("tumblr.com") -> "tumblr"
            else -> null
        }
    }

    /** Menggunakan API universalDownloader (/api/{platform}/download?url=) bila dikonfigurasi. */
    private fun resolveViaApi(url: String): String? {
        val base = BuildConfig.DOWNLOADER_API_URL.trim().trimEnd('/')
        if (base.isEmpty()) return null
        val platform = platformFor(url) ?: return null
        return try {
            val endpoint = "$base/api/$platform/download?url=${URLEncoder.encode(url, "UTF-8")}"
            val connection = URL(endpoint).openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = 15_000
                connection.readTimeout = 60_000
                connection.setRequestProperty("Accept", "application/json")
                if (connection.responseCode !in 200..299) return null
                val body = connection.inputStream.use { it.readBytes().toString(Charsets.UTF_8) }
                val json = JSONObject(body)
                if (!json.optBoolean("success", false)) return null
                findMediaLink(json.opt("data"))
            } finally {
                connection.disconnect()
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun findMediaLink(node: Any?): String? {
        val links = mutableListOf<String>()
        fun walk(value: Any?) {
            when (value) {
                is JSONObject -> value.keys().forEach { walk(value.opt(it)) }
                is JSONArray -> for (i in 0 until value.length()) walk(value.opt(i))
                is String -> if (value.startsWith("http://") || value.startsWith("https://")) links.add(value)
            }
        }
        walk(node)
        return links.firstOrNull { link ->
            val path = runCatching { URL(link).path.substringAfterLast('.', "").lowercase(Locale.ROOT) }.getOrDefault("")
            path in mediaExtensions && path !in setOf("jpg", "jpeg", "png", "webp")
        } ?: links.firstOrNull { link ->
            val path = runCatching { URL(link).path.substringAfterLast('.', "").lowercase(Locale.ROOT) }.getOrDefault("")
            path in mediaExtensions
        } ?: links.firstOrNull()
    }

    private fun resolveMediaUrl(pageUrl: String): String {
        var currentUrl = pageUrl
        repeat(3) {
            val connection = openConnection(currentUrl)
            try {
                val type = connection.contentType?.substringBefore(';')?.lowercase(Locale.ROOT).orEmpty()
                val extension = extensionFrom(currentUrl, type)
                if (type == "text/html" || type == "application/xhtml+xml") {
                    val html = connection.inputStream.use { input ->
                        val bytes = ByteArrayOutputStream(MAX_HTML_BYTES)
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var total = 0
                        while (total < MAX_HTML_BYTES) {
                            val count = input.read(buffer, 0, minOf(buffer.size, MAX_HTML_BYTES - total))
                            if (count < 0) break
                            bytes.write(buffer, 0, count)
                            total += count
                        }
                        bytes.toString(Charsets.UTF_8.name())
                    }
                    currentUrl = findPublicMediaUrl(html, currentUrl)
                        ?: throw IllegalArgumentException(
                            "Media tidak ditemukan. Konten privat atau media sosial mungkin membatasi unduhan."
                        )
                } else if (isMedia(type, extension)) {
                    return currentUrl
                } else {
                    throw IllegalArgumentException("Tautan ini tidak mengarah ke file media")
                }
            } finally {
                connection.disconnect()
            }
        }
        throw IllegalArgumentException("Tidak dapat menemukan tautan media langsung")
    }

    private fun openConnection(url: String): HttpURLConnection {
        var current = url
        repeat(MAX_REDIRECTS + 1) {
            val parsed = URL(current)
            require(parsed.protocol == "http" || parsed.protocol == "https") { "Skema tautan tidak didukung" }
            val connection = parsed.openConnection() as HttpURLConnection
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.instanceFollowRedirects = false
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 14) LifeLinkSaver")
            val status = connection.responseCode
            if (status in 300..399) {
                val location = connection.getHeaderField("Location")
                connection.disconnect()
                current = location?.let { URL(parsed, it).toString() }
                    ?: throw IllegalArgumentException("Pengalihan tautan tidak valid")
                return@repeat
            }
            if (status !in 200..299) {
                connection.disconnect()
                throw IllegalArgumentException("Server menolak unduhan (HTTP $status)")
            }
            return connection
        }
        throw IllegalArgumentException("Terlalu banyak pengalihan tautan")
    }

    private fun findPublicMediaUrl(html: String, pageUrl: String): String? {
        val candidates = mutableListOf<String>()
        val tags = Regex("<meta\\s[^>]*>", RegexOption.IGNORE_CASE)
        for (match in tags.findAll(html)) {
            val tag = match.value
            val property = attribute(tag, "property") ?: attribute(tag, "name") ?: continue
            if (property.lowercase(Locale.ROOT) !in setOf(
                    "og:video", "og:video:url", "og:video:secure_url", "twitter:player:stream"
                )
            ) continue
            attribute(tag, "content")?.let(candidates::add)
        }
        val sources = Regex("<(?:video|source)\\b[^>]*>", RegexOption.IGNORE_CASE)
        for (match in sources.findAll(html)) {
            attribute(match.value, "src")?.let(candidates::add)
        }
        return candidates.firstNotNullOfOrNull { candidate ->
            try {
                val resolved = URL(URL(pageUrl), candidate).toString()
                resolved.takeIf { URL(it).protocol in setOf("http", "https") && it != pageUrl }
            } catch (_: Exception) {
                null
            }
        }
    }

    private fun attribute(tag: String, name: String): String? =
        Regex("""\b${Regex.escape(name)}\s*=\s*["']([^"']*)["']""", RegexOption.IGNORE_CASE)
            .find(tag)?.groupValues?.get(1)

    private fun extensionFrom(url: String, contentType: String): String {
        val pathExtension = URL(url).path.substringAfterLast('.', "").lowercase(Locale.ROOT)
        if (pathExtension in mediaExtensions) return pathExtension
        return when (contentType) {
            "video/mp4" -> "mp4"
            "video/webm" -> "webm"
            "video/quicktime" -> "mov"
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "image/webp" -> "webp"
            "audio/mpeg" -> "mp3"
            "audio/mp4" -> "m4a"
            else -> "bin"
        }
    }

    private fun isMedia(contentType: String, extension: String): Boolean =
        contentType.startsWith("video/") ||
            contentType.startsWith("audio/") ||
            contentType.startsWith("image/") ||
            extension in mediaExtensions
}
