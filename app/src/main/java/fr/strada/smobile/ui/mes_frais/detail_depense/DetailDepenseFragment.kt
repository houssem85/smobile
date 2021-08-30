package fr.strada.smobile.ui.mes_frais.detail_depense

import android.app.AlertDialog
import android.app.Dialog
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.mes_frais.Depense
import fr.strada.smobile.data.models.mes_frais.TypesDepense
import fr.strada.smobile.data.models.mes_frais.UpdateDepense
import fr.strada.smobile.databinding.FragmentDetailDepenseBinding
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.demande_absence.TodayDecorator
import fr.strada.smobile.ui.mes_frais.SharedMesFraisViewModel
import fr.strada.smobile.ui.mes_frais.TypeDepenseAdapter
import fr.strada.smobile.ui.mes_frais.nouvelle_depense.dialog_enregister_depense.DialogEnregisterDepense
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.Toggle
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_request.edit_type
import kotlinx.android.synthetic.main.fragment_absence_request.img_expand
import kotlinx.android.synthetic.main.fragment_absence_request.menu_myadmin_expand
import kotlinx.android.synthetic.main.fragment_detail_depense.*
import kotlinx.android.synthetic.main.fragment_nouvelle_depense.*
import kotlinx.android.synthetic.main.fragment_nouvelle_depense.icBack
import kotlinx.android.synthetic.main.fragment_nouvelle_depense.recycle_types_depense
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DetailDepenseFragment : DialogFragment(){

    private lateinit var binding: FragmentDetailDepenseBinding
    private lateinit var viewModel: DetailDepenseViewModel

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var dialogSelectDate: Dialog
    private lateinit var sharedViewModel: SharedMesFraisViewModel
    lateinit var adapter: TypeDepenseAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_depense, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialogSelectDate()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setUpRecyclerTypesDepense()
        observeTypesDepense()
        setupNavigation()
        observeSharedEvents()
        if (savedInstanceState == null) {
            getArgumentDepense()
            viewModel.getTypesDepense()
        }
    }

    private fun initDialogSelectDate() {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
        val datePicker = dialogView.findViewById<MaterialCalendarView>(R.id.datePicker)
        val btnTerminer = dialogView.findViewById<Button>(R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<Button>(R.id.btnAnnuler)
        val currentDate = CalendarDay.today()
        dialogBuilder.setView(dialogView)
        datePicker.selectedDate = currentDate
        datePicker.addDecorators(TodayDecorator(requireActivity(), currentDate))
        datePicker.state().edit().setMaximumDate(currentDate).commit()
        dialogSelectDate = dialogBuilder.create()
        dialogSelectDate.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSelectDate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnTerminer.setOnClickListener {
            onClickTerminerListner(dialogSelectDate, datePicker)
        }
        btnAnnuler.setOnClickListener {
            onClickAnnulerlistner(dialogSelectDate, datePicker)
        }
    }

    private fun setUpRecyclerTypesDepense() {
        recycle_types_depense?.layoutManager = LinearLayoutManager(context)
        adapter = TypeDepenseAdapter(requireContext(), this::onClickItemTypeDepense)
        recycle_types_depense?.adapter = adapter

    }

    private fun onClickItemTypeDepense(item: TypesDepense) {
        viewModel.typeDepense.value = item
        Toggle.toggleLayout(
            menu_myadmin_expand.visibility == View.GONE,
            img_expand,
            menu_myadmin_expand
        )
    }

    private fun observeTypesDepense() {
        lifecycleScope.launchWhenStarted {
            viewModel.typesDepense.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        adapter.setData(it.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getArgumentDepense() {
        arguments?.let {
            try {
                val safeArgs = DetailDepenseFragmentArgs.fromBundle(it)
                val updateDepense = safeArgs.updateDepense
                val depense = updateDepense.depense
                viewModel.updateDepense = updateDepense
                viewModel.typeDepense.value = TypesDepense(depense.typeDepenseIcone,depense.typeDepenseId,depense.typeDepenseLibelle)
                viewModel.montant.value = depense.montant.toString()
                viewModel.commentaire.value = depense.commentaire
                val sfdApp = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val sdfApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateDepense = sdfApi.parse(depense.dateDepense.take(10))
                val year = depense.dateDepense.take(4).toInt()
                val month = depense.dateDepense.substring(5, 7).toInt()
                val day = depense.dateDepense.substring(8, 10).toInt()
                viewModel.dateDepense.value = sfdApp.format(dateDepense!!)
                viewModel.dayDepense.value = Day(year, month, day)
            } catch (ex: Exception) { }
        }
    }

    fun initComponent() {}

    fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(DetailDepenseViewModel::class.java)
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            providerFactory
        ).get(SharedMesFraisViewModel::class.java)
    }

    fun bindViewModel() {
        binding.viewModel = viewModel
    }

    fun setupNavigation() {
        edit_type.setOnClickListener {
            Toggle.toggleLayout(
                menu_myadmin_expand.visibility == View.GONE,
                img_expand,
                menu_myadmin_expand
            )
        }
        icBack?.setOnClickListener {
            val isFormatMontantCorrect = fr.strada.smobile.ui.mes_frais.nouvelle_depense.Utils.isDecimalNumber(viewModel.montant.value!!)
            val isDateDepenseNotEmpty = viewModel.dateDepense.value!!.trim().isNotEmpty()
            if (isFormatMontantCorrect && isDateDepenseNotEmpty) {
                showDialogEnregisterDepense()
            } else {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, {
            val isFormatMontantCorrect = fr.strada.smobile.ui.mes_frais.nouvelle_depense.Utils.isDecimalNumber(viewModel.montant.value!!)
            val isDateDepenseNotEmpty = viewModel.dateDepense.value!!.trim().isNotEmpty()
            if (isFormatMontantCorrect && isDateDepenseNotEmpty) {
                showDialogEnregisterDepense()
            } else {
                findNavController().popBackStack()
            }
        })
        btn_select_date_detail?.setOnClickListener {
            dialogSelectDate.show()
        }
        btn_add_justificatif?.setOnClickListener {
            /*
            if(groupImages.images!!.size < 3){
                dialogSelectCameraCallery.show()
            }*/
        }
        btn_enregistrer?.setOnClickListener {
            val isTablete = resources.getBoolean(R.bool.isTablet)
            updateDepense()
            if(!isTablete){
                findNavController().popBackStack()
            }else{
                dialog?.dismiss()
            }
        }
    }
    private fun observeSharedEvents() {

    }
    private fun showDialogEnregisterDepense() {
        val dialogEnregisterDepense = DialogEnregisterDepense()
        dialogEnregisterDepense.setOnPressBtnOuiListner {
            val isTablete = resources.getBoolean(R.bool.isTablet)
            updateDepense()
            if(!isTablete){
                findNavController().popBackStack()
            }else{
                dialog?.dismiss()
            }
        }
        dialogEnregisterDepense.setOnPressBtnNonListner {
            findNavController().popBackStack()
        }
        dialogEnregisterDepense.show(childFragmentManager, DialogEnregisterDepense.TAG)
    }


    private fun updateDepense() {
       try {
           val day = String.format("%02d", viewModel.dayDepense.value!!.day)
           val month = String.format("%02d", viewModel.dayDepense.value!!.month)
           val year = String.format("%04d", viewModel.dayDepense.value!!.year)
           val commentaire = viewModel.commentaire.value
           val dateDepense = "$year-$month-$day"
           val montant = viewModel.montant.value!!.toFloat()
           val typeDepenseId = viewModel.typeDepense.value!!.id
           val typeDepenseLibelle = viewModel.typeDepense.value!!.libelle
           val typeDepenseIcon = viewModel.typeDepense.value!!.icone
           val depenseId = viewModel.updateDepense!!.depense.depenseId
           val depense = Depense(
               commentaire,
               dateDepense,
               depenseId,
               listOf(),
               montant,
               typeDepenseIcon,
               typeDepenseId,
               typeDepenseLibelle
           )
            sharedViewModel.updateDepenseEvent.value = UpdateDepense(viewModel.updateDepense!!.index, depense)
        } catch (ex: Exception) { }
    }
    fun onClickTerminerListner(dialog: Dialog, datePicker: MaterialCalendarView) {
        val day = String.format("%02d", datePicker.selectedDate!!.day)
        val year = String.format("%02d", datePicker.selectedDate!!.year)
        val month = Utils.getMonthName(requireContext(), datePicker.selectedDate!!.month - 1)
        viewModel.dateDepense.value = "$day $month $year"
        viewModel.dayDepense.value = Day(
            datePicker.selectedDate!!.year,
            datePicker.selectedDate!!.month,
            datePicker.selectedDate!!.day
        )
        dialog.dismiss()
    }
    fun onClickAnnulerlistner(dialog: Dialog, datePicker: MaterialCalendarView) {
        dialog.dismiss()
    }

    override fun onResume()
    {
        super.onResume()
        val isTableteSize = resources.getBoolean(R.bool.isTablet)
        if (isTableteSize) {
            val window = dialog!!.window ?: return
            val params = window.attributes
            params.width = fromDpToPixels(600)
            params.height = fromDpToPixels(600)
            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun fromDpToPixels(dpValue: Int): Int {
        val metrics = this.resources.displayMetrics
        val dp = dpValue
        val fpixels = metrics.density * dp
        return (fpixels + 0.5f).toInt()
    }

    companion object {
       const val TAG = "DetailDepenseFragment"
    }
}