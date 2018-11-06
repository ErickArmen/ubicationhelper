package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.neoris.eeomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.inflate
import kotlinx.android.synthetic.main.row_travels.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerTravels:
        PagedListAdapter<String, RecyclerTravels.ViewHolder>(DIFF_CALLBACK) {

    internal var mListener: (item: String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.row_travels))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, mListener) }
    }


    class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView){

        val tvHour: TextView = itemView.tv_work_done_hour
        val tvDay: TextView = itemView.tv_work_done_day
        private val sdf = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())

        fun bind(item: String, mListener: (item: String) -> Unit){

            val formatedDate = sdf.format(item.toLong())
            tvHour.text = formatedDate.slice(0..4)
            tvDay.text = formatedDate.slice(5..16)
            itemView.setOnClickListener {
                mListener(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String?, newItem: String?): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String?, newItem: String?): Boolean =
                    oldItem == newItem
        }
    }
}
