package com.jeanpaulo.reignandroidtest.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.databinding.FragHitDetailBinding

class HitDetailFragment: Fragment() {

    private lateinit var viewDataBinding: FragHitDetailBinding

    //private val args: HitDetailFragmentArgs by navArgs()

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
}