package com.example.dailynews_livefeed

// FetchNewsTaskExecutor.kt
import News
import android.os.Handler
//import com.example.dailynews_livefeed.News
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class FetchNewsTaskExecutor(private val newsAdapter: NewsAdapter, private val mainHandler: Handler) {
    private val executor = Executors.newSingleThreadExecutor()

    fun executeTask() {
        executor.execute {
            try {
                val newsList = mutableListOf<News>()

                val url = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = urlConnection.inputStream

                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()

                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                val jsonObject = JSONObject(stringBuilder.toString())
                val articlesArray = jsonObject.getJSONArray("articles")

                for (i in 0 until articlesArray.length()) {
                    val articleObject: JSONObject = articlesArray.getJSONObject(i)
                    val headline = articleObject.getString("title")
                    val url = articleObject.getString("url")
                    val urlToImage = articleObject.getString("urlToImage")
                    val publishedAt = articleObject.getString("publishedAt")

                    newsList.add(News(headline, url, urlToImage, publishedAt))
                }

                bufferedReader.close()
                inputStream.close()
                urlConnection.disconnect()

                // Update UI on the main thread
                mainHandler.post {
                    // Update RecyclerView with news articles
                    newsAdapter.setNewsList(newsList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
