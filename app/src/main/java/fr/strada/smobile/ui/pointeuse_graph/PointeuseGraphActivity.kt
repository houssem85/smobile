package fr.strada.smobile.ui.pointeuse_graph

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.pointeuse.adapter.CommentaireAdapter
import fr.strada.smobile.ui.pointeuse.adapter.TypeActivitiePointeuseAdapter
import fr.strada.smobile.ui.pointeuse.millisToTime
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.listner.OnClickListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_pointeuse_graph.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PointeuseGraphActivity : PointeuseGraphActivityAnimations() , OnClickListener {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: PointeuseGraphViewModel
    private lateinit var typeActivitiePointeuseAdapter: TypeActivitiePointeuseAdapter
    private lateinit var commentaireAdapter : CommentaireAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        initViewModel()
        setUpChartWithAttributes()
        setupRecyclerTypeActivities()
        setupRecyclerCommentaires()
        observePointeuseAttributes()
        observeCommentaires()
        setupNavigation()
        observeTypeActivities()
        observeActivities()
        observeCurrentDay()
        observeAjoutCommentaireSingleEvent()
        if(savedInstanceState == null)
        {
            viewModel.getTypeActivitePointeuse()
            viewModel.getListActivitePointeuseByDay()
            viewModel.getCommentairesByDay()
        }
        registerRefreshListActivitiesPointeuseReceiver()
    }

    private fun injectDependencies()
    {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel()
    {
        viewModel  = ViewModelProvider(this, providerFactory).get(PointeuseGraphViewModel::class.java)
    }

    private fun setupRecyclerTypeActivities()
    {
        typeActivitiePointeuseAdapter = TypeActivitiePointeuseAdapter(this,this)
        recyclerViewTypesActivitiesPointeuse.layoutManager = LinearLayoutManager(this)
        recyclerViewTypesActivitiesPointeuse.adapter = typeActivitiePointeuseAdapter
    }

    private fun setupRecyclerCommentaires()
    {
        commentaireAdapter = CommentaireAdapter(this)
        recycle_commentaires.layoutManager = LinearLayoutManager(this)
        recycle_commentaires.adapter = commentaireAdapter
    }

    private fun observeCommentaires(){
        viewModel.commentaires.observe(this,{
            when(it.status){
                Status.SUCCESS->{
                    commentaireAdapter.setData(it.data!!)
                }
                Status.ERROR->{
                    Toast.makeText(baseContext,it.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observePointeuseAttributes()
    {
        SmobileApp.instance!!.isChronoStarted.observe(this,{
            if (!it) {
                val menuIsVisible = recyclerViewTypesActivitiesPointeuse.visibility == View.VISIBLE
                if(!menuIsVisible)
                {
                    imgStopPlay.setImageResource(R.drawable.ic_play_pointeuse_white)
                    circleView.circleColor = Color.parseColor("#5793CE")
                }else
                {
                    imgStopPlay.setImageResource(R.drawable.ic_stop_pointeuse)
                    circleView.circleColor = Color.parseColor("#5793CE")
                }
            } else {
                imgStopPlay.setImageResource(R.drawable.ic_stop_pointeuse)
                val activitiePointeuseColor = (application as SmobileApp).codeCouleurActivitiePointeuseStarted
                try {
                    circleView.circleColor = Color.parseColor(activitiePointeuseColor.take(7))
                }catch (ex:Exception){
                    circleView.circleColor = Color.parseColor("#5793CE")
                }
                hideMenuPointeuse()
            }
        })
        SmobileApp.instance!!.duration.observe(this, {
            val hours: Int = it / 3600
            val minutes: Int = (it / 60)%60
            val seconds: Int = it % 60
            txt_chronometre.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        })
    }

    /**
         * Hide menu pointeuse
         *
         */
        private fun hideMenuPointeuse()
        {
            recyclerViewTypesActivitiesPointeuse.visibility = View.GONE
        }

        private fun showMenuPointeuse()
        {
            recyclerViewTypesActivitiesPointeuse.visibility = View.VISIBLE
    }

    private fun setupNavigation()
    {
        btn_send_comment.setOnClickListener {
            val strCommentaire = txt_commentaire.text.toString()
            if(strCommentaire.isNotEmpty()){
                viewModel.ajoutCommentaire(strCommentaire)
            }else{
                Toast.makeText(baseContext,getString(R.string.ajouter_un_commentaire),Toast.LENGTH_LONG).show()
            }
        }
        btn_next_date.setOnClickListener {
            val dt = viewModel.day.value!!
            val c = Calendar.getInstance()
            c.time = dt
            c.add(Calendar.DATE, 1)
            viewModel.day.value = c.time
            viewModel.getListActivitePointeuseByDay()
            viewModel.getCommentairesByDay()
        }
        btn_previous_date.setOnClickListener {
            val dt = viewModel.day.value!!
            val c = Calendar.getInstance()
            c.time = dt
            c.add(Calendar.DATE, -1)
            viewModel.day.value = c.time
            viewModel.getListActivitePointeuseByDay()
            viewModel.getCommentairesByDay()
        }
        btn_commentaires.setOnClickListener {
            showAnimation()
        }
        btn_close_commetaires.setOnClickListener {
            hideAnimation()
        }
        img_arrow_back.setOnClickListener {
            onBackPressed()
        }
        btn_start.setOnClickListener {
            val menuIsVisible  = recyclerViewTypesActivitiesPointeuse.visibility == View.VISIBLE
            val isChronoStrated = SmobileApp.instance!!.isChronoStarted.value!!
            if(isChronoStrated && !menuIsVisible) // stop current activity
            {
                (application as SmobileApp).stop(this)

            }else if(!isChronoStrated && menuIsVisible) // hide menu
            {
                hideMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value = SmobileApp.instance!!.isChronoStarted.value
            } else if(!isChronoStrated && !menuIsVisible) // show menu
            {
                showMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value = SmobileApp.instance!!.isChronoStarted.value
            }
        }
    }

    private fun observeTypeActivities()
    {
       viewModel.typeActivities.observe(this,{
          when(it.status){
              Status.SUCCESS -> {
                 typeActivitiePointeuseAdapter.items = it.data!!
                 typeActivitiePointeuseAdapter.notifyDataSetChanged()
              }
              Status.ERROR -> {
                 Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
              }
          }
       })
    }

    private fun observeActivities()
    {
        viewModel.activities.observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val bareData = convertActivitesPointeuseToBareData(resource.data!!)
                    barChart.data = bareData
                    barChart.invalidate()
                    var sum = 0L
                    resource.data.forEach {
                        try {
                            if (it.duree != null)
                                if (it.duree!!.minutes > 0) {
                                    sum += it.duree!!.totalMilliseconds.toLong()
                                }
                        } catch (ex: Exception) {
                        }
                    }
                    txt_cumul.text = millisToTime(sum)
                }
                Status.ERROR -> {
                    Toast.makeText(baseContext, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeCurrentDay(){
        viewModel.day.observe(this,{
            val sdf = SimpleDateFormat("dd MMMM yyyy",locale)
            txt_date.text = sdf.format(it)
        })
    }

    private fun observeAjoutCommentaireSingleEvent(){
        viewModel.ajoutCommentairesResponse.observe(this,{
            when(it.status){
                Status.SUCCESS -> {
                    if(it.data!!){
                        txt_commentaire.setText("")
                        viewModel.getCommentairesByDay()
                    }else{
                        Toast.makeText(baseContext,"pas d'activities",Toast.LENGTH_LONG).show()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(baseContext,it.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        lifecycleScope.launch (Dispatchers.Main){
            val typeActivityPointeuse = typeActivitiePointeuseAdapter.items[position].id
            val isActivitieStarted = SmobileApp.instance!!.start(this@PointeuseGraphActivity,typeActivityPointeuse)
            if(isActivitieStarted)
            {
                hideMenuPointeuse()
            }
        }
    }

    private fun setUpChartWithAttributes()
    {
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        val description = Description()
        description.text = ""
        barChart.description = description
        barChart.xAxis.axisMaximum = 24F
        barChart.xAxis.axisMinimum = 0F
        barChart.axisLeft.axisMaximum = 5F
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.axisLineColor = ContextCompat.getColor(this, R.color.grayChart)
        barChart.xAxis.labelCount = 24
        barChart.setTouchEnabled(true)
        barChart.setDrawBarShadow(false)
        barChart.setPinchZoom(true)
    }

    private val refreshListActivitiesPointeuseReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            viewModel.getListActivitePointeuseByDay()
        }
    }

    private fun registerRefreshListActivitiesPointeuseReceiver() {
        val filter = IntentFilter("refreshListActivitiesPointeuseReceiver")
        registerReceiver(refreshListActivitiesPointeuseReceiver, filter)
    }

    private fun unregisterRefreshListActivitiesPointeuseReceiver() {
        unregisterReceiver(refreshListActivitiesPointeuseReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(refreshListActivitiesPointeuseReceiver)
    }
}