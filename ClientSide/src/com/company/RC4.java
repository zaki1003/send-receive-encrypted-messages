package com.company;

import java.util.Scanner;

public class RC4 {


    static int [] s = new int[256];

    final static int l = 256;

    static void  initialisation (byte [] k) {
        for (int i = 0; i < 256; i++) {
            s[i] = i;
        }

        int j = 0;
        int p;
        for (int i = 0; i < 256; i++) {
            j = (j + s[i] + k[i % l]) % 256;


            p = s[j];
            s[j] = s[i];
            s[i] = p;

        }


    }



    public static  char[] chiffrer (char [] mp,int[] octetC ){

        int i=0;
        int j=0;
        int p;
        int k=0;

        char[] resultat = new char[octetC.length];
        while (k<mp.length){
            i=(i+1)%256;

            j= (j+s[i])%256;

            p = s[j];
            s[j] = s[i];
            s[i] = p;

            octetC[k] = s[(s[i]+s[j])%256];

            resultat[k] = (char) (octetC[k] ^ (int) mp[k]);

            k++;
        }


        return  resultat;

    }


    public static char[] dechiffrer (char [] cp,int[] octetC ){



        char[] resultat = new char[octetC.length];
        for (int i = 0; i < octetC.length; i++) {



            resultat[i] = (char) (octetC[i] ^ (int) cp[i]);


        }

        return  resultat;

    }



}
