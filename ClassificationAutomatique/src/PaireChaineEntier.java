public class PaireChaineEntier implements Comparable<PaireChaineEntier> {
private String chaine;
private int entier;

    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        this.entier = entier;
    }

    public String getChaine() {
        return chaine;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    public int getEntier() {
        return entier;
    }

    public void setEntier(int entier) {
        this.entier = entier;
    }

    @Override
    public String toString() {
        return "Lexique {" +
                "Mots='" + chaine + '\'' +
                ", poids=" + entier +
                '}';
    }

    @Override
    public int compareTo(PaireChaineEntier autre) {
        return this.chaine.compareTo(autre.chaine);
    }
}
