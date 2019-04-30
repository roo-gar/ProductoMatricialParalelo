import java.io.*;
import java.util.Scanner;

public final class MultiplicacionMatricesIO {

    public static int[][] leerMatriz(String ruta) {
        File archivo = new File(ruta);
        try {
            Scanner scanner = new Scanner(archivo);
            int filas = scanner.nextInt();
            int columnas = scanner.nextInt();

            int[][] M = new int[filas][columnas];
            for (int fila = 0; fila < filas; fila++) {
                for (int columna = 0; columna < columnas; columna++)
                    M[fila][columna] = scanner.nextInt();
            }
            return M;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void escribirMatriz(int[][] M, String ruta) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ruta);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.printf("%d\t%d\n", M.length, M[0].length);
            for (int fila = 0; fila < M.length; fila++) {
                for (int columna = 0; columna < M[0].length; columna++)
                    printWriter.printf("%d\t", M[fila][columna]);
                printWriter.println();
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean compararArchivos(String rutaArhivo1, String rutaArchivo2) {
        File archivo1 = new File(rutaArhivo1);
        File archivo2 = new File(rutaArchivo2);
        try {
            Scanner scanner1 = new Scanner(archivo1);
            Scanner scanner2 = new Scanner(archivo2);

            while (scanner1.hasNextLine()) {
                if (!scanner1.nextLine().equals(scanner2.nextLine()))
                    return false;
            }

            return !scanner2.hasNextLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
