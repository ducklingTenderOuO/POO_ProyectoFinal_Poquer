/**
 * Representa una carta individual con valor, figura, color. 
 * @author (AYJB - LGLM)
 * @version (MAY 2025)
 */

package proyectofinal; 

public class Carta {
    private final int valor;
    private final String figura;
    private final String color;

    public Carta(int valor, String figura, String color) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
    }

    // --- Getters (inmutables) ---
    public int getValor() { return valor; }
    public String getFigura() { return figura; }
    public String getColor() { return color; }

    // --- Métodos de comparación ---
    public boolean esIgualA(Carta otraCarta) {
        return otraCarta != null && 
               valor == otraCarta.getValor() &&
               figura.equals(otraCarta.getFigura()) &&
               color.equals(otraCarta.getColor());
    }

    public boolean tieneElMismoValor(Carta otraCarta) {
        return otraCarta != null && valor == otraCarta.getValor();
    }

    public boolean tieneLaMismaFigura(Carta otraCarta) {
        return otraCarta != null && figura.equals(otraCarta.getFigura());
    }

    // --- Representación visual ---
    @Override
    public String toString() {
        String[] nombres = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String valorStr = (valor >= 1 && valor <= 13) ? nombres[valor - 1] : "?";
        return valorStr + " de " + figura + " (" + color + ")";
    }
    
    public String getRutaImagen() {
        String figuraStr = figura.toLowerCase();     
        String valorStr = String.valueOf(valor);     

        return "/proyectofinal/imagenes/" + figuraStr + valorStr + ".png";
    }
}
