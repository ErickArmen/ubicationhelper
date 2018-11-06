package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import android.view.View
import android.widget.ImageView
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.inflate
import com.neoris.eeomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.models.Kardex
import kotlinx.android.synthetic.main.row_kardex.view.*

class RecyclerKardex(private val mListener: InterfaceListener):
        RecyclerView.Adapter<RecyclerKardex.ViewHolder>() {

    private val list = mutableListOf<Kardex>()
    private val completedString = "Completed"
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.row_kardex), mListener)

    override fun getItemCount(): Int = list.size

    fun addItem(element: Kardex, position: Int = itemCount) = list.add(position, element)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(position){
            0 -> holder.lineTop.visibility = View.INVISIBLE
            (list.size - 1) -> {
                if (!list[position].name.equals(completedString)){
                    mRecyclerView?.findViewHolderForAdapterPosition(position-1)
                            ?.itemView?.img_state_event
                            ?.setImageResource(R.mipmap.ic_green_accept)
                    holder.imgStateEvent.setImageResource(R.mipmap.ic_green_point)
                }
                else {
                    mRecyclerView?.findViewHolderForAdapterPosition(position-1)
                            ?.itemView?.img_state_event
                            ?.setImageResource(R.mipmap.ic_green_accept)
                    holder.lineBottom.visibility = View.INVISIBLE
                }
            }
        }

        holder.tvName.text = list[position].name
        holder.tvDate.text = list[position].date
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }



    class ViewHolder(itemView: View, mListener: InterfaceListener): RecyclerView.ViewHolder(itemView){

        val tvName: TextView = itemView.tv_name_kardex
        val tvDate: TextView = itemView.tv_date_kardex
        val imgStateEvent: ImageView = itemView.img_state_event
        val lineTop: View = itemView.line1
        val lineBottom: View = itemView.line2

        init {
            itemView.setOnClickListener {
                mListener.click(adapterPosition)
            }
        }

    }
}