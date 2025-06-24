// Main.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ArbolRBN arbol = new ArbolRBN();
        String[] mensajes = new String[4000000]; // buffer para mensajes
        int index = 0;

        try {
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese el nombre del archivo de entrada: ");
            String archivo = consola.readLine();

            long inicio = System.nanoTime();
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                String mensaje = procesarLinea(arbol, linea);
                if (mensaje != null) {
                    mensajes[index++] = mensaje;
                }
            }
            br.close();
            long fin = System.nanoTime();

            // Imprimir todos los mensajes acumulados
            for (int i = 0; i < index; i++) {
                System.out.println(mensajes[i]);
            }

            double segundos = (fin - inicio) / 1e9;
            System.out.printf("Tiempo total: %.3f segundos%n", segundos);

        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
        }
    }

    private static String procesarLinea(ArbolRBN arbol, String comando) {
        String[] partes = comando.trim().split("\\s+");
        if (partes.length == 0) return null;

        String operacion = partes[0].toUpperCase();

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



