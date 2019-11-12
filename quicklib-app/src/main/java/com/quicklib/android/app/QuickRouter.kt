package com.quicklib.android.app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.google.android.material.snackbar.Snackbar

abstract class QuickRouter {

    protected fun startActivity(activity: Activity, intent: Intent, vararg pairs: Pair<View, String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && pairs.isNotEmpty()) {
            pairs.forEach {
                it.first?.transitionName = it.second
            }
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs)
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        } else {
            activity.startActivity(intent)
        }
    }

    protected fun startActivityForResult(activity: Activity, requestCode: Int, intent: Intent, vararg pairs: Pair<View, String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && pairs.isNotEmpty()) {
            pairs.forEach {
                it.first?.transitionName = it.second
            }
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle())
        } else {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    protected fun conditionalNavigation(rootView: View, message: String, condition: () -> Boolean, validAction: () -> Unit, invalidAction: () -> Unit) {
        when {
            condition.invoke() -> validAction.invoke()
            rootView.context is Activity -> {
                val dialogBuilder = AlertDialog.Builder(rootView.context)
                dialogBuilder.setMessage(message)
                dialogBuilder.setPositiveButton(R.string.ok) { dialog, _ ->
                    invalidAction.invoke()
                    dialog.dismiss()
                }
                dialogBuilder.setNegativeButton(R.string.later) { dialog, _ -> dialog.dismiss() }
                dialogBuilder.show()
            }
            else -> {
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction(R.string.ok) {
                    invalidAction.invoke()
                }.show()
            }
        }
    }



}