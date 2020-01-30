package ir.mrahimy.conceal

import ir.mrahimy.conceal.data.absolute
import ir.mrahimy.conceal.data.mapToUniformDouble
import org.junit.Test

class WaveManipulationUnitTest {

    val data = LongArray(11) {
        (it * 10).toLong()
    }

    val negs = LongArray(11) {
        (it * -10).toLong()
    }

    @Test
    fun mapToUniformDouble() {
        val double = data.mapToUniformDouble()
        assert(100L == data.max())
        assert(double[0] == 0.0)
        assert(double[1] == data[1].toDouble() / (data.max()?.toDouble() ?: 0.0))
        assert(double[1] == 0.1)
        assert(double.max() ?: 0.0 <= 1.0)
    }

    @Test
    fun mapToUniformDoubleNegative() {
        val data = negs
        val double = data.mapToUniformDouble()
        assert(100L == data.absolute().max())
        assert(double[0] == 0.0)
        assert(double[1] == data[1].toDouble() / (data.absolute().max()?.toDouble() ?: 0.0))
        assert(double[1] == -0.1)
        assert(double.max() ?: 0.0 <= 1.0)
    }
}
