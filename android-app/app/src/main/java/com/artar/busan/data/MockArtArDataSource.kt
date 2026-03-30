package com.artar.busan.data

import com.artar.busan.model.Artwork
import com.artar.busan.model.Event

class MockArtArDataSource : ArtArDataSource {
    private val events = listOf(
        Event(
            id = "event-busan-2026",
            name = "ArtAR Busan Showcase",
            slug = "artar-busan-2026",
            primaryColor = "#0A7E8C",
            secondaryColor = "#F2A65A",
            logoUrl = ""
        )
    )

    private val artworks = listOf(
        Artwork(
            id = "art-001",
            venueId = "venue-bexco-1",
            markerImageKey = "marker_001",
            titleI18n = mapOf(
                "ko" to "파도와 도시",
                "en" to "Wave and City",
                "jp" to "波と都市",
                "cn" to "海浪与城市"
            ),
            artist = "Kim Minseo",
            descriptionI18n = mapOf(
                "ko" to "부산의 해안선과 도심의 리듬을 대비해 표현한 작품입니다.",
                "en" to "A contrast between Busan's coastline and urban rhythm.",
                "jp" to "釜山の海岸線と都市のリズムを対比した作品です。",
                "cn" to "对比釜山海岸线与城市节奏的作品。"
            ),
            thumbnailUrl = "",
            audioGuideAvailable = true
        ),
        Artwork(
            id = "art-002",
            venueId = "venue-bexco-1",
            markerImageKey = "marker_002",
            titleI18n = mapOf(
                "ko" to "빛의 항로",
                "en" to "Route of Light",
                "jp" to "光の航路",
                "cn" to "光之航路"
            ),
            artist = "Lee Jiho",
            descriptionI18n = mapOf(
                "ko" to "야간 항구의 움직임을 추상적 선과 색으로 구성했습니다.",
                "en" to "Night harbor movements rendered in abstract lines and colors.",
                "jp" to "夜の港の動きを抽象的な線と色で表現しました。",
                "cn" to "以抽象线条和色彩表现夜间港口的动态。"
            ),
            thumbnailUrl = "",
            audioGuideAvailable = true
        )
    )

    override suspend fun getEvents(): List<Event> = events

    override suspend fun getArtworksByEvent(eventId: String): List<Artwork> {
        return if (eventId == "event-busan-2026") artworks else emptyList()
    }

    override suspend fun getArtwork(artworkId: String): Artwork? {
        return artworks.firstOrNull { it.id == artworkId }
    }
}
