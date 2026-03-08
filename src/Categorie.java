import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Categorie {
    private String nom;      
    private String nomMere; 

    private Categorie mere;

    private List<Categorie> filles;

    private Map<String, Caracteristique> caracteristiques;

    public Categorie(String nom, String nomMere) {
        this.nom = nom;
        this.nomMere = nomMere;

        this.filles = new ArrayList<>();
        this.caracteristiques = new HashMap<>();
        this.mere = null; 
    }

    public String getNom() { return nom; }
    public String getNomMere() { return nomMere; }
    public Categorie getMere() { return mere; }
    public List<Categorie> getFilles() { return filles; }
    public Map<String, Caracteristique> getCaracteristiques() { return caracteristiques; }

    public void setMere(Categorie mere) { this.mere = mere; }
    public void addFille(Categorie fille) { this.filles.add(fille); }

    public void addCaracteristique(Caracteristique carac) {
        this.caracteristiques.put(carac.getIntitule(), carac);
    }


    public boolean estCompatible(Observation obs) {
        // Parcours de TOUTES les caractéristiques définies pour cette catégorie.

        for (Caracteristique caracCat : this.caracteristiques.values()) {

            String intitule = caracCat.getIntitule();
            // 1. Condition Cruciale d'existence : L'observation doit posséder une valeur
            //    pour CHAQUE caractéristique requise par la catégorie.

            if (!obs.aCaracteristique(intitule)) {
                // Si une caractéristique requise est manquante, l'observation n'est pas compatible.
                return false; 
            }
            // Récupération de la valeur observée et du domaine de contrainte.

            Object valeurObs = obs.getValeur(intitule);
            Domaine domaineCat = caracCat.getDomaine();

            if (!domaineCat.contient(valeurObs)) {
                return false; 
            }
        }

        return true; 
    }

    
    public void validerHierarchie() throws ValidationException {
        // Lancement de la validation uniquement si la catégorie n'est pas la racine 'TOP'.

        if (this.mere != null && !this.nomMere.equals("TOP")) {
            // Logique de validation de la Contrainte d'Inclusion (du fils vers le père).

            for (Caracteristique caracMere : this.mere.getCaracteristiques().values()) {

                String intitule = caracMere.getIntitule();
                // Vérification si la caractéristique du parent est également définie chez l'enfant.

                if (this.caracteristiques.containsKey(intitule)) {

                    Domaine domaineFille = this.caracteristiques.get(intitule).getDomaine();
                    Domaine domaineMere = caracMere.getDomaine();
                    // Condition Cruciale : Le domaine de la caractéristique de la catégorie fille
                    // DOIT être inclus dans le domaine de la catégorie mère (règle d'héritage par spécialisation).

                    if (!domaineFille.estInclusDans(domaineMere)) {

                        throw new ValidationException(
                            "Incohérence: Domaine de '" + intitule + "' de " + this.nom +
                            " n'est pas inclus dans " + this.mere.getNom()
                        );
                    }
                }
                // Note: Si la fille n'a pas la caractéristique, elle hérite implicitement de la contrainte de la mère.
            }
        }
        // Appel Récursif : S'assure que toutes les sous-catégories (filles) sont également validées.

        for (Categorie fille : this.filles) {
            fille.validerHierarchie();
        }
    }
}
