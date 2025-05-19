/**
 * Write a description of class Carta here.
 *
 * @author (AYJB)
 * @version (FEB 2025)
 */

public class Carta {
    private int valor;
    private String figura;
    private String color;

    /**
     * Constructor que inicializa una carta con valor, figura y color.
     */
    public Carta(int valor, String figura, String color) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
    }

    /**
     * Obtiene el valor de la carta.
     */
    public int getValor() {
        return valor;
    }
    
    /**
     * Obtiene la figura de la carta.
     */
    public String getFigura() {
        return figura;
    }
    
    /**
     * Obtiene la figura de la carta.
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
            if (figura == otraCarta.getFigura()){
                if (color == otraCarta.getColor()){
                return true;
                }
            }
        }
        return false;
    }
    
    public boolean tieneElMismoValor(Carta otraCarta){
        if (valor == otraCarta.getValor()){
                return true;
        }
        return false;
    }
    
    public boolean tieneLaMismaFigura(Carta otraCarta){
        if (figura == otraCarta.getFigura()){
                return true;
        }
        return false;
    }
}