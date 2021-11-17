package com.muhammed.citylabadmin.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
@Metadata(
        mv = {1, 4, 2},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¨\u0006\u0007"},
        d2 = {"Lcom/muhammed/citylabadmin/helper/Resize;", "", "()V", "decodeFile", "Landroid/graphics/Bitmap;", "filePath", "", "City_Lab_Admin.app"}
)
public class ResizJavaImage {


    public static Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

// The new size we want to scale to
        int REQUIRED_SIZE = 1024;

// Find the correct scale value. It should be the power of 2.
        double width_tmp = o.outWidth;
        double height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

// Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize = scale;
        Bitmap image = BitmapFactory.decodeFile(filePath, o2);
        ExifInterface exif;
        try {
            exif =new  ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            );
            float rotate = 0f;
            switch (exifOrientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:  rotate = 90f;
                case ExifInterface.ORIENTATION_ROTATE_180 : rotate = 180f;
                case ExifInterface.ORIENTATION_ROTATE_270 : rotate = 270f;
            }
            if (rotate != 0f) {
                int w = image.getWidth();
                int h = image.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false);
            }
        } catch ( IOException e) {
            return null;
        }
        return image.copy(Bitmap.Config.ARGB_8888, true);
    }


    @Nullable
    public static   Bitmap decodeFile2(@Nullable String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth;
        int height_tmp = o.outHeight;

        int scale;
        for(scale = 1; width_tmp >= REQUIRED_SIZE || height_tmp >= REQUIRED_SIZE; scale *= 2) {
            width_tmp /= 2;
            height_tmp /= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap image = BitmapFactory.decodeFile(filePath, o2);
        ExifInterface exif = null;

        try {
            Intrinsics.checkNotNull(filePath);
            exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt("Orientation", 1);
            float rotate = 0.0F;
            switch(exifOrientation) {
                case 3:
                    rotate = 180.0F;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    rotate = 90.0F;
                    break;
                case 8:
                    rotate = 270.0F;
            }

            if (rotate != 0.0F) {
                Intrinsics.checkNotNullExpressionValue(image, "image");
                int w = image.getWidth();
                int h = image.getHeight();
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false);
            }
        } catch (IOException var15) {
            return null;
        }

        return image.copy(Bitmap.Config.ARGB_8888, true);
    }
}
