public class Caracteristique {
    private String intitule;
    private Domaine domaine;

    public Caracteristique(String intitule, Domaine domaine) {
        this.intitule = intitule;
        this.domaine = domaine;
    }

    public String getIntitule() { return intitule; }
    public Domaine getDomaine() { return domaine; }
}
