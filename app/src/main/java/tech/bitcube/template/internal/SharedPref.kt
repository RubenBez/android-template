package tech.bitcube.template.internal

import android.content.Context

class SharedPref(context: Context) {

    val sharedPref = context.getSharedPreferences("template", 0)

}
