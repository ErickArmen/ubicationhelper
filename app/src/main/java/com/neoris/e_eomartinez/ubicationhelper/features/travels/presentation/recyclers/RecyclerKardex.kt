package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import android.widget.ImageView
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.models.Kardex
import kotlinx.android.synthetic.main.row_kardex.view.*

class RecyclerKardex(private val mListener: InterfaceListener):
        RecyclerView.Adapter<RecyclerKardex.ViewHolder>() {

    private val list = mutableListOf<Kardex>()
    private val completedString = "Completed"
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_kardex, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int = list.size

    fun addItem(element: Kardex, position: Int = itemCount) = list.add(position, element)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(position){
            0 -> holder.line_top.visibility = View.INVISIBLE
            (list.size - 1) -> {
                if (!list[position].name.equals(completedString)){
                    mRecyclerView?.findViewHolderForAdapterPosition(position-1)
                            ?.itemView?.img_state_event
                            ?.setImageResource(R.mipmap.ic_green_accept)
                    holder.img_state_event.setImageResource(R.mipmap.ic_green_point)
                }
                else {
                    mRecyclerView?.findViewHolderForAdapterPosition(position-1)
                            ?.itemView?.img_state_event
                            ?.setImageResource(R.mipmap.ic_green_accept)
                    holder.line_bottom.visibility = View.INVISIBLE
                }
            }
        }

        holder.tv_name.text = list[position].name
        holder.tv_date.text = list[position].date
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
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