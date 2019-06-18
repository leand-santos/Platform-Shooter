import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;

public class ClienteFrame extends JFrame implements Runnable {
    // Constantes para acesso a String recebida do Servidor
    final int size = 2, cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3,
            gravCliente = 4, dirCliente = 5, estadoCliente = 6, posBulletX = 7, posBulletY = 8, bulletGo = 9,
            qntVida = 10;

    // Posições Jogador 1
    int posX1 = 64;
    int posY1 = 60;
    int posTiroX1 = 1030;
    int posTiroY1 = 1000;
    int posArmaX1 = 69; // 5
    int posArmaY1 = 75; // 10
    int posCorX1;

    // Atributos Jogador 1
    int dirCliente1 = 1;
    int estadoClient1 = 0;
    int gravidade1 = 0;
    int canBulletGo1 = 0;
    int canShoot1 = 0;
    int numVidas1 = 3;
    int posWinX1 = 2000;

    // Posições Jogador 2
    int posX2 = 960;
    int posY2 = 60;
    int posTiroX2 = 1030;
    int posTiroY2 = 1000;
    int posArmaX2 = 962; // 2
    int posArmaY2 = 75; // 10
    int posCorX2;
    int posWinX2 = 2000;

    // Atributos Jogador 2
    int dirCliente2 = 1;
    int estadoClient2 = 0;
    int gravidade2 = 0;
    int canBulletGo2 = 0;
    int canShoot2 = 0;
    int numVidas2 = 3;

    // Posição Y doscorações dos Jogadores 1 e 2
    int posCorY = 700;
    int posWinY = 400;

    // Variaveis para salvar as imagens
    Image personagem[] = new Image[24];
    Image weapon[] = new Image[3];
    Image bullet[] = new Image[2];
    Image win[] = new Image[2];
    Image heart[] = new Image[1];
    Image background[] = new Image[1];
    String estadoCliente1 = new String("Player1Parado"), estadoCliente2 = new String("Player2Parado"), inputValue;

    // Variavel para enviar String ao Servidor
    static PrintStream os = null;

    // Variáveis de controle para determinar cada cliente
    int varControle = -1;

    // Verificar se a tecla está apertada ou não
    boolean isKeyRightPressed = false, isKeyLeftPressed = false, isKeySpacePressed = false, isBulletOn = false,
            isBulletOnCliente1 = false, isBulletOnCliente2 = false, ganhou = false;

    public static void main(String[] args) {
        new Thread(new ClienteFrame()).start();
    }

    // Classe para carregar e desenhar as imagens no JFrame
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
                heart[0] = ImageIO.read(new File("img/heart/Heart.png"));
                win[0] = ImageIO.read(new File("img/victory/P1Win.png"));
                win[1] = ImageIO.read(new File("img/victory/P2Win.png"));
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

            // Carrega Background
            g.drawImage(background[0], 0, 0, this);

            // Carrega Coração Jogador 1
            posCorX1 = 28;
            for (int i = 1; i <= 3; i++) {
                if (i <= numVidas1) {
                    g.drawImage(heart[0], posCorX1, posCorY, heart[0].getWidth(this), heart[0].getHeight(this), this);
                    posCorX1 += 32;
                } else {
                    posCorX1 += 1000;
                    g.drawImage(heart[0], posCorX1, posCorY, heart[0].getWidth(this), heart[0].getHeight(this), this);
                }
            }

            // Carrega Coração Jogador 2
            posCorX2 = 900;
            for (int i = 1; i <= 3; i++) {
                if (i <= numVidas2) {
                    g.drawImage(heart[0], posCorX2, posCorY, heart[0].getWidth(this), heart[0].getHeight(this), this);
                    posCorX2 += 32;
                } else {
                    posCorX2 += 1000;
                    g.drawImage(heart[0], posCorX2, posCorY, heart[0].getWidth(this), heart[0].getHeight(this), this);
                }
            }

            // Carrega Tiro
            g.drawImage(bullet[0], posTiroX1, posTiroY1, bullet[0].getWidth(this) * size,
                    bullet[0].getHeight(this) * size, this);
            g.drawImage(bullet[1], posTiroX2, posTiroY2, bullet[1].getWidth(this) * size,
                    bullet[1].getHeight(this) * size, this);

            // Carrega Jogador 1
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente1)], posX1, posY1,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getWidth(this) * size * dirCliente1,
                    personagem[valorNumeroPersonagem.get(estadoCliente1)].getHeight(this) * size, this);

            // Carrega Jogador 2
            g.drawImage(personagem[valorNumeroPersonagem.get(estadoCliente2)], posX2, posY2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getWidth(this) * size * dirCliente2,
                    personagem[valorNumeroPersonagem.get(estadoCliente2)].getHeight(this) * size, this);

            // Carrega armas
            g.drawImage(weapon[0], posArmaX1, posArmaY1, weapon[0].getWidth(this) * size * dirCliente1,
                    weapon[0].getHeight(this) * size, this);
            g.drawImage(weapon[1], posArmaX2, posArmaY2, weapon[1].getWidth(this) * size * dirCliente2,
                    weapon[1].getHeight(this) * size, this);

            // Imagens WIN
            g.drawImage(win[0], posWinX1, posWinY, this);
            g.drawImage(win[1], posWinX2, posWinY, this);

            Toolkit.getDefaultToolkit().sync();
        }
    }

    // Monta a String para ser enviada ao Servidor
    public void concatenaValores(String btRecebido, int cliente) {
        if (cliente == 1)
            inputValue = varControle + " " + posX1 + " " + posY1 + " " + btRecebido + " 0 " + dirCliente1 + " "
                    + estadoClient1 + " " + posTiroX1 + " " + posTiroY1 + " " + canShoot1 + " " + numVidas1;
        if (cliente == 2)
            inputValue = varControle + " " + posX2 + " " + posY2 + " " + btRecebido + " 0 " + dirCliente2 + " "
                    + estadoClient2 + " " + posTiroX2 + " " + posTiroY2 + " " + canShoot2 + " " + numVidas2;
    }

    ClienteFrame() {

        super("Platform Shooter");
        setResizable(false);
        add(new Personagem());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // Lê as teclas
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
                    if (varControle == 0)
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
                    if (varControle == 0)
                        isBulletOnCliente1 = false;
                    else if (varControle == 1)
                        isBulletOnCliente2 = false;
                    break;
                }
            }
        });

        // Thread do tiro do jogador 1
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(100);
                        while (isBulletOnCliente1) {
                            canShoot1 = 1;
                            concatenaValores("BULLET", 1);
                            os.println(inputValue);
                            while (canBulletGo1 == 1) {
                                canShoot1 = -1;
                                concatenaValores("BULLET", 1);
                                os.println(inputValue);
                                Thread.sleep(15);
                            }
                        }
                    } while (ganhou == false);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        // Thread do tiro do jogador 2
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        Thread.sleep(100);
                        while (isBulletOnCliente2) {
                            canShoot2 = 1;
                            concatenaValores("BULLET", 2);
                            os.println(inputValue);
                            while (canBulletGo2 == 1) {
                                canShoot2 = -1;
                                concatenaValores("BULLET", 2);
                                os.println(inputValue);
                                Thread.sleep(15);
                            }
                        }
                    } while (ganhou == false);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        // Thread do movimento
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        if (canBulletGo1 == 1 || canBulletGo2 == 1)
                            Thread.sleep(20);
                        else
                            Thread.sleep(15);
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
                    } while (ganhou == false);
                } catch (InterruptedException e) {
                }
            }
        }).start();

        // Thread da gravidade
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (varControle == -1)
                        Thread.sleep(500);
                    do {
                        if (canBulletGo1 == 1 || canBulletGo2 == 1)
                            Thread.sleep(20);
                        else
                            Thread.sleep(15);
                        if (gravidade1 == 1 && varControle == 0) {
                            concatenaValores("GRAVIDADE", 1);
                            os.println(inputValue);
                        }
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

    // Muda a imagem do personagem para gerar animação
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

    public void verificaEstadoMorte(int client) {
        if (client == 0) {
            for (int i = 1; i <= 6; i++) {
                estadoCliente1 = "Player1Morte" + i;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        repaint();
                    }
                });
                try{                                
                    Thread.sleep(150);
                } catch(InterruptedException e){
                }
            }
        }
        if (client == 1) {
            for (int i = 1; i <= 6; i++) {
                estadoCliente2 = "Player2Morte" + i;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        repaint();
                    }
                });
                try{                                
                    Thread.sleep(150);
                } catch(InterruptedException e){
                }
            }
        }
    }

    // Recebe as informações do Servidor e conecta com ele
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

        // Verifica a variável de controle e define qual será o cliente
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
                //System.out.println(inputLine);
                vet = inputLine.split(" ");

                // Cliente recebido
                int numClienteRecebido = Integer.parseInt(vet[numCliente]);

                // Posição recebida
                int posAtualRecebidaX = Integer.parseInt(vet[posClienteX]);
                int posAtualRecebidaY = Integer.parseInt(vet[posClienteY]);
                int posAtualRecebidaTiroX = Integer.parseInt(vet[posBulletX]);
                int posAtualRecebidaTiroY = Integer.parseInt(vet[posBulletY]);

                // Atributos recebidos
                int gravRecebida = Integer.parseInt(vet[gravCliente]);
                int dirClienteRecebido = Integer.parseInt(vet[dirCliente]);
                int estClienteRecebido = Integer.parseInt(vet[estadoCliente]);
                int canBulletGo = Integer.parseInt(vet[bulletGo]);
                int qntVidaRecebida = Integer.parseInt(vet[qntVida]);

                // Modifica os atributos do Jogador 1
                if (numClienteRecebido == cliente1) {
                    numVidas2 = qntVidaRecebida;
                    if (numVidas2 == 0) {
                        ganhou = true;
                        posArmaY2 = 1000;
                        verificaEstadoMorte(1);
                        posWinX1 = 332;
                    }
                    // Define se o tiro pode continuar ou não
                    canBulletGo1 = canBulletGo;

                    // Altera a posição e atributos do jogador
                    posX1 = posAtualRecebidaX;
                    if (gravRecebida == 1) {
                        posY1 = posAtualRecebidaY;
                    }
                    estadoCliente1 = vet[estadoCliente];
                    dirCliente1 = dirClienteRecebido;
                    gravidade1 = gravRecebida;
                    verificaEstado(estClienteRecebido, 0);

                    // Define posição da arma
                    if (dirClienteRecebido == 1)
                        posArmaX1 = posAtualRecebidaX + 5;
                    else
                        posArmaX1 = posAtualRecebidaX - 5;
                    posArmaY1 = posAtualRecebidaY + 10;

                    // Definse a posição do tiro
                    if (dirClienteRecebido == 1)
                        posTiroX1 = posAtualRecebidaTiroX;
                    else
                        posTiroX1 = posAtualRecebidaTiroX;
                    posTiroY1 = posAtualRecebidaTiroY;
                }

                // Modifica os atributos do Jogador 2
                if (numClienteRecebido == cliente2) {
                    numVidas1 = qntVidaRecebida;
                    if (numVidas1 == 0) {
                        ganhou = true;
                        posArmaY1 = 1000;
                        verificaEstadoMorte(0);
                        posWinX2 = 332;
                    }
                    // Define se o tiro pode continuar ou não
                    canBulletGo2 = canBulletGo;

                    // Altera a posição e atributos do jogador
                    posX2 = posAtualRecebidaX;
                    if (gravRecebida == 1) {
                        posY2 = posAtualRecebidaY;
                    }
                    estadoCliente2 = vet[estadoCliente];
                    dirCliente2 = dirClienteRecebido;
                    gravidade2 = gravRecebida;
                    verificaEstado(estClienteRecebido, 1);

                    // Define posição da arma
                    if (dirClienteRecebido == 1)
                        posArmaX2 = posAtualRecebidaX + 2;
                    else
                        posArmaX2 = posAtualRecebidaX - 2;
                    posArmaY2 = posAtualRecebidaY + 10;

                    // Definse a posição do tiro
                    if (dirClienteRecebido == 1)
                        posTiroX2 = posAtualRecebidaTiroX;
                    else
                        posTiroX2 = posAtualRecebidaTiroX;
                    posTiroY2 = posAtualRecebidaTiroY;
                }

                // Atualiza a imagem
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
