package com.example.ochev.ml

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ochev.RTVRedactorMainActivity
import com.google.android.gms.tasks.Tasks
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executors


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

        private var counter = 0

        fun saveBitmap(bitmap: Bitmap, context: Context, filename: String? = null) {
            try {

                val file = if (filename == null) File(
                    context.getExternalFilesDir(null),
                    "bmp${counter.toString().padStart(4, '0')}.png"
                )
                else File(context.getExternalFilesDir(null), filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        out
                    ) // bmp is your Bitmap instance
                }
                counter++
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun saveBitmapToGallery(bitmap: Bitmap, activityRTVRedactor: RTVRedactorMainActivity, title: String) {

            if (ContextCompat.checkSelfPermission(
                    activityRTVRedactor,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activityRTVRedactor,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )
            } else {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, title)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
                values.put(MediaStore.Images.Media.DESCRIPTION, "")
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

                val contentResolver = activityRTVRedactor.contentResolver

                Tasks.call(
                    Executors.newCachedThreadPool(),
                    Callable {
                        writeBitmapToGallery(bitmap, contentResolver, values)
                    }
                )
                    .addOnSuccessListener {
                        if (it) Toast.makeText(activityRTVRedactor, "Saved to gallery", Toast.LENGTH_LONG)
                            .show()
                    }
            }
        }

        private fun writeBitmapToGallery(
            bitmap: Bitmap,
            contentResolver: ContentResolver,
            values: ContentValues
        ): Boolean {
            var url: Uri? = null

            try {
                url = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Log.d("saving", url.toString())

                if (url != null) {
                    val imageOut = contentResolver.openOutputStream(url)
                    imageOut.use {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    }
                    return true
                }
            } catch (e: Exception) {
                if (url != null) {
                    contentResolver.delete(url, null, null)
                }
                Log.e("saving", e.toString())
            }
            return false
        }
    }
}