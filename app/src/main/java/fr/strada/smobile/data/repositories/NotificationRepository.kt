package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.NotificationModel
import javax.inject.Inject

class NotificationRepository @Inject constructor(){

   fun getNotification(): ArrayList<NotificationModel>{

       val data: ArrayList<NotificationModel> = ArrayList()

       data.add(
           NotificationModel(
               title = "Demande d'absence",
               description = "Votre demande d'absence pour le 15/04/2020 a été acceptée le 10/04/2020",
               time = " il y a 2 min"
           )
       )
       data.add(
           NotificationModel(
               title = "Arrêt de la Pointeuse",
               description = "N'oubliez pas d'arrêter votre pointeuse avant de fermer l'application",
               time = "il y a 2 min"
           )
       )
       return data
   }

    fun getAllNotifications(): ArrayList<NotificationModel>{

        val data: ArrayList<NotificationModel> = ArrayList()

        data.add(
            NotificationModel(
                title = "Mes notes de frais",
                description = "Votre note de frais pour le 15/04/2020 a été acceptée le 10/04/2020",
                time = " il y a 2 min"
            )
        )
        data.add(
            NotificationModel(
                title = "Arrêt de la Pointeuse",
                description = "Votre temps de travail journalier a dépassé 10h",
                time = "il y a 2 min"
            )
        )
        data.add(
            NotificationModel(
                title = "Demande d'absence",
                description = "Votre demande d'absence pour le 15/04/2020 a été acceptée le 10/04/2020",
                time = "il y a 2 min"
            )
        )
        data.add(
            NotificationModel(
                title = "Infractions",
                description = "Une infraction a été relevée sur votre temps de conduite",
                time = "il y a 2 min"
            )
        )
        data.add(
            NotificationModel(
                title = "Demande d'absence",
                description = "Votre demande d'absence pour le 15/04/2020 a été acceptée le 10/04/2020",
                time = "il y a 2 min"
            )
        )
        data.add(
            NotificationModel(
                title = "Arrêt de la Pointeuse",
                description = "Pensez à arrêter la pointeuse avant de fermer l'application",
                time = "il y a 2 min"
            )
        )
        return data

    }


}