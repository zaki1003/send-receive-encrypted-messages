package com.company;



import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Date;

public class Main {

    static String cle ="";

    static String cipher ="";
    static String plain ="";


    static  byte [] k = new byte [256];



    static boolean isCle = false;
    static boolean isCipher = false;

    public static void main(String[] args) {


        RSA.RSA();


        final String serverHost = "localhost";

        Socket socketOfClient = null;
        BufferedWriter os = null;
        BufferedReader is = null;

        try {


            // Send a request to connect to the server is listening
            // on machine 'localhost' port 7777.
            socketOfClient = new Socket(serverHost, 7777);

            // Create output stream at the client (to send data to the server)
            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));


            // Input stream at Client (Receive data from the server).
            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + serverHost);
            return;
        }

        try {

            while (!isCipher || !isCle){
                // Write data to the output stream of the Client Socket.
                //   os.write("HELO! now is " + new Date());

                // End of line
                os.newLine();

            // Flush data.
            os.flush();
            os.write("I am a Client");
            os.newLine();

            os.write("E");
            os.newLine();
            os.write(RSA.e.toString());
            os.newLine();
            os.write("N");
            os.newLine();
            os.write(RSA.n.toString());
            os.newLine();
            os.flush();
       /*    os.write("QUIT");
            os.newLine();
            os.flush();*/


            // Read data sent from the server.
            // By reading the input stream of the Client Socket.
            String responseLine;

            while ((responseLine = is.readLine()) != null) {

                System.out.println("Server: " + responseLine);

                if (isCle) {

                    cle = RSA.dec(new BigInteger(responseLine), RSA.d, RSA.n).toString();
                    System.out.println("La cle ===================   " + cle);


                }
                if (isCipher) {

                    cipher = responseLine;

                    byte[] k = new byte[256];
                    for (int i = 0; i < cle.getBytes().length; i++) {
                        k[i] = cle.getBytes()[i];
                    }

                    RC4.initialisation(k);

                    int[] octetC = new int[cipher.length()];

                    char[] cipherArr = new char[cipher.length()];
                    RC4.chiffrer(cipherArr, octetC);
                    for (int i = 0; i < cipher.length(); i++) {
                        cipherArr[i] = cipher.charAt(i);
                    }


                    for (int i = 0; i < RC4.dechiffrer(cipherArr, octetC).length; i++) {
                        plain = plain + RC4.dechiffrer(cipherArr, octetC)[i];
                    }


                    System.out.println("Texte Déchiffré ========= " + plain);

                }


                if (responseLine.indexOf("cle") != -1) {
                    isCle = true;
                } else isCle = false;
                if (responseLine.indexOf("cipher") != -1) {
                    isCipher = true;
                } else isCipher = false;
                if (responseLine.indexOf("OK") != -1) {
                    break;
                }
            }


        }
            os.close();
            is.close();
            socketOfClient.close();

        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }


    }

}