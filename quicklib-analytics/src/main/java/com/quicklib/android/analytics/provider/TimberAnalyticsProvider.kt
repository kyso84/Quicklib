package com.quicklib.android.analytics.provider

import android.app.Activity
import android.content.Context
import com.quicklib.android.analytics.AnalyticsCategory
import com.quicklib.android.analytics.AnalyticsEventType
import com.quicklib.android.analytics.AnalyticsProvider
import timber.log.Timber

class TimberAnalyticsProvider(private val context: Context) : AnalyticsProvider {
    override fun setUserProperty(propName: String, propValue: String) {
        Timber.v("ANALYTICS PROP\t$propName=$propValue")
    }

    override fun pageView(activity: Activity?, screenName: String, subScreenName: String) {
        Timber.d("ANALYTICS SCREEN\t[${activity?.javaClass?.simpleName}] $screenName" + if (!subScreenName.isNullOrEmpty()) { " / $subScreenName" } else { "" })
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventName: String, page: String) {
        Timber.d("ANALYTICS EVENT\t[${category.name}][${type.name}] $page => $eventName")
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventNameId: Int, page: String) {
        trackEvent(category = category, type = type, eventName = context.getString(eventNameId), page = page)
    }
}
