package fr.strada.smobile.ui_tablette.infractions_tablette

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.infractions.InfractionsViewModel
import fr.strada.smobile.ui.infractions.detail_infraction.DetailInfractionFragment
import fr.strada.smobile.ui.infractions.filtre.FiltreFragment
import fr.strada.smobile.ui_tablette.accueil.Utils
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_infractions_tablette.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class InfractionsTabletteFragment : BaseFragment() {

    private lateinit var viewModel : InfractionsViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_infractions_tablette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContainer()
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfractionsTabletteFragment()
    }

    override fun initComponent() {}

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(InfractionsViewModel::class.java)
    }

    override fun bindViewModel() {}

    override fun setupNavigation() {
        btnFiltre?.setOnClickListener {
            val filterFragment = FiltreFragment()
            filterFragment.setOnSbmitListner { isPriodeFilterActive, dateDebut, dateFin , isCategorieClassFilterActive , infractionClass ->
                try {
                    val sdfUi = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
                    val sdfWebService = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
                    var strDateDebut : String? = null
                    var strDateFin : String? = null
                    var infraClass : Int? = null
                    if(isPriodeFilterActive){
                        val dDebut = sdfUi.parse(dateDebut)
                        val dFin  = sdfUi.parse(dateFin)
                        strDateDebut = sdfWebService.format(dDebut!!)
                        strDateFin = sdfWebService.format(dFin!!)
                    }
                    if(isCategorieClassFilterActive){
                        infraClass =  infractionClass
                    }
                    viewModel.getInfractions(strDateDebut,strDateFin,infraClass)
                }catch (ex:Exception){ }
            }
            filterFragment.show(childFragmentManager, FiltreFragment.TAG)
        }
        viewModel.pressItemInfractionEvent.observe(this,{
            val fragment = if(it != null){
                DetailInfractionFragment.newInstance(it)
            }else{
                DetailInfractionFragment()
            }
            val fragmentManager = childFragmentManager
            fragmentManager.beginTransaction().replace(R.id.second_fragment,fragment).commit()
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                second_fragment.visibility = VISIBLE
                btn_retour?.visibility = VISIBLE
                btnFiltre.visibility = GONE
            }
        })
        viewModel.hideDetailsInfractionEvent.observe(this,{
            second_fragment.visibility = GONE
            btn_retour?.visibility = GONE
            btnFiltre.visibility = VISIBLE
        })
        btn_retour?.setOnClickListener {
            second_fragment.visibility = GONE
            it.visibility = GONE
            btnFiltre.visibility = VISIBLE
        }
    }

    private fun setupContainer() // Fix widht container
    {
        val widthScreen = Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu = Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame.layoutParams = layoutParams
    }
}