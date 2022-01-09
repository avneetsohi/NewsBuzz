package com.example.newsadda

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {
    lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.recyclerview)
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchdata()
        mAdapter= NewsListAdapter(this)
        recyclerView.adapter=mAdapter

    }
    private fun fetchdata()
    {
        val url="https://newsapi.org/v2/top-headlines?country=in&apiKey=022059717208414d81a992fb1636131c"
        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url,null,
                {
                    val newsJsonArray=it.getJSONArray("articles")
                    val newsArray= ArrayList<News>()
                    for(i in 0 until newsJsonArray.length())
                    {val newsJsonObject=newsJsonArray.getJSONObject(i)
                        val news=News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener {
                    Toast.makeText(this,"Error Fetching News",Toast.LENGTH_LONG).show()
                }

        )
        { override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
        }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    override fun OnNewsItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }

}



