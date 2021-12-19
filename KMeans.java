import java.util.Arrays;
import java.util.Scanner;

public class KMeans
{

    public static int M = 0;  //numero di oggetti considerati
    public static int N = 0;  //numero di caratteristiche di ogni oggetto

    public static final int k = 4;  //numero di cluster
    public static final double alfa = 0.1; //soglia di terminazione
    public static final int iter = 1000;  //numero massimo di iterazioni

    public static void main(String[] args) 
    {
        double[][] Dati;  //matrice di dati di grandezza M x N
        int[] Cluster;    //per ogni M, memorizza il cluster di appartenenza
        double[][] Centri; //centro dei K cluster
        
        double obiettivo = 0; //valore della funzione obiettivo
        double precisione = 10; //variazione dell'obiettivo ad ogni iterazione


        //inserimento dei valori di M ed N da parte dell'utente
        Scanner cin = new Scanner(System.in);

        M = cin.nextInt();
        cin.nextLine();

        N = cin.nextInt();
        cin.close();

        //inizializzazione e stampa dei dati generati
        Dati = new double[M][N];
        Cluster = new int[M];
        Centri = new double[k][N];

        InizializzaDati(Dati);
        System.out.println("--- Matrice Dati ---");
        Utility.StampaMatrice(Dati);
        System.out.println();

        //cluster con oggetti scelti in modo casuale
        InizializzaCluster(Centri, Dati);

        //ciclo di calcolo
        int i = 0;
        for(i=0; (i<iter) && (precisione > alfa); i++)
        {
            CalcolaCluster(Cluster, Dati, Centri);
            AggiornaCentri(Cluster, Dati, Centri);

            precisione = Math.abs(obiettivo - CalcolaObiettivo(Cluster, Dati, Centri));
            obiettivo = CalcolaObiettivo(Cluster, Dati, Centri);
        }


        //stampa dei risultati all'utente
        System.out.println("--- Cluster ---");
        System.out.println(Arrays.toString(Cluster));
        System.out.println();

        System.out.println("--- Numero di iterazioni ---");
        System.out.println(i);
        System.out.println();

        System.out.println("--- Precisione raggiunta ---");
        System.out.println(precisione);
        System.out.println();      

        System.out.println("--- Motivo della terminazione ---");
        if(i == iter)
            System.out.println("Numero massimo di iterazioni");
        else
            System.out.println("Raggiunta precisione");
        System.out.println();        
    }




    //inizializza la matrice dati con numeri casuali tra 0 e 1
    public static void InizializzaDati(double[][] Dati)
    {
        for(int i=0; i<M; i++)
            for(int j=0; j<N; j++)
                Dati[i][j] = Math.random();
    }

    //inizializza la matrice centri con valori di k oggetti, scegliendo a caso
    //senza reimmissione k righe della matrice dati (supponendo k<M)
    public static void InizializzaCluster(double[][] Centri, double[][] Dati)
    {
        //verifica che gli oggetti scelti non siano ripetuti
        boolean[] Lookup = new boolean[M];
        
        //scelta a caso di k oggetti
        for(int i=0; i<k; i++)
        {
            //genera un numero di oggetto da 0 a M-1
            int oggettoRandom = (int)(Math.random() * M);

            if(Lookup[oggettoRandom] == false)
            {
                for(int j=0; j<N; j++)
                    Centri[i][j] = Dati[oggettoRandom][j];

                Lookup[oggettoRandom] = true;
            }
            else
            {
                i--;
            }            
        }
    }

    //per ogni oggetto calcola il cluster di appartenenza e memorizza il risultato
    //in altre parole, assegna ogni oggetto al cluster più vicino
    public static void CalcolaCluster(int[] Cluster, double[][] Dati, double[][] Centri)
    {
        //sfruttando la distanza euclidea, calcola di ogni oggetto il cluster con distanza minore
        for(int i=0; i<M; i++)
        {
            double distanzaMinore = 1000;
            int indexMinore = -1;

            for(int j=0; j<k; j++)
            {
                double distanza = Utility.DistanzaEuclidea(Dati[i], Centri[j]);

                if(distanza < distanzaMinore)
                {
                    distanzaMinore = distanza;
                    indexMinore = j;
                }
            }

            Cluster[i] = indexMinore;
        }
    }

    //calcola i valori del centro di ogni cluster
    public static void AggiornaCentri(int[] Cluster, double[][] Dati, double[][] Centri)
    {
        //il valore di ogni caratteristica di ogni centro di un dato cluster si calcola
        //come la media delle caratteristiche degli elementi del cluster
        for(int i=0; i<k; i++)
        {
            //somma delle caratteristice degli elementi del cluster
            double[] SommaCaratteristiche = new double[N];

            //se l'oggetto fa parte del cluster, viene considerato
            int numElementi = 0;
            for(int j=0; j<M; j++)
            {
                if(Cluster[j] == i)
                {
                    numElementi++;

                    for(int k=0; k<N; k++)
                        SommaCaratteristiche[k] = Dati[j][k];
                }
            }

            //calcolo della media di ogni caratteristica
            for(int j=0; j<N; j++)
                SommaCaratteristiche[j] = SommaCaratteristiche[j] / numElementi;

            //aggiornamento del centro del cluster
            for(int j=0; j<N; j++)
                Centri[i][j] = SommaCaratteristiche[j];
        }
    }
    
    //calcola il valore della funzione obiettivo
    public static double CalcolaObiettivo(int[] Cluster, double[][] Dati, double[][] Centri)
    {
        double distanza = 0;

        //calcolata la distanza tra il centro di un cluster e l'elemento m del cluster stesso,
        //l'obiettivo è calcolato come la somma delle distanze di ogni elemento dal centro
        //del cluster a cui appartiene
        for(int i=0; i<M; i++)
        {
            //distanza tra l'elemento e il centro di appartenenza
            double distanzaParziale = Utility.DistanzaEuclidea(Dati[i], Centri[Cluster[i]]);

            //la distanza è la somma delle distanze
            distanza = distanza + distanzaParziale;
        }

        return distanza;

    }
    


}