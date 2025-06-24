import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArbolRBN arbol = new ArbolRBN();
        Scanner entrada = new Scanner(System.in);

        while (true) {
            System.out.println("\n¿Qué desea hacer?");
            System.out.println("1. Insertar usuarios desde archivo");
            System.out.println("2. Eliminar usuarios desde archivo");
            System.out.println("3. Consultar sucesores desde archivo");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción (1-4): ");

            String opcion = entrada.nextLine().trim();

            switch (opcion) {
                case "1":
                    procesarArchivo(arbol, entrada, "INSERT");
                    break;
                case "2":
                    procesarArchivo(arbol, entrada, "DELETE");
                    break;
                case "3":
                    procesarArchivo(arbol, entrada, "QUERY");
                    break;
                case "4":
                    System.out.println("Saliendo del programa.");
                    return;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    private static void procesarArchivo(ArbolRBN arbol, Scanner entrada, String tipoOperacion) {
        System.out.print("Ingrese el nombre del archivo de entrada: ");
        String nombreArchivo = entrada.nextLine().trim();

        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + nombreArchivo);
            return;
        }

        List<String> resultados = new ArrayList<>();
        long inicio = System.nanoTime();

        try (Scanner lector = new Scanner(archivo)) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (linea.isEmpty()) continue;

                String resultado = procesarLinea(arbol, linea, tipoOperacion);
                if (resultado != null) {
                    resultados.add(resultado);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();
        double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;

        // Mostrar tiempo antes de imprimir resultados
        System.out.printf("Tiempo de ejecución para %s: %.6f segundos\n", tipoOperacion, tiempoSegundos);

        for (String r : resultados) {
            System.out.println(r);
        }
    }


    private static String procesarLinea(ArbolRBN arbol, String comando, String tipoEsperado) {
        String[] partes = comando.trim().split("\\s+");
        if (partes.length == 0) return null;

        String operacion = partes[0].toUpperCase();

        if (!operacion.equals(tipoEsperado)) {
            return "Operación ignorada: se esperaba '" + tipoEsperado + "', pero se encontró '" + operacion + "'";
        }

        switch (operacion) {
            case "INSERT":
                if (partes.length >= 2) {
                    try {
                        int clave = Integer.parseInt(partes[1]);
                        String nombre = (partes.length > 2)
                            ? comando.substring(comando.indexOf(partes[2]))
                            : null;
                        arbol.insertar(clave, nombre);
                        return String.format("Usuario %d agregado como '%s'", clave, arbol.obtenerNombre(clave));
                    } catch (NumberFormatException e) {
                        return "Error: clave inválida en línea: " + comando;
                    }
                }
                return "Error: formato inválido para INSERT: " + comando;

            case "DELETE":
                if (partes.length == 2) {
                    try {
                        int clave = Integer.parseInt(partes[1]);
                        String nombreAntiguo = arbol.obtenerNombre(clave);
                        if (nombreAntiguo == null) {
                            return "No existe usuario con ID " + clave;
                        }
                        arbol.eliminar(clave);
                        return String.format("Usuario %d ('%s') eliminado", clave, nombreAntiguo);
                    } catch (NumberFormatException e) {
                        return "Error: clave inválida en línea: " + comando;
                    }
                }
                return "Error: formato inválido para DELETE: " + comando;

            case "QUERY":
                if (partes.length == 2) {
                    try {
                        int clave = Integer.parseInt(partes[1]);
                        NodoRBN sucesor = arbol.buscarSucesor(clave);
                        if (sucesor != null) {
                            return String.format("Sucesor de %d: %d (%s)", clave, sucesor.clave, sucesor.nombre);
                        } else {
                            return "No hay sucesor para " + clave;
                        }
                    } catch (NumberFormatException e) {
                        return "Error: clave inválida en línea: " + comando;
                    }
                }
                return "Error: formato inválido para QUERY: " + comando;

            default:
                return "Línea inválida: " + comando;
        }
    }
}
