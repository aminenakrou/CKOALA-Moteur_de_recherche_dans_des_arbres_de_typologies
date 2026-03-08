

public class Intervalle implements Domaine {

    private final double inf; 
    private final double sup; 

    public Intervalle(double inf, double sup) {
        this.inf = inf;
        this.sup = sup;
    }


    public double getInf() { return inf; }
    public double getSup() { return sup; }


    @Override
    public boolean contient(Object valeur) {

        if (!(valeur instanceof Number)) {
            return false;
        }

        double val = ((Number) valeur).doubleValue();

        return val >= this.inf && val <= this.sup;
    }

    @Override
    public boolean estInclusDans(Domaine domaineMere) {
        if (!(domaineMere instanceof Intervalle)) {
            return false;
        }

        Intervalle mere = (Intervalle) domaineMere;
        return mere.getInf() <= this.inf && mere.getSup() >= this.sup;
    }

    @Override
    public String toString() {
        return "[" + inf + " ; " + sup + "]";
    }
}
