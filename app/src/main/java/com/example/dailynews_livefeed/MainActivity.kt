package com.example.dailynews_livefeed

// MainActivity.kt
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter()
        recyclerView.adapter = newsAdapter

        val mainHandler = Handler(mainLooper)

        // Fetch news articles
        FetchNewsTaskExecutor(newsAdapter, mainHandler).executeTask()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        // Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                newsAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsAdapter.filter(newText)
                return true
            }
        })


        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_old_to_new -> {
                // Handle Old to New sorting
                newsAdapter.sortByOldToNew()
                return true
            }
            R.id.action_sort_new_to_old -> {
                // Handle New to Old sorting
                newsAdapter.sortByNewToOld()
                return true
            }
            R.id.action_search_web->{
                val query = searchView.query.toString()
                if(query.isNotBlank()){
                    openWebSearch(query)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun openWebSearch(query:String){
        val searchUri = Uri.parse("https://www.google.com/search?q=$query")
        val searchIntent = Intent(Intent.ACTION_VIEW, searchUri)
        startActivity(searchIntent)
    }

}
