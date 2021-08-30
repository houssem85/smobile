package fr.strada.smobile.utils.ext

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun checkPermissionLocation(context: Context): Boolean = suspendCoroutine { cont ->
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
    {
        ExcuseMe.couldYouGive(context).permissionFor(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION){
            if (it.granted.contains(Manifest.permission.ACCESS_FINE_LOCATION) && it.granted.contains(
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                cont.resume(true)
            }else
            {
                cont.resume(false)
            }
        }
    }else
    {
        ExcuseMe.couldYouGive(context).permissionFor(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_BACKGROUND_LOCATION){
            if (it.granted.contains(Manifest.permission.ACCESS_FINE_LOCATION) && it.granted.contains(
                    Manifest.permission.ACCESS_COARSE_LOCATION) && it.granted.contains(Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                cont.resume(true)
            }else
            {
                cont.resume(false)
            }
        }
    }
}




 fun showDialogActiverLocalisation(context: Context){
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Attention")
    builder.setMessage("Il faut activer la position pour lancer l'activité")
    builder.setPositiveButton(android.R.string.ok) { _, _ ->
        /*val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)*/
    }
     builder.setCancelable(false)
    //builder.show()

     val dialog: AlertDialog = builder.create()
     val manager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
     val enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
     if(!enabled) {
         dialog.show()
         dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
             val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
             i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             dialog.dismiss()
             context.startActivity(i)

         }
     }else if (dialog.isShowing){
         dialog.dismiss()
     }




 }

fun isGPSEnabled(context: Context) : Boolean{
    val manager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

 suspend fun showDialogPermissionsAreDenied(context: Context)
{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        ExcuseMe.couldYouGive(context).please(
            explainAgainTitle = "Une autorisation est nécessaire",
            explainAgainExplanation = "L'application a besoin de cette autorisation pour Lancer une activité",
            showSettingsTitle = "Définir l'autorisation dans les paramètres",
            showSettingsExplanation = "L'application ouvrira les paramètres pour modifier l'autorisation à partir de là"
        )
            .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION )&& ExcuseMe.permissionFor(Manifest.permission.ACCESS_COARSE_LOCATION)&& ExcuseMe.permissionFor(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
    else
    {
        ExcuseMe.couldYouGive(context).please(
            explainAgainTitle = "Une autorisation est nécessaire",
            explainAgainExplanation = "L'application a besoin de cette autorisation pour Lancer une activité",
            showSettingsTitle = "Définir l'autorisation dans les paramètres",
            showSettingsExplanation = "L'application ouvrira les paramètres pour modifier l'autorisation à partir de là"
        )
            .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION )&& ExcuseMe.permissionFor(Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}



@SuppressLint("MissingPermission")
suspend fun getLastLocation(context: Context): Location? = suspendCoroutine { cont ->
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        cont.resume(location)
    }.addOnFailureListener {
        cont.resume(null)
    }
}

suspend fun insistOnPermission(context: Context) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        ExcuseMe.couldYouGive(context).gently { result ->
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Attention")
            dialog.setMessage("L'application a besoin de localisation pour Lancer une activité")
            dialog.setPositiveButton("Continuer") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.getPackageName(), null)
                intent.data = uri
                context.startActivity(intent)
            }
            dialog.setCancelable(false)
            //dialog.setOnCancelListener { result(false) } //important
            dialog.show()
        }
            .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION) && ExcuseMe.permissionFor(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) && ExcuseMe.permissionFor(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    }else{
        ExcuseMe.couldYouGive(context).gently { result ->
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Attention")
            dialog.setMessage("Il faut activer la position pour lancer l'activité")
            dialog.setPositiveButton("Continuer") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.getPackageName(), null)
                intent.data = uri
                context.startActivity(intent)
            }
            //dialog.setOnCancelListener { result(false) } //important
            dialog.show()
        }
            .permissionFor(Manifest.permission.ACCESS_FINE_LOCATION) && ExcuseMe.permissionFor(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}