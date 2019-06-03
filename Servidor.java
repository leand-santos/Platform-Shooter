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
    final int cliente1 = 0, cliente2 = 1, numCliente = 0, posClienteX = 1, posClienteY = 2, btCliente = 3,
            gravCliente = 4;
    Socket clientSocket;
    static PrintStream os[] = new PrintStream[3];
    static int cont = 0;

    Servindo(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            os[cont] = new PrintStream(clientSocket.getOutputStream());
            os[cont].println(cont);
            os[cont].flush();
        } catch (IOException erro) {
        }
    }

    public String verificaGrav(int posX, int posY) {
        MatrizMapa posMap = new MatrizMapa();
        int matX1, matX2, matY1, matY2, coordY1, coordY2, coordX1, coordX2;
        /*
         * matX = (posX - (posX % 32)) / 32; matY = (posY - (posY % 32))/ 32;
         */
        /*
         * matX = posX / 32; matY = posY / 32; coordX = posX + 32; coordY = posY + 32;
         */
        coordX1 = posX;
        coordY1 = posY + 32;
        coordX2 = posX + 32;
        coordY2 = coordY1;
        matX1 = coordX1 / 32;
        matX2 = coordX2 / 32;
        matY1 = coordY1 / 32;
        matY2 = coordY2 / 32;
        //System.out.println("matX1 " + matX1 + " matY1 " + matY1 + " matX2 " + matX2 + " matY2 " + matY2 + " "
        //        + posMap.matrizMapa[matX1][matY2] + " " + posMap.matrizMapa[matX2][matY2]);
        if (posMap.matrizMapa[matY1][matX1] == 0 || posMap.matrizMapa[matY2][matX2] == 0) {
            return " 0 ";
        }
        return " 1 ";
    }

    public void run() {
        try {
            Scanner is = new Scanner(clientSocket.getInputStream());
            os[cont++] = new PrintStream(clientSocket.getOutputStream());

            String inputLine, outputLine;
            String vet[] = new String[10];
            do { // distribuição para os clientes
                inputLine = is.nextLine();
                vet = inputLine.split(" ");
                int novaPosX = Integer.parseInt(vet[posClienteX]);
                int novaPosY = Integer.parseInt(vet[posClienteY]);
                for (int i = 0; i < cont; i++) {
                    if (vet[btCliente].compareTo("A") == 0) {
                        novaPosY++;
                        System.out.println(
                                "Cliente " + vet[numCliente] + " posX " + vet[posClienteX] + " posY " + novaPosY
                                        + " bt " + vet[btCliente] + " grav " + verificaGrav(novaPosX, novaPosY));
                        os[i].println(vet[numCliente] + " " + novaPosX + " " + novaPosY + " " + vet[btCliente]
                                + verificaGrav(novaPosX, novaPosY));
                        os[i].flush();
                    }
                    if (vet[btCliente].compareTo("VK_RIGHT") == 0) { // retornar qual player é
                        novaPosX++;
                        System.out.println(
                                "Cliente " + vet[numCliente] + " posX " + novaPosX + " posY " + vet[posClienteY]
                                        + " bt " + vet[btCliente] + " grav " + verificaGrav(novaPosX, novaPosY));
                        /*
                         * if (novaPosX < 224 - 45) { os[i].println(vet[numCliente] + " " + novaPosX +
                         * " " + vet[posClienteY] + " " + vet[btCliente] + verificaGrav(novaPosX,
                         * novaPosY)); os[i].flush(); } else { novaPosX--;
                         */
                        os[i].println(vet[numCliente] + " " + novaPosX + " " + novaPosY + " " + vet[btCliente]
                                + verificaGrav(novaPosX, novaPosY));
                        os[i].flush();
                        // }
                    }
                    if (vet[btCliente].compareTo("VK_LEFT") == 0) { // retornar qual player é
                        novaPosX--;
                        System.out.println(
                                "Cliente " + vet[numCliente] + " posX " + novaPosX + " posY " + vet[posClienteY]
                                        + " bt " + vet[btCliente] + " grav " + verificaGrav(novaPosX, novaPosY));
                        /*
                         * if (novaPosX < 224 - 45) { os[i].println(vet[numCliente] + " " + novaPosX +
                         * " " + vet[posClienteY] + " " + vet[btCliente] + verificaGrav(novaPosX,
                         * novaPosY)); os[i].flush(); } else {
                         */
                        os[i].println(vet[numCliente] + " " + novaPosX + " " + novaPosY + " " + vet[btCliente]
                                + verificaGrav(novaPosX, novaPosY));
                        os[i].flush();
                        // }
                    }
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