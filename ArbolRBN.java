// ArbolRBN.java
// ArbolRBN.java
public class ArbolRBN {
    private final NodoRBN NIL = new NodoRBN();
    public NodoRBN raiz;

    public ArbolRBN() {
        raiz = NIL;
    }

    public void insertar(int clave, String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            nombre = "Usuario " + clave;
        }

        NodoRBN nuevo = new NodoRBN(clave, nombre);
        nuevo.izq = NIL;
        nuevo.der = NIL;

        NodoRBN padre = null;
        NodoRBN actual = raiz;

        while (actual != NIL) {
            padre = actual;
            if (nuevo.clave < actual.clave) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }

        nuevo.padre = padre;

        if (padre == null) {
            raiz = nuevo;
        } else if (nuevo.clave < padre.clave) {
            padre.izq = nuevo;
        } else {
            padre.der = nuevo;
        }

        insertarFixup(nuevo);
    }

    public String obtenerNombre(int clave) {
        NodoRBN nodo = buscarNodo(clave);
        if (nodo != null && nodo != NIL) return nodo.nombre;
        return "(sin nombre)";
    }

    public NodoRBN buscarNodo(int clave) {
        NodoRBN actual = raiz;
        while (actual != NIL) {
            if (clave == actual.clave) return actual;
            if (clave < actual.clave) actual = actual.izq;
            else actual = actual.der;
        }
        return null;
    }

    public NodoRBN buscarSucesor(int valor) {
        NodoRBN actual = raiz;
        NodoRBN sucesor = null;
        while (actual != NIL) {
            if (actual.clave >= valor) {
                sucesor = actual;
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return sucesor;
    }

    private void insertarFixup(NodoRBN nodo) {
        while (nodo.padre != null && nodo.padre.color == NodoRBN.ROJO) {
            if (nodo.padre == nodo.padre.padre.izq) {
                NodoRBN tio = nodo.padre.padre.der;
                if (tio.color == NodoRBN.ROJO) {
                    nodo.padre.color = NodoRBN.NEGRO;
                    tio.color = NodoRBN.NEGRO;
                    nodo.padre.padre.color = NodoRBN.ROJO;
                    nodo = nodo.padre.padre;
                } else {
                    if (nodo == nodo.padre.der) {
                        nodo = nodo.padre;
                        rotarIzquierda(nodo);
                    }
                    nodo.padre.color = NodoRBN.NEGRO;
                    nodo.padre.padre.color = NodoRBN.ROJO;
                    rotarDerecha(nodo.padre.padre);
                }
            } else {
                NodoRBN tio = nodo.padre.padre.izq;
                if (tio.color == NodoRBN.ROJO) {
                    nodo.padre.color = NodoRBN.NEGRO;
                    tio.color = NodoRBN.NEGRO;
                    nodo.padre.padre.color = NodoRBN.ROJO;
                    nodo = nodo.padre.padre;
                } else {
                    if (nodo == nodo.padre.izq) {
                        nodo = nodo.padre;
                        rotarDerecha(nodo);
                    }
                    nodo.padre.color = NodoRBN.NEGRO;
                    nodo.padre.padre.color = NodoRBN.ROJO;
                    rotarIzquierda(nodo.padre.padre);
                }
            }
        }
        raiz.color = NodoRBN.NEGRO;
    }

    private void rotarIzquierda(NodoRBN x) {
        NodoRBN y = x.der;
        x.der = y.izq;
        if (y.izq != NIL) y.izq.padre = x;
        y.padre = x.padre;
        if (x.padre == null) {
            raiz = y;
        } else if (x == x.padre.izq) {
            x.padre.izq = y;
        } else {
            x.padre.der = y;
        }
        y.izq = x;
        x.padre = y;
    }

    private void rotarDerecha(NodoRBN x) {
        NodoRBN y = x.izq;
        x.izq = y.der;
        if (y.der != NIL) y.der.padre = x;
        y.padre = x.padre;
        if (x.padre == null) {
            raiz = y;
        } else if (x == x.padre.der) {
            x.padre.der = y;
        } else {
            x.padre.izq = y;
        }
        y.der = x;
        x.padre = y;
    }
    // La elimina ción la agregamos luego si lo deseas.

    public void eliminar(int clave) {
        NodoRBN nodo = buscarNodo(clave);
        if (nodo == null || nodo == NIL) return;

        NodoRBN y = nodo;
        NodoRBN x;
        boolean yColorOriginal = y.color;

        if (nodo.izq == NIL) {
            x = nodo.der;
            trasplantar(nodo, nodo.der);
        } else if (nodo.der == NIL) {
            x = nodo.izq;
            trasplantar(nodo, nodo.izq);
        } else {
            y = minimo(nodo.der);
            yColorOriginal = y.color;
            x = y.der;
            if (y.padre == nodo) {
                x.padre = y;
            } else {
                trasplantar(y, y.der);
                y.der = nodo.der;
                y.der.padre = y;
            }
            trasplantar(nodo, y);
            y.izq = nodo.izq;
            y.izq.padre = y;
            y.color = nodo.color;
        }

        if (yColorOriginal == NodoRBN.NEGRO) {
            eliminarFixup(x);
        }
    }

    private void eliminarFixup(NodoRBN x) {
        while (x != raiz && x.color == NodoRBN.NEGRO) {
            if (x == x.padre.izq) {
                NodoRBN w = x.padre.der;
                if (w.color == NodoRBN.ROJO) {
                    w.color = NodoRBN.NEGRO;
                    x.padre.color = NodoRBN.ROJO;
                    rotarIzquierda(x.padre);
                    w = x.padre.der;
                }
                if (w.izq.color == NodoRBN.NEGRO && w.der.color == NodoRBN.NEGRO) {
                    w.color = NodoRBN.ROJO;
                    x = x.padre;
                } else {
                    if (w.der.color == NodoRBN.NEGRO) {
                        w.izq.color = NodoRBN.NEGRO;
                        w.color = NodoRBN.ROJO;
                        rotarDerecha(w);
                        w = x.padre.der;
                    }
                    w.color = x.padre.color;
                    x.padre.color = NodoRBN.NEGRO;
                    w.der.color = NodoRBN.NEGRO;
                    rotarIzquierda(x.padre);
                    x = raiz;
                }
            } else {
                NodoRBN w = x.padre.izq;
                if (w.color == NodoRBN.ROJO) {
                    w.color = NodoRBN.NEGRO;
                    x.padre.color = NodoRBN.ROJO;
                    rotarDerecha(x.padre);
                    w = x.padre.izq;
                }
                if (w.der.color == NodoRBN.NEGRO && w.izq.color == NodoRBN.NEGRO) {
                    w.color = NodoRBN.ROJO;
                    x = x.padre;
                } else {
                    if (w.izq.color == NodoRBN.NEGRO) {
                        w.der.color = NodoRBN.NEGRO;
                        w.color = NodoRBN.ROJO;
                        rotarIzquierda(w);
                        w = x.padre.izq;
                    }
                    w.color = x.padre.color;
                    x.padre.color = NodoRBN.NEGRO;
                    w.izq.color = NodoRBN.NEGRO;
                    rotarDerecha(x.padre);
                    x = raiz;
                }
            }
        }
        x.color = NodoRBN.NEGRO;
    }

    private void trasplantar(NodoRBN u, NodoRBN v) {
        if (u.padre == null) {
            raiz = v;
        } else if (u == u.padre.izq) {
            u.padre.izq = v;
        } else {
            u.padre.der = v;
        }
        v.padre = u.padre;
    }

    private NodoRBN minimo(NodoRBN nodo) {
        while (nodo.izq != NIL) {
            nodo = nodo.izq;
        }
        return nodo;
    }


}

