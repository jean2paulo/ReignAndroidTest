package com.jeanpaulo.reignandroidtest.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.databinding.FragHitDetailBinding
import retrofit2.http.Url
import java.lang.ClassCastException

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

        viewDataBinding.webview.webViewClient = WebViewClient()
        viewDataBinding.webview.settings.javaScriptEnabled = true
        viewDataBinding.webview.loadUrl(args.url)

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


}

interface HitDetailFragmentListener {
    fun setTitle(title: String)
    fun setHomeUpAsEnable(boolean: Boolean)
}