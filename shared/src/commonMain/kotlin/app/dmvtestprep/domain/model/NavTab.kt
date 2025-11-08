package app.dmvtestprep.domain.model

enum class NavTab(
    val id: Int,
    val title: String
) {
    LEARN(0, "Learn"),
    TEST(1, "Test"),
    STATS(2, "Stats");

    companion object {
        val DEFAULT_TAB = TEST

        fun fromId(id: Int): NavTab {
            return entries.firstOrNull { it.id == id } ?: DEFAULT_TAB
        }
    }
}