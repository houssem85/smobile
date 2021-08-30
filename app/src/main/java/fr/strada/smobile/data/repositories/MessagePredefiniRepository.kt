package fr.strada.smobile.data.repositories

import fr.strada.smobile.data.models.MessageModel
import javax.inject.Inject

class MessagePredefiniRepository @Inject constructor() {

    fun getMessagesPredefini(): ArrayList<MessageModel> {

        val data: ArrayList<MessageModel> = ArrayList()

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 1",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "16:32",
                dateEnvoi = "15/07/2020",
                objet = "objet 1"
            )
        )

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 2",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "11:50",
                dateEnvoi = "12/06/2020",
                objet = "objet 2"
            )
        )

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 3",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "21:32",
                dateEnvoi = "10/04/2020",
                objet = "objet 3"
            )
        )

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 4",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "14:32",
                dateEnvoi = "12/07/2020",
                objet = "objet 4"
            )
        )

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 5",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "14:32",
                dateEnvoi = "10/06/2020",
                objet = "objet 5"
            )
        )

        data.add(
            MessageModel(
                titleMessagePredefini = "Objet 6",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "14:32",
                dateEnvoi = "12/07/2020",
                objet = "objet 6"
            )
        )
        return data
    }
}