import java.util.Arrays;

public class Utility 
{
    //considerati due oggetti m1 e m2, ne calcola la distanza euclidea
    public static double DistanzaEuclidea(double[] m1, double[] m2)
    {
        double distanza = 0;

        //calcola la distanza per ogni caratteristica
        for(int i=0; i<KMeans.N; i++)
        {
            distanza += Math.pow( (m1[i] - m2[1]), 2);
        }

        return Math.sqrt(distanza);
    }

    //stampa di una matrice double
    public static void StampaMatrice(double[][] Mat)
    {
        for(int i=0; i<Mat.length; i++)
            System.out.println(Arrays.toString(Mat[i]));
    }
}
