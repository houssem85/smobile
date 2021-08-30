package fr.strada.smobile.ui.spi

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogSeDeconnecterListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.dialog_profil_fragment.*
import timber.log.Timber
import javax.inject.Inject

class DialogProfilFragment : DialogFragment() , DialogSeDeconnecterListner {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: MainActivitySpiViewModel
    lateinit var dialogD : Dialog


    companion object {
        const val TAG = "DialogProfilFragment"
        fun newInstance() = DialogProfilFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_profil_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        viewModel = ViewModelProvider(
            requireActivity(),
            providerFactory
        ).get(MainActivitySpiViewModel::class.java)
        setupNagivation()
        observeUserImage()
        observeUserProfile()
    }

    private fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    var onClickDeconnecter: (() -> Unit)? = null
    var onClickprofil: (() -> Unit)? = null
    var onClickaPropos: (() -> Unit)? = null
    var onClickReglage: (() -> Unit)? = null


    private fun setupNagivation() {
        container_dex.setOnClickListener {
            //onClickDeconnecter?.invoke()

            initDialogDeconnecter()
        }
        container_reglage.setOnClickListener {
            onClickReglage!!.invoke()
        }
        btn_close.setOnClickListener {
            dialog?.dismiss()
        }
        button_profil.setOnClickListener {
            onClickprofil!!.invoke()
        }
        containe_aprops.setOnClickListener {
            onClickaPropos!!.invoke()
        }
    }

    private fun observeUserImage() {
        viewModel.userImage.observe(this, {
            if (it.isNotEmpty()) {
                val decodedBytes: ByteArray = Base64.decode(it, Base64.DEFAULT)
                val decodedByte: Bitmap =
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                img_profile.load(decodedByte) { transformations(CircleCropTransformation()) }
            }
        })
    }

    private fun observeUserProfile() {
        viewModel.userProfile.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    resource.data?.let { profileModel ->
                        profileModel.firstName?.let {
                            txt_dialog_name.text = it
                        }
                    }
                }
                Status.NO_CONTENT -> Timber.e("no_content")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        try {
            val isTableteSize = resources.getBoolean(R.bool.isTablet)
            if(!isTableteSize)
            {
                val window = dialog!!.window
                window!!.setGravity(Gravity.CENTER)
                val params = window.attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = params
            }
        }catch (ex:Exception){

        }
    }

    private fun initDialogDeconnecter()
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_se_deconnecter, null)
        val btnDeconnexion = dialogView.findViewById<AppCompatButton>(R.id.btnDeconnexion)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        dialogBuilder.setView(dialogView)
        dialogD = dialogBuilder.create()
        dialogD.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogD.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnAnnuler.setOnClickListener {
            onClickAnnulerListner(dialogD)
        }
        btnDeconnexion.setOnClickListener {
            onClickDeconnexionListner(dialogD)
        }
        dialogD.show()
    }


    override fun onClickAnnulerListner(dialogDe: Dialog) {
        dialogDe.dismiss()
    }

    override fun onClickDeconnexionListner(dialogDe: Dialog) {
        dialogDe.dismiss()
        dialog?.dismiss()
        (activity as BaseActivity).deconnecter()
    }
}