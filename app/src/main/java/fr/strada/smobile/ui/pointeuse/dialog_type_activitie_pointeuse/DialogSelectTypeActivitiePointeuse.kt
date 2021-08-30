package fr.strada.smobile.ui.pointeuse.dialog_type_activitie_pointeuse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.pointeuse.PointeuseViewModel
import fr.strada.smobile.ui.pointeuse.adapter.TypeActivitiePointeuseAdapter
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.OnClickListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.dialog_select_type_activitie_pointeuse.*
import javax.inject.Inject

class DialogSelectTypeActivitiePointeuse : DialogFragment() , OnClickListener  {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel : PointeuseViewModel

    lateinit var adapter : TypeActivitiePointeuseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_select_type_activitie_pointeuse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        setupRecyclerView()
        setupNavigation()
        if(savedInstanceState == null){
            viewModel.getTypeActivitePointeuse()
        }
        observeTypeActivitePointeuse()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    private fun injectDependencies(){
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel  = ViewModelProvider(this,providerFactory).get(PointeuseViewModel::class.java)
    }

    private fun setupRecyclerView(){
        adapter = TypeActivitiePointeuseAdapter(requireContext(),this)
        recycler_type_activities.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        recycler_type_activities.adapter = adapter
    }

    private fun setupNavigation(){
        btn_close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun observeTypeActivitePointeuse(){
        viewModel.typeActivities.observe(this,{
            when(it.status){
                Status.SUCCESS -> {
                   val data = it.data!!
                   adapter.items = data
                   adapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                   Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    var onSubmitListner : ((String)-> Unit)? = null

    override fun onClick(position: Int) {
        val typeActivityPointeuse = adapter.items[position].id
        onSubmitListner?.invoke(typeActivityPointeuse)
        dialog?.dismiss()
    }

    companion object {
        const val TAG = "DialogSelectTypeActivitiePointeuse"
        @JvmStatic
        fun newInstance() = DialogSelectTypeActivitiePointeuse()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window!!
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
    }
}