package tech.bitcube.template

import android.app.Application
import androidx.preference.PreferenceManager
import tech.bitcube.template.data.db.TemplateDatabase
import tech.bitcube.template.data.network.*
import tech.bitcube.template.data.network.interceptors.ConnectivityInterceptor
import tech.bitcube.template.data.network.interceptors.ConnectivityInterceptorImpl
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.data.repository.UserRepositoryImpl
import tech.bitcube.template.internal.SharedPref
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.*

class Application : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@Application))

        // Dao
        bind() from singleton { TemplateDatabase(instance()) }
        bind() from singleton { instance<TemplateDatabase>().userDao() }

        // Interceptors
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(
                instance()
            )
        }

        // Services
        bind() from singleton { TemplateApiService(instance()) }
        bind() from singleton { SharedPref(instance()) }
        // Data Source
        bind<TemplateNetworkDataSource>() with singleton { TemplateNetworkDataSourceImpl(instance(), instance()) }

        // Providers

        // Repositories
        bind<UserRepository>() with singleton { UserRepositoryImpl(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

}