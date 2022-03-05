package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class RSA {

    static BigInteger p;
    static BigInteger q;
    static BigInteger n;

    static BigInteger phi;

    static BigInteger e;
    static BigInteger d;

    static void RSA(int msg){


        int BIT_LENGTH = 200;

// Generate random primes
        Random rand = new SecureRandom();
        p = BigInteger.probablePrime(BIT_LENGTH / 2, rand);
        q = BigInteger.probablePrime(BIT_LENGTH / 2, rand);

// Calculate products
        n = p.multiply(q);

        phi = p.subtract(BigInteger.ONE)
                .multiply(q.subtract(BigInteger.ONE));
        System.out.println("La valeur de phi = " + phi);
// Generate public and private exponents

        do e = new BigInteger(phi.bitLength(), rand);
        while (e.compareTo(BigInteger.ONE) <= 0
                || e.compareTo(phi) >= 0
                || !e.gcd(phi).equals(BigInteger.ONE));

        System.out.println("La valeur de e = " + e);
        d = e.modInverse(phi);
        System.out.println("La valeur de d = " + d);

    }

    public static BigInteger getE() {
        return e;
    }

    public static BigInteger getD() {
        return d;
    }

    public static  BigInteger enc(int msg, BigInteger e, BigInteger n){

        return BigInteger.valueOf(msg).modPow(e, n);
    }

    public static  BigInteger edec(BigInteger enc,BigInteger d,BigInteger n){

        return  enc.modPow(d, n);
    }




}
