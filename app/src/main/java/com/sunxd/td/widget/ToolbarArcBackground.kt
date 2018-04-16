package com.sunxd.td.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.sunxd.td.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * <br/>
 * sunxd<br/>
 * sunxd14@gmail.com<br/>
 * 2018/4/12 下午4:32<br/>
 */
class ToolbarArcBackground(context: Context, attrs: AttributeSet?): View(context, attrs) {

    var disposable: Disposable? = null

    private var scale = 1F
    private val arcSize = 100
    private var timeScale = 0F
    private var isNight = false
    private val extendOverBoundary = 96
    private val colourEvaluator = ArgbEvaluator()

    private var cloud1X = 100
    private var cloud1Y = 300
    private var cloud1OffsetX = 100
    private var cloud1OffsetY = 10

    private var cloud2X = 450
    private var cloud2Y = 200
    private var cloud2OffsetX = 300
    private var cloud2OffsetY = 10

    private var cloud3X = 900
    private var cloud3Y = 250
    private var cloud3OffsetX = 200
    private var cloud3OffsetY = 300

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val ovalPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.content_background)
    }

    private val alphaPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }


    private val cloud1Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_cloud_01)
    }

    private val cloud2Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_cloud_02)
    }

    private val cloud3Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_cloud_03)
    }

    private val starBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_stars)
    }


    var sunOffsetX: Int = 0
    private val sunMorningBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_sun_morning)
    }
    private val sunNoonBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_sun_noon)
    }
    private val sunEveningBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_sun_evening)
    }
    private val moonBitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_moon)
    }


    init {

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sunOffsetX = sunMorningBitmap.width
        disposable = Observable.interval(1000, 20, TimeUnit.MILLISECONDS)
                .subscribe {
                    val num = it.toInt() / 1500
                    val mod = it % 1500
                    val isNight = (num % 2 == 1)
                    setTimeScale(isNight, mod/1500F)
                    //Log.d("TAG", "=========$mod")
                }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable?.dispose()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //调整参数，以屏幕宽高为基础
        cloud1X = w / 10
        cloud1Y = h / 10 * 6
        cloud1OffsetX = cloud1X * 2

        cloud2X = cloud1X * 4
        cloud2Y = h / 10 * 4
        cloud2OffsetX = cloud1X * 3

        cloud3X = cloud1X * 8
        cloud3Y = h/10 * 5
        cloud3OffsetX = cloud1X * 2
        cloud3OffsetY = h/10 * 6
    }

    fun setScale(scale: Float) {
        this.scale = if (scale < 0) {
            0f
        } else {
            scale
        }

        invalidate()
    }

    fun setTimeScale(isNight: Boolean, timeScale: Float) {
        this.timeScale  = timeScale.coerceIn(0f, 1f)

        this.isNight = isNight
        //invalidate()
        postInvalidate()
    }

    private fun calculateColour1(): Int {
        return colourEvaluator.evaluate(scale,
                ContextCompat.getColor(context, R.color.toolbar_gradient_1_noon),
                calculateColour1Base()) as Int

    }

    private fun calculateColour1Base(): Int {

        val interpolatedScale = interpolate(timeScale)

        return if (isNight) {
            when (interpolatedScale) {
                in 0.0f..0.25f ->
                    colourEvaluator.evaluate(interpolatedScale * 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_evening),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_midnight)) as Int
                in 0.25f..0.5f ->
                    colourEvaluator.evaluate((interpolatedScale -0.25f)* 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_midnight),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_midnight)) as Int
                else -> colourEvaluator.evaluate((interpolatedScale - 0.5f) * 2,
                        ContextCompat.getColor(context, R.color.toolbar_gradient_1_midnight),
                        ContextCompat.getColor(context, R.color.toolbar_gradient_1_morning)) as Int
            }
        } else {
            when (interpolatedScale) {
                in 0.0f..0.5f ->
                    colourEvaluator.evaluate(interpolatedScale * 2,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_morning),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_noon)) as Int
                in 0.5f..0.75f ->
                    colourEvaluator.evaluate((interpolatedScale - 0.5f) * 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_noon),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_1_noon_evening)) as Int
                else -> colourEvaluator.evaluate((interpolatedScale - 0.75f) * 4,
                        ContextCompat.getColor(context, R.color.toolbar_gradient_1_noon_evening),
                        ContextCompat.getColor(context, R.color.toolbar_gradient_1_evening)) as Int
            }
        }
    }


    private fun calculateColour2(): Int {
        return colourEvaluator.evaluate(scale,
                ContextCompat.getColor(context, R.color.toolbar_gradient_2_noon),
                calculateColour2Base()) as Int

    }

    private fun calculateColour2Base(): Int {

        val interpolatedScale = interpolate(timeScale)

        return if (isNight) {
            when (interpolatedScale) {
                in 0.0f..0.25f ->
                    colourEvaluator.evaluate(interpolatedScale * 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_evening),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_midnight)) as Int
                in 0.25f..0.5f ->
                    colourEvaluator.evaluate((interpolatedScale -0.25f)* 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_midnight),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_midnight)) as Int
                else -> colourEvaluator.evaluate((interpolatedScale - 0.5f) * 2,
                        ContextCompat.getColor(context, R.color.toolbar_gradient_2_midnight),
                        ContextCompat.getColor(context, R.color.toolbar_gradient_2_morning)) as Int
            }
        } else {
            when (interpolatedScale) {
                in 0.0f..0.5f ->
                    colourEvaluator.evaluate(interpolatedScale * 2,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_morning),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_noon)) as Int
                in 0.5f..0.75f ->
                    colourEvaluator.evaluate((interpolatedScale - 0.5f) * 4,
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_noon),
                            ContextCompat.getColor(context, R.color.toolbar_gradient_2_noon_evening)) as Int
                else -> colourEvaluator.evaluate((interpolatedScale - 0.75f) * 4,
                        ContextCompat.getColor(context, R.color.toolbar_gradient_2_noon_evening),
                        ContextCompat.getColor(context, R.color.toolbar_gradient_2_evening)) as Int
            }
        }
    }

    private fun interpolate(timeScale: Float): Float {
        /*return when(timeScale) {
            in 0.0f..0.25f ->  (timeScale + 1) * timeScale
            in 0.75f..1f ->  (timeScale + 1) * timeScale
            else -> timeScale
        }*/
        /*var y :Float
        if(timeScale <= 0.5) {
            y = 2 * Math.pow((timeScale - 0.5f).toDouble(), 2.toDouble()).toFloat() + 0.5f
        }else {
            y = 0.5f - 2 * Math.pow((timeScale - 0.5f).toDouble(), 2.toDouble()).toFloat()
        }
        return y*/
        return timeScale
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //背景
        paint.shader = gradientShader()
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)

        //星星
        canvas.drawBitmap(starBitmap,
                0f,
                0f,
                alphaPaint)
        alphaPaint.alpha = getStarAlpha()


        //白云1
        canvas.drawBitmap(cloud1Bitmap,
                cloud1X - cloud1OffsetX * (1 - scale),
                cloud1Y + cloud1OffsetY * (1 - scale),
                null)
        //白云2
        canvas.drawBitmap(cloud2Bitmap,
                cloud2X - cloud2OffsetX * (1 - scale),
                cloud2Y + cloud2OffsetY * (1 - scale),
                null)
        //白云3
        canvas.drawBitmap(cloud3Bitmap,
                cloud3X + cloud3OffsetX * (1 - scale),
                cloud3Y + cloud3OffsetY * (1 - scale),
                null)

        //太阳 月亮
        var left = -sunOffsetX/5*3 + (width + sunOffsetX/2) * timeScale
        canvas.drawBitmap(sunOrMoon(),
                -sunOffsetX/5*3 + (width + sunOffsetX/2) * timeScale,
                getSunTop(left),
                null)

        //椭圆
        canvas.drawOval((-extendOverBoundary).toFloat(),
                height - arcSize * scale,
                (width + extendOverBoundary).toFloat(),
                height + arcSize * scale,
                ovalPaint)
    }

    private fun getStarAlpha(): Int {
        val dlt = .6F
        var alpha: Float
        if(isNight) {
            if(timeScale > dlt) {
                alpha = 1.toFloat() - (timeScale - dlt) * 2.5F
            }else {
                alpha = (1 - dlt) + timeScale
            }
        }else {
            if(timeScale > dlt) {
                alpha = timeScale - dlt
            }else {
                alpha = 0F
            }
        }
        return (alpha.coerceIn(0f, 1f) * 255).toInt()
    }

    private fun sunOrMoon(): Bitmap? {
        return if(isNight) {
             moonBitmap
        }else {
            when(timeScale) {
                in 0.0f..0.25f ->  sunMorningBitmap
                in 0.25f..0.75f ->  sunNoonBitmap
                else ->  sunEveningBitmap
            }
        }
    }

    private fun gradientShader(): LinearGradient {
        return LinearGradient(0f, 0f, scale * width, scale * height,
                calculateColour1(), calculateColour2(), Shader.TileMode.CLAMP)
    }


    private fun getSunTop(x: Float): Float {
        var w = width - sunOffsetX
        var y: Float
        if(x < (w/2 - sunOffsetX/4)) {
            y = -x/4
        }else if(x > (w/2 + sunOffsetX/4)){
            y = (x-w/2)/4 - (w/2/4)
        }else{
            y = -((w/2 - sunOffsetX/4)/4).toFloat()
        }
        return y - sunOffsetX/10
    }
}