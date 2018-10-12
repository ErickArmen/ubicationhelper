package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.e_eomartinez.ubicationhelper.R
import kotlinx.android.synthetic.main.row_travels.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerTravels(private val mListener: InterfaceListener):
        RecyclerView.Adapter<RecyclerTravels.ViewHolder>() {

    private val list = mutableListOf<String>()
    private val sdf = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_travels, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): String = list[position]

    fun addItem(element: String) = list.add(element)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatedDate = sdf.format(Date(list[position].toLong()))
        holder.tv_hour.text = formatedDate.slice(0..4)
        holder.tv_day.text = formatedDate.slice(5..16)
    }


    class ViewHolder(itemView: View, mListener: InterfaceListener): RecyclerView.ViewHolder(itemView){

        val tv_hour: TextView = itemView.tv_work_done_hour
        val tv_day: TextView = itemView.tv_work_done_day

        init {
            itemView.setOnClickListener {
                mListener.click(adapterPosition)
            }
        }

    }
}