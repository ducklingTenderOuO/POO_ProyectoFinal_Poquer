/**
 * Write a description of class Carta here.
 *
 * @author (AYJB - LGLM)
 * @version (MAY 2025)
 */

public class Carta {
    private int valor;
    private String figura;
    private String color;
    private boolean bocaArriba;

    /**
     * Constructor que inicializa una carta con valor, figura y color.
     */
    public Carta(int valor, String figura, String color, boolean bocaArriba) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
        this.bocaArriba = false;
    }

    /**
     * Obtiene el valor de la carta.
     * @return 
     */
    public int getValor() {
        return valor;
    }
    
    /**
     * Obtiene la figura de la carta.
     * @return 
     */
    public String getFigura() {
        return figura;
    }
    
    /**
     * Obtiene la figura de la carta.
     * @return 
     */
    public String getColor() {
        return color;
    }

    /**
     * Se representa la carta en forma de texto
     */
    @Override
    public String toString() {
        return valor + " de " + figura + " (" + color + ")";
    }
    
    public boolean esIgualA(Carta otraCarta){
        if (valor == otraCarta.getValor()){
            if (figura.equals(otraCarta.getFigura())){
                if (color.equals(otraCarta.getColor())){
                    return true;
                }
            } else {
            }
        }
        return false;
    }
    
    public boolean tieneElMismoValor(Carta otraCarta){
        return valor == otraCarta.getValor();
    }
    
    public boolean tieneLaMismaFigura(Carta otraCarta){
        return figura.equals(otraCarta.getFigura());
    }
}
