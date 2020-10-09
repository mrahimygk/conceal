package ir.mrahimy.conceal.repository

import ir.mrahimy.conceal.data.Sample
import kotlin.random.Random

class SampleRepositoryImpl : SampleRepository {
    override fun getSampleInitList(): List<Sample> =
        listOf(
            Sample(1, "one"),
            Sample(2, "two"),
            Sample(3, "three")
        )

    override fun getRandomSample(size: Int) =
        Sample(
            Random.nextInt(size, 100), "Random ##"
        )
}