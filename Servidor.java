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
    int direcao;

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
        matY2 =  (posY + 32)/ 32;
        if (posMap.matrizMapa[matY1][matX1] == 0 || posMap.matrizMapa[matY2][matX2] == 0)
            return 0;
        return 1;
    }

    public String verificaEstado(String estado, String client) {
        
        if(client.compareTo("1") == 0){
            if(estado.compareTo("Player1Parado") == 0)
                estado = "Player1Mov1";
            else if(estado.compareTo("Player1Mov1") == 0)
                estado = "Player1Mov2";
            else if(estado.compareTo("Player1Mov2") == 0)
                estado = "Player1Mov3";
            else if(estado.compareTo("Player1Mov3") == 0)
                estado = "Player1Mov4";
            else if(estado.compareTo("Player1Mov4") == 0)
                estado = "Player1Mov5";
            else if(estado.compareTo("Player1Mov5") == 0)
                estado = "Player1Parado";
        } else{
            if(estado.compareTo("Player2Parado") == 0)
                estado = "Player2Mov1";
            else if(estado.compareTo("Player2Mov1") == 0)
                estado = "Player2Mov2";
            else if(estado.compareTo("Player2Mov2") == 0)
                estado = "Player2Mov3";
            else if(estado.compareTo("Player2Mov3") == 0)
                estado = "Player2Mov4";
            else if(estado.compareTo("Player2Mov4") == 0)
                estado = "Player2Mov5";
            else if(estado.compareTo("Player2Mov5") == 0)
                estado = "Player2Parado";
        }
        return estado;
    }
    
    public void enviaDados(int i, int novaPosX, int novaPosY) {
        os[i].println(vet[numCliente] + " " + novaPosX + " " + novaPosY + " " + vet[btCliente] + " "
                + verificaGrav(novaPosX, novaPosY) + " " + direcao + " " + verificaEstado(vet[estadoCliente], vet[numCliente]));
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
                if (vet[btCliente].compareTo("A") == 0 || verificaGrav(novaPosX, novaPosY) == 1)
                    novaPosY += anda;
                if (vet[btCliente].compareTo("VK_RIGHT") == 0){
                    novaPosX += anda;
                    direcao = 1;
                }
                if (vet[btCliente].compareTo("VK_LEFT") == 0){
                    novaPosX -= anda;
                    direcao = -1;
                }
                System.out.println("Cliente " + vet[numCliente] + " posX " + novaPosX + " posY " + vet[posClienteY]
                        + " bt " + vet[btCliente] + " grav " + verificaGrav(novaPosX, novaPosY) + " dir " + direcao + " est " + verificaEstado(vet[estadoCliente], vet[numCliente]));
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