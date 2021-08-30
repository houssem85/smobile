package fr.strada.smobile.ui.pointeuse_graph

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import fr.strada.smobile.R
import fr.strada.smobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pointeuse_graph.*

open class PointeuseGraphActivityAnimations : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pointeuse_graph)
    }

    protected fun showAnimation()
    {
        val anim1 = ValueAnimator.ofFloat( 0f,0.35f)
        val params1: ConstraintLayout.LayoutParams = guideline_right_commentaire.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneRightCommentaires = params1.guidePercent
        val params2: ConstraintLayout.LayoutParams = guideline_left_commentaire.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneLeftCommentaires = params2.guidePercent
        anim1.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val param1: ConstraintLayout.LayoutParams = guideline_right_commentaire.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = percentQuideLigneRightCommentaires - valeur
            guideline_right_commentaire.layoutParams = param1
            val param2: ConstraintLayout.LayoutParams = guideline_left_commentaire.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = percentQuideLigneLeftCommentaires - valeur
            guideline_left_commentaire.layoutParams = param2
        }
        anim1.duration = 800
        anim1.doOnStart {
            btn_commentaires.isEnabled = false
            btn_close_commetaires.isEnabled = false
        }
        anim1.doOnEnd {
            btn_commentaires.isEnabled = true
            btn_close_commetaires.isEnabled = true
        }
        anim1.start()
        val anim2 = ValueAnimator.ofFloat( 0f,0.15f)
        val params3: ConstraintLayout.LayoutParams = guideline_right_date.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneRightDate = params3.guidePercent
        val params4: ConstraintLayout.LayoutParams = guideline_left_date.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneLeftDate = params4.guidePercent
        anim2.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val param3: ConstraintLayout.LayoutParams = guideline_right_date.layoutParams as ConstraintLayout.LayoutParams
            param3.guidePercent = percentQuideLigneRightDate - valeur
            guideline_right_date.layoutParams = param3
            val param4: ConstraintLayout.LayoutParams = guideline_left_date.layoutParams as ConstraintLayout.LayoutParams
            param4.guidePercent = percentQuideLigneLeftDate - valeur
            guideline_left_date.layoutParams = param4
        }
        anim2.duration = 800
        anim2.start()
    }

    protected fun hideAnimation()
    {
        val anim1 = ValueAnimator.ofFloat( 0f,0.35f)
        val params1: ConstraintLayout.LayoutParams = guideline_right_commentaire.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneRightCommentaires = params1.guidePercent
        val params2: ConstraintLayout.LayoutParams = guideline_left_commentaire.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneLeftCommentaires = params2.guidePercent
        anim1.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val param1: ConstraintLayout.LayoutParams = guideline_right_commentaire.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = percentQuideLigneRightCommentaires + valeur
            guideline_right_commentaire.layoutParams = param1
            val param2: ConstraintLayout.LayoutParams = guideline_left_commentaire.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = percentQuideLigneLeftCommentaires + valeur
            guideline_left_commentaire.layoutParams = param2
        }
        anim1.duration = 800
        anim1.start()
        val anim2 = ValueAnimator.ofFloat( 0f,0.15f)
        val params3: ConstraintLayout.LayoutParams = guideline_right_date.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneRightDate = params3.guidePercent
        val params4: ConstraintLayout.LayoutParams = guideline_left_date.layoutParams as ConstraintLayout.LayoutParams
        val percentQuideLigneLeftDate = params4.guidePercent
        anim2.addUpdateListener { valueAnimator ->
            val valeur = valueAnimator.animatedValue as Float
            val param3: ConstraintLayout.LayoutParams = guideline_right_date.layoutParams as ConstraintLayout.LayoutParams
            param3.guidePercent = percentQuideLigneRightDate + valeur
            guideline_right_date.layoutParams = param3
            val param4: ConstraintLayout.LayoutParams = guideline_left_date.layoutParams as ConstraintLayout.LayoutParams
            param4.guidePercent = percentQuideLigneLeftDate + valeur
            guideline_left_date.layoutParams = param4
        }
        anim2.duration = 800
        anim2.start()
    }
}