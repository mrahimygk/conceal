package ir.mrahimy.conceal.util.ktx

import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat


/**
 *  converts epoch millis to persian date format
 */
fun Long.toPersianFormat(format: String = "Y-m-d H:i:s"): String {
    return PersianDateFormat(format).format(PersianDate(this))
}