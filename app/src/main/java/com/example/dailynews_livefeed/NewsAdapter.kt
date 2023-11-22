package com.example.dailynews_livefeed

// NewsAdapter.kt
import News
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var newsList: MutableList<News> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.bind(currentNews)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setNewsList(newsList: List<News>) {
        this.newsList.clear()
        this.newsList.addAll(newsList)
        notifyDataSetChanged()
    }

    fun sortByOldToNew() {
        newsList.sortBy { it.publishedAt }
        notifyDataSetChanged()
    }

    fun sortByNewToOld() {
        newsList.sortByDescending { it.publishedAt }
        notifyDataSetChanged()
    }
    fun filter(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            // If the query is empty or null, show the original list
            newsList
        } else {
            // Filter the list based on the query
            newsList.filter { it.headline.contains(query, ignoreCase = true) }
        }

        setNewsList(filteredList)
    }


    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headlineTextView: TextView = itemView.findViewById(R.id.headlineTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val publishedAtTextView: TextView = itemView.findViewById(R.id.publishedAtTextView)

        fun bind(news: News) {
            headlineTextView.text = news.headline
            publishedAtTextView.text = news.publishedAt

            // Use Picasso for image loading without placeholder or error handling
            Picasso.get().load(news.urlToImage).into(imageView)

            // Set click listener to open the article in a browser
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                itemView.context.startActivity(intent)
            }
        }
    }
}
