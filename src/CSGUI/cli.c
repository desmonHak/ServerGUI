#include "csgui.h"

int main(){
    char buffer[BUFFER_SIZE];
    char input[BUFFER_SIZE];
    if (csgui(init_socket_t)()!= 0) {
        printf("Error al inicializar los sockets\n");
        return EXIT_FAILURE;
    }

    socket_t sockfd = csgui(create_socket_t)(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        printf("Error al crear el socket");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }

    sockaddr_in_t server_addr;
    if (csgui(bind_t)(&server_addr, AF_INET, SERVER_PORT, SERVER_IP) <= 0) {
        perror("Direccion IP no válida o no soportada");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }


    // Desactivar el algoritmo Nagle
    int flag = 1;
    if (csgui(setsockopt_t)(sockfd, IPPROTO_TCP, TCP_NODELAY, (void*)&flag, sizeof(int)) < 0) {
        printf("Error al desactivar TCP_NODELAY");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        printf("Error al conectar con el servidor");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }
    printf("Conectado al servidor %s:%d\n", SERVER_IP, SERVER_PORT);
    printf("Escribe 'exit' para cerrar la conexión.\n");

    // Bucle interactivo
    while (1) {
        // Limpiar buffers
        memset(buffer, 0, BUFFER_SIZE);
        memset(input, 0, BUFFER_SIZE);

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
    csgui(close_t)(sockfd);
    printf("Conexión cerrada\n");

    return 0;

}