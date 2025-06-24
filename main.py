# main.py
from arbol_rbn import ArbolRBN

def procesar_comandos(nombre_archivo, arbol):
    try:
        with open(nombre_archivo, 'r') as archivo:
            for linea in archivo:
                ejecutar_comando(linea.strip(), arbol)
    except FileNotFoundError:
        print(f"No se encontró el archivo '{nombre_archivo}'")

def ejecutar_comando(comando_str, arbol):
    partes = comando_str.split()
    if not partes:
        return

    comando = partes[0].upper()
    if comando == "INSERT" and len(partes) == 2:
        clave = int(partes[1])
        arbol.insertar(clave)
        print(f"Usuario {clave} agregado")
    elif comando == "DELETE" and len(partes) == 2:
        clave = int(partes[1])
        eliminado = arbol.eliminar(clave)
        if eliminado:
            print(f"Usuario {clave} eliminado")
        else:
            print(f"Usuario {clave} no encontrado")
    elif comando == "QUERY" and len(partes) == 2:
        clave = int(partes[1])
        sucesor = arbol.buscar_sucesor(clave)
        if sucesor is not None:
            print(f"Sucesor de {clave}: {sucesor}")
        else:
            print(f"No hay sucesor para {clave}")
    else:
        print(f"Línea inválida: {comando_str}")

def modo_interactivo(arbol):
    print("\nModo interactivo activado. Escribe comandos (INSERT, DELETE, QUERY) o 'SALIR' para terminar:")
    while True:
        entrada = input(" > ").strip()
        if entrada.upper() == "SALIR":
            print("Finalizando programa...")
            break
        ejecutar_comando(entrada, arbol)

if __name__ == "__main__":
    arbol = ArbolRBN()
    nombre_archivo = input("Ingrese el nombre del archivo de entrada (ej. usuarios.txt): ")
    procesar_comandos(nombre_archivo, arbol)

    respuesta = input("¿Desea ingresar más operaciones manualmente? (S/N): ").strip().upper()
    if respuesta == "S":
        modo_interactivo(arbol)
    else:
        print("Programa finalizado.")
