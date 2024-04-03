package com.example.healthzensignuplogin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.model.ArticleDto
import com.google.android.material.progressindicator.LinearProgressIndicator
import io.reactivex.schedulers.Schedulers

class NewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsRecyclerAdapter
    private lateinit var progressIndicator: LinearProgressIndicator
    private val articleList = mutableListOf<ArticleDto>()
    private val removedArticleUrls = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView = findViewById(R.id.news_recycler_view)
        progressIndicator = findViewById(R.id.progress_bar)

        setupRecyclerView()
        getNews()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NewsRecyclerAdapter(articleList)
        recyclerView.adapter = adapter
    }

    private fun changeInProgress(show: Boolean) {
        progressIndicator.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    @SuppressLint("CheckResult")
    private fun getNews() {
        changeInProgress(true)

        val newsApiRepository = NewsApiRepository("89030b4bd9d046ea9b7c4fa46158a7e5")

        newsApiRepository.getTopHeadlines(
            category = Category.HEALTH,
            country = Country.US,
            pageSize = 20,
            page = 1
        )
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .toList()
            .subscribe({ articles ->
                runOnUiThread {
                    changeInProgress(false)
                    val filteredArticles = articles.filterNot {removedArticleUrls.contains(it.url)}
                    articleList.clear()
                    articleList.addAll(filteredArticles)
                    adapter.notifyDataSetChanged()
                }
            },
                { t: Throwable ->
                    changeInProgress(false)
                    Log.e("getTopHeadlines error", "Error fetching news", t)
                })
    }
}
