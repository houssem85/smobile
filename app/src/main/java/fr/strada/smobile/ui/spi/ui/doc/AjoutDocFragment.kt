package fr.strada.smobile.ui.spi.ui.doc

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shrikanthravi.collapsiblecalendarview.data.Day
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.activities.mensuel.Utils
import fr.strada.smobile.ui.demande_absence.TodayDecorator
import fr.strada.smobile.ui.infractions.detail_infraction.Utils.correcteImageOrientation
import fr.strada.smobile.ui.infractions.detail_infraction.Utils.getResizedBitmap
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.dialog_select_pdf_camera_gallery.*
import kotlinx.android.synthetic.main.fragment_ajout_doc.*
import kotlinx.android.synthetic.main.fragment_calendrier_equipe.*
import kotlinx.android.synthetic.main.fragment_nouvelle_depense.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [AjoutDocFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class AjoutDocFragment : Fragment() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var viewModel: DocViewModel
    private lateinit var dialogSelectDate: Dialog
    private lateinit var dialogSelectCameraCallery: BottomSheetDialog

    val PICK_PDF_FILE = 2
    var listOfPDF: ArrayList<ImageModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajout_doc, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectDependencies()
        initViewModel()
        initDialogSelectDate()
        initDialogSelectCameraGallery()
        navigation()
        setDropDown()
    }


    private fun injectDependencies() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(), providerFactory).get(
            DocViewModel::class.java
        )
    }

    private fun navigation() {
        input_date.setOnClickListener {
            dialogSelectDate.show()
        }

        imageViewadddoc.setOnClickListener {
            if (groupeImageDoc.images!!.size < 3) {
                dialogSelectCameraCallery.show()

            }
        }
        button.setOnClickListener {
            generatePDF()
        }
    }

    private fun setDropDown() {
        val items = listOf("Permis de conduite", "Carte d'identité", "Option3", "Option 4")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_document, items)


        (selectdoctype)?.setAdapter(adapter)

        selectdoctype.setText("Type du document", false)
    }

    private fun initDialogSelectDate() {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_date, null)
        val datePicker =
            dialogView.findViewById<MaterialCalendarView>(fr.strada.smobile.R.id.datePicker)
        val btnTerminer = dialogView.findViewById<Button>(fr.strada.smobile.R.id.btnTerminer)
        val btnAnnuler = dialogView.findViewById<Button>(fr.strada.smobile.R.id.btnAnnuler)
        val currentDate = CalendarDay.today()
        dialogBuilder.setView(dialogView)
        datePicker.selectedDate = currentDate
        datePicker.addDecorators(TodayDecorator(requireActivity(), currentDate))
        datePicker.state().edit().setMinimumDate(currentDate).commit()
        input_date.setText(viewModel.dateDepense.value.toString())
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

    private fun initDialogSelectCameraGallery() {
        dialogSelectCameraCallery =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogThemeTransparent)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_select_pdf_camera_gallery, null)
        val btnAnnuler = dialogView.findViewById<AppCompatTextView>(R.id.btnAnnuler)
        val btnCamera = dialogView.findViewById<AppCompatTextView>(R.id.btnCamera)
        val btnCalerie = dialogView.findViewById<AppCompatTextView>(R.id.btnCalerie)
        val btnPdf = dialogView.findViewById<AppCompatTextView>(R.id.btnPdf)
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
        btnPdf.setOnClickListener {
            onClickPDFListner(dialogSelectCameraCallery)
        }
    }

    fun openFile() {
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(browseStorage, "Select PDF"), 99
        )
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
        input_date.setText(viewModel.dateDepense.value.toString())
        dialog.dismiss()
    }

    fun onClickAnnulerlistner(dialog: Dialog, datePicker: MaterialCalendarView) {
        dialog.dismiss()
    }

    //-----------------------DialogSelectCameraGalleryListner------------------------//

    fun onClickAnnulerListner(dialog: BottomSheetDialog) {
        dialog.dismiss()
    }

    private fun onClickGalleryListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this)
            .galleryMimeTypes(mimeTypes = arrayOf("image/png", "image/jpg", "image/jpeg"))
            .galleryOnly().start()
    }

    private fun onClickCameraListner(dialog: BottomSheetDialog) {
        ImagePicker.with(this).cameraOnly().start()
    }

    private fun onClickPDFListner(dialog: BottomSheetDialog) {

        openFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (requestCode == 99 && data != null) {

                    listOfPDF.add(ImageModel(data.data!!, "pdf"))
                    val strTime = Calendar.getInstance().time.toString()
                    val drawable = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_pdf_file_svgrepo_com
                    )
                    val bm = drawable?.toBitmap()
                    groupeImageDoc.addImage(
                        fr.strada.smobile.ui.infractions.detail_infraction.Utils.imageDrawableToFile(
                            bm!!,
                            requireContext().filesDir.path + "/" + strTime + "imagePDF.png"
                        )
                    )
                    scrollToBottom()

                } else {
                    Timber.tag("camera").e("image view")
                    dialogSelectCameraCallery.dismiss()
                    val file: File? = ImagePicker.getFile(data)
                    if (file != null) {
                        val isExtensionImage =
                            fr.strada.smobile.ui.infractions.detail_infraction.Utils.isExtensionImage(
                                file.extension
                            )
                        if (isExtensionImage) {
                            listOfPDF.add(ImageModel(data!!.data!!, "image"))
                            groupeImageDoc.addImage(file)
                            scrollToBottom()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.le_format_selectionne_est_incorrect),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.veuillez_selectionner_un_document_depuis_votre_memoire_interne),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {

            }
        }
    }

    private fun generatePDF() {
        //delete old pdf file
        val oldfile =
            File(requireContext().filesDir.path + "/" + "final.pdf")
        oldfile.delete()
        var imagefound = false

        val strTime = Calendar.getInstance().time.toString()
        val file = File(requireContext().filesDir.path + "/" + strTime + "_file.pdf")

        val pdfDocument = PdfDocument()
        var i = 0
        //add taken images to the new generated file
        listOfPDF.forEach {
            if (it.type.equals("image")) {
                imagefound = true
                val ei = ExifInterface(it.uri.path!!)

                var bitmap = BitmapFactory.decodeFile(it.uri.path!!)
                bitmap = getResizedBitmap(bitmap, 600)
                bitmap = correcteImageOrientation(it.uri.path!!, bitmap)

                i++
                val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
                val canvas: Canvas = page.canvas


                val paint = Paint()
                paint.color = Color.WHITE

                canvas.drawPaint(paint)

                canvas.drawBitmap(bitmap, 1f, 1f, paint)
                pdfDocument.finishPage(page)
            }


        }

        val fileOutputStream = FileOutputStream(file, true)

        pdfDocument.writeTo(fileOutputStream)

        pdfDocument.close()
        // add new streams to the generated file
        val ut = PDFMergerUtility()

        if (imagefound) {
            val inputStream1 = file.inputStream()
            ut.addSource(inputStream1)
        }

        listOfPDF.forEach {
            if (it.type.equals("pdf")) {

                val inputStream = requireContext().contentResolver.openInputStream(it.uri)
                ut.addSource(inputStream)

            }


        }

        val file2 =
            File(requireContext().filesDir.path + "/" + strTime + "_final.pdf")

        val fileOutputStream2 = FileOutputStream(file2)
        try {
            ut.destinationStream = fileOutputStream2
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly())
        } finally {

            fileOutputStream.close()
        }
        imagefound = false

        lifecycleScope.launch(Dispatchers.IO) {
            Timber.tag("api").e("api call")
            val actualFile = file2 // log contenue file
            val requestFile: RequestBody =
                RequestBody.Companion.create("multipart/form-data".toMediaTypeOrNull(), actualFile)
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", actualFile.name, requestFile)
             delay(1000)
            viewModel.uploadFile(body)
        }


    }


    private fun scrollToBottom() {
        nestedScrollViewDoc.post { nestedScrollViewDoc.fullScroll(View.FOCUS_DOWN) }
    }


}

data class ImageModel(
    val uri: Uri,
    val type: String
)