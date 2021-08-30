package fr.strada.smobile.ui.pointeuse.jour_activitie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.databinding.FragmentJourActivityTabBinding
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.pointeuse.adapter.ActivitieAdapter
import fr.strada.smobile.ui.pointeuse.adapter.CommentaireAdapter
import fr.strada.smobile.ui.pointeuse.getCurrentDate
import fr.strada.smobile.utils.KeyBoardUtils
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.ext.hide
import fr.strada.smobile.utils.ext.toast
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_jour_activity_tab.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class JourActivitieFragment : BaseFragment() {

    lateinit var vm: JourActivitieViewModel
    lateinit var activitieAdapter: ActivitieAdapter
    lateinit var commentaireAdapter: CommentaireAdapter
    lateinit var binding: FragmentJourActivityTabBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_jour_activity_tab, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivitieAdapter()
        initCommentaireAdapter()
        configRecyclerViewActivities()
        configRecyclerViewCommentaires()
        observeJourActivitie()
        observeCommentaires()
        if (savedInstanceState == null) {
            getExtraJourActivitie()
        }
    }

    override fun initViewModel() {
        vm = ViewModelProvider(
            requireActivity(),
            providerFactory
        ).get(JourActivitieViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = vm
    }

    override fun setupNavigation() {
        btn_send_comment.setOnClickListener {
            val commentaire = edit_commentaire.text.toString()
            if (commentaire.isNotEmpty()) {
                vm.ajoutCommentaire(commentaire)
            } else {
                requireContext().toast(resources.getString(R.string.ajouter_un_commentaire))
            }
        }
        btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun initComponent() {}

    override fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun getExtraJourActivitie() {
        if (arguments != null) {
            if (requireArguments().containsKey(EXTRA_JOUR_ACTIVITE)) {
                vm.jourActivitie.value = requireArguments().getParcelable(EXTRA_JOUR_ACTIVITE)
            }
        } else {
            val date = getCurrentDate()
            vm.getJourActivities(date)
        }
    }

    private fun observeJourActivitie() {
        vm.jourActivitie.observe(requireActivity(), { jourActivite ->
            jourActivite.activites?.let {
                activitieAdapter.setData(it)
                if (it.isEmpty()) {
                    view_title!!.hide()
                } else {
                    // view_title!!.show()
                }
            }
            jourActivite.commentaires?.let {
                vm.getCommentaires()
            }
            jourActivite.jourActivite?.let {
                try {
                    val locale = resources.configuration.locale
                    val sf = SimpleDateFormat("yyyy-MM-dd", locale)
                    val sfMonth = SimpleDateFormat("MMMM", locale)
                    val sfDay = SimpleDateFormat("dd", locale)
                    val date = sf.parse(it.take(10))
                    val strDate =
                        "${sfDay.format(date!!)} ${sfMonth.format(date).capitalize(Locale.ROOT)}"
                    txt_current_date.text = strDate
                } catch (ex: Exception) {

                }
            }
        })
    }

    private fun observeCommentaires() {
        vm.commentaires.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    commentaireAdapter.setData(it.data!!)
                }
            }
        })

        vm.ajoutCommentairesResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!) {
                        KeyBoardUtils.hideKeyboard(requireActivity())
                        edit_commentaire.setText("")
                        SmobileApp.instance!!.sendRefreshListActivitiesPointeuseReceiver()
                        vm.getCommentaires()
                    } else {
                        Toast.makeText(context, "pas d'activities", Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun initActivitieAdapter() {
        activitieAdapter = ActivitieAdapter(requireContext())
    }

    private fun initCommentaireAdapter() {
        commentaireAdapter = CommentaireAdapter((requireContext()))
    }

    private fun configRecyclerViewActivities() {
        recyclerViewActivitiesfrag.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = activitieAdapter
        }
    }

    private fun configRecyclerViewCommentaires() {
        recyclerViewCommentairesfrag.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = commentaireAdapter
        }
    }

    companion object {
        fun factory(jourActivitie: JourActivite): JourActivitieFragment {
            val fragment = JourActivitieFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_JOUR_ACTIVITE, jourActivitie)
            fragment.arguments = args
            return fragment
        }

        const val EXTRA_JOUR_ACTIVITE = "EXTRA_JOUR_ACTIVITE"
    }
}