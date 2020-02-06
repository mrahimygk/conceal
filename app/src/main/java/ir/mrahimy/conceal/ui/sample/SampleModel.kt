package ir.mrahimy.conceal.ui.sample

import ir.mrahimy.conceal.base.BaseModel
import ir.mrahimy.conceal.data.Sample
import kotlin.random.Random

class SampleModel : BaseModel() {
    fun getSampleInitList() =
        listOf(
            Sample(1, "one"),
            Sample(2, "two"),
            Sample(3, "three")
        )

    fun getRandomSample(size: Int) =
        Sample(Random.nextInt(size, 100), "Random ##")

}