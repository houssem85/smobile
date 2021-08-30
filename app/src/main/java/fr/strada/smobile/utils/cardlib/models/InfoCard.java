package fr.strada.smobile.utils.cardlib.models;

public class InfoCard {

    public enum Lecteur {
        Conducteur,
        Convoyeur
    }

    public enum EtatConduite {
        Seul,
        Equipage
    }

    public static class EnumMapper {
        public static Lecteur ToLecteur(boolean b) {
            if (b)
                return Lecteur.Convoyeur;

            return Lecteur.Conducteur;
        }

        public static EtatConduite ToEtatConduite(boolean b) {
            if (b)
                return EtatConduite.Equipage;

            return EtatConduite.Seul;
        }

        public enum EtatCarteConducteur {
            Inseree,
            NonInseree
        }

        public static EtatCarteConducteur ToEtatCarteConducteur(boolean b) {
            if (b)
                return EtatCarteConducteur.NonInseree;

            return EtatCarteConducteur.Inseree;
        }

        public enum ActiviteConducteur {
            Repos,
            Disponibilite,
            Travail,
            Conduite
        }

        public static ActiviteConducteur ToActivite(boolean[] biteArray) throws Exception {
            // boolean[] biteArray = byteArray2BitArray(bitarrays);

            if (!biteArray[0] && !biteArray[1])
                return ActiviteConducteur.Repos;

            if (!biteArray[0] && biteArray[1])
                return ActiviteConducteur.Disponibilite;

            if (biteArray[0] && !biteArray[1])
                return ActiviteConducteur.Travail;

            if (biteArray[0] && biteArray[1])
                return ActiviteConducteur.Conduite;

            throw new Exception("Activite inconnue");
        }
    }

}
