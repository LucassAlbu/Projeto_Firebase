package com.projetoFirebase.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import java.text.SimpleDateFormat
import java.util.Locale

private const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val OUTPUT_DATE_FORMAT = "dd-MM-yyyy HH:mm"

private const val AMERICAM_INPUT_DATEFORMAT = "yyyy-MM-dd"
private const val BR_OUTPUT_DATE_FORMAT = "dd-MM-yyyy"

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom
) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        this.requestLayout()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun String.formatDate(): String {

    val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
    val outputFormat = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.getDefault())

    val date = inputFormat.parse(this)

    return date.let { outputFormat.format(it!!) }
}

fun String.convertDate(): String {

    val americanInputFormat = SimpleDateFormat(AMERICAM_INPUT_DATEFORMAT, Locale.getDefault())
    val brOutputFormat = SimpleDateFormat(BR_OUTPUT_DATE_FORMAT, Locale.getDefault())

    val date = americanInputFormat.parse(this)
    return date.let { brOutputFormat.format(it!!) }

}