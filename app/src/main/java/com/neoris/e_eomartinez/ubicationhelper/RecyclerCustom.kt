package com.neoris.e_eomartinez.ubicationhelper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.row_time_line.view.*

class RecyclerCustom(private val list: List<String>, private val mListener: InterfaceListener):
        RecyclerView.Adapter<RecyclerCustom.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_time_line, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.setText(list[position])
        when{
            list[position].equals("Bus Station") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_bus)
            list[position].equals("Coffee Shop") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_cafe)
            list[position].equals("Car Wash") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_car)
            list[position].equals("Church") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_church)
            list[position].equals("Park") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_park)
            list[position].equals("Restaurant") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_restaurant)
            list[position].equals("Shop") -> holder.itemView?.img1?.setImageResource(R.drawable.ic_shop)
        }
    }


    class ViewHolder(itemView: View, mListener: InterfaceListener): RecyclerView.ViewHolder(itemView){

        val img1: ImageView = itemView.img1
        val textView: TextView = itemView.textView

        init {
            itemView.setOnClickListener {
                mListener.click(adapterPosition)
            }
        }

    }
}