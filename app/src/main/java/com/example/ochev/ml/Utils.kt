package com.example.ochev.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class Utils {
    companion object {
        @Throws(IOException::class)
        fun assetFile(
            context: Context,
            assetName: String?
        ): File? {
            val file = File(context.filesDir, assetName)
            if (file.exists() && file.length() > 0) {
                return file
            }
            context.assets.open(assetName!!).use { `is` ->
                FileOutputStream(file).use { os ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (`is`.read(buffer).also { read = it } != -1) {
                        os.write(buffer, 0, read)
                    }
                    os.flush()
                }
                return file
            }
        }

        fun loadBitmapFromView(v: View): Bitmap? {
            val b = Bitmap.createBitmap(
                v.measuredWidth,
                v.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            return b
        }


        fun saveBitmap(bitmap: Bitmap, context: Context, filename : String) {
            try {
                val file = File(context.getExternalFilesDir(null), filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}