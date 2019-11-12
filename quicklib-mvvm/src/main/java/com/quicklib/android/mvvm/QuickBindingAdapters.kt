package com.quicklib.android.mvvm

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object QuickBindingAdapters {
    @JvmStatic
    @BindingAdapter("selectedIf")
    fun selectedIf(view: View, visible: Boolean?) {
        view.isSelected = visible ?: false
    }

    @JvmStatic
    @BindingAdapter("enableIf")
    fun enableIf(view: View, value: Boolean?) {
        view.isEnabled = value ?: false
    }

    @JvmStatic
    @BindingAdapter("goneIf")
    fun goneIf(view: View, value: Boolean?) {
        view.visibility = if (value == true) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("goneIf")
    fun goneIf(view: View, text: String) {
        view.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("goneIf")
    fun goneIf(view: View, value: Any?) {
        view.visibility = if (value == null) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("invisibleIf")
    fun invisibleIf(view: View, value: Boolean?) {
        view.visibility = if (value == true) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("invisibleIf")
    fun invisibleIf(view: View, text: String) {
        view.visibility = if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("percent")
    fun percent(textView: TextView, value: Double) {
        val text = NumberFormat.getPercentInstance(Locale.getDefault()).format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter("percent")
    fun percent(textView: TextView, value: Int) {
        val text = NumberFormat.getPercentInstance(Locale.getDefault()).format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter("percent")
    fun percent(textView: TextView, value: Long) {
        val text = NumberFormat.getPercentInstance(Locale.getDefault()).format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter("percent")
    fun percent(textView: TextView, value: Float) {
        val text = NumberFormat.getPercentInstance(Locale.getDefault()).format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["amount", "currency"], requireAll = true)
    fun amount(textView: TextView, value: Double, currency: Currency) {
        val nf = NumberFormat.getCurrencyInstance(Locale.getDefault())
        nf.currency = currency
        val text = nf.format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["amount", "currency"], requireAll = true)
    fun amount(textView: TextView, value: Int, currency: Currency) {
        val nf = NumberFormat.getCurrencyInstance(Locale.getDefault())
        nf.currency = currency
        val text = nf.format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["amount", "currency"], requireAll = true)
    fun amount(textView: TextView, value: Long, currency: Currency) {
        val nf = NumberFormat.getCurrencyInstance(Locale.getDefault())
        nf.currency = currency
        val text = nf.format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["amount", "currency"], requireAll = true)
    fun amount(textView: TextView, value: Float, currency: Currency) {
        val nf = NumberFormat.getCurrencyInstance(Locale.getDefault())
        nf.currency = currency
        val text = nf.format(value)
        if (textView.text != text) {
            textView.text = text
        }
    }
}
