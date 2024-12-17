#include "csgui.h"

static inline int init_socket_t() {
    /*
     * Si retorna algo distinto a 0 ocurrio error en la inicializacion de winsock
     */
    #ifdef __linux__
    return 0;
    #elif defined(_WIN32) || defined(_WIN64)
    static WSADATA wsaData;
    return WSAStartup(MAKEWORD(2, 2), &wsaData);
    #endif
}

socket_t create_socket_t(int af, int type, int protocol) {
    /*
     * retorna un socket
     */

    // inicializamos un socket con error:
    socket_t self = (socket_t)(~0);
    self = socket(af, type, protocol);
    return self;
}

inline int setsockopt_t(socket_t self, int level, int optname, const char *optval, int optlen){
    /*
     * Si retorna algo distinto a 0 ocurrio error
     */
    return setsockopt(self, level, optname, optval, optlen);
}

inline int close_t(socket_t self) {
    #ifdef __linux__
    return close(self);
    #elif defined(_WIN32) || defined(_WIN64)
    int val = closesocket(self);
    WSACleanup();
    return val;
    #endif
}

inline int bind_t(sockaddr_in_t *addr, short family, unsigned short port, const char *ip_address) {
    addr->sin_family = family;
    addr->sin_port   = htons(port);

    return inet_pton(AF_INET, SERVER_IP, &(addr->sin_addr));
}

int main(){
    char buffer[BUFFER_SIZE];
    char input[BUFFER_SIZE];
    if (init_socket_t()!= 0) {
        printf("Error al inicializar los sockets\n");
        return EXIT_FAILURE;
    }

    socket_t sockfd = create_socket_t(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        printf("Error al crear el socket");
        close_t(sockfd);
        return EXIT_FAILURE;
    }

    sockaddr_in_t server_addr;
    if (bind_t(&server_addr, AF_INET, SERVER_PORT, SERVER_IP) <= 0) {
        perror("Direccion IP no válida o no soportada");
        close_t(sockfd);
        return EXIT_FAILURE;
    }


    // Desactivar el algoritmo Nagle
    int flag = 1;
    if (setsockopt_t(sockfd, IPPROTO_TCP, TCP_NODELAY, (void*)&flag, sizeof(int)) < 0) {
        printf("Error al desactivar TCP_NODELAY");
        close_t(sockfd);
        return EXIT_FAILURE;
    }

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        printf("Error al conectar con el servidor");
        close_t(sockfd);
        return EXIT_FAILURE;
    }
    printf("Conectado al servidor %s:%d\n", SERVER_IP, SERVER_PORT);
    printf("Escribe 'exit' para cerrar la conexión.\n");

    // Bucle interactivo
    while (1) {
        // Limpiar buffers
        memset(buffer, 0, BUFFER_SIZE);
        memset(input, 0, BUFFER_SIZE);

        // Leer entrada del usuario
        printf("Cliente> ");
        if (fgets(input, BUFFER_SIZE, stdin) == NULL) {
            printf("Error al leer la entrada");
            break;
        }

        // Remover salto de línea al final
        input[strcspn(input, "\n")] = '\0';

        // Salir si el usuario escribe "exit"
        if (strcmp(input, "exit") == 0) {
            printf("Cerrando conexión...\n");
            break;
        }

        // Agregar delimitador de línea al mensaje
        strcat(input, "\r\n");

        // Enviar el mensaje
        if (send(sockfd, input, strlen(input), 0) < 0) {
            printf("Error al enviar el mensaje");
            break;
        }

        // Recibir respuesta del servidor
        ssize_t bytes_recibidos = recv(sockfd, buffer, BUFFER_SIZE - 1, 0);
        if (bytes_recibidos < 0) {
            printf("Error al recibir la respuesta del servidor");
            break;
        } else if (bytes_recibidos == 0) {
            printf("El servidor cerró la conexión.\n");
            break;
        }

        // Mostrar la respuesta del servidor
        buffer[bytes_recibidos] = '\0'; // Asegurar que el buffer termina en '\0'
        printf("Servidor> %s\n", buffer);
    }

    // Cerrar el socket
    close_t(sockfd);
    printf("Conexión cerrada\n");

    return 0;

}