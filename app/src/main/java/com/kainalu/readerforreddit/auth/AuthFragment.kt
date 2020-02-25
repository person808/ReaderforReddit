package com.kainalu.readerforreddit.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kainalu.readerforreddit.BuildConfig
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.databinding.FragmentAuthBinding
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.util.viewBinding
import javax.inject.Inject

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request?.url?.host == BuildConfig.REDIRECT_HOST) {
                viewModel.handleCallback(request.url)
                return true
            }

            if (request?.url?.host == "www.reddit.com") {
                return false
            }

            // I don't want to handle this link, so launch another Activity that handles URLs
            with(Intent(Intent.ACTION_VIEW, request?.url)) {
                startActivity(this)
            }
            return true
        }
    }

    private val binding by viewBinding(FragmentAuthBinding::bind)
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.apply {
            webViewClient = MyWebViewClient()
            loadUrl("https://www.reddit.com/api/v1/authorize.compact?client_id=${BuildConfig.CLIENT_ID}&response_type=code&state=testing&redirect_uri=${BuildConfig.REDIRECT_URI}&duration=permanent&scope=*")
        }
        viewModel.navigationLiveData.observe(viewLifecycleOwner, Observer { event ->
            if (event.getContentIfNotHandled() == AuthViewModel.NavigationEvent.POP_TO_FEED) {
                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToFeedFragment())
            }
        })
    }

}