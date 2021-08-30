package fr.strada.smobile.di.infractions.detail_infraction

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import fr.strada.smobile.ui.infractions.detail_infraction.DetailInfractionFragment
import fr.strada.smobile.utils.listner.DialogEtesVousSurDeVouloirQuitterListner
import fr.strada.smobile.utils.listner.DialogImageViewerListner
import fr.strada.smobile.utils.listner.DialogSelectCameraGalleryListner

@Subcomponent(modules = [DetailInfractionModule::class,DetailInfractionViewModelModule::class])
interface DetailInfractionComponent {

    fun inject(detailInfractionFragment: DetailInfractionFragment)

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun setDialogEtesVousSurDeVouloirQuitterListner(dialogEtesVousSurDeVouloirQuitterListner: DialogEtesVousSurDeVouloirQuitterListner?) : Builder

        @BindsInstance
        fun setDialogImageViewerListner(dialogImageViewerListner: DialogImageViewerListner?) : Builder

        @BindsInstance
        fun setDialogSelectCameraGalleryListner(dialogSelectCameraGalleryListner: DialogSelectCameraGalleryListner?) : Builder

        @BindsInstance
        fun setContext(context: Context) : Builder

        fun build() : DetailInfractionComponent
    }
}