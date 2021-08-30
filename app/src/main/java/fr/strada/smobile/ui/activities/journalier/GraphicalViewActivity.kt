package fr.strada.smobile.ui.activities.journalier

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.activites.day.Activite
import fr.strada.smobile.databinding.ActivityGraphicalViewBinding
import fr.strada.smobile.di.graphical_view.GraphicalViewComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.activities.mensuel.Utils.millisToTime
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_graphical_view.*
import javax.inject.Inject


class GraphicalViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGraphicalViewBinding
    private lateinit var component : GraphicalViewComponent
    private lateinit var viewModel : GraphicalViewViewModel
    @Inject
    internal lateinit var providerFactory : ViewModelProviderFactory
    //@Inject
    //internal lateinit var adapter : CommentGraphAdapter
    @Inject
    internal lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        configViewComments()
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        bindCurrentDate()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        setUpChartWithAttributes()
        observeActivitesJournaliere()
        if(savedInstanceState == null){
            val date = getCurrentStrDay()
            viewModel.getActivitesJournaliere(date)
        }
    }


    private fun configViewComments()
    {   val tabletSize = resources.getBoolean(R.bool.isTablet)
        val dp = if(tabletSize) { 230f }else { 140f }
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val metrics = this.resources.displayMetrics
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        (recycleView.layoutParams as ConstraintLayout.LayoutParams).matchConstraintMaxHeight = height - pixels
    }

    private fun initComponent()
    {
        component = (application as SmobileApp).appComponent.graphicalViewComponent().setContext(this).build()
    }

    private fun injectDependencies()
    {
        component.inject(this)
    }

    private fun setupBinding()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_graphical_view)
        binding.lifecycleOwner = this
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProvider(this, providerFactory).get(GraphicalViewViewModel::class.java)
    }

    private fun bindViewModel()
    {
        binding.viewModel = viewModel
    }

    private fun setupNavigation()
    {
        viewModel.pressBtnBackEvent.observe(this, Observer {
            onBackPressed()
        })
        viewModel.pressBtnShowCommentsEvent.observe(this, Observer {
            showCommentsView()
        })
        viewModel.pressBtnHideCommentsEvent.observe(this, Observer {
            hideCommentsView()
        })
        viewModel.pressBtnNextDayEvent.observe(this, Observer {
            val date = getCurrentStrDay()
            viewModel.getActivitesJournaliere(date)
        })
        viewModel.pressBtnPreviousDayEvent.observe(this, Observer {
            val date = getCurrentStrDay()
            viewModel.getActivitesJournaliere(date)
        })
        viewModel.pressBtnSendCommentEvent.observe(this, Observer {
            // recycleView.scrollToPosition(viewModel.comments.value!!.size-1)
        })
    }

    private fun bindCurrentDate()
    {
        val hasParames = intent.hasExtra("year") && intent.hasExtra("month") && intent.hasExtra("day")
        if(hasParames)
        {
            val year = intent.getIntExtra("year",0)
            val month = intent.getIntExtra("month",0)
            val day = intent.getIntExtra("day",0)
            viewModel.currentDay.value = Day(year,month,day)
        }
    }

    private fun attachAdapterInRecycleView()
    {
        // recycleView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView()
    {
        recycleView.layoutManager = layoutManager
    }

    private fun setUpChartWithAttributes()
    {
        mp_barChart.axisLeft.isEnabled = false
        mp_barChart.axisRight.isEnabled = false
        mp_barChart.legend.isEnabled = false
        mp_barChart.xAxis.setDrawGridLines(false)
        mp_barChart.axisLeft.setDrawGridLines(false)
        val description = Description()
        description.text = ""
        mp_barChart.description = description
        mp_barChart.xAxis.axisMaximum = 24F
        mp_barChart.xAxis.axisMinimum = 0F
        mp_barChart.axisLeft.axisMaximum = 5F
        mp_barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        mp_barChart.xAxis.axisLineColor = ContextCompat.getColor(this, R.color.grayChart)
        mp_barChart.xAxis.labelCount = 24
        mp_barChart.setTouchEnabled(true)
        mp_barChart.setDrawBarShadow(false)
        mp_barChart.setPinchZoom(true)
    }

    private fun observeActivitesJournaliere()
    {
        viewModel.activitesJournaliere.observe(this , {
            when(it.status){
                Status.SUCCESS -> {
                    val totalList = ArrayList<Activite>()
                    totalList.addAll(it.data!!.activitesPointeuse)
                    totalList.addAll(it.data.activitesConduite)
                    val bareData = convertActivitesPointeuseConduiteToBareData(totalList)
                    mp_barChart.data = bareData
                    mp_barChart.invalidate()
                    txt_duration.text = millisToTime(totalList.map { if(it.duree!=null){ it.duree.totalMilliseconds }else{ 0L } }.sum())
                }
                Status.ERROR -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun sendSelectDateReceiver(currentDay:Day)
    {
        val intent = Intent("SelectDateReceiver")
        intent.putExtra("day",currentDay.day)
        intent.putExtra("month",currentDay.month)
        intent.putExtra("year",currentDay.year)
        sendBroadcast(intent)
    }

    override fun onBackPressed()
    {
        sendSelectDateReceiver(viewModel.currentDay.value!!)
        super.onBackPressed()
    }
    //---------------------------------manager comments view------------------------------//
    private fun showCommentsView()
    {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val x1= width * 0.4
        val x2= width * 0.2
        view_date_picker.animate().translationX(-x2.toFloat()).setStartDelay(120).duration = 1000
        val metrics = this.resources.displayMetrics
        val dp = 50f
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        val anim = ValueAnimator.ofInt(view_cumul.measuredWidth, pixels)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view_cumul.getLayoutParams()
            layoutParams.width = `val`
            view_cumul.setLayoutParams(layoutParams)
        }
        anim.duration = 1000
        anim.start()
        ///
        val animViewComments = ValueAnimator.ofFloat(0f, 0.4f)
        animViewComments.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val params1: ConstraintLayout.LayoutParams = verticalGuideline1.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = 1f - valeur
            verticalGuideline1.layoutParams = params1
            val params2: ConstraintLayout.LayoutParams = verticalGuideline2.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = 1.4f - valeur
            verticalGuideline2.layoutParams = params2
        }
        animViewComments.duration = 1000
        animViewComments.start()
    }

    private fun hideCommentsView()
    {
        view_date_picker.animate().translationX(0f).setStartDelay(100).duration = 1000
        val metrics = this.resources.displayMetrics
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        val dp = if(tabletSize){ 235f }else{ 190f }
        val fpixels = metrics.density * dp
        val pixels = (fpixels + 0.5f).toInt()
        val anim = ValueAnimator.ofInt(view_cumul.measuredWidth, pixels)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                view_cumul.getLayoutParams()
            layoutParams.width = `val`
            view_cumul.setLayoutParams(layoutParams)
        }
        anim.duration = 1000
        anim.start()
        ///
        val animViewComments = ValueAnimator.ofFloat(0f, 0.4f)
        animViewComments.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val params1: ConstraintLayout.LayoutParams = verticalGuideline1.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = 0.6f + valeur
            verticalGuideline1.layoutParams = params1
            val params2: ConstraintLayout.LayoutParams = verticalGuideline2.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = 1f + valeur
            verticalGuideline2.layoutParams = params2
        }
        animViewComments.duration = 1000
        animViewComments.start()
    }

    private fun getCurrentStrDay() : String {
        val locale  = Utils.getCurrentLocal(this)
        return getStrDate(viewModel.currentDay.value!!.year,viewModel.currentDay.value!!.month+1,viewModel.currentDay.value!!.day,locale)
    }
}
