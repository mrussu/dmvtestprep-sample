package app.dmvtestprep.domain.resources

import kotlin.test.Test
import kotlin.test.assertEquals

class MaxErrorsResourcesTest {
    @Test
    fun testGetText_LowerBoundary() {
        assertEquals("Impossible", MaxErrorsResources.getText(0.0))
    }

    @Test
    fun testGetText_UpperBoundary() {
        assertEquals("Already passed", MaxErrorsResources.getText(1.0))
    }

    @Test
    fun testGetText_BoundaryValues() {
        assertEquals("Extremely hard", MaxErrorsResources.getText(0.001))
        assertEquals("Guaranteed", MaxErrorsResources.getText(0.999))
    }

    @Test
    fun testGetText_OutOfBounds() {
        assertEquals("Impossible", MaxErrorsResources.getText(-0.5))
        assertEquals("Impossible", MaxErrorsResources.getText(0.0))
        assertEquals("Already passed", MaxErrorsResources.getText(1.0))
        assertEquals("Already passed", MaxErrorsResources.getText(1.5))
    }
}