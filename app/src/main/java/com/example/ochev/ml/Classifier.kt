package com.example.ochev.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.call
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class Classifier(val context: Context){
    private var interpreter : Interpreter? = null
    var isInitialized = false
        private set

    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private var inputImageWidth: Int = 0 // will be inferred from TF Lite model
    private var inputImageHeight: Int = 0 // will be inferred from TF Lite model
    private var modelInputSize: Int = 0 // will be inferred from TF Lite model

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
        // Initialize TF Lite Interpreter with NNAPI enabled
        val options = Interpreter.Options()
        options.setUseNNAPI(true)

        val modelFile = Utils.assetFile(context, MODEL_FILE)
        modelFile ?: throw FileNotFoundException("Could not load model")

        interpreter = Interpreter(modelFile, options)

        // Read input shape from model file
        val inputShape = interpreter!!.getInputTensor(0).shape()
        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]

        Log.d("Classify", inputShape.contentToString())

        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE

        isInitialized = true
    }

    private fun classify(bitmap: Bitmap): String {
        if (!isInitialized) {
            throw IllegalStateException("TF Lite Interpreter is not initialized yet.")
        }

        var startTime: Long
        var elapsedTime: Long

        // Preprocessing: resize the input
        startTime = System.nanoTime()
        val resizedImage = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
        val byteBuffer = convertBitmapToByteBuffer(resizedImage)
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Preprocessing time = " + elapsedTime + "ms")

        startTime = System.nanoTime()
        val result = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }
        interpreter?.run(byteBuffer, result)
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")

        return getOutputString(result[0])
    }

    fun classifyAsync(bitmap: Bitmap): Task<String> {
        return call(executorService, Callable<String> { classify(bitmap) })
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(3*modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        Log.d("Classify", "cfg: ${bitmap.config}")

        for (pixelValue in pixels) {
            val alpha = (pixelValue shr 24 and 0xFF)

            val normalizedPixelValue = alpha / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
            byteBuffer.putFloat(normalizedPixelValue)
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    private fun getOutputString(output: FloatArray): String {
        val maxIndex = output.indices.maxBy { output[it] } ?: -1
//        return "Prediction Result: %d\nConfidence: %2f".format(maxIndex, output[maxIndex])
        return "Prediction Result: ${output.contentToString()}"
    }

    companion object {
        private const val MODEL_FILE = "model.tflite"

        private const val TAG = "StrokeClassifier"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1

        private const val OUTPUT_CLASSES_COUNT = 2
    }
}