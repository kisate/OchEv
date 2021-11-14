package com.example.ochev.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.ochev.baseclasses.dataclasses.Stroke
import com.example.ochev.baseclasses.dataclasses.vertexfigures.Vertexes
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.call
import org.tensorflow.lite.Interpreter
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService


class Classifier(private val context: Context) {
    private var interpreter: Interpreter? = null
    var isInitialized = false
        private set

    private var inputImageWidth: Int = 0 // will be inferred from TF Lite model
    private var inputImageHeight: Int = 0 // will be inferred from TF Lite model
    private var modelInputSize: Int = 0 // will be inferred from TF Lite model

    fun initialize(executorService: ExecutorService): Task<Void> {
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

    fun classify(bitmap: Bitmap): Vertexes? {
        if (!isInitialized) {
            throw IllegalStateException("TF Lite Interpreter is not initialized yet.")
        }

        var startTime: Long
        var elapsedTime: Long

        // Preprocessing: resize the input
        startTime = System.nanoTime()
        val byteBuffer = convertBitmapToByteBuffer(processBitmap(bitmap))
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Preprocessing time = " + elapsedTime + "ms")

        startTime = System.nanoTime()
        val result = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }
        interpreter?.run(byteBuffer, result)
        elapsedTime = (System.nanoTime() - startTime) / 1000000
        Log.d(TAG, "Inference time = " + elapsedTime + "ms")

        Log.d(TAG, "Results : ${result[0].contentToString()}")

        return getVertex(result[0])
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
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

    private fun processBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
    }

    private fun getOutputString(output: FloatArray): String {
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        return "Prediction Result: ${output.contentToString()}"
    }

    private fun getVertex(output: FloatArray): Vertexes? {
        var maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        if (output.maxOrNull()!! < THRESHOLD) maxIndex = -1
        return Vertexes.fromInt(maxIndex)
    }

    companion object {
        private const val MODEL_FILE = "model8.tflite"

        private const val TAG = "StrokeClassifier"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 3

        private const val OUTPUT_CLASSES_COUNT = 3

        private const val THRESHOLD = 0.65
    }
}