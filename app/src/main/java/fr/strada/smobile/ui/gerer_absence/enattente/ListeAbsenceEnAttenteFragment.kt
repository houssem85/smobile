package fr.strada.smobile.ui.gerer_absence.enattente


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.base.BaseNFragment
import fr.strada.smobile.utils.TopSpacingItemDecoration

/**
 * A simple [Fragment] subclass.
 */
class ListeAbsenceEnAttenteFragment : BaseNFragment() {

    lateinit var recyclerView: RecyclerView
    private val viewModel: ListeAbsenceEnAttenteViewModel by lazy {
        ListeAbsenceEnAttenteViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liste_absence_en_attente, container, false)
        initRecyclerView(view)

        return view
    }

    private fun initRecyclerView(view: View) {

        recyclerView = view.findViewById<RecyclerView>(R.id.recycleView)
        recyclerView.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val topSpacingDecorator =
                TopSpacingItemDecoration(15, 0, 0, 15)
            addItemDecoration(topSpacingDecorator)
            adapter = ListeAbsenceEnAttenteAdapter(
                viewModel.getListeAbsenceEnAttente(),
                context
            )
        }
    }


}
