package com.jeanpaulo.reignandroidtest.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.databinding.FragHitDetailBinding


class HitDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragHitDetailBinding

    private val args: HitDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_hit_detail, container, false)
        viewDataBinding = FragHitDetailBinding.bind(root)
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWebView(args.url!!)
        setupToolbar()
    }

    lateinit var listener: HitDetailFragmentListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as HitDetailFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context} must implement ${HitDetailFragmentListener::class.java}")
        }
    }

    fun setupToolbar() {
        listener.setTitle(args.title)
        listener.setHomeUpAsEnable(true)
    }

    fun setupWebView(url: String){
        viewDataBinding.webview.webViewClient = WebViewClient()
        viewDataBinding.webview.settings.javaScriptEnabled = true
        viewDataBinding.webview.loadUrl(url)
        //to show progress before load page
        // ref: https://stackoverflow.com/questions/3149216/how-to-listen-for-a-webview-finishing-loading-a-url
        viewDataBinding.webview.webViewClient = object : WebViewClient() {
            var loadingFinished = true
            var redirect = false

            override fun shouldOverrideUrlLoading(webView: WebView?, urlNewString: String?): Boolean {

                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                webView?.let { webView -> webView.loadUrl(urlNewString) }
                return true
            }

            fun onPageStarted(view: WebView?, url: String?) {
                loadingFinished = false
                //viewDataBinding.loading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (!redirect) {
                    loadingFinished = true
                    //viewDataBinding.loading.visibility = View.GONE
                } else {
                    redirect = false
                }
            }
        /*override fun onPageFinished(view: WebView, url: String) {
                viewDataBinding.loading.visibility = View.GONE
            }*/
        }
    }

}

interface HitDetailFragmentListener {
    fun setTitle(title: String)
    fun setHomeUpAsEnable(boolean: Boolean)
}