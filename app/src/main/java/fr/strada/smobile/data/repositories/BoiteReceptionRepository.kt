package fr.strada.smobile.data.repositories

import fr.strada.smobile.R
import fr.strada.smobile.data.models.MessageConversationModel
import fr.strada.smobile.data.models.MessageModel
import fr.strada.smobile.ui_tablette.messagerie.boite_reception.TypeMessage
import javax.inject.Inject

class BoiteReceptionRepository @Inject constructor() {

    fun getAllMessagesInBoiteReception(): ArrayList<MessageModel> {

        val data: ArrayList<MessageModel> = ArrayList()

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "14:32",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "20:32",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "17:52",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "18:15",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "16:50",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "19:40",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )

        return data

    }

    fun getMessagesLuInBoiteReception(): ArrayList<MessageModel> {

        val data: ArrayList<MessageModel> = ArrayList()

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "20:32",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )



        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "16:50",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "19:40",
                imageProfile = R.drawable.ic_profile,
                imageTypeOfMessage = R.drawable.ic_fleche_envoi,
                typeOfMessage = "reply"
            )
        )

        return data

    }

    fun getMessagesNonLuInBoiteReception(): ArrayList<MessageModel> {

        val data: ArrayList<MessageModel> = ArrayList()

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "14:32",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "17:52",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        data.add(
            MessageModel(
                nameSender = "Miguel Albert",
                messageContent = "Lorum ipsum die dalr ipsum ae ipsum oreum ip ipsum ae...",
                heureEnvoi = "18:15",
                imageProfile = R.drawable.ic_profile_colored,
                imageTypeOfMessage = R.drawable.circle_blue
            )
        )

        return data

    }

    fun getMessagesConversations(): ArrayList<MessageConversationModel> {

        val data: ArrayList<MessageConversationModel> = ArrayList()

        data.add(MessageConversationModel(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd",TypeMessage.RECUS))

        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.ENVOYEE
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        data.add(
            MessageConversationModel(
                "Lorem ipsum dolor sit amee",TypeMessage.RECUS
            )
        )
        return data
    }

}