package com.emenjivar.core.data.models

object Mocks {
    val songModel1 = SongModel(
        id = 1,
        title = "Mayonaka no Door / Stay with mee",
        artist = "Miki Matsuraba",
        previewUrl = "fake_url",
        albumId = 10,
        albumName = "Miki Matsubara Best Collection",
        albumCover = "fake_url",
        albumCoverSmall = "fake_url"
    )

    val songModel2 = SongModel(
        id = 1,
        title = "Soleil Soleil",
        artist = "Pomme",
        previewUrl = "fake_url",
        albumId = 20,
        albumName = "les failles cachées",
        albumCover = "fake_url",
        albumCoverSmall = "fake_url"
    )

    val emotion1 = EmotionData(
        name = "sadness",
        color = 0xff0d47a1,
        description = "Emotional pain associated with, or characterized by, feeling of disadvantage, loss, despair, grief, helplessness, disappointment and sorrow."
    )
}
