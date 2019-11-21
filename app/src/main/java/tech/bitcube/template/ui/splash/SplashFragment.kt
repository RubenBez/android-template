package tech.bitcube.template.ui.splash


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import tech.bitcube.template.BuildConfig
import tech.bitcube.template.R

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    val splashTime = if (BuildConfig.BUILD_TYPE.equals("debug")) 1500L else 3000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.splash_fragment, container, false)

        Handler().postDelayed({ findNavController().navigate(R.id.action_splashFragment_to_loginFragment)}, splashTime)
        return view
    }


}
