package ir.mrahimy.conceal

import ir.mrahimy.conceal.util.Rgb
import ir.mrahimy.conceal.util.remove3Lsb
import org.junit.Test

class ImageManipulationUnitTest {

    private val rgbList = mutableListOf<Rgb>().apply {
        add(Rgb(192, 117, 115))
        add(Rgb(180, 215, 216))
        add(Rgb(181, 25, 26))
        add(Rgb(81, 250, 16))
        add(Rgb(50, 200, 19))
        add(Rgb(150, 200, 190))
        add(Rgb(90, 51, 17))
        add(Rgb(17, 90, 51))
        add(Rgb(170, 190, 151))
    }

    private val removedLsb = rgbList.remove3Lsb()

    private fun `test removing 3 lsb of index`(index: Int, vararg intArray: Int) {
        assert(removedLsb[index].r == intArray[0]) //0
        assert(removedLsb[index].g == intArray[1])
        assert(removedLsb[index].b == intArray[2])
    }

    @Test
    fun `test removing 3 lsb`() {
        var index = 0
        `test removing 3 lsb of index`(index++, 192, 112, 112) //0
        `test removing 3 lsb of index`(8, 168, 184, 144) //8
    }

    @Test
    fun `test putting sampleRate after removing LSB`() {

    }
}
