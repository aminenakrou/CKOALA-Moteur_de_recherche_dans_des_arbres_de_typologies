import java.util.HashMap;
import java.util.Map;


public class Observation {

    private final Map<String, Object> valeursObservees;


    public Observation() {
        this.valeursObservees = new HashMap<>();
    }


    public void ajouter(String intitule, Object valeur) {
        this.valeursObservees.put(intitule, valeur);
    }

    public Object getValeur(String intitule) {
        return this.valeursObservees.get(intitule);
    }

    public boolean aCaracteristique(String intitule) {
        return this.valeursObservees.containsKey(intitule);
    }
}
