import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {

    private String nom;
    private ArrayList<PaireChaineEntier> lexique;

    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }

    public void setLexique(ArrayList<PaireChaineEntier> lexique) {
        this.lexique = lexique;
    }

    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        lexique = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int indexSeparator = ligne.indexOf(':');
                String mot = ligne.substring(0, indexSeparator);
                int poids = Integer.parseInt(ligne.substring(indexSeparator + 1));
                PaireChaineEntier p=new PaireChaineEntier(mot, poids);
                lexique.add(p);
            }
            UtilitairePaireChaineEntier.triSelect_O(lexique);
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        int result=0;
        int i = 0;
        ArrayList<String>mots=d.getMots();
        while ( i < mots.size() ) {
            result = result + UtilitairePaireChaineEntier.entierPourChaine(this.getLexique(),mots.get(i));
            i++;

        }

        return result;
    }


}
