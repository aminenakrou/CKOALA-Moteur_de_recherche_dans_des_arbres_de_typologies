
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Typologie {

    private final Map<String, Categorie> categoriesMap;

    private Categorie racine;

    public Typologie() {
        this.categoriesMap = new HashMap<>();
        this.racine = null;
    }

    public Categorie getCategorie(String nom) {
        return categoriesMap.get(nom);
    }
    public Categorie getRacine() {
        return racine;
    }

    @Override
    public String toString(){
        return this.categoriesMap.toString();
    }

    public void chargerDepuisXML(String cheminFichier) throws Exception {

        this.categoriesMap.clear();
        this.racine = null;

        File xmlFile = new File(cheminFichier);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList categoriesXML = doc.getElementsByTagName("categorie");

        // Étape 1 : Création des objets Categorie et peuplement des Caractéristiques. 
        for (int i = 0; i < categoriesXML.getLength(); i++) {
            Element catElement = (Element) categoriesXML.item(i);

            String nom = catElement.getElementsByTagName("nom").item(0).getTextContent();
            String mere = catElement.getElementsByTagName("mere").item(0).getTextContent();


            Categorie cat = new Categorie(nom, mere);

            NodeList caracsXML = catElement.getElementsByTagName("caracteristique");
            for (int j = 0; j < caracsXML.getLength(); j++) {
                Element caracElement = (Element) caracsXML.item(j);
                cat.addCaracteristique(parseCaracteristique(caracElement));
            }

            this.categoriesMap.put(nom, cat);
        }
        // Étape 2 (Cruciale) : Construction de la hiérarchie arborescente.
        for (Categorie cat : this.categoriesMap.values()) {

            if (cat.getNomMere().equals("TOP")) {
                // Définition du point d'entrée de l'arbre.
                this.racine = cat;
            } else {
                Categorie mere = this.categoriesMap.get(cat.getNomMere());

                if (mere != null) {
                    // Lien de l'enfant vers le parent et inversement.
                    mere.addFille(cat); 
                    cat.setMere(mere); 
                } else {
                    // Validation : Lève une erreur si une catégorie mère est déclarée, mais non trouvée.
                    throw new ValidationException("Mère '" + cat.getNomMere() + "' non trouvée pour '" + cat.getNom() + "'.");
                }
            }
        }
        // Étape 3 (Cruciale) : Validation de l'arbre après construction.
        if (this.racine != null) {
            // Validation que l'arbre ne contient pas de boucles ou de problèmes de structure.
            this.racine.validerHierarchie();
        } else {
            throw new ValidationException("Aucune catégorie racine (mère=TOP) trouvée.");
        }
    }


    public List<Categorie> classifier(Observation obs) {
        List<Categorie> categoriesCompatibles = new ArrayList<>();

        if (this.racine != null) {
            // Point de départ de la descente récursive, à partir de la racine de la typologie.
            classifierRecursif(this.racine, obs, categoriesCompatibles);
        }

        return categoriesCompatibles;
    }
    /**
     * But: Parcourir l'arbre de la typologie de manière récursive pour trouver toutes les catégories compatibles.
     *
     * @param cat La catégorie courante (le nœud de l'arbre) à évaluer.
     * @param obs L'observation à classer.
     * @param resultats Liste accumulant les catégories compatibles trouvées.
     */

    
    private void classifierRecursif(Categorie cat, Observation obs, List<Categorie> resultats) {

        if (cat.estCompatible(obs)) {
            resultats.add(cat);

            for (Categorie fille : cat.getFilles()) {
                classifierRecursif(fille, obs, resultats);
            }
        }

    }

   
    private Caracteristique parseCaracteristique(Element caracElement) {
        String intitule = caracElement.getElementsByTagName("intitule").item(0).getTextContent();

        Domaine domaine; 

        NodeList ensembles = caracElement.getElementsByTagName("ensemble");
        NodeList intervalles = caracElement.getElementsByTagName("intervalle");

        if (ensembles.getLength() > 0) {
            Set<String> elementsSet = new HashSet<>();
            NodeList elements = ((Element) ensembles.item(0)).getElementsByTagName("element");
            for (int k = 0; k < elements.getLength(); k++) {
                elementsSet.add(elements.item(k).getTextContent());
            }
            domaine = new Ensemble(elementsSet);

        } else {
            Element intervalleEl = (Element) intervalles.item(0);
            double inf = Double.parseDouble(intervalleEl.getElementsByTagName("inf").item(0).getTextContent());
            double sup = Double.parseDouble(intervalleEl.getElementsByTagName("sup").item(0).getTextContent());
            domaine = new Intervalle(inf, sup);
        }

        return new Caracteristique(intitule, domaine);
    }
}
