public class MultiplicacionMatricesThreads implements Runnable {

    private static final int NUM_THREADS = 4;

    private final int[][] A, B, C;
    private final int filaInicio, filaFinal;

    private MultiplicacionMatricesThreads(int[][] A, int[][] B, int[][] C, int filaInicio, int filaFinal) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.filaInicio = filaInicio;
        this.filaFinal = filaFinal;
    }

    @Override
    public void run() {
        for (int fila = this.filaInicio; fila < this.filaFinal; fila++) {
            for (int columna = 0; columna < this.C[0].length; columna++) {
                this.C[fila][columna] = 0;

                for (int i = 0; i < A[0].length; i++)
                    this.C[fila][columna] += this.A[fila][i] * this.B[i][columna];
            }
        }
    }

    private static int[][] multiplicar(int[][] A, int[][] B) {
        assert A[0].length == B.length :
                "El número de columnas de la matriz A tiene que ser igual al número de filas de la matriz B";
        assert A.length % NUM_THREADS == 0:
                "La multiplicación no se puede paralelizar en partes iguales con " + NUM_THREADS + "threads";

        int C[][] = new int[A.length][B[0].length];

        Thread[] threads = new Thread[NUM_THREADS];

        int filasPorThread =  A[0].length / NUM_THREADS;
        for (int i = 0, j = 0; i < NUM_THREADS; i++, j += filasPorThread)
            threads[i] = new Thread(new MultiplicacionMatricesThreads(A, B, C, j, j + filasPorThread));

        for (Thread thread: threads)
            thread.start();

        try {
            for (Thread thread: threads)
                thread.join();
        } catch (InterruptedException e) { /*No sucede*/ }

        return C;
    }

    public static void main(String[] args) {
        int[][] A = MultiplicacionMatricesIO.leerMatriz("matrizA.txt");
        int[][] B = MultiplicacionMatricesIO.leerMatriz("matrizB.txt");

        long start = System.nanoTime();
        int[][] C = multiplicar(A, B);
        long end = System.nanoTime();

        MultiplicacionMatricesIO.escribirMatriz(C, "resultadoThreads.txt");

        System.out.printf("T%d = %.4fs%n", NUM_THREADS,  (end - start) / 1E9);
        System.out.printf("resultadoSecuencial.txt == resultadoThreads.txt => %b%n",
                MultiplicacionMatricesIO.compararArchivos("resultadoSecuencial.txt", "resultadoThreads.txt"));
    }

}
