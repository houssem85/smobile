package fr.strada.smobile.ui.profil

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentProfilBinding
import fr.strada.smobile.ui.activities.Utils.inFromBottomAnimation
import fr.strada.smobile.ui.activities.Utils.inFromRightAnimation
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogSeDeconnecterListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.fragment_profil.*
import javax.inject.Inject

/**
 * Profil fragment
 *
 * @constructor Create empty Profil fragment
 */
class ProfilFragment : BaseFragment(), DialogSeDeconnecterListner {

    private lateinit var viewModel : ProfilViewModel
    private lateinit var binding : FragmentProfilBinding
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        initComponent()
        initViewModel()
        initDialogDeconnecter()
        observeUserProfile()
        observeUserImage()
        viewModel.getUserProfile()
        viewModel.getUserImage()
        if(activity is MainActivitySpi)
        {
            appCompatImageView2?.visibility = View.INVISIBLE
            (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.INVISIBLE
            (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE

        }
        val isTablette = resources.getBoolean(R.bool.isTablet)
        if(isTablette){
            setupContainer()
        }
    }

    private fun initDialogDeconnecter()
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_se_deconnecter, null)
        val btnDeconnexion = dialogView.findViewById<AppCompatButton>(R.id.btnDeconnexion)
        val btnAnnuler = dialogView.findViewById<AppCompatButton>(R.id.btnAnnuler)
        dialogBuilder.setView(dialogView)
        dialog = dialogBuilder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnAnnuler.setOnClickListener {
            onClickAnnulerListner(dialog)
        }
        btnDeconnexion.setOnClickListener {
            onClickDeconnexionListner(dialog)
        }
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frameProfile.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frameProfile.layoutParams = layoutParams
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfilFragment()
    }

    private fun startAnimation() {
        val fromBottomAnimation = inFromBottomAnimation()
        val formRightAnimation = inFromRightAnimation()
        nestedScrollView.startAnimation(fromBottomAnimation)
        imgProfile.startAnimation(formRightAnimation)
        nameEmployee?.startAnimation(formRightAnimation)
    }

    override fun initComponent() {}

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }
    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(ProfilViewModel::class.java)
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnSeDeconnecterEvent.observe(viewLifecycleOwner, {
            it?.let {
                dialog.show()
            }
        })
        viewModel.pressBtnOpenMenuEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (activity is MainActivity) {
                    (activity as MainActivity).openDrawer()
                }
            }
        })
    }

    private fun observeUserProfile()
    {
        viewModel.userProfile.observe(viewLifecycleOwner,{ resource ->
            when(resource.status){
                Status.LOADING -> {

                }
                Status.ERROR -> {
                   Toast.makeText(context,resource.message,Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    resource.data?.let { profileModel ->
                        profileModel.firstName?.let {
                            lblNom.text = it
                        }
                        profileModel.lastName?.let {
                            lblPrenom.text = it
                        }
                        profileModel.fullName?.let {
                            nameEmployee?.text = it
                        }
                        profileModel.card?.let {
                            lblMatricule.text = it.cn
                        }
                    }
                }
            }
        })
    }


    private fun observeUserImage()
    {
        viewModel.userImage.observe(viewLifecycleOwner,{
            if(it.isNotEmpty()){
                val decodedBytes: ByteArray = Base64.decode(it, Base64.DEFAULT)
                val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                imgProfile.load(decodedByte){transformations(CircleCropTransformation())}
            }
        })
    }

    override fun onClickAnnulerListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onClickDeconnexionListner(dialog: Dialog) {
        dialog.dismiss()
        (activity as BaseActivity).deconnecter()
    }
}

