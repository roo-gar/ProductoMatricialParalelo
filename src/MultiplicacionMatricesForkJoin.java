import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MultiplicacionMatricesForkJoin extends RecursiveAction{

    private static final int NUM_THREADS = 4;
    private static int THRESHOLD;

    private final int[][] A, B, C;
    private final int filaInicio, filaFinal;

    private MultiplicacionMatricesForkJoin(int[][] A, int[][] B, int[][] C, int filaInicio, int filaFinal) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.filaInicio = filaInicio;
        this.filaFinal = filaFinal;
    }

    @Override
    protected void compute() {
        if (this.filaFinal - this.filaInicio <= THRESHOLD) {
            for (int fila = this.filaInicio; fila < this.filaFinal; fila++) {
                for (int columna = 0; columna < this.C[0].length; columna++) {
                    this.C[fila][columna] = 0;

                    for (int i = 0; i < this.A[0].length; i++)
                        this.C[fila][columna] += this.A[fila][i] * this.B[i][columna];
                }
            }

        } else {
            int filaMitad = (this.filaInicio + this.filaFinal) >> 1;
            MultiplicacionMatricesForkJoin mmfj1 = new MultiplicacionMatricesForkJoin(A, B, C, this.filaInicio, filaMitad);
            MultiplicacionMatricesForkJoin mmfj2 = new MultiplicacionMatricesForkJoin(A, B, C, filaMitad, this.filaFinal);
            invokeAll(mmfj1, mmfj2);
        }
    }

    private static int[][] multiplicar(int[][] A, int[][] B) {
        assert A[0].length == B.length :
                "El número de columnas de la matriz A tiene que ser igual al número de filas de la matriz B";
        assert A.length % NUM_THREADS == 0:
                "La multiplicación no se puede paralelizar en partes iguales con " + NUM_THREADS + "threads";

        THRESHOLD = A.length / NUM_THREADS;

        int C[][] = new int[A.length][B[0].length];

        ForkJoinPool pool = new ForkJoinPool();
        MultiplicacionMatricesForkJoin mmfj = new MultiplicacionMatricesForkJoin(A, B, C, 0, A.length);
        pool.invoke(mmfj);

        return C;
    }

    public static void main(String[] args) {
        int[][] A = MultiplicacionMatricesIO.leerMatriz("matrizA.txt");
        int[][] B = MultiplicacionMatricesIO.leerMatriz("matrizB.txt");

        long start = System.nanoTime();
        int[][] C = multiplicar(A, B);
        long end = System.nanoTime();

        MultiplicacionMatricesIO.escribirMatriz(C, "resultadoForkJoin.txt");

        System.out.printf("T%d = %.4fs%n", NUM_THREADS,  (end - start) / 1E9);
        System.out.printf("resultadoSecuencial.txt == resultadoForkJoin.txt => %b%n",
                MultiplicacionMatricesIO.compararArchivos("resultadoSecuencial.txt", "resultadoForkJoin.txt"));
    }

}
