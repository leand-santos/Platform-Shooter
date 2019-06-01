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
    Image personagem[] = new Image[20];
    Image background[] = new Image[1];
    static PrintStream os = null;
    int posX1 = 0; // Variável para posição X do personagem 1
    int posX2 = 100; // Variável para posição X do personagem 2
    int posY1 = 0;
    int posY2 = 100;
    int varControle = -1; // Variável para saber qual cliente é
    final int cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3;

    class Personagem extends JPanel {
        // String para nomes das imagens dos personagens e cenário
        String nomePersonagem[] = { "RambroParado", "RambroMov1", "RambroMov2", "RambroQuaseParado", "BromaxParado",
                "BromaxMov1", "BromaxMov2", "BromaxQuaseParado" };
        // Hashtable armazena os valores de acesso ao vetor de imagem
        Hashtable<String, Integer> valorNumeroPersonagem = new Hashtable<String, Integer>();

        Personagem() {
            // Tamanho das sprites: 32 x 32 personagem
            setPreferredSize(new Dimension(1024, 768)); // 32*32 e 32*24 Tamanho do mapa
            try {
                background[0] = ImageIO.read(new File("img/bg/Background.png"));
                // Lê todas as imagens da pasta img e coloca os valores da posição da imagem no
                // vetor
                for (int i = 0; i < 8; i++) {
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
            g.drawImage(personagem[valorNumeroPersonagem.get("RambroParado")], posX1, posY1,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("RambroParado")].getHeight(this) * 3, this);
            g.drawImage(personagem[valorNumeroPersonagem.get("BromaxParado")], posX2,
                    getSize().height - (personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3) - 32,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getWidth(this) * 3,
                    personagem[valorNumeroPersonagem.get("BromaxParado")].getHeight(this) * 3, this);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    /*
    public void gravidade() { 
        int delay = 0; // delay de 5 seg. 
        int interval = 5; // intervalo de 1 seg. Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() { 
         public void run() { 
                posY1++;
                repaint(); 
        } }, delay, interval); 
    }
    */

    ClienteFrame() {
        super("TowerFall");
        setResizable(false);
        add(new Personagem());
        // gravidade();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                String inputValue = new String(varControle + " ");
                // (número do cliente, botão apertado)
                switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (varControle == cliente1)
                        inputValue += posX1 + " " + posY1 + " VK_RIGHT ";
                    else if (varControle == cliente2)
                        inputValue += posX2 + " " + posY2 + " VK_RIGHT ";
                    os.println(inputValue);
                    // System.out.println(inputValue);
                    break;
                }
            }
        });
        //String inputVal = new String(varControle + " " + posX1 + " " + posY1 + " NULL ");
        //os.println(inputVal);
        
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

                int posAtualRecebida = Integer.parseInt(vet[posClienteX]);
                int numClienteRecebido = Integer.parseInt(vet[numCliente]);
                int numAtualRecebidaY = Integer.parseInt(vet[posClienteY]);

                if (numClienteRecebido == cliente1)
                    posX1 = posAtualRecebida;
                else if (numClienteRecebido == cliente2)
                    posX2 = posAtualRecebida;
                //posY1 = numAtualRecebidaY;
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