package com.jeanpaulo.reignandroidtest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.databinding.ItemHitBinding
import com.jeanpaulo.reignandroidtest.datasource.remote.util.NetworkState
import com.jeanpaulo.reignandroidtest.model.Hit
import com.jeanpaulo.reignandroidtest.viewmodel.HitViewModel

class HitListAdapter(
    private val viewModel: HitViewModel,
    private val listener: (Hit) -> Unit
) : PagedListAdapter<Hit, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            val layoutInflater = LayoutInflater.from(parent.getContext())
            val itemBinding: ItemHitBinding =
                ItemHitBinding.inflate(layoutInflater, parent, false)
            MusicViewHolder(itemBinding)
        } else FooterViewHolder.create(
            {
                viewModel.refresh()
            },
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            val music = getItem(position)
            (holder as MusicViewHolder).bind(music, listener)
            //holder.itemView.setOnClickListener({ listenerFun(music) })
        } else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem.objectId == newItem.objectId
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem.objectId == newItem.objectId
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == NetworkState.LOADING || state == NetworkState.ERROR)
    }

    class MusicViewHolder(val binding: ItemHitBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hit: Hit?, listener: (Hit) -> Unit) {
            if (hit != null) {
                binding.hit = hit
                itemView.setOnClickListener { listener(hit) }
            }
        }
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(status: NetworkState?) {
           /* itemView.progress_bar.visibility =
                if (status == NetworkState.LOADING) View.VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility =
                if (status == NetworkState.ERROR) View.VISIBLE else View.INVISIBLE*/
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
                //view.txt_error.setOnClickListener { retry() }
                return FooterViewHolder(
                    view
                )
            }
        }
    }
}