package com.example.weatherapp

import android.content.Intent
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.mikhaellopez.rxanimation.*
import io.reactivex.rxjava3.disposables.CompositeDisposable


class SplashScreenActivity : AppCompatActivity()  {
    var disposable = CompositeDisposable()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startSplashAnimation()
    }

    private fun startSplashAnimation() {
        disposable.add(
            RxAnimation.sequentially(
                RxAnimation.together(
                    binding.imageViewAtBottom.translationY(500f),
                    binding.imageViewEllipseStructure.fadeOut(0L),
                    binding.imageViewAtBottom.fadeOut(0L),
                    binding.imageViewLargeCloud.translationX(-500F, 0L),
                    binding.imageViewTinyCloud.translationX(500f, 0L),
                    binding.imageViewShadowAtBottom.translationY(500f),
                    binding.imageViewCentralCloud.fadeOut(0L),
                    binding.imageViewShadowAtBottom.fadeOut(0L)
                ),

                RxAnimation.together(
                    binding.imageViewAtBottom.fadeIn(1000L),
                    binding.imageViewAtBottom.translationY(-1f),
                    binding.imageViewShadowAtBottom.fadeIn(1250L),
                    binding.imageViewShadowAtBottom.translationY(-1f)
                ),

                RxAnimation.together(
                    binding.imageViewEllipseStructure.fadeIn(1000L),
                    binding.imageViewEllipseStructure.translationY(-50F, 1000L)
                ),

                RxAnimation.together(
                    binding.imageViewLargeCloud.translationX(-15f, 1000L),
                    binding.imageViewTinyCloud.translationX(25f, 1000L)
                ),

                binding.imageViewCentralCloud.fadeIn(500L),
            ).doOnTerminate {
                endSplashAnimation()
            }
                .subscribe()
        )
    }

    private fun endSplashAnimation() {
        disposable.add(
            RxAnimation.sequentially(
                RxAnimation.together(
                    binding.imageViewAtBottom.fadeOut(300L),
                    binding.imageViewAtBottom.translationY(100f),
                    binding.imageViewShadowAtBottom.fadeOut(300L),
                    binding.imageViewShadowAtBottom.translationY(100f)
                ),

                RxAnimation.together(
                    binding.imageViewEllipseStructure.fadeOut(300L),
                    binding.imageViewEllipseStructure.translationY(500F, 300L)
                ),

                RxAnimation.together(
                    binding.imageViewLargeCloud.translationX(500f, 300L),
                    binding.imageViewTinyCloud.translationX(-500f, 300L)
                ),

                binding.imageViewCentralCloud.fadeOut(300L),
            )
                .doOnTerminate {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .doOnError {}
                .subscribe()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
