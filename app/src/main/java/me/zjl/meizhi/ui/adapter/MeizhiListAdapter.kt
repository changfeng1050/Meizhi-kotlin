package me.zjl.meizhi.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import me.zjl.meizhi.R
import me.zjl.meizhi.data.entity.Meizhi
import me.zjl.meizhi.func.OnMeizhiTouchListener
import me.zjl.meizhi.util.L
import me.zjl.meizhi.widget.RatioImageView
import org.jetbrains.anko.find

/**
 * Created by chang on 2017-08-20.
 */
class MeizhiListAdapter(val context: Context, val list: List<Meizhi>) : RecyclerView.Adapter<MeizhiListAdapter.ViewHolder>() {

    companion object {
        val TAG: String = L.makeLogTag(MeizhiListAdapter::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_meizhi, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meizhi = list[position]
        val limit = 48

        val text = if (meizhi.desc!!.length > limit) {
            meizhi.desc!!.substring(0, limit) + "   "
        } else {
            meizhi.desc!!
        }

        holder.meizhi = meizhi
        holder.titleView.text = text
        holder.card.tag = meizhi.desc

        Glide.with(context)
                .load(meizhi.url)
                .centerCrop()
                .into(holder.meizhiView)
                .getSize { _, _ ->
                    if (!holder.card.isShown) {
                        holder.card.visibility = View.VISIBLE
                    }
                }
    }

    override fun onViewRecycled(holder: ViewHolder?) {
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    var onMeizhiTouchListener: OnMeizhiTouchListener? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var card: View = itemView
        var meizhiView: RatioImageView = itemView.find(R.id.ratio_image_view_meizhi)
        var titleView: TextView = itemView.find(R.id.text_view_title)
        lateinit var meizhi: Meizhi

        init {
            meizhiView.setOnClickListener(this)
            card.setOnClickListener(this)
            meizhiView.setOriginalSize(50, 50)

        }

        override fun onClick(v: View) {
            L.i(TAG, "onClick() view:$v")
            onMeizhiTouchListener!!.onTouch(v, meizhiView, card, meizhi)
        }
    }
}