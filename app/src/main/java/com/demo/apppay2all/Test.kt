package com.demo.apppay2all

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlin.concurrent.fixedRateTimer

class Test : AppCompatActivity() {

    lateinit var iv_test : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        iv_test=findViewById(R.id.iv_test)

        startAngleChangeJob()
    }

    private fun startAngleChangeJob() {
        var shape = iv_test.background
        var borderGrad = shape as GradientDrawable

        var timer = fixedRateTimer("colorTimer", false, 0L, 100) {
            var ori = borderGrad.orientation.ordinal
            var newOri = (ori + 1) % 7

            this@Test.runOnUiThread {
                borderGrad.orientation = GradientDrawable.Orientation.values()[newOri]
            }
        }
    }
}