package com.example.ochev.ml

import android.content.Context
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.call
import ml.dmlc.xgboost4j.java.Booster
import ml.dmlc.xgboost4j.java.DMatrix
import ml.dmlc.xgboost4j.java.XGBoost
import java.io.File
import java.io.FileInputStream
import java.lang.IllegalStateException
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Classifier(private val context: Context){
    private var booster : Booster? = null
    var isInitialized = false
        private set

    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    fun initialize() : Task<Void> {
        return call(
            executorService,
            Callable {
                initializeBooster()
                null
            }
        )
    }

    private fun initializeBooster() {
        val file = File(context.getExternalFilesDir(null), MODEL_FILE)
        val inputStream = FileInputStream(file)
        booster = XGBoost.loadModel(inputStream)
    }

    fun classify (stroke : Stroke){

        if (!isInitialized) {
            throw IllegalStateException("Booster is not initialized yet.")
        }

        val elapsedTime: Long
        val startTime: Long = System.nanoTime()

        val mat = DMatrix(stroke.toFloatArray(), 1, POINTS_IN_STROKE)
        val predicted = booster?.predict(mat)
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")
        Log.d(TAG, "Prediction results : $predicted")
//        return predicted?.get(0)?.get(0)!!
    }

    companion object {
        private const val MODEL_FILE = "model.bst"
        private const val POINTS_IN_STROKE = 200
        private const val TAG = "StrokeClassifier"
    }
}