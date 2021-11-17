package com.muhammed.citylabadmin.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException

class Resize {

   fun decodeFile(filePath: String?): Bitmap? {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, o)

// The new size we want to scale to
        val REQUIRED_SIZE = 1024

// Find the correct scale value. It should be the power of 2.
        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

// Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        var image = BitmapFactory.decodeFile(filePath, o2)
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val exifOrientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            )
            var rotate = 0f
            when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270f
            }
            if (rotate != 0f) {
                val w = image.width
                val h = image.height

                // Setting pre rotate
                val mtx = Matrix()
                mtx.preRotate(rotate)

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false)
            }
        } catch (e: IOException) {
            return null
        }
        return image.copy(Bitmap.Config.ARGB_8888, true)
    }

}