package ir.mrahimy.conceal

import org.junit.Test

class LoopTest {

    @Test
    fun `test forEach continue`() {
        listOf(1, 2, 3, 4, 5).forEach{
            if (it == 3) return@forEach // local return to the caller of the lambda, i.e. the forEach loop
            print(it)
        }
        print(" done with implicit label")
    }

    @Test
    fun `test forEach break`() {
        each()
        print(" done with explicit label")
    }

    private fun each(){
        listOf(1, 2, 3, 4, 5).forEach{
            if (it == 3) return
            print(it)
        }
    }


}