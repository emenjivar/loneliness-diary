package com.emenjivar.core.data.repositories

import com.emenjivar.core.data.models.EmotionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface EmotionsRepository {
    fun getAll(): Flow<List<EmotionData>>
}

class EmotionsRepositoryImp : EmotionsRepository {
    @Suppress("MaxLineLength")
    override fun getAll(): Flow<List<EmotionData>> {
        return flowOf(
            listOf(
                EmotionData(
                    name = "sad ",
                    color = BLUE_DARKEN_4,
                    description = "Feeling sorrow, typically in response to loss."
                ),
                EmotionData(
                    name = "angry ",
                    color = RED_DARKEN_4,
                    description = "Feeling intense displeasure when facing perceived threats, injustice or blocked goals."
                ),
                EmotionData(
                    name = "calm ",
                    color = GREEN_DARKEN_4,
                    description = "Feeling peaceful, relaxed and emotionally balanced."
                ),
                EmotionData(
                    name = "happy ",
                    color = YELLOW_DARKEN_1,
                    description = "Feeling joy, contentment or pleasure from positive experiences or achievements."
                )
            )
        )
    }
}

// TODO: move this to a different module, maybe `Theme` or `UI`
private const val BLUE_DARKEN_4 = 0xff0d47a1
private const val RED_DARKEN_4 = 0xffb71c1c
private const val GREEN_DARKEN_4 = 0xff1b5e20
private const val YELLOW_DARKEN_1 = 0xfffdd835
