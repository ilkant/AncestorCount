/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ancestorcount;

import static java.lang.Math.pow;

/**
 * Laskee summittaisen laskennallisen arvion esipolvien syntymävuodesta
 * ja tarkan määrän ihmisten lukumäärästä esipolvissa.
 * 
 * @author Ilpo Kantonen 6.1.2016
 * @version 1.1
 */
public class AncestorCount {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String TAB    = "\t";
        final String TABB   = "\t\t";
        final String TABBB  = "\t\t\t";

        final int ANCESTORS = 8;		// Sukupolvien maksimimäärä + 1
        final int AVEMOTHAGE = 30;		// Äidin keskimääräinen ikä synnyttäessä
        int lapsia = 2;                         // Lasten lukumäärä perheessä
        int i;  				// Sukupolvilaskuri
        int omaSyntymaVuosi = 1962;		// Oma syntymävuosi
        int vanhempia=0;                        // Vanhempia
        long edsukuplapsia = 0;                 // Edellisen sukupolven lapsia
        int ihmisia = 1;
        int edelliset = 0;                      // Edellisen sukupolven ihmismäärä

        long[] suku = new long[ANCESTORS+1];      // Ihmisten lukumäärä

        intro(ANCESTORS,lapsia);                                    // Ohjelman esittely
        alustaTaulukko(ANCESTORS,lapsia,suku);
        
        System.out.println("\nEsimerkki: jälkipolven ihmisiä keskimäärin, Tamura = " + tamura(25,3,3,1));
        
        // System.exit(0);
        System.out.println("Vuosiarvio\tEsipolvivanhempia\tYhteensä\tLapsia\t\tSuvun ihmisiä");
        for(i=0; i < ANCESTORS; i++) {
            System.out.print( omaSyntymaVuosi - AVEMOTHAGE * i + TABB);
            
            System.out.print("" + ((int) java.lang.Math.pow(2,(i))) + TABBB +
                    laskeEsiVanhemmat(i) + TABB);
            
            System.out.print( (int) ( java.lang.Math.pow(lapsia,(i+1))) + TABB );    // Lasten määrä sillä sukupolvella
            
            ihmisia += laskeEsiVanhemmat(i);

            System.out.println( ihmisia );
        }
        
        /*
        * Perheessä kolme lasta. Todennäköisyys, että ei ole poikia ja monenko
        * sukupolven päästä keskimäärin isä-linja katkeaa.
        */
        double tn = binTn(0.5 , 3, 0);
        System.out.print("\n\nPerheessä kolme lasta. Todennäköisyys, että perheessä ei ole yhtään poikaa = " + tn + ".");
        System.out.println("Tällä mallilla isä-linja katkeaa keskimäärin " + ( (double) 1 / tn ) + " sukupolven välein.\n");
    }

    /**
     * Laske esipolvitason esi-isien ja -äitien määrä rekursiivisesti lähtöhenkilöstä.
     * Lähtöhenkilöä ei lasketa mukaan esivanhempiin. Level 0 tarkoittaa lähtöhenkilöä.
     * 
     * @param level     Esipolven taso
     * @return int      Esipolvien henkilöiden lukumäärä pl. lähtöhenkilö
     */
    private static int laskeEsiVanhemmat(int level) {
        int apu = 2;

        if(level == 0) return 0;
        if(level == 1) return 2;

        for(int i=2; i<level+1; i++) {
            apu += java.lang.Math.pow(2,(i));
        }

        return apu;
    }
    
    
     /**
     * Laskee laskennallisen suvun elävien ihmisten lukumäärän. Lähtötason
     * sukupolvessa on yksi lähtöhenkilö ja tälle aviopuoliso.
     * 
     * Kaava on Ilpo Kantosen kehittelemä 12.4.2016.
     * 
     * @param n         Sukupolvien lukumäärä
     * @param lapsia    Montako lasta perheessä on laskennallisesti
     * @return          Suvun elävien ihmisten lukumäärä
     */
    private static long laskeElavat(int n, int lapsia) {
        long ihmisia = 0;
        
        for(int i=n-2; i < n ; i++) {
            ihmisia += java.lang.Math.pow(lapsia, i);
        }
        
        ihmisia += java.lang.Math.pow(lapsia, n);
        
        return ihmisia;
    }

    
    /**
     * Laskee lasten lukumäärän kullakin sukupolvella ja vie tuloksen taulukkoon
     * @param a         Maksimi esipolvien lukumäärä
     * @param i         Sukupolvitaso
     * @param lapsia    Montako lasta perheessä on laskennallisesti
     //* @param s         Montako lasta tällä kyseisellä sukupolvella on
     * @return          lasten määrä
     */
    private static long laskeLapsetSukupolvella(int a, int i, int lapsia, long[] s) {
        for(int j=a-1; j >= a - i ; j--) {
            System.out.println("\nKierros ANCESTORS - i = " + (a - i) + " suku[" + j + "]=" + s[j] + " pot= " +
            ((int) java.lang.Math.pow(lapsia,(a - j) )));
            }
        
        return 0;
    }


    /**
     * Tulostaa suvun sisällön
     * 
     * @version 1.0
     */
    private static void tulostaSuku(int a, long[] s) {
        System.out.print("Suku: ");

        for(int k=0;k<a;k++)
            System.out.print(s[k] + " ");
        System.out.println();
    }


    /**
     * Alustaa taulukon, jossa ihmisten määrä kullakin sukupolvella 
     * 
     * @version 1.0
     */
    private static void alustaTaulukko(int koko, int lapset, long[] s) {
        int i;
        long lapsiaTasolla;
        
        for(i=koko; i >= 0; i--) {
                if(i==koko) {
                    s[i] =  1;
//System.out.println("Eka kierros suku[" + i + "] = " + s[i] );
                }
                else {
                    lapsiaTasolla = s[i+1];
                    s[i] = (int) ( java.lang.Math.pow(lapset,(koko - i - 1)));
//System.out.println("suku[" + i + "] = " + s[i] );
                }
        }
    }


    /**
     *  Tulostaa näytölle ohjelman esittelyn.
     * 
     */
    private static void intro(int sp, int lapset) {
        System.out.println("Ohjelma laskee summittaiset syntymävuodet kullekin " + 
                (sp - 1) + ":lle sukupolvelle.");
        System.out.println("Sekä esivanhempien lukumäärän kullakin sukupolvitasolla.");
        System.out.println("Ja suvun ihmisten lukumäärän kun yhdessä perheessä on " +
                lapset + " lasta.");
    }
    
    /**
     * This method calculates amount of descendants.
     * http://www.tamurajones.net/AvgNumOfNthGenDescendants.xhtml
     * 
     * D = C × 2n ÷ A × c
     * 
     * @param C     number of individuals in Current generation
     * @param n     number of generations in between
     * @param A     number of individuals in Ancestral generation
     * @param c     correction factor
     * 
     * @return int  average number of Descendants 
     */
    private static int tamura(int C, int n, int A, int c) {
        int D = 0;

        D = (C * 2 * n) / ( A * c);
        
        return D;
    }
    
    /*
    * Binääritodennäköisyys
    *
    */
    private static double binTn(double p, int n, int k) {
        double q;
        double alla, apu;
        
        q = 1 - p;
        
        alla = kertoma(n) / ( kertoma(k) * kertoma( n - k ) );
        
        apu = alla * pow((double) p, (double) k) * pow(q,( (double) n - (double) k));
        
        return apu;
    }
    
    private static int kertoma(int n) {
      int i,            // paikalliset muuttujat 
      tulo = 1;         // alustetaan tulo arvoon 1

      // laskee tulon n x (n-1) x (n-2) x ... x 2 x 1 
      for  (i = n;  i > 1;  i = i - 1) {
        tulo = tulo * i;
      }

      return (tulo);
    }
}
