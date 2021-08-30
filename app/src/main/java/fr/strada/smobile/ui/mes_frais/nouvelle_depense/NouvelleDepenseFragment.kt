package fr.strada.smobile.ui.mes_frais.nouvelle_depense

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import fr.strada.smobile.databinding.FragmentNouvelleDepenseBinding
import fr.strada.smobile.ui.activities.mensuel.Utils.getMonthName
import fr.strada.smobile.ui.demande_absence.TodayDecorator
import fr.strada.smobile.ui.infractions.detail_infraction.Utils
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.TypeDepenseAdapter
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.Utils.isDecimalNumber
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.dialog_ajouter_autre_depense.DialogAjouterAutreDepense
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.dialog_enregister_depense.DialogEnregisterDepense
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.Toggle
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_request.edit_type
import kotlinx.android.synthetic.main.fragment_absence_request.img_expand
import kotlinx.android.synthetic.main.fragment_absence_request.menu_myadmin_expand
import kotlinx.android.synthetic.main.fragment_nouvelle_depense.*
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject

class NouvelleDepenseFragment : DialogFragment()  {

    private lateinit var binding: FragmentNouvelleDepenseBinding
    private lateinit var viewModel:NouvelleDepenseViewModel
    private lateinit var sharedViewModel : SharedMesFraisViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var dialogSelectDate: Dialog
    private lateinit var dialogSelectCameraCallery: BottomSheetDialog

    lateinit var adapter : TypeDepenseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nouvelle_depense, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        initDialogSelectDate()
        initDialogSelectCameraGallery()
        setUpRecyclerTypesDepense()
        observeTypesDepense()
        if(savedInstanceState == null)
        {
            viewModel.getTypesDepense()
        }
    }

    private fun initDialogSelectDate()
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
        val datePicker = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        val btnTerminer = dialogView.findViewById<Button>(R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
        val currentDate= CalendarDay.today()
        dialogBuilder.setView(dialogView)
        datePicker.selectedDate = currentDate
        datePicker.addDecorators(TodayDecorator(requireActivity(), currentDate))
        datePicker.state().edit().setMaximumDate(currentDate).commit()
        dialogSelectDate = dialogBuilder.create()
        dialogSelectDate.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSelectDate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnTerminer.setOnClickListener {
            onClickTerminerListner(dialogSelectDate,datePicker)
        }
        btnAnnuler.setOnClickListener {
            onClickAnnulerlistner(dialogSelectDate,datePicker)
        }
    }

    private fun initDialogSelectCameraGallery()
    {
        dialogSelectCameraCallery = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogThemeTransparent)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_camera_callery,null)
        val btnAnnuler=dialogView.findViewById<AppCompatTextView>(R.id.btnAnnuler)
        val btnCamera=dialogView.findViewById<AppCompatTextView>(R.id.btnCamera)
        val btnCalerie=dialogView.findViewById<AppCompatTextView>(R.id.btnCalerie)
        dialogSelectCameraCallery.setContentView(dialogView)
        btnAnnuler.setOnClickListener {
            onClickAnnulerListner(dialogSelectCameraCallery)
        }
        btnCamera.setOnClickListener {
            onClickCameraListner(dialogSelectCameraCallery)
        }
        btnCalerie.setOnClickListener {
            onClickGalleryListner(dialogSelectCameraCallery)
        }
    }

    private fun setUpRecyclerTypesDepense(){
        recycle_types_depense?.layoutManager = LinearLayoutManager(context)
        adapter = TypeDepenseAdapter(requireContext() , this::onClickItemTypeDepense)
        recycle_types_depense?.adapter = adapter
    }

    private fun onClickItemTypeDepense(item : TypesDepense)
    {
        viewModel.strTypeDepense.value = item.libelle
        viewModel.typeDepense.value = item
        Toggle.toggleLayout(menu_myadmin_expand.visibility == View.GONE, img_expand, menu_myadmin_expand)
    }

    private fun observeTypesDepense(){
        lifecycleScope.launchWhenStarted {
            viewModel.typesDepense.collect {
                when(it.status)
                {
                    Status.SUCCESS -> {
                        adapter.setData(it.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(NouvelleDepenseViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), providerFactory).get(SharedMesFraisViewModel::class.java)
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        edit_type.setOnClickListener {
            Toggle.toggleLayout(menu_myadmin_expand.visibility == View.GONE, img_expand, menu_myadmin_expand)
        }
        icBack?.setOnClickListener {
            val isFormatMontantCorrect = isDecimalNumber(viewModel.montant.value!!)
            val isDateDepenseNotEmpty = viewModel.dateDepense.value!!.trim().isNotEmpty()
            val isTypeDepenseNotEmpty = viewModel.strTypeDepense.value!!.trim().isNotEmpty()
            if(isFormatMontantCorrect && isDateDepenseNotEmpty && isTypeDepenseNotEmpty)
            {
                showDialogEnregisterDepense()
            }else
            {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,true,{
            val isFormatMontantCorrect = isDecimalNumber(viewModel.montant.value!!)
            val isDateDepenseNotEmpty = viewModel.dateDepense.value!!.trim().isNotEmpty()
            val isTypeDepenseNotEmpty = viewModel.strTypeDepense.value!!.trim().isNotEmpty()
            if(isFormatMontantCorrect && isDateDepenseNotEmpty && isTypeDepenseNotEmpty)
            {
                showDialogEnregisterDepense()
            }else
            {
                findNavController().popBackStack()
            }
        })
        btn_select_date?.setOnClickListener {
            dialogSelectDate.show()
        }
        btn_add_justificatif?.setOnClickListener {
            /*
            if(groupImages.images!!.size < 3){
                dialogSelectCameraCallery.show()
            }*/
        }
        btn_ajouter?.setOnClickListener {
            val dialogAjouterAutreDepense = DialogAjouterAutreDepense()
            dialogAjouterAutreDepense.setOnPressBtnTerminerListner {
                addNouvelleDepense()
                val isTablete = resources.getBoolean(R.bool.isTablet)
                if(isTablete){
                    dismiss()
                }else{
                    findNavController().popBackStack()
                }
            }
            dialogAjouterAutreDepense.setOnPressBtnAjouterUneAutreDepenseListner {
                addNouvelleDepense()
                viewModel.reset()
                groupImages.clear()
            }
            dialogAjouterAutreDepense.show(childFragmentManager, DialogAjouterAutreDepense.TAG)
        }
    }

    private fun addNouvelleDepense()
    {
        try {
            val day  = String.format("%02d", viewModel.dayDepense.value!!.day)
            val month = String.format("%02d", viewModel.dayDepense.value!!.month)
            val year  = String.format("%04d", viewModel.dayDepense.value!!.year)
            val commentaire = viewModel.commentaire.value
            val dateDepense = "$year-$month-$day"
            val montant = viewModel.montant.value!!.toFloat()
            val typeDepenseIcon = viewModel.typeDepense.value!!.icone
            val typeDepenseId = viewModel.typeDepense.value!!.id
            val typeDepenseLibelle = viewModel.typeDepense.value!!.libelle
            val depense  = Depense(commentaire,dateDepense,"",listOf(),montant,typeDepenseIcon,typeDepenseId,typeDepenseLibelle)
            sharedViewModel.addNouvelleDepenseEvent.value = depense
        }catch (ex:Exception)
        {
            Toast.makeText(context,ex.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun showDialogEnregisterDepense(){
        val dialogEnregisterDepense = DialogEnregisterDepense()
        dialogEnregisterDepense.setOnPressBtnOuiListner {
            addNouvelleDepense()
            findNavController().popBackStack()
        }
        dialogEnregisterDepense.setOnPressBtnNonListner {
            findNavController().popBackStack()
        }
        dialogEnregisterDepense.show(childFragmentManager, DialogEnregisterDepense.TAG)
    }

    companion object {
        const val TAG = "NouvelleDepenseFragment"
        @JvmStatic
        fun newInstance() = NouvelleDepenseFragment()
    }

    //--------------------DialogSelectDateListner--------------------//

    fun onClickTerminerListner(dialog: Dialog, datePicker: MaterialCalendarView) {
        val day  = String.format("%02d", datePicker.selectedDate!!.day)
        val year  = String.format("%02d", datePicker.selectedDate!!.year)
        val month  = getMonthName(requireContext(),datePicker.selectedDate!!.month-1)
        viewModel.dateDepense.value = "$day $month $year"
        viewModel.dayDepense.value = Day(datePicker.selectedDate!!.year,datePicker.selectedDate!!.month,datePicker.selectedDate!!.day)
        dialog.dismiss()
    }

    fun onClickAnnulerlistner(dialog: Dialog, datePicker: MaterialCalendarView) {
        dialog.dismiss()
    }

    //-----------------------DialogSelectCameraGalleryListner------------------------//

    fun onClickAnnulerListner(dialog: BottomSheetDialog) {
        dialog.dismiss()
    }

    fun onClickGalleryListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this).galleryMimeTypes(mimeTypes = arrayOf("image/png", "image/jpg", "image/jpeg")).galleryOnly().start()
    }

    fun onClickCameraListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this).cameraOnly().start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                dialogSelectCameraCallery.dismiss()
                val file: File? = ImagePicker.getFile(data)
                if(file!=null)
                {   val isExtensionImage= Utils.isExtensionImage(file.extension)
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

    private fun scrollToBottom()
    {
        nestedScrollView.post { nestedScrollView.fullScroll(View.FOCUS_DOWN) }
    }

    override fun onResume()
    {
        super.onResume()
        val isTableteSize = resources.getBoolean(R.bool.isTablet)
        if(isTableteSize){
            val window = dialog!!.window ?: return
            val params = window.attributes
            params.width = fromDpToPixels(600)
            params.height = fromDpToPixels(600)
            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun fromDpToPixels(dpValue:Int):Int
    {
        val metrics = this.resources.displayMetrics
        val dp = dpValue
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        return pixels
    }
}