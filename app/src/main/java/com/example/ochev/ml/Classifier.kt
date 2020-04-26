package com.example.ochev.ml

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.call
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Classifier(private val context: Context){
    private var module : Module? = null
    var isInitialized = false
        private set

    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    fun initialize() : Task<Void> {
        return call(
            executorService,
            Callable<Void> {
                initializeBooster()
                null
            }
        )
    }

    private fun initializeBooster() {
        module = Module.load(Utils.assetFilePath(context, MODEL_FILE))
        isInitialized = true
    }

    fun classify (stroke : Stroke){

        if (!isInitialized) {
            throw IllegalStateException("Classifier is not initialized yet.")
        }

        val elapsedTime: Long
        val startTime: Long = System.nanoTime()

        val bitmap = BitmapFactory.decodeStream(context.assets.open("image.jpg"))

        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            bitmap,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB
        )

        Log.d(TAG, "Model : $module")
        val outputTensor = module!!.forward(IValue.from(inputTensor))?.toTensor()
        val scores = outputTensor?.dataAsFloatArray

        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")
        Log.d(TAG, "Prediction results : $scores")
//        return predicted?.get(0)?.get(0)!!
    }

    companion object {
        private const val MODEL_FILE = "model.pt"
        private const val POINTS_IN_STROKE = 200
        private const val TAG = "StrokeClassifier"
    }
}