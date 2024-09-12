package com.example.zipfilemanger.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import com.example.zipfilemanger.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private lateinit var charSequence: CharSequence
    private var index: Int = 0
    private var delay: Long = 200
    val handler = Handler()

    //private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "<font color=#ffffff>Zip </font> <font color=#000000>FileManager</font>"
        binding.loadingText.text = Html.fromHtml(text)

        binding.splashProgress.max = 100
        val currentProgress = 100
        ObjectAnimator.ofInt(binding.splashProgress, "progress", currentProgress).setDuration(4500).start()

        Handler().postDelayed(object : Runnable{
            override fun run() {
                startActivity(
                    Intent(this@SplashActivity, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
        },4000)

    }
}