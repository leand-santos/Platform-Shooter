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
    static PrintStream os = null;
    int posX1 = 20; // Variável para posição X do personagem 1
    int posX2 = 100; // Variável para posição X do personagem 2
    int varControle = -1; // Variável para saber qual cliente é
    final int cliente1 = 0, cliente2 = 1, numCliente = 0, posCliente = 1, btCliente = 2; // Constantes para facilitar a leitura do código

    class Personagem extends JPanel {
        // String para nomes das imagens dos personagens e cenário
        String nomePersonagem[] = { "RambroParado", "RambroMov1", "RambroMov2", "RambroQuaseParado", "BromaxParado",
                "BromaxMov1", "BromaxMov2", "BromaxQuaseParado" };
        // Hashtable armazena os valores de acesso ao vetor de imagem
        Hashtable<String, Integer> valorNumeroPersonagem = new Hashtable<String, Integer>();

        Personagem() {
            // Tamanho das sprites: 32 x 32 personagem
            setPreferredSize(new Dimension(1024, 704)); // 32*32 e 32*22 Tamanho do mapa
            try {
                // Lê todas as imagens da pasta img e coloca os valores da posição da imagem no veto
                for (int i = 0; i < 8; i++) {
                    personagem[i] = ImageIO.read(new File("img/" + nomePersonagem[i] + ".png"));
                    valorNumeroPersonagem.put(nomePersonagem[i], i);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } 

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(personagem[valorNumeroPersonagem.get("RambroParado")], posX1,
                    getSize().height - (personagem[valorNumeroPersonagem.get("RambroParado")].getHeight(this) * 3) - 38,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getHeight(this) * 3, this);
            g.drawImage(personagem[valorNumeroPersonagem.get("BromaxParado")], posX2,
                    getSize().height - (personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3) - 38,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3, this);
            Toolkit.getDefaultToolkit().sync();
        }

    }

    ClienteFrame() {
        super("TowerFall");
        setResizable(false);
        setLayout(new GridLayout());
        add(new Personagem());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                String inputValue = new String(varControle + " ");
                // (número do cliente, botão apertado)
                switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    inputValue += posX1 + " VK_RIGHT ";
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
                int posAtualRecebida = Integer.parseInt(vet[posCliente]);
                int numClienteRecebido = Integer.parseInt(vet[numCliente]);
                if (numClienteRecebido == cliente1) 
                    posX1 = posAtualRecebida;
                 else if (numClienteRecebido == cliente2) 
                    posX2 = posAtualRecebida;
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
