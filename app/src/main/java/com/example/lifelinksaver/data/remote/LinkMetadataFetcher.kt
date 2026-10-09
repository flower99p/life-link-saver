package com.example.lifelinksaver.data.remote

import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LinkMetadata(val title: String?, val thumbnailUrl: String?)

object LinkMetadataFetcher {
    private const val MAX_BYTES = 300_000

    fun normalizeUrl(raw: String): String? {
        val trimmed = raw.trim()
        if (trimmed.isEmpty() || trimmed.any { it.isWhitespace() }) return null
        val withScheme = if (trimmed.contains("://")) trimmed else "https://$trimmed"
        return try {
            val uri = URI(withScheme)
            if ((uri.scheme == "http" || uri.scheme == "https") && !uri.host.isNullOrBlank()) withScheme else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetch(url: String): LinkMetadata = withContext(Dispatchers.IO) {
        val youtube = youtubeThumbnail(url)
        val tiktok = if (isTikTokUrl(url)) tiktokMetadata(url) else null
        try {
            val html = download(url)
            val thumbnail = (meta(html, "og:image") ?: meta(html, "twitter:image"))
                ?.let { resolve(url, it) }
            val pageTitle = meta(html, "og:title") ?: meta(html, "twitter:title") ?: titleTag(html)
            LinkMetadata(
                title = tiktok?.title?.takeUnless { isTikTokPlaceholderTitle(url, it) }
                    ?: pageTitle?.takeUnless { isTikTokPlaceholderTitle(url, it) },
                thumbnailUrl = thumbnail ?: tiktok?.thumbnailUrl ?: youtube
            )
        } catch (e: Exception) {
            LinkMetadata(
                tiktok?.title?.takeUnless { isTikTokPlaceholderTitle(url, it) },
                tiktok?.thumbnailUrl ?: youtube
            )
        }
    }

    private fun download(url: String): String {
        var current = url
        repeat(5) {
            val conn = URL(current).openConnection() as HttpURLConnection
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.instanceFollowRedirects = false
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 14) LifeLinkSaver")
            try {
                val code = conn.responseCode
                if (code in 300..399) {
                    val loc = conn.getHeaderField("Location") ?: error("redirect")
                    val next = URL(URL(current), loc).toString()
                    if (!next.startsWith("http://") && !next.startsWith("https://")) error("scheme")
                    current = next
                    return@repeat
                }
                val bytes = conn.inputStream.use { input ->
                    val buf = ByteArray(MAX_BYTES)
                    var total = 0
                    while (total < MAX_BYTES) {
                        val n = input.read(buf, total, MAX_BYTES - total)
                        if (n < 0) break
                        total += n
                    }
                    buf.copyOf(total)
                }
                return String(bytes, Charsets.UTF_8)
            } finally {
                conn.disconnect()
            }
        }
        error("too many redirects")
    }

    private fun meta(html: String, key: String): String? {
        val tag = Regex("<meta\\s[^>]*>", RegexOption.IGNORE_CASE)
        val k = Regex.escape(key)
        for (m in tag.findAll(html)) {
            val t = m.value
            if (!Regex("(property|name)\\s*=\\s*[\"']$k[\"']", RegexOption.IGNORE_CASE).containsMatchIn(t)) continue
            val c = Regex("content\\s*=\\s*\"([^\"]*)\"|content\\s*=\\s*'([^']*)'", RegexOption.IGNORE_CASE)
                .find(t) ?: continue
            val v = (c.groups[1]?.value ?: c.groups[2]?.value).orEmpty().let(::unescape).trim()
            if (v.isNotEmpty()) return v
        }
        return null
    }

    private fun titleTag(html: String): String? =
        Regex("<title[^>]*>(.*?)</title>", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
            .find(html)?.groupValues?.get(1)?.let(::unescape)?.trim()?.takeIf { it.isNotEmpty() }

    private fun unescape(s: String) = s.replace("&amp;", "&").replace("&quot;", "\"")
        .replace("&#39;", "'").replace("&lt;", "<").replace("&gt;", ">")

    fun isTikTokPlaceholderTitle(url: String, title: String): Boolean =
        isTikTokUrl(url) && title.trim().equals("TikTok - Make Your Day", ignoreCase = true)

    private fun resolve(base: String, link: String): String? = try {
        val r = URL(URL(base), link).toString()
        if (r.startsWith("http://") || r.startsWith("https://")) r else null
    } catch (e: Exception) {
        null
    }

    private fun youtubeThumbnail(url: String): String? {
        val uri = try { URI(url) } catch (e: Exception) { return null }
        val host = uri.host?.removePrefix("www.")?.removePrefix("m.") ?: return null
        val id = when (host) {
            "youtu.be" -> uri.path?.trim('/')
            "youtube.com" -> Regex("(?:^|&)v=([\\w-]{6,})").find(uri.rawQuery ?: "")?.groupValues?.get(1)
            else -> null
        }
        return if (id.isNullOrBlank()) null else "https://img.youtube.com/vi/$id/hqdefault.jpg"
    }

    private fun tiktokMetadata(url: String): LinkMetadata? {
        return try {
            val encodedUrl = URLEncoder.encode(url, Charsets.UTF_8.name())
            val response = download("https://www.tiktok.com/oembed?url=$encodedUrl")
            val json = JSONObject(response)
            val title = (json.opt("title") as? String)
                ?.let(::unescape)
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
            val thumbnail = (json.opt("thumbnail_url") as? String)
                ?.let { resolve("https://www.tiktok.com", it) }
            LinkMetadata(title, thumbnail)
        } catch (e: Exception) {
            null
        }
    }

    private fun isTikTokUrl(url: String): Boolean {
        val host = try { URI(url).host?.lowercase() } catch (e: Exception) { null } ?: return false
        return host == "tt.site" || host.endsWith(".tt.site") ||
            host == "tiktok.com" || host.endsWith(".tiktok.com")
    }
}
