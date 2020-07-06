package com.quicklib.android.analytics

import android.app.Activity
import androidx.annotation.StringRes

interface AnalyticsProvider {
    fun setUserProperty(propName: String, propValue: String)
    fun pageView(activity: Activity? = null, screenName: String, subScreenName: String = "")
    fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventName: String, page: String = "")
    fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, @StringRes eventNameId: Int, page: String = "")
}
