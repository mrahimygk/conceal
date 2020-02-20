package ir.mrahimy.conceal.data.capsules

import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.util.ktx.toValidPath
import ir.mrahimy.conceal.util.writeWave
import java.io.File
import java.util.*

data class SaveWaveInfoCapsule(
    val name: String?,
    val time: Date?,
    val data: Waver
)

fun SaveWaveInfoCapsule.save(path: String): String {
    val filePath = path.toValidPath() + "${name}_${time?.time}.wav"
    File(filePath).writeWave(data)
    return filePath
}