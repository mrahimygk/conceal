package ir.mrahimy.conceal

import ir.mrahimy.conceal.util.*
import org.junit.Test

class ImageManipulationUnitTest {

    private val sampleRate = 44100
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
    private val injectedSampleRate = removedLsb.putSampleRate(sampleRate)

    private fun `test removing 3 lsb of index`(index: Int, vararg intArray: Int) {
        assert(removedLsb[index].r == intArray[0]) //0
        assert(removedLsb[index].g == intArray[1])
        assert(removedLsb[index].b == intArray[2])
    }

    @Test
    fun `test removing 3 lsb`() {
        var index = 0
        `test removing 3 lsb of index`(index++, 192, 112, 112) //0
        `test removing 3 lsb of index`(index++, 176, 208, 216) //0
        `test removing 3 lsb of index`(index++, 176, 24, 24) //0
        `test removing 3 lsb of index`(8, 168, 184, 144) //8
    }

    @Test
    fun `test putting sampleRate after removing LSB`() {
        val audioSampleRate = sampleRate.toString().toSeparatedDigits()
        assert(audioSampleRate.elementCount == audioSampleRate.digits.size)

        val sampleRateElementCount = audioSampleRate.elementCount.toBinString(format = "%4s")
        assert(sampleRateElementCount == "0101")

        var position = 0

        var binaryString2BitsChunkStr = sampleRateElementCount.substring(0, 2)
        assert(binaryString2BitsChunkStr == "01")

        var binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)
        assert(binaryString2BitsChunk == 1)

        var data = removedLsb[position].r.bitwiseOr(binaryString2BitsChunk)
        assert(data == 193)
        removedLsb[position].r = data
        position += 1

        binaryString2BitsChunkStr = sampleRateElementCount.substring(2, 4)
        assert(binaryString2BitsChunkStr == "01")
        binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)
        assert(binaryString2BitsChunk == 1)

        data = removedLsb[position].r.bitwiseOr(binaryString2BitsChunk)
        assert(data ==177)
        removedLsb[position].r = data
        position += 1

        repeat(audioSampleRate.elementCount) {
            val element = it.toBinString(format = "%4s")
            binaryString2BitsChunk = element.substring(0, 2).toInt(2)
            removedLsb[position].r = removedLsb[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            binaryString2BitsChunk = element.substring(2, 4).toInt(2)
            removedLsb[position].r = removedLsb[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1
        }
    }

    @Test
    fun `test injected sample rate`(){

    }
}
