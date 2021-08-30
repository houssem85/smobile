package fr.strada.smobile.ui.infractions.detail_infraction


import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.util.*


object Utils {

    fun isExtensionImage(extention:String):Boolean
    {
        return extention.toLowerCase(Locale.ROOT)=="png" || extention.toLowerCase(Locale.ROOT) == "jpg" || extention.toLowerCase(
            Locale.ROOT
        ) == "jpeg"
    }

    fun getResizedBitmap(image: Bitmap,maxSizeImage:Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0)
        {
            width = maxSizeImage
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSizeImage
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun correcteImageOrientation(path:String,image:Bitmap):Bitmap
    {
        val ei = ExifInterface(path)
        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(image, 90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(image, 180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(image, 270f)

            ExifInterface.ORIENTATION_NORMAL -> image

            else -> image
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
     fun imageDrawableToFile(image:Bitmap,name:String):File
    {
        val imageFile = File(name)
        val fos = FileOutputStream(imageFile);
        image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/,fos)

        fos.close()

     return imageFile
    }
}