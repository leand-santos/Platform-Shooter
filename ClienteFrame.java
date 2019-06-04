import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Timer;

import javax.imageio.*;

public class ClienteFrame extends JFrame implements Runnable {
    Image personagem[] = new Image[24];
    Image background[] = new Image[1];
    static PrintStream os = null;
    int posX1 = 64;
    int posY1 = 64;
    int posX2 = 960;
    int posY2 = 64;
    int varControle = -1; // Variável para saber qual cliente é
    final int cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3,
            gravCliente = 4;
    String estadoCliente1 = new String("Player1Parado"), estadoCliente2 = new String("Player2Parado"), inputValue;

    class Personagem extends JPanel {
        // String para nomes das imagens dos personagens e cenário
        String nomePersonagem[] = { "Player1Parado", "Player1Mov1", "Player1Mov2", "Player1Mov3", "Player1Mov4",
                "Player1Mov5", "Player1Morte1", "Player1Morte2", "Player1Morte3", "Player1Morte4", "Player1Morte5",
                "Player1Morte6", "Player2Parado", "Player2Mov1", "Player2Mov2", "Player2Mov3", "Player2Mov4",
                "Player2Mov5", "Player2Morte1", "Player2Morte2", "Player2Morte3", "Player2Morte4", "Player2Morte5",
                "Player2Morte6" };
        // Hashtable armazena os valores de acesso ao vetor de imagem
        Hashtable<String, Integer> valorNumeroPersonagem = new Hashtable<String, Integer>();

        Personagem() {
            // Tamanho das sprites: 32 x 32 personagem
            setPreferredSize(new Dimension(1024, 768)); // 32*32 e 32*24 Tamanho do mapa
            try {
                background[0] = ImageIO.read(new File("img/bg/Background.png"));
                // Lê todas as imagens da pasta img e coloca os valores da posição da imagem no
                // vetor
                for (int i = 0; i < 24; i++) {
                    personagem[i] = ImageIO.read(new File("img/player/" + nomePersonagem[i] + ".png"));
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
            g.drawImage(background[0], 0, 0, this);
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente1)], posX1, posY1,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getWidth(this) * 2,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getHeight(this) * 2, this);
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente2)], posX2, posY2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getWidth(this) * 2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getHeight(this) * 2, this);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    ClienteFrame() {
        super("TowerFall");
        setResizable(false);
        // gravidade(os);
        add(new Personagem());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                inputValue = new String(varControle + " ");
                // (número do cliente, botão apertado)
                switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (varControle == cliente1)
                        inputValue += posX1 + " " + posY1 + " VK_RIGHT " + " 0 ";
                    else if (varControle == cliente2)
                        inputValue += posX2 + " " + posY2 + " VK_RIGHT " + " 0 ";
                    os.println(inputValue);
                    // System.out.println(inputValue);
                    break;
                case KeyEvent.VK_LEFT:
                    if (varControle == cliente1)
                        inputValue += posX1 + " " + posY1 + " VK_LEFT " + " 0 ";
                    else if (varControle == cliente2)
                        inputValue += posX2 + " " + posY2 + " VK_LEFT " + " 0 ";
                    os.println(inputValue);
                    // System.out.println(inputValue);
                    break;
                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(5000);
                    do {
                        inputValue = new String(varControle + " ");
                        Thread.sleep(10);
                        if (varControle == cliente1)
                            inputValue += posX1 + " " + posY1 + " A " + " 0 ";
                        if (varControle == cliente2)
                            inputValue += posX2 + " " + posY2 + " A " + " 0 ";
                        os.println(inputValue);
                    } while (true);
                } catch (InterruptedException e) {

                }

            }

        }).start();
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
            // Verifica a variável de controle e define qual será o cliente
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
                int posAtualRecebidaX = Integer.parseInt(vet[posClienteX]);
                int posAtualRecebidaY = Integer.parseInt(vet[posClienteY]);
                int numClienteRecebido = Integer.parseInt(vet[numCliente]);
                int gravRecebida = Integer.parseInt(vet[gravCliente]);
                if (numClienteRecebido == cliente1) {
                    posX1 = posAtualRecebidaX;
                    if (gravRecebida == 1) {
                        posY1 = posAtualRecebidaY;
                    }
                }
                if (numClienteRecebido == cliente2) {
                    posX2 = posAtualRecebidaX;
                    if (gravRecebida == 1) {
                        posY2 = posAtualRecebidaY;
                    }
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        repaint();
                    }
                });
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
