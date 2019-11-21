package tech.bitcube.template.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import tech.bitcube.template.R
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.SharedPref
import tech.bitcube.template.internal.getViewModel
import tech.bitcube.template.ui.user.profile.ProfileViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val pref: SharedPref by instance()

    private lateinit var navController: NavController

    private val viewModel: ProfileViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            ProfileViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)

        val appBarConfiguration =
            AppBarConfiguration.Builder(setOf(R.id.homeFragment)).setDrawerLayout(drawer_layout)
                .build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> hideNav()
                R.id.loginFragment -> hideNav()
                R.id.registerFragment -> hideNav()
                R.id.forgotPasswordFragment -> hideNav()
                else -> hideNav(false)
            }
        }


        //TODO: Setup AppCenter for app
//       AppCenter.start(
//            application, "",
//            Analytics::class.java, Crashes::class.java
//        )
    }

    private fun hideNav(hide: Boolean = true) {
        if (hide) {
            drawer_layout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            toolbar.visibility = View.GONE
            nav_view.visibility = View.GONE
        } else {
            drawer_layout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            toolbar.visibility = View.VISIBLE
            nav_view.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment),
            drawer_layout
        )
    }

    // TODO Move these to the right fragment to fix navigation crash
    fun onForgotPassword(view: View) =
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)

    fun onCreateAccount(view: View) =
        navController.navigate(R.id.action_loginFragment_to_registerFragment)

    fun onBackToLogin(view: View) = navController.popBackStack()

    fun onLogout(item: MenuItem) {
        pref.sharedPref.edit().putString("authToken", "").apply()
        pref.sharedPref.edit().putInt("userId", -1).apply()
        navController.popBackStack(R.id.homeFragment, true)
        navController.navigate(R.id.loginFragment)
    }

}
