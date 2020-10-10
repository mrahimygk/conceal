package ir.mrahimy.conceal.repository

import ir.mrahimy.conceal.data.Sample

interface SampleRepository {
    fun getSampleInitList(): List<Sample>
    fun getRandomSample(size: Int): Sample
}