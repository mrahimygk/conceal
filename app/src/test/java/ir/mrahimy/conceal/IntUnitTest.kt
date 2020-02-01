package ir.mrahimy.conceal

import ir.mrahimy.conceal.util.IntegerUtil
import ir.mrahimy.conceal.util.bitwiseOr
import ir.mrahimy.conceal.util.toBinString
import org.junit.Test

class IntUnitTest {

    @Test
    fun `check combine 2 lsb`() {
        val a = 3
        val b = 2
        val binA = a.toBinString()
        val binB = b.toBinString()

        assert(binA == "00000011")
        assert(binB == "00000010")

        val lsbA = binA.drop(6)
        val lsbB = binB.drop(6)

        assert(lsbA == "11")
        assert(lsbB == "10")

        val whole = lsbA + lsbB

        assert(whole == "1110")

        val int = whole.toInt(2)

        assert(int == 14)
    }

    @Test
    fun `check bits back with combine`() {
        val number = 6
        val carrierIn1 = 192 // 1100 0000
        val carrierIn2 = 208 // 1101 0000

        val numberBinString = number.toBinString(format = "%4s") // 0110
        assert(numberBinString == "0110")

        val binaryString2BitsChunk1 = numberBinString.substring(0, 2).toInt(2) // 01
        assert(binaryString2BitsChunk1 == 1)
        val binaryString2BitsChunk2 = numberBinString.substring(2, 4).toInt(2) // 10
        assert(binaryString2BitsChunk2 == 2)

        val carrierOut1 = carrierIn1.bitwiseOr(binaryString2BitsChunk1)
        assert(carrierOut1==193)
        val carrierOut2 = carrierIn2.bitwiseOr(binaryString2BitsChunk2)
        assert(carrierOut2==210)

        val binA = carrierOut1.toBinString()
        val binB = carrierOut2.toBinString()

        assert(binA == "11000001")
        assert(binB == "11010010")

        val lsbA = binA.drop(6)
        val lsbB = binB.drop(6)

        assert(lsbA == "01")
        assert(lsbB == "10")

        val whole = lsbA + lsbB

        assert(whole == "0110")

        val int = whole.toInt(2)

        assert(int == 6)
    }

    @Test
    fun `test getting 2 lsb`(){
        val number = 15
        val _2lsb = IntegerUtil.get2LsBits(number)
        assert(_2lsb==3)
    }

    @Test
    fun `test getting 3 lsb`(){
        val number = 15
        val _3lsb = IntegerUtil.get3LsBits(number)
        assert(_3lsb==7)
    }

}
