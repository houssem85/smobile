package fr.strada.smobile.utils.cardlib

import java.util.regex.Pattern

class Validation {
    companion object
    {
        fun isEmailValide(email:String):Boolean
        {
            var emailPattren= Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            return emailPattren.matcher(email).matches() && email.length > 0
        }

        fun isNotEmpty(str:String):Boolean
        {
            return str.trim().isNotEmpty()
        }

        fun isPasswordIdenticalConfirmPassword(password:String,confirmPassword:String):Boolean
        {
            return (password == confirmPassword) && password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty()
        }


        fun isRegxNameValide(str:String):Boolean
        {
            return str.matches("^[a-zA-Z]*$".toRegex())
        }

        fun isLenghtNameValide(str:String):Boolean
        {
            return str.length in 2..20
        }

        fun isContactNoValide(contactNo:String):Boolean
        {
          return  (contactNo.matches("^\\+[0-9]*\$".toRegex())  && contactNo.length in 9..14 )
                  || (contactNo.matches("^[0-9]*\$".toRegex()) && contactNo.length in 8..13)
        }

    }
}