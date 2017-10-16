package io.github.jbarr21.appsbyregex.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.github.jbarr21.appsbyregex.R
import io.github.jbarr21.appsbyregex.data.App
import io.github.jbarr21.appsbyregex.ui.util.resolveDrawableId

class AppAdapter(val items: MutableList<App>, private val appClickedListener: OnAppItemClicked) : RecyclerView .Adapter<AppAdapter.AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        view.setBackgroundResource(view.context.resolveDrawableId(R.attr.selectableItemBackground))
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = items[position]
        holder.icon.setImageDrawable(app.icon)
        holder.name.text = app.name
        holder.itemView.setOnClickListener { appClickedListener.onAppClicked(app) }
    }

    override fun getItemCount() = items.size

    fun setItems(items: Collection<App>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView by lazy { itemView.findViewById<ImageView>(R.id.icon) }
        val name: TextView by lazy { itemView.findViewById<TextView>(R.id.name) }
    }
}

interface OnAppItemClicked {
    fun onAppClicked(app: App)
}
