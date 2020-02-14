package ir.mrahimy.conceal

import ir.mrahimy.conceal.util.ktx.getNameFromPath
import org.junit.Test

import org.junit.Assert.*

class RegexUnitTest {

    @Test
    fun `test if file name is correct on getting name`(){
        val str = "/storage/hello / but wow!/ nook$.mp3"
        val name = str.getNameFromPath()
        assert(name == " nook\$")
    }
}
