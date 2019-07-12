package com.kainalu.readerforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.network.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.get().inject(this)
        GlobalScope.launch {
            apiService.getSubreddit()
            //val result = apiService.getComments("nba", "c8p4d9")
            //Log.d("WOO", result.toString())
        }
    }
}
