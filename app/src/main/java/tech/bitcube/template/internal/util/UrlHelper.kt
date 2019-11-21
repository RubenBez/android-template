package tech.bitcube.template.internal.util

import tech.bitcube.template.BuildConfig.API_URL

object UrlHelper {

    private const val VERSION = "/api/v1/"
    const val URL = "$API_URL$VERSION"
    const val BASE_URL = API_URL

}