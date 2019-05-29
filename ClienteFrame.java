import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;

public class ClienteFrame extends JFrame implements Runnable {
    Image personagem[] = new Image[20];
    Personagem person = new Personagem(); //
    static PrintStream os = null;
    JTextArea textArea;
    int pos1 = 20;
    int pos2 = 100;
    int varControle = -1;

    class Personagem extends JPanel {
        String nomePersonagem[] = { "RambroParado", "RambroMov1", "RambroMov2", "RambroQuaseParado", "BromaxParado",
                "BromaxMov1", "BromaxMov2", "BromaxQuaseParado" };
        Hashtable<String, Integer> valorNumeroPersonagem = new Hashtable<String, Integer>();

        Personagem() {
            // 32 x 32 personagem
            setPreferredSize(new Dimension(1000, 720));
            try {
                for (int i = 0; i < 8; i++) {
                    personagem[i] = ImageIO.read(new File("img/" + nomePersonagem[i] + ".png"));
                    valorNumeroPersonagem.put(nomePersonagem[i], i);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } // RESOLVER PROBLEMA

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(personagem[valorNumeroPersonagem.get("RambroParado")], pos1,
                    getSize().height - (personagem[valorNumeroPersonagem.get("RambroParado")].getHeight(this) * 3) - 38,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getHeight(this) * 3, this);
            g.drawImage(personagem[valorNumeroPersonagem.get("BromaxParado")], pos2,
                    getSize().height - (personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3) - 38,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3, this);
            Toolkit.getDefaultToolkit().sync();
        }

    }

    ClienteFrame() {
        super("TowerFall");
        add(person);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                String inputValue = new String(varControle + " ");
                // (número do cliente, botão apertado)
                switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    inputValue += "VK_RIGHT ";
                    os.println(inputValue);
                    // System.out.println(inputValue);
                    break;
                }
            }
        });
    }

    public static void main(String[] args) {
        new Thread(new ClienteFrame()).start();
    }

    public void run() {
        Socket socket = null;
        Scanner is = null;

        try {
            socket = new Socket("25.79.169.156", 80);
            os = new PrintStream(socket.getOutputStream(), true); // Recebendo os dados que o servidor manda
            is = new Scanner(socket.getInputStream()); // Escaneia os dados fornecidos pelo cliente
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host");
        }

        try {
            if (varControle < 0) {
                String inputCliente;
                inputCliente = is.nextLine();
                int vl = Integer.parseInt(inputCliente);
                varControle = vl;
                System.out.println("Cliente " + varControle);
            }
            String inputLine;
            String vet[] = new String[10];

            do {
                inputLine = is.nextLine();
                System.out.println(inputLine);
                vet = inputLine.split(" ");
                int valor = Integer.parseInt(vet[1]);
                int valor2 = Integer.parseInt(vet[0]);
                if (valor2 == 0) 
                    pos1 += valor;
                 else if (valor2 == 1) 
                    pos2 += valor;
                repaint();
            } while (!inputLine.equals(""));

            os.close();
            is.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}

/*
 * JogoBase() { super("Trabalho"); setDefaultCloseOperation(EXIT_ON_CLOSE);
 * add(des); pack(); setVisible(true);
 * 
 * new Thread() { public void run() { while (true) { try { vida(); repaint();
 * sleep(50); } catch (InterruptedException ex) { } } } }.start();
 * 
 * addKeyListener(new KeyAdapter() {
 * 
 * public void keyPressed(KeyEvent e) { switch (e.getKeyCode()) { case
 * KeyEvent.VK_RIGHT: if (dir == -1) pos += 10 - img[estado].getWidth(null) * 3;
 * pos += 10; dir = 1; estado = ANDA0; break; case KeyEvent.VK_LEFT: if (dir ==
 * 1) pos -= 10 - img[estado].getWidth(null) * 3; // pos -=
 * 10-img[estado].getWidth(null); // tentou evitar o salto pos -= 10; dir = -1;
 * estado = ANDA0; break; case KeyEvent.VK_W: estado = SOCO0; break; }
 * System.out.println(pos); repaint(); }
 * 
 * }); }
 */
