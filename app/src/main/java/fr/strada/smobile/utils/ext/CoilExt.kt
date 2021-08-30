package fr.strada.smobile.utils.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*

suspend fun loadImageSvg(link : String ,context : Context): Bitmap = suspendCoroutine { cont ->
    val request = ImageRequest.Builder(context)
        .data(link)
        .decoder(SvgDecoder(context))
        .target(
            onStart = { _ ->
            },
            onSuccess = { result ->
                cont.resume(result.toBitmap(200 ,200 , Bitmap.Config.ARGB_8888))
            },
            onError = { _ ->
                cont.resumeWithException(Exception(""))
            }
        )
        .build()
    context.imageLoader.enqueue(request)
}
*/

suspend fun loadImage(link : String ,context : Context): Bitmap = suspendCoroutine { cont ->
    Glide.with(context)
        .asBitmap()
        .load(link)
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                cont.resume(resource)
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                try {
                    cont.resumeWithException(Exception(""))
                }catch (ex:Exception){ }
            }
        })
}