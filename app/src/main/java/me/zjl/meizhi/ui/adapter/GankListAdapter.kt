package me.zjl.meizhi.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.zjl.meizhi.R
import me.zjl.meizhi.data.entity.Gank
import me.zjl.meizhi.ui.WebActivity
import me.zjl.meizhi.util.L
import me.zjl.meizhi.util.StringStyles
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by chang on 2017-08-27.
 */
class GankListAdapter(val gankList: List<Gank>) : AnimRecyclerViewAdapter<GankListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_gank, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gank = gankList[position]

        if (position == 0) {
            showCategory(holder)
        } else {
            val theLastCategoryEqualsToThis = gankList[position - 1].type == gankList[position].type

            if (!theLastCategoryEqualsToThis) {
                showCategory(holder)
            } else {
                hideCategory(holder)
            }
        }

        holder.category.text = gank.type

        val builder = SpannableStringBuilder(gank.desc).append(
                StringStyles.format(holder.gank.context, "(via ${gank.who})", R.style.ViaTextAppearance))
        val gankText = builder.subSequence(0, builder.length)

        holder.gank.text = gankText
        showItemAnim(holder.gank, position)

    }

    override fun getItemCount(): Int {
        return gankList.count()
    }


    private fun showCategory(holder: ViewHolder) {
        if (!holder.category.isVisible()) {
            holder.category.visibility = View.VISIBLE
        }
    }

    private fun hideCategory(holder: ViewHolder) {
        if (holder.category.isVisible()) {
            holder.category.visibility = View.GONE
        }
    }

    private fun View.isVisible(): Boolean {
        return this.visibility == View.VISIBLE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var category: TextView = itemView.find(R.id.category)
        var gank: TextView = itemView.find(R.id.title)

        init {
            val gankLayout: View = itemView.find(R.id.gank_layout)
            gankLayout.onClick { v ->
                val gank = gankList[layoutPosition]
                val intent = WebActivity.newIntent(v!!.context, gank.url!!, gank.desc!!)
                v.context.startActivity(intent)
            }
        }
    }
}