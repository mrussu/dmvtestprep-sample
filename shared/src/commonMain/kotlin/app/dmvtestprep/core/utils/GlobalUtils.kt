package app.dmvtestprep.core.utils

import app.dmvtestprep.domain.model.Entity
import app.dmvtestprep.domain.model.ListNavigator

fun <T : Entity> emptyListNavigator(): ListNavigator<T> {
    return ListNavigator(
        list = emptyList(),
        currentIndex = 0
    )
}

fun <T> List<T>.reshuffle(): List<T> {
    if (size > 1) repeat(10) {
        shuffled().takeIf { it != this }?.let { return it }
    }
    return this
}