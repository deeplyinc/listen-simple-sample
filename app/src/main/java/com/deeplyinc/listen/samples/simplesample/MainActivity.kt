package com.deeplyinc.listen.samples.simplesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.deeplyinc.listen.sdk.Listen
import kotlinx.coroutines.*
import java.lang.Runnable

class MainActivity : AppCompatActivity() {
    companion object {
        private const val N_TEST = 100
        private const val INPUT_AUDIO_SECOND = 60
        private const val WAIT_SECOND = 20L
    }

    private lateinit var listen: Listen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onStart() {
        super.onStart()

        listen = Listen(this)
        listen.load("", "")

//        withLeak()
//        withoutLeakSingleThread()
        withoutLeakCoroutine()
    }

    private fun withoutLeakCoroutine() {
        val runnableRepeat = Runnable {
            for (i in 0 until N_TEST) {
                val audioBuffer = ShortArray(INPUT_AUDIO_SECOND * listen.getAudioParams().sampleRate)
                CoroutineScope(Dispatchers.Default).launch {
                    analyze(audioBuffer)
                }
                Thread.sleep(WAIT_SECOND * 1000)
            }
        }
        Thread(runnableRepeat).start()
    }

    private suspend fun analyze(audioSamples: ShortArray) {
        withContext(Dispatchers.Default) {
            Log.d("SimpleSample", "analyze() - start")
            val results = listen.inference(audioSamples)
            Log.d("SimpleSample", "analyze() - results: $results")
        }
    }

    private fun withLeak() {
        val runnableAnalyze = Runnable {
            Log.d("SimpleSample", " 분석 전 : ")
            val results = listen.inference(ShortArray(INPUT_AUDIO_SECOND * listen.getAudioParams().sampleRate))
            Log.d("SimpleSample", " 분석 후 : ")
            Log.d("SimpleSample", "Results: $results")
        }
        val runnableRepeat = Runnable {
            for (i in 0 until N_TEST) {
                Thread(runnableAnalyze).start()
                Thread.sleep(WAIT_SECOND * 1000)
            }
        }
        Thread(runnableRepeat).start()
    }


    private fun withoutLeakSingleThread() {
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