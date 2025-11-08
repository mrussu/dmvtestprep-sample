package app.dmvtestprep.domain.model

data class ListNavigator<T : Entity>(
    private val list: List<T>,
    val currentIndex: Int = 0
) {
    val size: Int
        get() = list.size

    val indices: IntRange
        get() = list.indices

    val currentOrNull: T?
        get() = list.getOrNull(currentIndex)

    operator fun get(index: Int): T = list[index]

    fun any(predicate: (T) -> Boolean): Boolean = list.any(predicate)

    fun filter(predicate: (T) -> Boolean): ListNavigator<T> {
        return copy(
            list = list.filter(predicate),
            currentIndex = 0
        )
    }

    fun toList(): List<T> = list

    fun goTo(index: Int): ListNavigator<T> {
        val newIndex = index.coerceIn(0, (size - 1).coerceAtLeast(0))
        return copy(currentIndex = newIndex)
    }

    fun goNext(): ListNavigator<T> {
        val newIndex = (currentIndex + 1).coerceAtMost(size - 1)
        return copy(currentIndex = newIndex)
    }

    fun goPrevious(): ListNavigator<T> {
        val newIndex = (currentIndex - 1).coerceAtLeast(0)
        return copy(currentIndex = newIndex)
    }

    fun updateById(id: Int, transform: (T) -> T): ListNavigator<T> {
        return copy(
            list = list.map { if (it.id == id) transform(it) else it }
        )
    }
}