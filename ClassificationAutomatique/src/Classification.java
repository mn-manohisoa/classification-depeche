import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Classification {
    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }


    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier);
            // Structures pour garder une trace des comptes corrects et des scores totaux pour chaque catégorie
            ArrayList<PaireChaineEntier> correctCounts = new ArrayList<>();
            ArrayList<PaireChaineEntier> totalScores = new ArrayList<>();

            // Initialisation des compteurs de classifications correctes et des scores totaux
            for (Categorie categorie : categories) {
                correctCounts.add(new PaireChaineEntier(categorie.getNom(), 0));
                totalScores.add(new PaireChaineEntier(categorie.getNom(), 0));
            }
            UtilitairePaireChaineEntier.triSelect_O(correctCounts);
            UtilitairePaireChaineEntier.triSelect_O(totalScores);

            for (Depeche depeche : depeches) {
                ArrayList<PaireChaineEntier> scores = new ArrayList<>();
                //
                for (Categorie categorie : categories) {
                    int score = categorie.score(depeche);
                    PaireChaineEntier res=new PaireChaineEntier(categorie.getNom(), score);
                    scores.add(res);
                }

                String categorieMax = UtilitairePaireChaineEntier.chaineMax(scores);
                file.write(depeche.getId() + ":" + categorieMax + "\n");

                // Mise à jour des compteurs de classifications correctes et des scores totaux
                UtilitairePaireChaineEntier.triSelect_O(totalScores);
                int indexCategorieReelle = UtilitairePaireChaineEntier.indicePourChaine(totalScores, depeche.getCategorie());
                if (indexCategorieReelle != -1) {
                    // Mise à jour des scores totaux pour la catégorie réelle de la dépêche
                    PaireChaineEntier totalScorePair = totalScores.get(indexCategorieReelle);
                    totalScores.set(indexCategorieReelle, new PaireChaineEntier(totalScorePair.getChaine(), totalScorePair.getEntier() + 1));

                    // Mise à jour des classifications correctes si la catégorie prédite correspond à la catégorie réelle
                    if (depeche.getCategorie().equals(categorieMax)) {
                        PaireChaineEntier correctCountPair = correctCounts.get(indexCategorieReelle);
                        correctCounts.set(indexCategorieReelle, new PaireChaineEntier(correctCountPair.getChaine(), correctCountPair.getEntier() + 1));
                    }
                }
            }

            // Écriture des statistiques de classification
            file.write("\n");
            for (int i = 0; i < categories.size(); i++) {
                double pourcentage = (double) correctCounts.get(i).getEntier() / totalScores.get(i).getEntier() * 100;
                file.write(String.format("%s: %.2f%%\n", categories.get(i).getNom(), pourcentage));
            }

            // Calcul et écriture de la moyenne générale des classifications correctes
            double moyenne = UtilitairePaireChaineEntier.moyenne(correctCounts);
            file.write(String.format("MOYENNE : %.2f%%\n", moyenne));

            file.close();
            System.out.println("Les résultats de classification ont été écrits avec succès dans " + nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        ArrayList<String> motsDejaAjoutes = new ArrayList<>();

        for (int i = 0; i < depeches.size(); i++) {
            Depeche depeche = depeches.get(i);
            if (depeche.getCategorie().toUpperCase().equals(categorie.toUpperCase())) {
                ArrayList<String> mots = depeche.getMots();
                for (int j = 0; j <mots.size() ; j++) {
                    if (!motsDejaAjoutes.contains(mots.get(j))) {
                        PaireChaineEntier currentPaire = new PaireChaineEntier(mots.get(j), 0);
                        resultat.add(currentPaire);
                        motsDejaAjoutes.add(mots.get(j));
                    }
                }
            }
        }
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
       int nbrComparaison=0;

        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equals(categorie)) {
                for (String motDep : depeche.getMots()) {
                    for (PaireChaineEntier paire : dictionnaire) {
                        nbrComparaison++;
                        if (paire.getChaine().equals(motDep)) {
                            paire.setEntier(paire.getEntier() + 1);
                        }
                    }
                }
            } else {
                for (String motDep : depeche.getMots()) {
                    for (PaireChaineEntier paire : dictionnaire) {
                        nbrComparaison++;
                        if (paire.getChaine().equals(motDep)) {
                            paire.setEntier(paire.getEntier() - 1);
                        }
                    }
                }
            }
        }
        System.out.println("Le nombre de comparaison d effectués est :  "+ nbrComparaison);
    }

    public static int poidsPourScore(int score) {
        if (score >= 4) {
            return 3;
        } else if (score >= -1) {
            return 2;
        } else {
            return 1;
        }
    }
    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        ArrayList<PaireChaineEntier> lexique = initDico(depeches, categorie);
        calculScores(depeches, categorie, lexique);
        try (FileWriter fileWriter = new FileWriter(nomFichier)) {
            for (PaireChaineEntier paire : lexique) {
                int poids = poidsPourScore(paire.getEntier());
                String chaineSansDeuxPoints = paire.getChaine().replace(":", "");
                String ligne = chaineSansDeuxPoints + ":" + poids + "\n";
                fileWriter.write(ligne);
            }
            System.out.println("Le fichier lexique pour la catégorie " + categorie + " a été créé avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime, endTime, duration;
        // Introduction
        System.out.println("Démarrage du programme de classification des dépêches journalistiques.\n");

        // Chargement des dépêches en mémoire
        System.out.println("Chargement des dépêches depuis le fichier depeches.txt");
        startTime = System.currentTimeMillis();
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Chargement terminé en " + duration + " ms.\n");


        // Initialisation des catégories et de leurs lexiques
        System.out.println("\nInitialisation des lexiques par catégorie.");
        ArrayList<Categorie> allCategorie = new ArrayList<>();
        allCategorie.add(new Categorie("SPORTS"));
        allCategorie.add(new Categorie("CULTURE"));
        allCategorie.add(new Categorie("POLITIQUE"));
        allCategorie.add(new Categorie("ECONOMIE"));
        allCategorie.add(new Categorie("ENVIRONNEMENT-SCIENCES"));

        for (Categorie categorie : allCategorie) {
            categorie.initLexique("./lexique_" + categorie.getNom().toLowerCase() + ".txt");
        }

        System.out.println("--- Exemple d'affichage des lexiques de sport ---");
        for (PaireChaineEntier lexique:allCategorie.get(0).getLexique()){
            System.out.println(lexique);
        }

        // Interaction avec l'utilisateur pour démontrer la recherche de poids d'un mot
        System.out.println("\nEntrez un mot pour obtenir son poids dans le lexique des sports :");
        Scanner scanner = new Scanner(System.in);
        String mot = scanner.nextLine();
        int poids = UtilitairePaireChaineEntier.entierPourChaine(allCategorie.get(0).getLexique(), mot);
        if (poids != 0) {
            System.out.println("Le poids de \"" + mot + "\" dans la catégorie SPORTS est : " + poids);
        } else {
            System.out.println("Mot non trouvé dans le lexique des sports.");
        }

        // Présentation des scores pour la catégorie SPORTS
        System.out.println("\nCalcul des scores pour chaque dépêche 474 dans la catégorie SPORTS :");
        startTime = System.currentTimeMillis();
        Categorie sport = allCategorie.get(0);
        int score = sport.score(depeches.get(473));
        System.out.println("Dépêche ID " + depeches.get(473).getId() + " Score : " + score);

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Score trouvé en " + duration + " ms.\n");

        // Classification de toutes les dépêches et écriture dans un fichier
        System.out.println("\nClassification des dépêches et écriture des résultats dans le fichier Classement.txt");
        startTime = System.currentTimeMillis();
        classementDepeches(depeches, allCategorie, "./Classement.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Classification terminée en " + duration + " ms.\n");


        //test anle depche tsotra fa am test
        System.out.println("\nChargement des dépêches de test depuis le fichier test.txt et classification.");
        ArrayList<Depeche> testDepeches = lectureDepeches("./test.txt");

        System.out.println("\nClassification des test et écriture des résultats dans le fichier ClassementTest1.txt");
        startTime = System.currentTimeMillis();
        classementDepeches(testDepeches, allCategorie, "./ClassementTest1.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Classification terminée en " + duration + " ms.\n");

        // Génération des lexiques à partir des dépêches existantes
        System.out.println("\nGénération de nouveaux lexiques à partir des dépêches.");
        startTime = System.currentTimeMillis();
        ArrayList<Categorie> categories = new ArrayList<>();
        categories.add(new Categorie("SPORTS"));
        categories.add(new Categorie("CULTURE"));
        categories.add(new Categorie("POLITIQUE"));
        categories.add(new Categorie("ECONOMIE"));
        categories.add(new Categorie("ENVIRONNEMENT-SCIENCES"));
        for (Categorie categorie : categories) {
            String nomFichierLexique = categorie.getNom().toLowerCase() + "_lexique.txt";
            generationLexique(depeches, categorie.getNom(), nomFichierLexique);
        }
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Génération des lexiques terminée en " + duration + " ms.\n");

        for (Categorie categorie : categories) {
            categorie.initLexique("./"+categorie.getNom().toLowerCase()+"_lexique.txt");
        }

        // Chargement et classification d'un ensemble de test de dépêches
        startTime = System.currentTimeMillis();
        System.out.println("Classification des dépêches et écriture des résultats dans le fichier ClassementTest.txt");
        classementDepeches(testDepeches, categories, "./ClassementTest.txt");
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Classification terminée en " + duration + " ms.\n");


        System.out.println("\nFin du programme de classification des dépêches. Vérifiez les fichiers de sortie pour les résultats.");

    }

}

