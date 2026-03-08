import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

/**
 * Tests unitaires pour la classe Categorie, focalisés sur validerHierarchie().
 */
class CategorieTest {

    @Test
    void testValiderHierarchie_CaracteristiqueRedefinieInvalide() {
        Categorie mere = new Categorie("arbre", "TOP");
        mere.addCaracteristique(new Caracteristique("taille", new Intervalle(5, 40)));

        // Créer une catégorie fille "epicea" avec taille=[20, 50] (50 > 40 donc invalide)
        Categorie fille = new Categorie("epicea", "arbre");
        fille.addCaracteristique(new Caracteristique("taille", new Intervalle(20, 50)));

        mere.addFille(fille);
        fille.setMere(mere);

        // La validation doit échouer car 50 > 40
        ValidationException thrown = assertThrows(
            ValidationException.class,
            () -> fille.validerHierarchie(),
            "Doit échouer car taille=[20, 50] n'est pas inclus dans [5, 40]"
        );

        assertTrue(thrown.getMessage().contains("taille"),
            "Le message d'erreur doit mentionner la caractéristique invalide");
    }

    @Test
    void testValiderHierarchie_SansRedefintionCaracteristiques() {
        Categorie mere = new Categorie("arbre", "TOP");
        mere.addCaracteristique(new Caracteristique("taille", new Intervalle(5, 40)));
        mere.addCaracteristique(new Caracteristique("forme", new Ensemble(Set.of("conique", "rond"))));

        // Créer une catégorie fille qui ne redéfinit pas les caractéristiques
        Categorie fille = new Categorie("epicea", "arbre");
        
        mere.addFille(fille);
        fille.setMere(mere);

        // La validation doit réussir car pas de redéfinition
        assertDoesNotThrow(
            () -> fille.validerHierarchie(),
            "Doit passer car pas de conflit avec la mère"
        );
    }

    @Test
    void testValiderHierarchie_Racine() {
        // La racine a nomMere="TOP" et mere=null
        Categorie racine = new Categorie("arbre", "TOP");
        racine.addCaracteristique(new Caracteristique("taille", new Intervalle(5, 40)));

        // La validation doit réussir car c'est la racine
        assertDoesNotThrow(
            () -> racine.validerHierarchie(),
            "La racine doit toujours être valide"
        );
    }

    @Test
    void testValiderHierarchie_RedefinitionValide() {
        Categorie mere = new Categorie("arbre", "TOP");
        mere.addCaracteristique(new Caracteristique("taille", new Intervalle(5, 40)));

        // Créer une catégorie fille "epicea" avec taille=[20, 30]
        Categorie fille = new Categorie("epicea", "arbre");
        fille.addCaracteristique(new Caracteristique("taille", new Intervalle(20, 30)));

        mere.addFille(fille);
        fille.setMere(mere);

        // La validation doit réussir car [20, 30] est inclus dans [5, 40]
        assertDoesNotThrow(
            () -> fille.validerHierarchie(),
            "Doit passer car [20, 30] est inclus dans [5, 40]"
        );
    }
}
