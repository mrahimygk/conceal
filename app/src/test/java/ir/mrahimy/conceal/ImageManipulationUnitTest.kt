package ir.mrahimy.conceal

import android.graphics.Color
import ir.mrahimy.conceal.util.parse
import ir.mrahimy.conceal.util.toRgb
import org.junit.Test

class ImageManipulationUnitTest {


    @Test
    fun `convert to rgb and parse back`() {
        val pixel = -4013374
        val rgb = pixel.toRgb()
        println("red: ${rgb.r}, green ${rgb.g}, blue ${rgb.b}")
        val fromRgb = rgb.parse()
        assert(pixel == fromRgb)
    }
}
