// NodoRBN.java
public class NodoRBN {
    public static final boolean ROJO = true;
    public static final boolean NEGRO = false;

    public int clave;
    public String nombre;  // nombre del usuario
    public boolean color;
    public NodoRBN padre;
    public NodoRBN izq;
    public NodoRBN der;

    public NodoRBN(int clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
        this.color = ROJO;
    }

    public NodoRBN() {
        // Nodo NIL
        this.color = NEGRO;
        this.nombre = null;
    }
}

