package fr.strada.smobile.ui.infractions.detail_infraction

import android.annotation.TargetApi
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.bumptech.glide.Glide
import fr.strada.smobile.R
import fr.strada.smobile.ui.infractions.detail_infraction.Utils.correcteImageOrientation
import fr.strada.smobile.ui.infractions.detail_infraction.Utils.getResizedBitmap
import fr.strada.smobile.utils.listner.GroupImagesListner
import java.io.File


class GroupImageViews: LinearLayout {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val heightImage = 200
    private val maxSizeImage = 400

    var listner: GroupImagesListner? = null

    var images : ObservableList<File>? = ObservableArrayList()
    set(value) {
        field = value
        value?.let {
            for (image in it)
            {
                try
                {   if(image.isHttpOrHttps()) // image in server
                    {
                        addCustomImageViewFromServer(image)

                    }else // local image
                    {
                        addCustomImageViewFromLocal(image)
                    }

                }catch (ex:java.lang.Exception)
                {
                    images?.remove(image)
                    notifyDataChanged()
                }
            }
        }
    }


    fun addImage(image:File)
    {
        if(image.exists())
        {   try {
                images?.add(image)
                notifyDataChanged()
                addCustomImageViewFromLocal(image)
            }catch (ex:Exception){
                images?.remove(image)
                notifyDataChanged()
                Toast.makeText(context,ex.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun notifyDataChanged(){
        listner?.onDataChanged(images!!)
    }

    private fun addCustomImageViewFromLocal(image:File)
    {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image,null) as RelativeLayout
        val btnDelete= view.findViewById<AppCompatImageView>(R.id.btnDelete)
        val imageView= view.findViewById<AppCompatImageView>(R.id.imageView)
        val widhtPixels = heightImage * context.resources.displayMetrics.density
        view.layoutParams = LayoutParams(0, widhtPixels.toInt(), 1f)
        btnDelete.setOnClickListener {
            this@GroupImageViews.removeView(view)
            images?.remove(image)
            notifyDataChanged()
        }
        imageView.setOnClickListener {
            listner?.onImageClickListner(image)
        }
        this.addView(view)
        var imageBitmap = BitmapFactory.decodeFile(image.absolutePath)
        imageBitmap = getResizedBitmap(imageBitmap,maxSizeImage)
        imageBitmap = correcteImageOrientation(image.absolutePath,imageBitmap)
        imageView.setImageBitmap(imageBitmap)
    }

    private fun addCustomImageViewFromServer(image:File)
    {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image,null) as RelativeLayout
        val btnDelete= view.findViewById<AppCompatImageView>(R.id.btnDelete)
        val imageView= view.findViewById<AppCompatImageView>(R.id.imageView)
        val widhtPixels = heightImage * context.resources.displayMetrics.density
        view.layoutParams = LayoutParams(0, widhtPixels.toInt(), 1f)
        btnDelete.setOnClickListener {
            this@GroupImageViews.removeView(view)
            images?.remove(image)
            notifyDataChanged()
        }
        imageView.setOnClickListener {
            listner?.onImageClickListner(image)
        }
        this.addView(view)
        Glide.with(context).load(image.path).into(imageView)
    }

    fun clear()
    {
        this.removeAllViews()
        images = ObservableArrayList()
    }
}