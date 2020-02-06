package ir.mrahimy.conceal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import androidx.test.platform.app.InstrumentationRegistry
import ir.mrahimy.conceal.data.Rgb
import ir.mrahimy.conceal.util.remove3Lsb
import ir.mrahimy.conceal.util.ktx.toRgb
import ir.mrahimy.conceal.util.writeBitmap
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.*


class LowLevelOperationsManipulationInstrumentedTest {
    private val rgbList = mutableListOf<Rgb>()
    private val removedLsb = mutableListOf<Rgb>()

    private val image_width = 8
    private val image_height = 5

    private lateinit var context: Context
    @Before
    fun t() {
        context = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext
    }

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

    @Test
    fun convertToRgbAndInitBack() {
        val pixel = -4013374
        val rgb = pixel.toRgb()
        /**
        android.graphics.Color must be used in instrumented test
         */
        val fromRgb = Color.rgb(
            rgb.r,
            rgb.g,
            rgb.b
        )
        assert(pixel == fromRgb)
    }

    @Test
    fun saveBitmap() {

        val bitmap = Bitmap.createBitmap(image_width, image_height, Bitmap.Config.ARGB_8888)

        var x = 0
        var y = 0
        repeat(rgbList.size) { l ->
            val rgb = rgbList[l]
            bitmap[x, y] = Color.rgb(rgb.r, rgb.g, rgb.b)
            x++
            if (x >= image_width) {
                y++
                x = 0
            }
        }

        val date = Date()
        File(context.externalCacheDir?.absolutePath + "/0test_img_${date.time}.png")
            .writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 100)

    }
}
