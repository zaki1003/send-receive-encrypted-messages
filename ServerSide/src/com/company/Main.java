package com.company;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    static int [] s = new int[256];

    final static int l = 256;
    static  byte [] k = new byte [l];
    static int[] octetC ;
    static char[] m;
    static String cle;
    static String cleE;
    static String N;
    static char [] cipher;
   static boolean isCleE = false;
    static boolean isN = false;
    private static void log(String message) {
        System.out.println(message);
    }


    public static void main(String args[]) throws IOException {





        System.out.println("Entrez la clé");
        Scanner sc = new Scanner(System.in);
         cle= sc.next();
        byte [] k = new byte[256];
        for (int i = 0; i < cle.getBytes().length; i++) {
            k[i]= cle.getBytes()[i];
        }

        RC4.initialisation(k);


        System.out.println("Entrez une chaine de caractere");

        String ms= sc.next();

     m = new char[ms.length()];
        for (int i = 0; i < ms.length(); i++) {
            m[i]=ms.charAt(i);

        }


        octetC = new int[m.length];
        cipher = RC4.chiffrer (m,octetC);
        System.out.print("Texte chiffré: ");
        System.out.println(cipher);




        ServerSocket listener = null;

        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;

        // Try to open a server socket on port 7777
        // Note that we can't choose a port less than 1023 if we are not
        // privileged users (root)

        try {
            listener = new ServerSocket(7777);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {
                // Accept client connection request
                // Get new Socket at Server.

                Socket socketOfServer = listener.accept();
                new ServiceThread(socketOfServer, clientNumber++).start();
            }
        } finally {
            listener.close();
        }

    }


    private static class ServiceThread extends Thread {

        private int clientNumber;
        private Socket socketOfServer;

        public ServiceThread(Socket socketOfServer, int clientNumber) {
            this.clientNumber = clientNumber;
            this.socketOfServer = socketOfServer;

            // Log
            log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
        }

        @Override
        public void run() {

            try {

                // Open input and output streams
                BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

                while (true) {
                    // Read data to the server (sent from client).
                    String line = is.readLine();

                    // Write to socket of Server
                    // (Send to client)

                    os.write("Hi i am the server ");
                    os.newLine();

                    // If users send QUIT (To end conversation).
          /*          if (line.equals("QUIT")) {
                        os.write(">> OK");
                        os.newLine();
                        os.flush();
                        break;
                    }*/

                   String responseLine;
                    while ((responseLine = is.readLine()) != null) {
                        System.out.println("Client: " + responseLine);
                        if (isCleE) {
                            cleE=responseLine;


                        }
                        if (isN) {
                            N=responseLine;
                            os.write("cle");
                            os.newLine();
                             os.write(  RSA.enc(Integer.parseInt(cle), new BigInteger(cleE),new BigInteger(N)).toString());
                            os.newLine();
                            os.flush();

                            os.write("cipher");
                            os.newLine();
                            os.write(cipher);
                            os.newLine();
                            // Flush data.
                            os.flush();
                        }



                        if (responseLine.indexOf("E") != -1) {
                            isCleE = true;
                        }else isCleE=false;
                        if (responseLine.indexOf("N") != -1) {
                            isN = true;
                        }else isN=false;
                        if (responseLine.indexOf("OK") != -1) {
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}