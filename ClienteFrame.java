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
    int pos = 20;
    int varControle = -1;
    String nomePersonagem[] = { "RambroParado", "RambroMov1", "RambroMov2", "RambroQuaseParado", "BromaxParado",
            "BromaxMov1", "BromaxMov2", "BromaxQuaseParado"};
    Hashtable<String, Integer> valorPosicaoImagem = new Hashtable<String, Integer>();

    class Personagem extends JPanel {
        Personagem() {
            // 32 x 32 personagem
            setPreferredSize(new Dimension(1000, 720));
            //try {
                //for(int i = 0; i < 8; i++) { //
                    //personagem[i] = ImageIO.read(new File("img/" + nomePersonagem[i] + ".png"));
                    //valorPosicaoImagem.put(nomePersonagem[i], i);
                    //System.out.println(valorPosicaoImagem.get(nomePersonagem[i]));
                //}

            //} catch (IOException e) {
            //    JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro",
            //            JOptionPane.ERROR_MESSAGE);
            //    System.exit(1);
            // }
        } //RESOLVER PROBLEMA

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //g.drawImage(personagem[valorPosicaoImagem.get("RambroParado")], pos,
            //        getSize().height - (personagem[valorPosicaoImagem.get("RambroParado")].getHeight(this) * 3) - 38,
            //        personagem[valorPosicaoImagem.get("RambroParado")].getWidth(this) * 3,
            //        personagem[valorPosicaoImagem.get("RambroParado")].getHeight(this) * 3, this);
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
                    System.out.println(inputValue);
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
            socket = new Socket("127.0.0.1", 80);
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

            do {
                inputLine = is.nextLine();
                int valor = Integer.parseInt(inputLine);
                pos += valor;
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
