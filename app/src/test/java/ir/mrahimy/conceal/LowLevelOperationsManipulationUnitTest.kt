package ir.mrahimy.conceal

import ir.mrahimy.conceal.data.Rgb
import ir.mrahimy.conceal.data.toSeparatedDigits
import ir.mrahimy.conceal.util.*
import ir.mrahimy.conceal.util.ktx.bitwiseOr
import ir.mrahimy.conceal.util.ktx.toBinString
import org.junit.Before
import org.junit.Test

class LowLevelOperationsManipulationUnitTest {

    private val sampleRate = 44100
    private val rgbList = mutableListOf<Rgb>()
    private val removedLsb = mutableListOf<Rgb>()

    private val image_width = 8
    private val image_height = 5

    @Before
    fun initRgbList() {
        rgbList.clear()
        rgbList.apply {
            add(Rgb(192, 117, 115))
            add(Rgb(180, 215, 216))
            add(Rgb(181, 25, 26))
            add(Rgb(81, 250, 16))
            add(Rgb(50, 200, 19))
            add(Rgb(150, 200, 190))
            add(Rgb(90, 51, 17))
            add(Rgb(190, 251, 217))
            add(Rgb(170, 190, 151))
            add(Rgb(240, 151, 117))
            add(Rgb(17, 90, 51))
            add(Rgb(100, 90, 101))
            add(Rgb(192, 117, 115))
            add(Rgb(192, 117, 115))
            add(Rgb(180, 215, 216))
            add(Rgb(181, 25, 26))
            add(Rgb(81, 250, 16))
            add(Rgb(50, 200, 19))
            add(Rgb(170, 190, 151))
            add(Rgb(150, 200, 190))
            add(Rgb(50, 200, 19))
            add(Rgb(150, 200, 190))
            add(Rgb(90, 51, 17))
            add(Rgb(190, 251, 217))
            add(Rgb(170, 190, 151))
            add(Rgb(240, 151, 117))
            add(Rgb(17, 90, 51))
            add(Rgb(100, 90, 101))
            add(Rgb(192, 117, 115))
            add(Rgb(180, 215, 216))
            add(Rgb(181, 25, 26))
            add(Rgb(81, 250, 16))
            add(Rgb(50, 200, 19))
            add(Rgb(150, 200, 190))
            add(Rgb(90, 51, 17))
            add(Rgb(190, 251, 217))
            add(Rgb(170, 190, 151))
            add(Rgb(240, 151, 117))
            add(Rgb(17, 90, 51))
            add(Rgb(100, 90, 101))
        }//40

        removedLsb.clear()
        removedLsb.addAll(rgbList.remove3Lsb())
    }

    private fun `test removing 3 lsb of index`(index: Int, vararg intArray: Int) {
        assert(removedLsb[index].r == intArray[0]) //0
        assert(removedLsb[index].g == intArray[1])
        assert(removedLsb[index].b == intArray[2])
    }

    @Test
    fun `test injected sample rate position`() {
        val returnedPosition = removedLsb.map { it }.putSampleRate(sampleRate)
        val audioSampleRate = sampleRate.toString().toSeparatedDigits()
        assert(returnedPosition == (audioSampleRate.elementCount + 1) * 2)
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
        assert(data == 177)
        removedLsb[position].r = data
        position += 1

        audioSampleRate.digits.forEach {
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
    fun `test injected sample rate`() {
        val injected = mutableListOf<Rgb>().apply { addAll(removedLsb.map { it }) }
        val pos = injected.putSampleRate(sampleRate)

        var i = 0
        // 5 = 0101
        assert(injected[i++].r == 193) //01 + 192
        assert(injected[i++].r == 177) // 01 + 176

        //4 = 0100
        assert(injected[i++].r == 177) // 01 + 176
        assert(injected[i++].r == 80) // 00 + 80

        //4 = 0100
        assert(injected[i++].r == 49) // 01 + 48
        assert(injected[i++].r == 144) // 00 + 144

        //1 = 0001
        assert(injected[i++].r == 88) // 00 + 88
        assert(injected[i++].r == 185) // 01 + 184

        //0 = 0000
        assert(injected[i++].r == 168) // 00 + 168
        assert(injected[i++].r == 240) // 00 + 240

        //0 = 0000
        assert(injected[i++].r == 16) // 00 + 16
        assert(injected[i++].r == 96) // 00 + 96

        assert(i == 12)
    }

    @Test
    fun `test retrieving sample rate`() {
        val injected = mutableListOf<Rgb>().apply { addAll(removedLsb.map { it }) }
        val pos = injected.putSampleRate(sampleRate)

        val res = injected.getSampleRate()
        assert(res.number == sampleRate)
        assert(res.position == 12)
    }

    @Test
    fun `test putting signed integer`() {
        val position = rgbList.putSignedInteger(0, 251, Layer.R)
        assert(position == 4)
    }
//
//    @Test
//    fun `test putting signed integer array`() {
//        val array = intArrayOf(
//            250, 151, -200, -60, 25, 14, 1, 36, 19, 255,
//            250, 151, -200, -60, 25, 14, 1, 36, 19, 255,
//            30, 39, 255
//        )
//        val full = array.size
//        val threshold = (image_height) * (image_width)
//        assert(threshold == 40)
//        val position = rgbList.putAllSignedIntegers(0, array, image_width, image_height)
//        println(position)
//        println(threshold)
//        println(position % threshold)
//        assert(position == (position % threshold))
//    }

    @Test
    fun `test getting back positive integer inside rgbList layer red`() {
        val rgbList = listOf(
            Rgb(192, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )
        val parsed = rgbList.getSignedInteger(0, Layer.R)
        assert(parsed.toInt() == 5)
    }

    @Test
    fun `test getting back negative integer inside rgbList layer red`() {
        val rgbList = listOf(
            Rgb(196, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )
        val parsed = rgbList.getSignedInteger(0, Layer.R)
        assert(parsed.toInt() == -5)
    }

    @Test
    fun `test getting back negative integer inside rgbList layer green`() {
        val rgbList = listOf(
            Rgb(196, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )
        val parsed = rgbList.getSignedInteger(0, Layer.G)
        assert(parsed.toInt() == -118)
    }

    @Test
    fun `test getting back integer inside rgbList layer green middle`() {
        val rgbList = listOf(
            Rgb(196, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )
        val int = rgbList.getSignedInteger(6, Layer.G)
        assert(int == 251)
    }

    @Test
    fun `test getting back all signed integers inside rgbList`() {
        val rgbList = listOf(
            Rgb(196, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )

        val list = rgbList.getAllSignedIntegers(6)
        assert(list.containsAll(listOf(168, -118, 15, 200, 229)))
    }

    @Test
    fun `test putting signed in and getting it back`() {
        val input = -251
        removedLsb.putSignedInteger(0, input, Layer.R)
        removedLsb.putSignedInteger(4, input / 2, Layer.R)
        removedLsb.putSignedInteger(8, -input / 2, Layer.R)
        assert(removedLsb.subList(0, 4).map { it.r }.containsAll(listOf(199, 179, 178, 83)))

        var parsed = removedLsb.getSignedInteger(0, Layer.R)
        assert(parsed == input)

        parsed = removedLsb.getSignedInteger(4, Layer.R)
        assert(parsed == -125)

        parsed = removedLsb.getSignedInteger(8, Layer.R)
        assert(parsed == 125)
    }

    @Test
    fun `test getting back all signed integers inside rgbList 3 layers`() {
        val rgbList = listOf(
            Rgb(196, 117, 115),
            Rgb(180, 215, 216),
            Rgb(181, 25, 26),
            Rgb(81, 250, 16),
            Rgb(50, 200, 19),
            Rgb(150, 200, 190),
            Rgb(90, 51, 17),
            Rgb(190, 251, 217),
            Rgb(170, 190, 151),
            Rgb(240, 151, 117),
            Rgb(17, 90, 51)
        )

        val list = rgbList.getAllSignedIntegers(1)
        assert(list.containsAll(listOf(-22, -170, 15, 200, 229)))
    }
}
