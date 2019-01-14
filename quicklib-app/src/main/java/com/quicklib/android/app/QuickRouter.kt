package com.quicklib.android.app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

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

}