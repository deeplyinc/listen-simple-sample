package com.deeplyinc.listen.samples.simplesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.deeplyinc.listen.sdk.Listen

class MainActivity : AppCompatActivity() {
    companion object {
        private const val N_TEST = 100
        private const val INPUT_AUDIO_SECOND = 60
        private const val WAIT_SECOND = 20L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listen = Listen(this)
        listen.load("", "")

        val runnable = Runnable {
            for (i in 0 until N_TEST) {
                val results = listen.inference(ShortArray(INPUT_AUDIO_SECOND * listen.getAudioParams().sampleRate))
                Log.d("SimpleSample", "$i Results: $results")
                Thread.sleep(WAIT_SECOND * 1000)
            }
        }

        Log.d("SimpleSample", "Start")
        val t = Thread(runnable)
        t.start()
    }
}