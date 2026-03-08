import java.util.Set; 
import java.util.HashSet;

public class Ensemble implements Domaine {

    private final Set<String> elements;

    public Ensemble(Set<String> elements) {
        this.elements = new HashSet<>(elements);
    }

    public Set<String> getElements() { return elements; }

    @Override
    public boolean contient(Object valeur) { 
        if (!(valeur instanceof String)) {
            return false;
        }

        return elements.contains((String) valeur);
    }


    @Override
    public boolean estInclusDans(Domaine domaineMere) { 
        if (!(domaineMere instanceof Ensemble)) {
            return false;
        }

        Ensemble mere = (Ensemble) domaineMere;

        return mere.getElements().containsAll(this.elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
