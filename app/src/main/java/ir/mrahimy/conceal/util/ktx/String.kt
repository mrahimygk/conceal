package ir.mrahimy.conceal.util.ktx

import java.io.File

fun String.toValidPath(): String {
    return if (this.endsWith('/')) this else "${this}/"
}

fun String.getNameFromPath() = File(this).name.split('.')[0]