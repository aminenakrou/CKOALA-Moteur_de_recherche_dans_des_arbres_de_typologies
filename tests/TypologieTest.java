import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors; 
import static org.junit.jupiter.api.Assertions.*;


class TypologieTest {

    // On déclare la typologie ici pour qu'elle soit dispo dans tous les tests
    private Typologie typologieArbres;

    // Chemins vers les fichiers (ils doivent être à la racine de votre projet)
    private final String ARBRES_XML_PATH = "arbres.xml";
    private final String NUAGES_XML_PATH = "nuages.xml";

    /*
    Cette méthode (annotée @BeforeEach) sera exécutée AVANT CHAQUE méthode @Test. Ça garantit
    que chaque test repart d'une base saine.
     */

    @BeforeEach
    void setUp() throws Exception {
        typologieArbres = new Typologie();
        typologieArbres.chargerDepuisXML(ARBRES_XML_PATH);
    }

    @Test
    void testChargementXML_Arbres() {
        // Le setup a chargé le fichier.
        // On vérifie que les objets ont bien été créés.
        assertNotNull(typologieArbres.getCategorie("arbre"));
        assertNotNull(typologieArbres.getCategorie("conifere"));

        // On vérifie que la hiérarchie est correcte
        Categorie conifere = typologieArbres.getCategorie("conifere");
        assertEquals("arbre", conifere.getNomMere());
        assertNotNull(conifere.getMere()); 
        assertEquals("arbre", conifere.getMere().getNom());
    }

    @Test
    void testValidationHierarchieValide() {
        // Le setup() a déjà validé "arbres.xml".
        // On teste "nuages.xml" pour être sûr.
        // "assertDoesNotThrow" vérifie qu'aucune exception n'est levée.
         assertDoesNotThrow(() -> {
            Typologie t = new Typologie();
            t.chargerDepuisXML(NUAGES_XML_PATH);
        }, "Le fichier nuages.xml doit être valide");
    }

    @Test
    void testClassifier_Epicea_Valide() {
        Observation obs = new Observation();
        obs.ajouter("forme", "conique");
        obs.ajouter("taille du tronc", 1.0);
        obs.ajouter("taille", 25.0); // Dans l'intervalle [20, 30] de l'épicéa
        obs.ajouter("ecorce", "ecailles");
        obs.ajouter("aiguilles", "brosse"); // Requis par l'épicéa

        // Exécution : lancer la classification
        List<Categorie> resultats = typologieArbres.classifier(obs);

        // On prend juste les noms
        List<String> noms = resultats.stream().map(Categorie::getNom).collect(Collectors.toList());

        assertEquals(3, noms.size(), "Doit trouver 3 catégories");
        assertTrue(noms.contains("arbre"));
        assertTrue(noms.contains("conifere"));
        assertTrue(noms.contains("epicea"));
    }

    @Test
    void testClassifier_ConifereMaisPasEpicea() {
        Observation obs = new Observation();
        obs.ajouter("forme", "conique");
        obs.ajouter("taille du tronc", 1.8);
        obs.ajouter("taille", 35.0); // VALIDE pour conifere [5, 40], INVALIDE pour epicea [20, 30]
        obs.ajouter("ecorce", "fissuree");


        List<Categorie> resultats = typologieArbres.classifier(obs);
        List<String> noms = resultats.stream().map(Categorie::getNom).collect(Collectors.toList());


        assertEquals(2, noms.size(), "Doit trouver 2 catégories");
        assertTrue(noms.contains("arbre"));
        assertTrue(noms.contains("conifere"));
        assertFalse(noms.contains("epicea"), "Ne doit PAS être un épicéa");
    }

    @Test
    void testClassifier_CaracteristiqueManquante() {

        Observation obs = new Observation();
        obs.ajouter("forme", "conique");
        obs.ajouter("taille", 35.0);
        // IL MANQUE "ecorce" (requis par "arbre")

        List<Categorie> resultats = typologieArbres.classifier(obs);

        //"arbre" n'est pas compatible (carac manquante) donc ses enfants ne sont même pas testés.
        assertTrue(resultats.isEmpty(), "Ne doit rien trouver");
    }
}
