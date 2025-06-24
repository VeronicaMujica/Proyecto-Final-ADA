# arbol_rbn.py

ROJO = "rojo"
NEGRO = "negro"

class NodoRBN:
    def __init__(self, clave):
        self.clave = clave
        self.color = ROJO
        self.padre = None
        self.izq = None
        self.der = None

class ArbolRBN:
    def __init__(self):
        self.NIL = NodoRBN(None)
        self.NIL.color = NEGRO
        self.raiz = self.NIL

    def insertar(self, clave):
        nuevo = NodoRBN(clave)
        nuevo.izq = self.NIL
        nuevo.der = self.NIL

        padre = None
        actual = self.raiz

        while actual != self.NIL:
            padre = actual
            if nuevo.clave < actual.clave:
                actual = actual.izq
            else:
                actual = actual.der

        nuevo.padre = padre

        if padre is None:
            self.raiz = nuevo
        elif nuevo.clave < padre.clave:
            padre.izq = nuevo
        else:
            padre.der = nuevo

        nuevo.color = ROJO
        self.insertar_fixup(nuevo)

    def insertar_fixup(self, nodo):
        while nodo.padre and nodo.padre.color == ROJO:
            if nodo.padre == nodo.padre.padre.izq:
                tio = nodo.padre.padre.der
                if tio and tio.color == ROJO:
                    nodo.padre.color = NEGRO
                    tio.color = NEGRO
                    nodo.padre.padre.color = ROJO
                    nodo = nodo.padre.padre
                else:
                    if nodo == nodo.padre.der:
                        nodo = nodo.padre
                        self.rotar_izquierda(nodo)
                    nodo.padre.color = NEGRO
                    nodo.padre.padre.color = ROJO
                    self.rotar_derecha(nodo.padre.padre)
            else:
                tio = nodo.padre.padre.izq
                if tio and tio.color == ROJO:
                    nodo.padre.color = NEGRO
                    tio.color = NEGRO
                    nodo.padre.padre.color = ROJO
                    nodo = nodo.padre.padre
                else:
                    if nodo == nodo.padre.izq:
                        nodo = nodo.padre
                        self.rotar_derecha(nodo)
                    nodo.padre.color = NEGRO
                    nodo.padre.padre.color = ROJO
                    self.rotar_izquierda(nodo.padre.padre)

        self.raiz.color = NEGRO

    def rotar_izquierda(self, x):
        y = x.der
        x.der = y.izq
        if y.izq != self.NIL:
            y.izq.padre = x
        y.padre = x.padre
        if x.padre is None:
            self.raiz = y
        elif x == x.padre.izq:
            x.padre.izq = y
        else:
            x.padre.der = y
        y.izq = x
        x.padre = y

    def rotar_derecha(self, x):
        y = x.izq
        x.izq = y.der
        if y.der != self.NIL:
            y.der.padre = x
        y.padre = x.padre
        if x.padre is None:
            self.raiz = y
        elif x == x.padre.der:
            x.padre.der = y
        else:
            x.padre.izq = y
        y.der = x
        x.padre = y

    def inorder(self, nodo=None):
        if nodo is None:
            nodo = self.raiz
        if nodo != self.NIL:
            self.inorder(nodo.izq)
            print(f"{nodo.clave} ({nodo.color})", end=' ')
            self.inorder(nodo.der)

    def buscar_sucesor(self, valor):
        actual = self.raiz
        sucesor = None
        while actual != self.NIL:
            if actual.clave >= valor:
                sucesor = actual
                actual = actual.izq
            else:
                actual = actual.der
        return sucesor.clave if sucesor else None

    def buscar(self, clave):
        actual = self.raiz
        while actual != self.NIL:
            if clave == actual.clave:
                return actual
            elif clave < actual.clave:
                actual = actual.izq
            else:
                actual = actual.der
        return None

    def minimo(self, nodo):
        while nodo.izq != self.NIL:
            nodo = nodo.izq
        return nodo

    def transplantar(self, u, v):
        if u.padre is None:
            self.raiz = v
        elif u == u.padre.izq:
            u.padre.izq = v
        else:
            u.padre.der = v
        v.padre = u.padre

    def eliminar(self, clave):
        z = self.buscar(clave)
        if z is None:
            return False

        y = z
        y_original_color = y.color
        if z.izq == self.NIL:
            x = z.der
            self.transplantar(z, z.der)
        elif z.der == self.NIL:
            x = z.izq
            self.transplantar(z, z.izq)
        else:
            y = self.minimo(z.der)
            y_original_color = y.color
            x = y.der
            if y.padre == z:
                x.padre = y
            else:
                self.transplantar(y, y.der)
                y.der = z.der
                y.der.padre = y
            self.transplantar(z, y)
            y.izq = z.izq
            y.izq.padre = y
            y.color = z.color

        if y_original_color == NEGRO:
            self.eliminar_fixup(x)
        return True

    def eliminar_fixup(self, x):
        while x != self.raiz and x.color == NEGRO:
            if x == x.padre.izq:
                w = x.padre.der
                if w.color == ROJO:
                    w.color = NEGRO
                    x.padre.color = ROJO
                    self.rotar_izquierda(x.padre)
                    w = x.padre.der
                if w.izq.color == NEGRO and w.der.color == NEGRO:
                    w.color = ROJO
                    x = x.padre
                else:
                    if w.der.color == NEGRO:
                        w.izq.color = NEGRO
                        w.color = ROJO
                        self.rotar_derecha(w)
                        w = x.padre.der
                    w.color = x.padre.color
                    x.padre.color = NEGRO
                    w.der.color = NEGRO
                    self.rotar_izquierda(x.padre)
                    x = self.raiz
            else:
                w = x.padre.izq
                if w.color == ROJO:
                    w.color = NEGRO
                    x.padre.color = ROJO
                    self.rotar_derecha(x.padre)
                    w = x.padre.izq
                if w.der.color == NEGRO and w.izq.color == NEGRO:
                    w.color = ROJO
                    x = x.padre
                else:
                    if w.izq.color == NEGRO:
                        w.der.color = NEGRO
                        w.color = ROJO
                        self.rotar_izquierda(w)
                        w = x.padre.izq
                    w.color = x.padre.color
                    x.padre.color = NEGRO
                    w.izq.color = NEGRO
                    self.rotar_derecha(x.padre)
                    x = self.raiz
        x.color = NEGRO
