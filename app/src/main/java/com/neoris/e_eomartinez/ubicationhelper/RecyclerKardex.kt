package com.neoris.e_eomartinez.ubicationhelper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.row_kardex.view.*

class RecyclerKardex(private val list: List<Kardex>, private val mListener: InterfaceListener):
        RecyclerView.Adapter<RecyclerKardex.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_kardex, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(position){
            0 -> holder.line_top.visibility = View.INVISIBLE
            (list.size - 1) -> {
                holder.line_bottom.visibility = View.INVISIBLE
                if (!list[position].name.equals("Completed"))
                    holder.img_state_event.setImageResource(R.mipmap.ic_green_point)
            }
        }

        holder.tv_name.text = list[position].name
        holder.tv_date.text = list[position].date
    }


    class ViewHolder(itemView: View, mListener: InterfaceListener): RecyclerView.ViewHolder(itemView){

        val tv_name: TextView = itemView.tv_name_kardex
        val tv_date: TextView = itemView.tv_date_kardex
        val img_state_event: ImageView = itemView.img_state_event
        val line_top: View = itemView.line1
        val line_bottom: View = itemView.line2

        init {
            itemView.setOnClickListener {
                mListener.click(adapterPosition)
            }
        }

    }
}