package com.jeanpaulo.reignandroidtest.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.util.getViewModelFactory
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.databinding.FragMainListBinding
import com.jeanpaulo.reignandroidtest.datasource.local.util.DataSourceException
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.view.adapter.HitListAdapter
import com.jeanpaulo.reignandroidtest.view.util.CustomLinearLayoutManager
import com.jeanpaulo.reignandroidtest.view.util.setupRefreshLayout
import com.jeanpaulo.reignandroidtest.viewmodel.HitViewModel
import java.lang.ClassCastException

class HitListFragment : Fragment() {

    private val viewModel: HitViewModel by viewModels<HitViewModel> { getViewModelFactory() }
    private lateinit var viewBinding: FragMainListBinding

    private lateinit var hitListAdapter: HitListAdapter

    lateinit var listener: HitListFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragMainListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewBinding.root
    }


    //MENU FUNCTIONS

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val hit = hitListAdapter.getItemSelected() ?: return false

        when (item.itemId) {
            R.id.context_action_delete -> hit.localId?.let { viewModel.delete(hit) }
        }
        return super.onContextItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as HitListFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context} must implement ${HitListFragmentListener::class.java}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        setupRefreshLayout(viewBinding.refreshLayout, viewBinding.hitList)

        setupToolbar()
        setupListAdapter()
        setupSnackBar()
        setObservables()

        viewModel.init()
    }

    fun setupToolbar() {
        listener.setHomeUpAsEnable(false)
    }

    fun setupListAdapter() {
        val viewModel = viewBinding.viewmodel
        if (viewModel != null) {

            hitListAdapter =
                HitListAdapter(
                    viewModel
                ) { it ->
                    openHit(it.formatedTitle, it.storyUrl!!)
                }
            viewBinding.hitList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.hitList.adapter = hitListAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            viewBinding.hitList.addItemDecoration(itemDecorator);

        } else {
            throw Exception("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupSnackBar() {
        //view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    fun setObservables() {
        viewModel.hitList?.observe(viewLifecycleOwner, Observer { it: PagedList<Hit> ->
            hitListAdapter.submitList(it)
            //hitListAdapter.notifyDataSetChanged()
        })

        viewModel.errorLoading.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                viewBinding.txtError.visibility = View.VISIBLE
                viewBinding.txtError.text = showException(exception)
            } else
                viewBinding.txtError.visibility = View.GONE
        })

        //view components

        viewBinding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun showException(exception: DataSourceException): String {
        return when (exception.knownNetworkError) {
            DataSourceException.Error.NO_INTERNET_EXCEPTION ->
                getString(R.string.no_internet_connection)
            DataSourceException.Error.TIMEOUT_EXCEPTION ->
                getString(R.string.loading_hit_error)

            else -> {
                exception.toString()
            }
        }
    }

    private fun openHit(formatedTitle: String, url: String) {
        val action = HitListFragmentDirections
            .actionHitListFragmentToHitDetailFragment(
                formatedTitle,
                url
            )
        findNavController().navigate(action)
    }
}

interface HitListFragmentListener {
    fun setTitle(title: String)
    fun setHomeUpAsEnable(boolean: Boolean)
}