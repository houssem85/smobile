package fr.strada.smobile.ui.infractions.detail_infraction

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.infractions.Infraction
import fr.strada.smobile.databinding.FragmentDetailInfractionBinding
import fr.strada.smobile.di.infractions.detail_infraction.DIALOG_ETES_VOUS_SUR_DE_VOULOIR_QUITTER
import fr.strada.smobile.di.infractions.detail_infraction.DIALOG_IMAGE_VIEWER
import fr.strada.smobile.di.infractions.detail_infraction.DetailInfractionComponent
import fr.strada.smobile.ui.activities.Utils.inFromBottomAnimation
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.infractions.InfractionsViewModel
import fr.strada.smobile.ui.infractions.detail_infraction.Utils.isExtensionImage
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.DialogEtesVousSurDeVouloirQuitterListner
import fr.strada.smobile.utils.listner.DialogImageViewerListner
import fr.strada.smobile.utils.listner.DialogSelectCameraGalleryListner
import fr.strada.smobile.utils.listner.GroupImagesListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_detail_infraction.*
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DetailInfractionFragment : BaseFragment() , DialogSelectCameraGalleryListner , DialogImageViewerListner , GroupImagesListner , DialogEtesVousSurDeVouloirQuitterListner {

    private lateinit var component : DetailInfractionComponent
    private lateinit var binding: FragmentDetailInfractionBinding
    private lateinit var viewModel: DetailInfractionViewModel
    private lateinit var infractionsViewModel: InfractionsViewModel

    lateinit var providerFactory: ViewModelProviderFactory
    @Inject set

    lateinit var dialog : BottomSheetDialog
    @Inject set

    @Named(DIALOG_IMAGE_VIEWER)
    @Inject
    internal lateinit var dialogImageviwer : Dialog

    @Named(DIALOG_ETES_VOUS_SUR_DE_VOULOIR_QUITTER)
    @Inject
    internal lateinit var dialogEtesVousSurDeVouloirQuiter : Dialog

    lateinit var dialogLoading : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infraction, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInfractionArgument()
        initDialogLoading()
        observeInfraction()
        observeDetailsInfraction()
        observeUpdateInfractionRespense()
        observeIsLoading()
        setGroupImagesListnerInGroupImageViews()
    }

    private fun getInfractionArgument()
    {
        arguments?.let {
            val safeArgs = DetailInfractionFragmentArgs.fromBundle(it)
            val infraction = safeArgs.infraction
            viewModel.infraction.value = infraction
        }
    }

    private fun observeInfraction(){
        lifecycleScope.launchWhenStarted {
            viewModel.infraction.collect {
                if(it == null){
                    constraint_no_infraction.visibility = VISIBLE
                }else {
                    viewModel.getDetailsInfraction(it.infractionId)
                    val strDate = "${it.infractionDate.substring(8,10)}/${it.infractionDate.substring(5,7)}/${it.infractionDate.substring(0,4)}"
                    constraint_no_infraction.visibility = GONE
                    lbl_title?.text = it.infractionCategorieLibelle
                    lbl_heure.text = millisToTime(it.infractionHeure.totalMilliseconds)
                    lbl_date.text = strDate
                }
            }
        }
    }

    private fun observeDetailsInfraction()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.detailsInfraction.collect {
                when(it.status){
                    Status.SUCCESS -> {
                        val detailsInfraction = it.data!!
                        lbl_duree_authorisee.text = millisToTime(detailsInfraction.dureeAutorisee.totalMilliseconds)
                        lbl_duree_effectuee.text = millisToTime(detailsInfraction.dureeEffectuee.totalMilliseconds)
                        lbl_repos_autorisee.text = millisToTime(detailsInfraction.reposAutorisee.totalMilliseconds)
                        lbl_repos_effectuee.text = millisToTime(detailsInfraction.reposEffectuee.totalMilliseconds)
                        val images: ObservableArrayList<File> = ObservableArrayList()
                        detailsInfraction.justificatifsInfraction.forEach {
                            val file = File(it)
                            images.add(file)
                        }
                        // groupImages.images = images
                        lbl_text_loi.text = detailsInfraction.infractionTextLoi
                        detailsInfraction.commentaire?.let {
                            txt_commentaire.setText(it)
                        }
                    }
                    Status.ERROR ->{
                        Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING ->{

                    }
                }
            }
        }
    }

    private fun observeUpdateInfractionRespense()
    {
        viewModel.updateInfractionResponse.observe(this,{
            when(it.status){
                Status.SUCCESS ->{
                    if(it.data!!){
                        val tabletSize = resources.getBoolean(R.bool.isTablet)
                        val orientation = resources.configuration.orientation
                        if(tabletSize){ // reset infraction a null
                            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                                viewModel.infraction.value = null
                                infractionsViewModel.setHideDetailsInfractionEvent()
                            }else{
                                Toast.makeText(context,resources.getString(R.string.infraction_envoyée) ,Toast.LENGTH_LONG).show()
                            }
                        }else {
                            findNavController().popBackStack()
                        }
                    }else{
                        Toast.makeText(context,"ereur",Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                   Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeIsLoading()
    {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                if(it) {
                    dialogLoading.show()
                }else {
                    dialogLoading.dismiss()
                }
            }
        }
    }

    override fun initComponent()
    {
        component = SmobileApp.instance!!.appComponent.detailInfractionComponent()
                                                            .setContext(requireContext())
                                                            .setDialogSelectCameraGalleryListner(this)
                                                            .setDialogImageViewerListner(this)
                                                            .setDialogEtesVousSurDeVouloirQuitterListner(this)
                                                            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailInfractionViewModel::class.java)
        infractionsViewModel = ViewModelProvider(requireActivity(), providerFactory).get(InfractionsViewModel::class.java)
    }

    override fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    override fun setupNavigation()
    {
        icBack?.setOnClickListener {
           findNavController().navigateUp()
        }
        btn_validate.setOnClickListener {
            val strCommentaire = txt_commentaire.text.toString()
            viewModel.updateInfraction(strCommentaire)
        }
        btn_add_justificatif.setOnClickListener {
            /*
            if(groupImages.images!!.size < 3){
                dialog.show()
            }
            */
        }
    }

    private fun setGroupImagesListnerInGroupImageViews(){
       groupImages.listner = this
    }

    override fun onClickAnnulerListner(dialog: BottomSheetDialog) {
        dialog.dismiss()
    }

    override fun onClickGalleryListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this).galleryMimeTypes(mimeTypes = arrayOf("image/png", "image/jpg", "image/jpeg")).galleryOnly().start()
    }

    override fun onClickCameraListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this).cameraOnly().start()
    }

    override fun onShowListner(dialog: Dialog) {

    }

    override fun onDismissListner() {
    }

    private fun scrollToBottom()
    {
        nestedScrollView.post { nestedScrollView.fullScroll(View.FOCUS_DOWN) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                dialog.dismiss()
                val file: File? = ImagePicker.getFile(data)
                if(file!=null)
                {   val isExtensionImage= isExtensionImage(file.extension)
                    if(isExtensionImage)
                    {
                        groupImages.addImage(file)
                        scrollToBottom()
                    }else
                    {
                        Toast.makeText(requireContext(), resources.getString(R.string.le_format_selectionne_est_incorrect), Toast.LENGTH_SHORT).show()
                    }
                }else
                {
                    Toast.makeText(requireContext(), resources.getString(R.string.veuillez_selectionner_un_document_depuis_votre_memoire_interne), Toast.LENGTH_SHORT).show()
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
    }

    override fun onShowListner(imageView: AppCompatImageView) {
         var imageBitmap = BitmapFactory.decodeFile(image!!.absolutePath)
         imageBitmap = Utils.correcteImageOrientation(image!!.absolutePath, imageBitmap)
         imageView.setImageBitmap(imageBitmap)
    }

    override fun onClickDialogListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onDataChanged(images: ObservableList<File>) {

    }

    private var image : File?= null

    override fun onImageClickListner(image: File) {
        this.image = image
        dialogImageviwer.show()
    }

    fun startEnterAnimation()
    {
        val animation = inFromBottomAnimation()
        nestedScrollView.startAnimation(animation)
    }

    override fun onClickOuiListner(dialog: Dialog) {
        dialog.dismiss()
    }

    override fun onCLickNonListner(dialog: Dialog) {
        dialog.dismiss()
    }

    private fun initDialogLoading()
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loader_empty_msg, null)
        dialogBuilder.setView(dialogView)
        dialogLoading = dialogBuilder.create()
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLoading.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
    }

    companion object{
        const val KEY_INFRACTION = "infraction"
        fun newInstance(infraction : Infraction) : DetailInfractionFragment{
            val fragment = DetailInfractionFragment()
            val args = Bundle()
            args.putParcelable(KEY_INFRACTION, infraction)
            fragment.arguments = args
            return fragment
        }
    }
}