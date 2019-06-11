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
    Image weapon[] = new Image[3];
    Image bullet[] = new Image[2];
    Image background[] = new Image[1];
    static PrintStream os = null;
    int posX1 = 64;
    int posY1 = 63;
    int posX2 = 960;
    int posY2 = 63;
    int posTiroX1 = 1030;
    int posTiroY1 = 1000;
    int posTiroX2 = 1030;
    int posTiroY2 = 1000;
    int posArmaX1 = 69; // 5
    int posArmaY1 = 75; // 10
    int posArmaX2 = 962; // 2
    int posArmaY2 = 75; // 10
    int varControle = -1; // Variável para saber qual cliente é
    int dirCliente1 = 1, dirCliente2 = 1, estadoClient1 = 0, estadoClient2 = 0;
    int gravidade1 = 0, gravidade2 = 0, qntPulo = 0, canBulletGo1 = 0, canBulletGo2 = 0;
    final int size = 2, cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3,
            gravCliente = 4, dirCliente = 5, estadoCliente = 6, posBulletX = 7, posBulletY = 8, bulletGo = 9;
    String estadoCliente1 = new String("Player1Parado"), estadoCliente2 = new String("Player2Parado"), inputValue;
    boolean isKeyRightPressed = false, isKeyLeftPressed = false, isKeySpacePressed = false, isBulletOn = false,
            isBulletOnCliente1 = false, isBulletOnCliente2 = false;

    public static void main(String[] args) {
        new Thread(new ClienteFrame()).start();
    }

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
                bullet[0] = ImageIO.read(new File("img/bullets/Bullet1.png"));
                bullet[1] = ImageIO.read(new File("img/bullets/Bullet2.png"));
                weapon[0] = ImageIO.read(new File("img/weapons/Weapon1.png"));
                weapon[1] = ImageIO.read(new File("img/weapons/Weapon2.png"));
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
            g.drawImage(bullet[0], posTiroX1, posTiroY1, bullet[0].getWidth(this) * size,
                    bullet[0].getHeight(this) * size, this);
            g.drawImage(bullet[1], posTiroX2, posTiroY2, bullet[1].getWidth(this) * size,
                    bullet[1].getHeight(this) * size, this);
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente1)], posX1, posY1,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getWidth(this) * size * dirCliente1,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getHeight(this) * size, this);
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente2)], posX2, posY2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getWidth(this) * size * dirCliente2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getHeight(this) * size, this);
            g.drawImage(weapon[0], posArmaX1, posArmaY1, weapon[0].getWidth(this) * size * dirCliente1,
                    weapon[0].getHeight(this) * size, this);
            g.drawImage(weapon[1], posArmaX2, posArmaY2, weapon[1].getWidth(this) * size * dirCliente2,
                    weapon[1].getHeight(this) * size, this);

            Toolkit.getDefaultToolkit().sync();
        }
    }

    public void concatenaValores(String btRecebido, int cliente) {
        if (cliente == 1)
            inputValue = varControle + " " + posX1 + " " + posY1 + " " + btRecebido + " 0 " + dirCliente1 + " "
                    + estadoClient1 + " " + posTiroX1 + " " + posTiroY1 + " 0";
        if (cliente == 2)
            inputValue = varControle + " " + posX2 + " " + posY2 + " " + btRecebido + " 0 " + dirCliente2 + " "
                    + estadoClient2 + " " + posTiroX2 + " " + posTiroY2 + " 0";
    }

    ClienteFrame() {
        super("TowerFall");
        setResizable(false);
        add(new Personagem());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_D:
                    isKeyRightPressed = true;
                    break;
                case KeyEvent.VK_A:
                    isKeyLeftPressed = true;
                    break;
                case KeyEvent.VK_SPACE:
                    isKeySpacePressed = true;
                    break;
                case KeyEvent.VK_W:
                    isBulletOn = true;
                    if(varControle == 0)
                        isBulletOnCliente1 = true;
                    else if (varControle == 1)
                        isBulletOnCliente2 = true;
                    break;
                }
            }

            public void keyReleased(KeyEvent e2) {
                switch (e2.getKeyCode()) {
                case KeyEvent.VK_D:
                    isKeyRightPressed = false;
                    break;
                case KeyEvent.VK_A:
                    isKeyLeftPressed = false;
                    break;
                case KeyEvent.VK_SPACE:
                    isKeySpacePressed = false;
                    break;
                case KeyEvent.VK_W:
                    isBulletOn = false;
                    if(varControle == 0)
                        isBulletOnCliente1 = false;
                    else if (varControle == 1)
                        isBulletOnCliente2 = false;
                    break;
                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(100);
                        if (isBulletOnCliente1) {
                            while(canBulletGo1 == 1){
                                concatenaValores("BULLET", 1);
                                os.println(inputValue);
                                Thread.sleep(30);
                            } 
                        }
                    } while (true);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(100);
                        if (isBulletOnCliente2) {
                            while(canBulletGo2 == 1){
                                concatenaValores("BULLET", 2);
                                os.println(inputValue);
                                Thread.sleep(30);
                            }
                        }
                    } while (true);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        new Thread(new Runnable() {

    public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(30);
                        if (isKeyRightPressed) {
                            if (varControle == cliente1)
                                concatenaValores("RIGHT", 1);
                            else if (varControle == cliente2)
                                concatenaValores("RIGHT", 2);
                            os.println(inputValue);
                        }
                        if (isKeyLeftPressed) {
                            if (varControle == cliente1)
                                concatenaValores("LEFT", 1);
                            else if (varControle == cliente2)
                                concatenaValores("LEFT", 2);
                            os.println(inputValue);
                        }
                        if (isKeySpacePressed) {
                            if (varControle == cliente1) {
                                if (isKeyRightPressed == true)
                                    concatenaValores("SPACE-AND-RIGHT", 1);
                                else if (isKeyLeftPressed == true)
                                    concatenaValores("SPACE-AND-LEFT", 1);
                                else
                                    concatenaValores("SPACE", 1);
                            } else if (varControle == cliente2) {
                                if (isKeyRightPressed == true)
                                    concatenaValores("SPACE-AND-RIGHT", 2);
                                else if (isKeyLeftPressed == true)
                                    concatenaValores("SPACE-AND-LEFT", 2);
                                else
                                    concatenaValores("SPACE", 2);
                            }
                            os.println(inputValue);
                        }
                    } while (true);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(30);
                        if (gravidade1 == 1 && varControle == 0) {
                            concatenaValores("GRAVIDADE", 1);
                            os.println(inputValue);
                        }
                    } while (true);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(30);
                        if (gravidade2 == 1 && varControle == 1) {
                            concatenaValores("GRAVIDADE", 2);
                            os.println(inputValue);
                        }
                    } while (true);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    public void verificaEstado(int estado, int client) {
        if (client == 0) {
            if (estado == 0)
                estadoCliente1 = "Player1Parado";
            if (estado == 1)
                estadoCliente1 = "Player1Mov1";
            if (estado == 2)
                estadoCliente1 = "Player1Mov2";
            if (estado == 3)
                estadoCliente1 = "Player1Mov3";
            if (estado == 4)
                estadoCliente1 = "Player1Mov4";
            if (estado == 5)
                estadoCliente1 = "Player1Mov5";
        }
        if (client == 1) {
            if (estado == 0)
                estadoCliente2 = "Player2Parado";
            if (estado == 1)
                estadoCliente2 = "Player2Mov1";
            if (estado == 2)
                estadoCliente2 = "Player2Mov2";
            if (estado == 3)
                estadoCliente2 = "Player2Mov3";
            if (estado == 4)
                estadoCliente2 = "Player2Mov4";
            if (estado == 5)
                estadoCliente2 = "Player2Mov5";
        }
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
                int posAtualRecebidaTiroX = Integer.parseInt(vet[posBulletX]);
                int posAtualRecebidaTiroY = Integer.parseInt(vet[posBulletY]);
                int numClienteRecebido = Integer.parseInt(vet[numCliente]);
                int gravRecebida = Integer.parseInt(vet[gravCliente]);
                int dirClienteRecebido = Integer.parseInt(vet[dirCliente]);
                int estClienteRecebido = Integer.parseInt(vet[estadoCliente]);
                int canBulletGo = Integer.parseInt(vet[bulletGo]);
                if (numClienteRecebido == cliente1) {
                    canBulletGo1 = canBulletGo;
                    posX1 = posAtualRecebidaX;

                    if (dirClienteRecebido == 1)
                        posArmaX1 = posAtualRecebidaX + 5;
                    else
                        posArmaX1 = posAtualRecebidaX - 5;
                    posArmaY1 = posAtualRecebidaY + 10;
                    
                    if (dirClienteRecebido == 1)
                        posTiroX1 = posAtualRecebidaTiroX;
                    else
                        posTiroX1 = posAtualRecebidaTiroX;
                    posTiroY1 = posAtualRecebidaTiroY;
                    
                    estadoCliente1 = vet[estadoCliente];
                    dirCliente1 = dirClienteRecebido;
                    verificaEstado(estClienteRecebido, 0);
                    if (gravRecebida == 1) {
                        posY1 = posAtualRecebidaY;
                    }
                    gravidade1 = gravRecebida;
                }
                if (numClienteRecebido == cliente2) {
                    canBulletGo2 = canBulletGo;
                    posX2 = posAtualRecebidaX;

                    if (dirClienteRecebido == 1)
                        posArmaX2 = posAtualRecebidaX + 2;
                    else
                        posArmaX2 = posAtualRecebidaX - 2;
                    posArmaY2 = posAtualRecebidaY + 10;

                    if (dirClienteRecebido == 1)
                        posTiroX2 = posAtualRecebidaTiroX;
                    else
                        posTiroX2 = posAtualRecebidaTiroX;
                    posTiroY2 = posAtualRecebidaTiroY;
                    
                    estadoCliente2 = vet[estadoCliente];
                    dirCliente2 = dirClienteRecebido;
                    verificaEstado(estClienteRecebido, 1);
                    if (gravRecebida == 1) {
                        posY2 = posAtualRecebidaY;
                    }
                    gravidade2 = gravRecebida;
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
