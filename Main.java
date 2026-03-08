

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String xmlPath = args.length > 0 ? args[0] : "nuages.xml";
        Typologie typologie = new Typologie();

        try {
            typologie.chargerDepuisXML(xmlPath);
        } catch (ValidationException ve) {
            System.err.println("Erreur de validation du XML : " + ve.getMessage());
            return;
        } catch (Exception e) {
            System.err.println("Impossible de charger le fichier XML (" + xmlPath + ") : " + e.getMessage());
            return;
        }

        Categorie racine = typologie.getRacine();
        if (racine == null) {
            System.err.println("Aucune catégorie racine trouvée dans la typologie.");
            return;
        }

        System.out.println("Catégorie racine : " + racine.getNom());
        Observation obs = new Observation();
        Scanner sc = new Scanner(System.in);

        for (Caracteristique carac : racine.getCaracteristiques().values()) {
            String intitule = carac.getIntitule();
            Domaine domaine = carac.getDomaine();

            if (domaine instanceof Intervalle) {
                Intervalle iv = (Intervalle) domaine;
                while (true) {
                    System.out.print(intitule + " (nombre) " + iv + " : ");
                    String line = sc.nextLine().trim();
                    try {
                        double val = Double.parseDouble(line);
                        if (iv.contient(val)) {
                            obs.ajouter(intitule, val);
                            break;
                        } else {
                            System.out.println("Valeur hors intervalle, réessayez.");
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("Entrée non valide, fournir un nombre.");
                    }
                }
            } else if (domaine instanceof Ensemble) {
                Ensemble ens = (Ensemble) domaine;
                Set<String> options = ens.getElements();
                System.out.println(intitule + " (options) : " + options);
                while (true) {
                    System.out.print(intitule + " : ");
                    String line = sc.nextLine().trim();
                    if (ens.contient(line)) {
                        obs.ajouter(intitule, line);
                        break;
                    } else {
                        System.out.println("Option invalide, réessayez.");
                    }
                }
            } else {
                System.out.print(intitule + " (entrée libre) : ");
                String line = sc.nextLine().trim();
                obs.ajouter(intitule, line);
            }
        }

        List<Categorie> compatibles = typologie.classifier(obs);
        System.out.println();
        if (compatibles.isEmpty()) {
            System.out.println("Aucune catégorie compatible trouvée pour cette observation.");
        } else {
            System.out.println("Catégories compatibles (" + compatibles.size() + ") :");
            for (Categorie c : compatibles) {
                System.out.println(" - " + c.getNom());
            }
        }

        sc.close();
    }
}