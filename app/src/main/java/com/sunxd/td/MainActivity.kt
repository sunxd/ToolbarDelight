package com.sunxd.td


import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.sunxd.td.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val toolbarElevation = 5.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFullScreen(this)
        setContentView(R.layout.activity_main)
        
        
        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }


                val scale = 1 + verticalOffset / scrollRange.toFloat()

                toolbarArcBackground.setScale(scale)

                if (scale <= 0) {
                    appbarLayout.elevation = toolbarElevation
                } else {
                    appbarLayout.elevation = 0f
                }

            }
        })

        (findViewById<ImageView>(R.id.ivWalkingMan).drawable as AnimationDrawable).start()
    }



    fun setWindowFullScreen(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //or View.SYSTEM_UI_FLAG_LOW_PROFILE
                        //or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                        //or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                activity.window.statusBarColor = ContextCompat.getColor(this, R.color.translucent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
