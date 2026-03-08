public interface Domaine {

    boolean contient(Object valeur);
    boolean estInclusDans(Domaine domaineMere);
}
