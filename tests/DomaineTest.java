import org.junit.jupiter.api.Test; // Import pour l'annotation @Test
import static org.junit.jupiter.api.Assertions.*; // Import pour assertTrue/assertFalse
import java.util.Set;

/**
 * Classe de test pour les Domaines.
 * Chaque méthode @Test est un cas de test indépendant.
 */
class DomaineTest {

    @Test // Signale à JUnit que c'est une méthode de test
    void testIntervalleContient() {
        // Préparation : créer l'objet à tester
        Domaine d = new Intervalle(10.0, 20.0);

        // Tests (Assertions)
        assertTrue(d.contient(15.0)); // Test "dans l'intervalle"
        assertTrue(d.contient(10.0)); // Test "borne inférieure"
        assertTrue(d.contient(20.0)); // Test "borne supérieure"
        assertFalse(d.contient(9.9));   // Test "juste avant"
        assertFalse(d.contient(20.1));  // Test "juste après"
        assertFalse(d.contient("texte")); // Test "mauvais type"
    }

    @Test
    void testEnsembleContient() {
        Domaine d = new Ensemble(Set.of("conique", "arrondi"));

        assertTrue(d.contient("conique"));
        assertFalse(d.contient("irregulier"));
        assertFalse(d.contient(15.0)); // Mauvais type
    }

    @Test
    void testIntervalleInclusionStricte() {
        Domaine mere = new Intervalle(5.0, 50.0);

        // Cas valides
        assertTrue(new Intervalle(10.0, 30.0).estInclusDans(mere));
        assertTrue(new Intervalle(5.0, 50.0).estInclusDans(mere)); // Égalité

        // Cas invalides
        assertFalse(new Intervalle(4.0, 30.0).estInclusDans(mere)); // Inf < Mère
        assertFalse(new Intervalle(10.0, 51.0).estInclusDans(mere)); // Sup > Mère
    }

    @Test
    void testEnsembleInclusionStricte() {
        Domaine mere = new Ensemble(Set.of("a", "b", "c", "d"));

        // Cas valide
        assertTrue(new Ensemble(Set.of("a", "c")).estInclusDans(mere));

        // Cas invalide
        assertFalse(new Ensemble(Set.of("a", "e")).estInclusDans(mere)); // "e" n'est pas dans la mère
    }
}
