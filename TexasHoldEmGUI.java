package proyectofinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.net.URL;

public class TexasHoldEmGUI extends JFrame {

    private List<Jugador> jugadores;
    private Bote bote;
    private Baraja baraja;
    private List<Carta> cartasComunitarias;

    private Jugador jugadorActual;
    private int indiceJugadorActual;

    private JTextArea infoArea = new JTextArea(10, 30);
    private JPanel cartasJugadorPanel = new JPanel();
    private JPanel cartasComunitariasPanel = new JPanel();
    private List<JToggleButton> botonesCartas = new ArrayList<>();

    private JButton btnPasar = new JButton("Pasar");
    private JButton btnApostar = new JButton("Apostar");
    private JButton btnIgualar = new JButton("Igualar");
    private JButton btnSubir = new JButton("Subir");
    private JButton btnRetirarse = new JButton("Retirarse");
    private JButton btnFlop = new JButton("Flop");
    private JButton btnTurn = new JButton("Turn");
    private JButton btnRiver = new JButton("River");

    private JTextField txtApuesta = new JTextField(5);

    private enum EstadoRonda {PREFLOP, FLOP, TURN, RIVER, SHOWDOWN, FIN}
    private EstadoRonda estado;

    private int apuestaMinima = 10;
    private int apuestaActual = 0;
    private int jugadoresQueIgualaron = 0;
    private int jugadoresActivos;

    public TexasHoldEmGUI(List<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        this.bote = new Bote();
        this.cartasComunitarias = new ArrayList<>();
        this.estado = EstadoRonda.PREFLOP;

        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        infoArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(infoArea);
        add(scroll, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        cartasComunitariasPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        mainPanel.add(cartasComunitariasPanel, BorderLayout.NORTH);
        
        cartasJugadorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        mainPanel.add(cartasJugadorPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel();
        panelAcciones.add(new JLabel("Apuesta:"));
        panelAcciones.add(txtApuesta);
        panelAcciones.add(btnPasar);
        panelAcciones.add(btnApostar);
        panelAcciones.add(btnIgualar);
        panelAcciones.add(btnSubir);
        panelAcciones.add(btnRetirarse);
        panelAcciones.add(btnFlop);
        panelAcciones.add(btnTurn);
        panelAcciones.add(btnRiver);

        add(panelAcciones, BorderLayout.SOUTH);

        btnPasar.addActionListener(e -> accionPasar());
        btnApostar.addActionListener(e -> accionApostar());
        btnIgualar.addActionListener(e -> accionIgualar());
        btnSubir.addActionListener(e -> accionSubir());
        btnRetirarse.addActionListener(e -> accionRetirarse());
        btnFlop.addActionListener(e -> mostrarFlop());
        btnTurn.addActionListener(e -> mostrarTurn());
        btnRiver.addActionListener(e -> mostrarRiver());
    }
    
    public void iniciarJuego() {
        setVisible(true);
        empezarRonda();
    }

    private void empezarRonda() {
        // Reiniciar estado del juego
        for (Jugador j : jugadores) {
            j.reiniciarEstado();
        }
        
        baraja.barajar();
        cartasComunitarias.clear();
        
        // Repartir cartas a jugadores
        for (Jugador j : jugadores) {
            j.recibirCarta(baraja.sacarCarta());
            j.recibirCarta(baraja.sacarCarta());
        }

        bote.limpiar();
        apuestaActual = 0;
        jugadoresActivos = jugadores.size();
        jugadoresQueIgualaron = 0;

        estado = EstadoRonda.PREFLOP;
        indiceJugadorActual = 0;
        jugadorActual = jugadores.get(indiceJugadorActual);

        infoArea.setText("");
        infoArea.append("¡Nueva ronda de Texas Hold'em! Comienza la fase Preflop.\n");
        actualizarGUI();
    }

    private void actualizarGUI() {
        cartasJugadorPanel.removeAll();
        cartasComunitariasPanel.removeAll();
        botonesCartas.clear();

        infoArea.append("\nTurno: " + jugadorActual.getNombre() + 
                       " | Fichas: " + jugadorActual.getFichas() + 
                       " | Bote: " + bote.getCantidad() + "\n");
        
        // Mostrar cartas del jugador actual
        for (Carta c : jugadorActual.getMano()) {
            ImageIcon icono = cargarImagenCarta(c);
            JLabel lblCarta = new JLabel(icono);
            cartasJugadorPanel.add(lblCarta);
        }

        // Mostrar cartas comunitarias
        for (Carta c : cartasComunitarias) {
            ImageIcon icono = cargarImagenCarta(c);
            JLabel lblCarta = new JLabel(icono);
            cartasComunitariasPanel.add(lblCarta);
        }

        // Controlar botones según estado
        btnFlop.setEnabled(estado == EstadoRonda.PREFLOP && jugadoresQueIgualaron >= jugadoresActivos);
        btnTurn.setEnabled(estado == EstadoRonda.FLOP && jugadoresQueIgualaron >= jugadoresActivos);
        btnRiver.setEnabled(estado == EstadoRonda.TURN && jugadoresQueIgualaron >= jugadoresActivos);
        
        btnPasar.setEnabled(apuestaActual == 0);
        btnApostar.setEnabled(apuestaActual == 0);
        btnIgualar.setEnabled(apuestaActual > 0);
        btnSubir.setEnabled(apuestaActual > 0);
        btnRetirarse.setEnabled(apuestaActual > 0);

        cartasJugadorPanel.revalidate();
        cartasJugadorPanel.repaint();
        cartasComunitariasPanel.revalidate();
        cartasComunitariasPanel.repaint();
    }

    private ImageIcon cargarImagenCarta(Carta c) {
        URL imagenURL = getClass().getResource(c.getRutaImagen());
        if (imagenURL == null) {
            System.err.println("No se encontró la imagen: " + c.getRutaImagen());
            return null;
        }
        ImageIcon iconoOriginal = new ImageIcon(imagenURL);
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    // --- Fases del juego ---
    private void mostrarFlop() {
        baraja.sacarCarta(); // Quemar carta
        for (int i = 0; i < 3; i++) {
            cartasComunitarias.add(baraja.sacarCarta());
        }
        estado = EstadoRonda.FLOP;
        apuestaActual = 0;
        jugadoresQueIgualaron = 0;
        infoArea.append("Se muestra el Flop: " + cartasComunitarias + "\n");
        actualizarGUI();
    }

    private void mostrarTurn() {
        baraja.sacarCarta(); // Quemar carta
        cartasComunitarias.add(baraja.sacarCarta());
        estado = EstadoRonda.TURN;
        apuestaActual = 0;
        jugadoresQueIgualaron = 0;
        infoArea.append("Se muestra el Turn: " + cartasComunitarias.get(3) + "\n");
        actualizarGUI();
    }

    private void mostrarRiver() {
        baraja.sacarCarta(); // Quemar carta
        cartasComunitarias.add(baraja.sacarCarta());
        estado = EstadoRonda.RIVER;
        apuestaActual = 0;
        jugadoresQueIgualaron = 0;
        infoArea.append("Se muestra el River: " + cartasComunitarias.get(4) + "\n");
        actualizarGUI();
    }

    // --- Acciones de jugadores ---
    private void siguienteTurno() {
        do {
            indiceJugadorActual = (indiceJugadorActual + 1) % jugadores.size();
            jugadorActual = jugadores.get(indiceJugadorActual);
        } while (!jugadorActual.estaActivo());

        // Verificar si terminó la ronda de apuestas
        if (jugadoresQueIgualaron >= jugadoresActivos) {
            switch (estado) {
                case PREFLOP:
                    btnFlop.setEnabled(true);
                    break;
                case FLOP:
                    btnTurn.setEnabled(true);
                    break;
                case TURN:
                    btnRiver.setEnabled(true);
                    break;
                case RIVER:
                    estado = EstadoRonda.SHOWDOWN;
                    determinarGanador();
                    break;
            }
        }

        infoArea.append("Turno de: " + jugadorActual.getNombre() + "\n");
        actualizarGUI();
    }

    private void accionPasar() {
        if (apuestaActual > 0) {
            infoArea.append("No puedes pasar, hay apuesta activa.\n");
            return;
        }
        infoArea.append(jugadorActual.getNombre() + " pasa.\n");
        jugadoresQueIgualaron++;
        siguienteTurno();
    }

    private void accionApostar() {
        if (apuestaActual > 0) {
            infoArea.append("Ya hay apuesta activa, usa igualar o subir.\n");
            return;
        }
        try {
            int cant = Integer.parseInt(txtApuesta.getText());
            if (cant < apuestaMinima) {
                infoArea.append("La apuesta mínima es " + apuestaMinima + ".\n");
                return;
            }
            if (!jugadorActual.apostar(cant)) {
                infoArea.append("No tienes fichas suficientes para apostar esa cantidad.\n");
                return;
            }
            apuestaActual = cant;
            bote.agregarApuesta(cant);
            jugadoresQueIgualaron = 1;
            infoArea.append(jugadorActual.getNombre() + " apuesta " + cant + ".\n");
            siguienteTurno();
        } catch (NumberFormatException e) {
            infoArea.append("Cantidad inválida.\n");
        }
    }

    private void accionIgualar() {
        if (apuestaActual == 0) {
            infoArea.append("No hay apuesta que igualar.\n");
            return;
        }
        if (!jugadorActual.apostar(apuestaActual)) {
            infoArea.append("No tienes fichas suficientes para igualar.\n");
            return;
        }
        bote.agregarApuesta(apuestaActual);
        jugadoresQueIgualaron++;
        infoArea.append(jugadorActual.getNombre() + " iguala " + apuestaActual + ".\n");
        siguienteTurno();
    }

    private void accionSubir() {
        if (apuestaActual == 0) {
            infoArea.append("Primero debes apostar.\n");
            return;
        }
        try {
            int cant = Integer.parseInt(txtApuesta.getText());
            if (cant <= apuestaActual) {
                infoArea.append("La subida debe ser mayor que " + apuestaActual + ".\n");
                return;
            }
            if (!jugadorActual.apostar(cant)) {
                infoArea.append("No tienes fichas suficientes para subir.\n");
                return;
            }
            apuestaActual = cant;
            bote.agregarApuesta(cant);
            jugadoresQueIgualaron = 1;
            infoArea.append(jugadorActual.getNombre() + " sube la apuesta a " + cant + ".\n");
            siguienteTurno();
        } catch (NumberFormatException e) {
            infoArea.append("Cantidad inválida.\n");
        }
    }

    private void accionRetirarse() {
        jugadorActual.retirarse();
        jugadoresActivos--;
        infoArea.append(jugadorActual.getNombre() + " se retiró.\n");

        if (jugadoresActivos == 1) {
            estado = EstadoRonda.FIN;
            determinarGanador();
            return;
        }
        siguienteTurno();
    }

    private void determinarGanador() {
        infoArea.append("\n--- Resultado Final ---\n");
        infoArea.append("Cartas comunitarias: " + cartasComunitarias + "\n");

        Jugada mejorJugada = null;
        Jugador ganador = null;

        for (Jugador j : jugadores) {
            if (!j.estaActivo()) continue;

            Jugada jugada = EvaluadorMano.evaluarMejorMano(j.getMano(), cartasComunitarias);
            j.setJugada(jugada);

            infoArea.append(j.getNombre() + " tiene: " + jugada.getTipoJugada() + 
                          " (" + jugada.getCartas() + ")\n");

            if (mejorJugada == null || jugada.compareTo(mejorJugada) > 0) {
                mejorJugada = jugada;
                ganador = j;
            }
        }

        if (ganador != null) {
            infoArea.append("\n¡" + ganador.getNombre() + " gana el bote de " + 
                           bote.getCantidad() + " fichas!\n");
            ganador.ajustarFichas(bote.getCantidad());
        } else {
            infoArea.append("\nNo hay ganador claro.\n");
        }

        estado = EstadoRonda.FIN;

        int opcion = JOptionPane.showConfirmDialog(this, "¿Quieres jugar otra ronda?", 
                                                  "Fin de ronda", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            empezarRonda();
        } else {
            System.exit(0);
        }
    }
}