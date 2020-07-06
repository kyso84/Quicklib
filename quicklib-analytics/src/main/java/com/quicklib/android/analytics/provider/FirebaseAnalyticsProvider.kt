package com.quicklib.android.analytics.provider

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.quicklib.android.analytics.AnalyticsCategory
import com.quicklib.android.analytics.AnalyticsEventType
import com.quicklib.android.analytics.AnalyticsProvider

class FirebaseAnalyticsProvider(private val context: Context, private val firebaseAnalytics: FirebaseAnalytics) : AnalyticsProvider {
    override fun setUserProperty(propName: String, propValue: String) {
        firebaseAnalytics.setUserProperty(propName, propValue)
    }

    override fun pageView(activity: Activity?, screenName: String, subScreenName: String) {
        activity?.let {
            firebaseAnalytics.setCurrentScreen(it, screenName, subScreenName)
        }
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventName: String, page: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SOURCE, category.toString().toLowerCase())
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, type.toString().toLowerCase())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, eventName)
        if (!page.isNullOrEmpty()) {
            bundle.putString(FirebaseAnalytics.Param.ORIGIN, page)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventNameId: Int, page: String) {
        trackEvent(category = category, type = type, eventName = context.getString(eventNameId), page = page)
    }
}
