package proyectofinal;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
/**
 *
 * @author llesl
 */

public class Interfaz extends javax.swing.JFrame {

    private String tipoJuego;
    private ArrayList<Jugador> jugadores;
    
    private javax.swing.JButton apostar;
    private javax.swing.JTextArea apuestaJuego;
    private javax.swing.JButton decrementar;
    private javax.swing.JTextArea faseJuego;
    private javax.swing.JTextArea fichasJugador;
    private javax.swing.JButton igualar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane mesa;
    private javax.swing.JPanel panelManoJugador;
    private javax.swing.JButton pasar;
    private javax.swing.JButton retirarse;

    /**
     * Creates new form interfaz
     */
    public Interfaz() {
        initComponents();
        personalizarInterfaz();
    }

    private void personalizarInterfaz() {
        setTitle("Juego de Poquer - Texas Hold'em/Five Card Draw");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        
        faseJuego.setEditable(false);
        fichasJugador.setEditable(false);
        apuestaJuego.setEditable(false);
    }

    public void iniciarJuegoConDatos(String tipoJuego, ArrayList<Jugador> jugadores) {
        if (!verificarComponentes()) {
            JOptionPane.showMessageDialog(this, 
                "Error: Componentes no inicializados correctamente", 
                "Error de Sistema", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.tipoJuego = tipoJuego;
        this.jugadores = jugadores;
        
        // Configurar interfaz con los datos recibidos
        jLabel1.setText("POQER - " + tipoJuego.toUpperCase());
        actualizarInformacionJugadores();
        configurarBotones();
    }

    private boolean verificarComponentes() {
        return jLabel1 != null && faseJuego != null && 
               fichasJugador != null && apuestaJuego != null;
    }

    private void actualizarInformacionJugadores() {
        faseJuego.setText("Fase: Pre-Flop\nJugadores: " + jugadores.size());
        
        StringBuilder sb = new StringBuilder("Jugadores:\n");
        for (Jugador jugador : jugadores) {
            sb.append("- ").append(jugador.getNombre()).append("\n");
        }
        fichasJugador.setText(sb.toString());
    }

    private void configurarBotones() {
        apostar.addActionListener(e -> manejarApuesta());
        pasar.addActionListener(e -> manejarPasar());
        retirarse.addActionListener(e -> manejarRetirarse());
        igualar.addActionListener(e -> manejarIgualar());
        jButton1.addActionListener(e -> manejarIncremento());
        decrementar.addActionListener(e -> manejarDecremento());
    }

    private void manejarApuesta() {
        String monto = JOptionPane.showInputDialog(this, "Ingrese monto a apostar:");
        if (monto != null && !monto.isEmpty()) {
            apuestaJuego.append("Apuesta: $" + monto + "\n");
        }
    }

    private void manejarPasar() {
        apuestaJuego.append("Has pasado tu turno\n");
    }

    private void manejarRetirarse() {
        apuestaJuego.append("Te has retirado de la ronda\n");
    }

    private void manejarIgualar() {
        apuestaJuego.append("Has igualado la apuesta\n");
    }

    private void manejarIncremento() {
        apuestaJuego.append("Aumentando apuesta...\n");
    }

    private void manejarDecremento() {
        apuestaJuego.append("Disminuyendo apuesta...\n");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Componentes principales
        jLabel1 = new javax.swing.JLabel();
        panelManoJugador = new javax.swing.JPanel();
        mesa = new javax.swing.JTextPane();
        
        // Áreas de texto con scroll
        apuestaJuego = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setViewportView(apuestaJuego);
        
        faseJuego = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane2.setViewportView(faseJuego);
        
        fichasJugador = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane3.setViewportView(fichasJugador);
        
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane4.setViewportView(mesa);

        // Botones
        apostar = new javax.swing.JButton();
        pasar = new javax.swing.JButton();
        retirarse = new javax.swing.JButton();
        igualar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        decrementar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        // Configuración básica de componentes
        jLabel1.setText("POQUER");
        
        panelManoJugador.setBackground(new java.awt.Color(0, 0, 102));
        panelManoJugador.setLayout(new javax.swing.BoxLayout(panelManoJugador, javax.swing.BoxLayout.LINE_AXIS));
        
        apostar.setText("Apostar");
        pasar.setText("Pasar");
        retirarse.setText("Retirarse");
        igualar.setText("Igualar");
        jButton1.setText("+");
        decrementar.setText("-");
        jButton2.setText("Descarte");

        // Organización del layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(panelManoJugador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(apostar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pasar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(retirarse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(igualar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decrementar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelManoJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apostar)
                    .addComponent(pasar)
                    .addComponent(retirarse)
                    .addComponent(igualar)
                    .addComponent(jButton1)
                    .addComponent(decrementar)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}
