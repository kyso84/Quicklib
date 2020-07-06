package com.quicklib.android.analytics

import android.app.Activity

class Analytics(private val providers: List<AnalyticsProvider> = listOf()) : AnalyticsManager {
    override fun setUserProperty(propName: String, propValue: String) {
        providers.forEach { it.setUserProperty(propName, propValue) }
    }

    override fun pageView(activity: Activity?, screenName: String, subScreenName: String) {
        providers.forEach { it.pageView(activity, screenName, subScreenName) }
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventName: String, page: String) {
        providers.forEach { it.trackEvent(category, type, eventName, page) }
    }

    override fun trackEvent(category: AnalyticsCategory, type: AnalyticsEventType, eventNameId: Int, page: String) {
        providers.forEach { it.trackEvent(category, type, eventNameId, page) }
    }
}
