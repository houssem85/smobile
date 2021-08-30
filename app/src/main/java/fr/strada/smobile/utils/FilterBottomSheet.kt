package fr.strada.smobile.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.strada.smobile.R

class FilterBottomSheet : BottomSheetDialogFragment() {

    lateinit var btnAZ: AppCompatButton
    lateinit var btnZA: AppCompatButton
    lateinit var switshDateEcheance: Switch
    lateinit var switchAlpha: Switch
    lateinit var switchCongePaye: Switch
    lateinit var switchCongeSansSolde: Switch
    lateinit var switchRecuperation: Switch
    lateinit var switchReposCompensateur: Switch
    var switshAlphabetiqueChecked = false
    var switshAlphabetiqueCheckedAZ = true

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialogTheme)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.dialog_bottomsheet_filter, container, false)
        init(view)
        filter()
        return view
    }

    private fun init(view: View) {
        btnAZ = view.findViewById(R.id.btn_a_z)
        btnZA = view.findViewById(R.id.btn_z_a)
        switshDateEcheance = view.findViewById<Switch>(R.id.switch_date_echeance)
        switchAlpha = view.findViewById<Switch>(R.id.switch_alpha)
        switchCongePaye = view.findViewById<Switch>(R.id.switch_conge_paye)
        switchCongeSansSolde = view.findViewById<Switch>(R.id.switch_conge_sans_solde)
        switchRecuperation = view.findViewById<Switch>(R.id.switch_recuperation)
        switchReposCompensateur = view.findViewById<Switch>(R.id.switch_repos_compensateur)
    }


    private fun filter() {
        switchAlpha.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                btnAZ.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_blue)
                btnZA.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_solde_white)
                btnZA.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                btnAZ.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                btnAZ.performClick()
                btnAZ.isEnabled = true
                btnZA.isEnabled = true
                switshAlphabetiqueChecked = true

                if (switshAlphabetiqueCheckedAZ) {
                    btnAZ.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_blue)
                    btnZA.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_solde_white)
                    btnZA.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    btnAZ.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                    switshAlphabetiqueCheckedAZ = true
                } else {
                    btnAZ.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_solde_white)
                    btnZA.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_blue)
                    btnZA.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                    btnAZ.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    switshAlphabetiqueCheckedAZ = false
                }

            } else {
                btnAZ.isEnabled = false
                btnZA.isEnabled = false
                btnAZ.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_gray)
                btnZA.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_gray)
                btnZA.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                btnAZ.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                switshAlphabetiqueChecked = false
            }

        }

        btnAZ.setOnClickListener {

            switshAlphabetiqueCheckedAZ = true
            if (switshAlphabetiqueChecked) {
                btnAZ.isEnabled = true
                btnZA.isEnabled = true
                btnAZ.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_filter_blue)
                btnZA.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_solde_white)
                btnZA.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                btnAZ.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                switshAlphabetiqueCheckedAZ = true
            } else {
                switshAlphabetiqueCheckedAZ = false
            }

        }
        btnZA.setOnClickListener {
            switshAlphabetiqueCheckedAZ = false

            if (switshAlphabetiqueChecked) {
                btnAZ.isEnabled = true
                btnZA.isEnabled = true
                btnAZ.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_solde_white)
                btnZA.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_widget_blue)
                btnZA.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
                btnAZ.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                switshAlphabetiqueCheckedAZ = false


            } else {

                switshAlphabetiqueCheckedAZ = true
            }

        }


    }
}


