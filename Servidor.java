import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

class Servidor {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(80);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + 80 + ", " + e);
            System.exit(1);
        }

        for (int i = 0; i < 3; i++) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: " + 80 + ", " + e);
                System.exit(1);
            }

            System.out.println("Accept Funcionou!");

            new Servindo(clientSocket).start();

        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Servindo extends Thread {
    String nomePersonagem[] = { "Player1Parado", "Player1Mov1", "Player1Mov2", "Player1Mov3", "Player1Mov4",
            "Player1Mov5", "Player1Morte1", "Player1Morte2", "Player1Morte3", "Player1Morte4", "Player1Morte5",
            "Player1Morte6", "Player2Parado", "Player2Mov1", "Player2Mov2", "Player2Mov3", "Player2Mov4", "Player2Mov5",
            "Player2Morte1", "Player2Morte2", "Player2Morte3", "Player2Morte4", "Player2Morte5", "Player2Morte6" };
    final int anda = 3, cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3,
            gravCliente = 4, dirCliente = 5, estadoCliente = 6;
    Socket clientSocket;
    static PrintStream os[] = new PrintStream[3];
    static int cont = 0;
    String vet[] = new String[20];
    int direcao, estadoClient1 = 1, estadoClient2 = 1, estado;

    Servindo(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            os[cont] = new PrintStream(clientSocket.getOutputStream());
            os[cont].println(cont);
            os[cont].flush();
        } catch (IOException erro) {
        }
    }

    public int verificaGrav(int posX, int posY) {
        MatrizMapa posMap = new MatrizMapa();
        int matX1, matX2, matY1, matY2;
        matX1 = posX / 32;
        matX2 = (posX + 32) / 32;
        matY1 = (posY + 32) / 32;
        matY2 = (posY + 32) / 32;
        if (posMap.matrizMapa[matY1][matX1] == 0 || posMap.matrizMapa[matY2][matX2] == 0)
            return 0;
        return 1;
    }

    public int verificaWallEsq(int posX, int posY) {
        MatrizMapa posMap = new MatrizMapa();
        int matX1, matX2, matY1, matY2;
        matX1 = posX / 32;
        matY1 = (posY + 32) / 32;
        matX2 = posX / 32;
        matY2 = posY / 32;
        if (posMap.matrizMapa[matY1][matX1] == 0 || posMap.matrizMapa[matY2][matX2] == 0)
            return 0;
        return 1;
    }

    public int verificaWallDir(int posX, int posY) {
        MatrizMapa posMap = new MatrizMapa();
        int matX1, matX2, matY1, matY2;
        matX1 = (posX + 32) / 32;
        matY1 = posY / 32;
        matX2 = (posX + 32) / 32;
        matY2 = (posY + 32) / 32;
        if (posMap.matrizMapa[matY1][matX1] == 0 || posMap.matrizMapa[matY2][matX2] == 0)
            return 0;
        return 1;
    }

    public void enviaDados(int i, int novaPosX, int novaPosY) {
        os[i].println(vet[numCliente] + " " + novaPosX + " " + novaPosY + " " + vet[btCliente] + " "
                + verificaGrav(novaPosX, novaPosY) + " " + direcao + " " + estado);
        os[i].flush();
    }

    public void run() {
        try {
            Scanner is = new Scanner(clientSocket.getInputStream());
            os[cont++] = new PrintStream(clientSocket.getOutputStream());

            String inputLine;
            do { // distribuição para os clientes
                inputLine = is.nextLine();
                vet = inputLine.split(" ");
                int novaPosX = Integer.parseInt(vet[posClienteX]);
                int novaPosY = Integer.parseInt(vet[posClienteY]);

                if (vet[btCliente].compareTo("VK_RIGHT") == 0) {

                    if (vet[dirCliente].compareTo("-1") == 0)
                        novaPosX += anda - 32;
                    else
                        novaPosX += anda;

                    if (verificaWallDir(novaPosX, novaPosY) == 0 && vet[dirCliente].compareTo("-1") == 0)
                        novaPosX -= anda + 32;
                    else if (verificaWallDir(novaPosX, novaPosY) == 0)
                        novaPosX -= anda;
                    direcao = 1;
                }
                if (vet[btCliente].compareTo("VK_LEFT") == 0) {

                    if (vet[dirCliente].compareTo("1") == 0)
                        novaPosX -= anda - 32;
                    else
                        novaPosX -= anda;

                    if (verificaWallEsq(novaPosX, novaPosY) == 0 && vet[dirCliente].compareTo("1") == 0)
                        novaPosX += anda + 32;
                    else if (verificaWallEsq(novaPosX - 32, novaPosY) == 0)
                        novaPosX += anda;
                    direcao = -1;
                }

                if (vet[numCliente].compareTo("0") == 0) {
                    estado = estadoClient1;
                    estadoClient1++;
                    if (estadoClient1 == 5)
                        estadoClient1 = 1;
                }
                if (vet[numCliente].compareTo("1") == 0) {
                    estado = estadoClient2;
                    estadoClient2++;
                    if (estadoClient2 == 5)
                        estadoClient2 = 1;
                }
                if (vet[btCliente].compareTo("A") == 0 || verificaGrav(novaPosX, novaPosY) == 1)
                    novaPosY += anda;
                System.out.println("Cliente " + vet[numCliente] + " posX " + novaPosX + " posY " + vet[posClienteY]
                        + " bt " + vet[btCliente] + " grav " + verificaGrav(novaPosX, novaPosY) + " dir " + direcao
                        + " est1 " + estadoClient1 + " est2 " + estadoClient2);
                for (int i = 0; i < cont; i++) {
                    enviaDados(i, novaPosX, novaPosY);
                }
            } while (!inputLine.equals(""));

            for (int i = 0; i < cont; i++)
                os[i].close(); // fecha todos os printstream
            is.close();
            clientSocket.close(); // fecha o cliente

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("Conexao terminada pelo cliente");
        }
    }
}

class MatrizMapa {
    public int matrizMapa[][] = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
}