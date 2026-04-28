package com.example.booksport

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.booksport.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate logo fade + scale in
        binding.ivSplashLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setStartDelay(200)
            .start()

        binding.tvSplashName.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(600)
            .start()

        binding.tvSplashTagline.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(800)
            .withEndAction {
                // After animations, go to Login
                android.os.Handler(mainLooper).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }, 600)
            }
            .start()

        // Set initial translation for slide-up effect
        binding.tvSplashName.translationY = 30f
        binding.tvSplashTagline.translationY = 30f
    }
}
