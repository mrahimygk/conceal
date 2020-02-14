package ir.mrahimy.conceal.util.ktx

fun String.toValidPath(): String {
    return if (this.endsWith('/')) this else "${this}/"
}