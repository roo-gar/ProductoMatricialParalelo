
public class MultiplicacionMatricesSecuencial {

    private static int[][] multiplicar(int[][] A, int[][] B) {
        assert A[0].length == B.length :
                "El número de columnas de la matriz A tiene que ser igual al número de filas de la matriz B";

        int C[][] = new int[A.length][B[0].length];

        for (int fila = 0; fila < C.length; fila++) {
            for (int columna = 0; columna < C[0].length; columna++) {
                C[fila][columna] = 0;

                for (int i = 0; i < A[0].length; i++)
                    C[fila][columna] += A[fila][i] * B[i][columna];
            }
        }

        return C;
    }

    public static void main(String[] args) {
        int[][] A = MultiplicacionMatricesIO.leerMatriz("matrizA.txt");
        int[][] B = MultiplicacionMatricesIO.leerMatriz("matrizB.txt");

        long start = System.nanoTime();
        int[][] C = multiplicar(A, B);
        long end = System.nanoTime();

        MultiplicacionMatricesIO.escribirMatriz(C, "resultadoSecuencial.txt");

        System.out.printf("T1 = %.4fs%n", (end - start) / 1E9);
    }

}
