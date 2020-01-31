package ir.mrahimy.conceal

import android.graphics.Color
import ir.mrahimy.conceal.util.toRgb
import org.junit.Test

class ImageManipulationInstrumentedTest {

    @Test
    fun convertToRgbAndInitBack() {
        val pixel = -4013374
        val rgb = pixel.toRgb()
        val fromRgb = Color.rgb(
            rgb.r,
            rgb.g,
            rgb.b
        ) //android.graphics.Color must be used in instrumented test
        assert(pixel == fromRgb)
    }
}
