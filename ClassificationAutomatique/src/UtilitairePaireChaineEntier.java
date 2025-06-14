import java.util.ArrayList;

public class UtilitairePaireChaineEntier {

    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        return rechIndDicho(listePaires, chaine);
    }


    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int index = indicePourChaine(listePaires, chaine);
        if (index != -1) {
            return listePaires.get(index).getEntier();
        } else {
            return 0;
        }
    }


    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        PaireChaineEntier max = listePaires.get(0);
        for (int i = 0; i < listePaires.size(); i++) {
            if (max.getEntier() < listePaires.get(i).getEntier()) {
                max = listePaires.get(i);
            }
        }
        return max.getChaine();
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        float somme = 0.0f;
        for (int i = 0; i < listePaires.size(); i++) {
            somme = somme + listePaires.get(i).getEntier();
        }
        return somme / listePaires.size();
    }

    public static ArrayList<PaireChaineEntier> triBulle_O(ArrayList<PaireChaineEntier> listePaires) {

        ArrayList<PaireChaineEntier> listetriee = new ArrayList<>(listePaires);
        int j;
        boolean onAPermute = true;
        int i = 0;
        while (onAPermute) {
            j = listetriee.size() - 1;
            onAPermute = false;
            while (j > i) {
                if (listetriee.get(j).compareTo(listetriee.get(j - 1)) < 0) {
                    PaireChaineEntier temporaire = listetriee.get(j);
                    listetriee.set(j, listetriee.get(j - 1));
                    listetriee.set(j - 1, temporaire);
                    onAPermute = true;
                }

                j = j - 1;
            }
            i++;
        }
        return listetriee;
    }


    public static int triInsert_O(ArrayList<PaireChaineEntier> listePaires) {

        int j;
        int cpt = 0;
        PaireChaineEntier valAPlacer;
        int i = 1;
        while (i < listePaires.size()) {
            j = i;

            valAPlacer = listePaires.get(i);
            while (j > 0 && listePaires.get(j - 1).compareTo(valAPlacer) > 0) {

                listePaires.set(j, listePaires.get(j - 1));
                j--;
                cpt++;
            }
            if (j > 0) {
                cpt++;
            }


            listePaires.set(j, valAPlacer);
            i++;
        }

        return cpt;

    }


    public static ArrayList<PaireChaineEntier> triSelect_O(ArrayList<PaireChaineEntier> listePaires) {

        int i = 0;
        int cpt = 0;
        while (i < listePaires.size() - 1) {
            int indMin = i;
            int j = i + 1;
            while (j < listePaires.size()) {
                cpt++;
                if (listePaires.get(j).compareTo(listePaires.get(indMin)) <= 0) {
                    indMin = j;
                }
                j = j + 1;
            }
            if (indMin != i) {
                PaireChaineEntier temp = listePaires.get(i);
                listePaires.set(i, listePaires.get(indMin));
                listePaires.set(indMin, temp);
            }
            i++;

        }
        return listePaires;
    }

    public static int rechIndDicho(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int cpt = 0;
        int i = 0;
        int j = listePaires.size() - 1;

        if (i >= j) {
            return -1;
        }

        while (i < j) {
            int m = (i + j) / 2;
            if (listePaires.get(m).getChaine().compareTo(chaine) < 0) {
                i = m + 1;
            } else {
                j = m;
            }
        }
        if (listePaires.get(i).getChaine().equals(chaine)) {
            return i;
        } else {
            return -1;
        }

    }
}