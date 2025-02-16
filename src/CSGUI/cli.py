import socket
import select
import sys

# Configuración
SERVER_IP = "127.0.0.1"
SERVER_PORT = 450
BUFFER_SIZE = 1024
TIMEOUT = 0.01  # 10 ms

# Crear socket
try:
    sockfd = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sockfd.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sockfd.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
    sockfd.settimeout(TIMEOUT)
    
    sockfd.connect((SERVER_IP, SERVER_PORT))
    sockfd.send(b"root\n")
    sockfd.send(b"1234\n")
    sockfd.send(b"""
drawPixel<2,2,(255,255,0)>
drawPixel<4,4,(0,255,255)>
""")
    print(f"Conectado al servidor {SERVER_IP}:{SERVER_PORT}")
    print("Escribe 'exit' para cerrar la conexión.")
except Exception as e:
    print(f"Error al conectar: {e}")
    sys.exit(1)

# Bucle de comunicación
while True:
    # Esperar datos con select()
    ready, _, _ = select.select([sockfd], [], [], TIMEOUT)

    if ready:
        try:
            buffer = sockfd.recv(BUFFER_SIZE).decode()
            if not buffer:
                print("El servidor cerró la conexión.")
                break
            print(f"Servidor> {buffer.strip()}")
        except socket.timeout:
            print("Timeout: no hay datos disponibles")
        except Exception as e:
            print(f"Error al recibir datos: {e}")
            break

    # Leer entrada del usuario
    try:
        input_text = input("Cliente> ").strip()
        if input_text.lower() == "exit":
            print("Cerrando conexión...")
            break

        # Enviar datos
        sockfd.sendall((input_text + "\r\n").encode())
    except Exception as e:
        print(f"Error al enviar datos: {e}")
        break

# Cerrar socket
sockfd.close()
print("Conexión cerrada.")
