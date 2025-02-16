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

    int reuse = 1;
    // enable the socket to reuse the address
    if (csgui(setsockopt_t)(sockfd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse)) < 0) perror("failed allowing server socket to reuse address");

    // Desactivar el algoritmo Nagle
    int flag = 1;
    if (csgui(setsockopt_t)(sockfd, IPPROTO_TCP, TCP_NODELAY, (void*)&flag, sizeof(int)) < 0) {
        printf("Error al desactivar TCP_NODELAY");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }

    /**
     * https://stackoverflow.com/questions/28244082/proper-use-of-getsockopt-and-setsockopt-for-so-rcvtimeo-and-so-sndtimeo
     * 
     * No está inicializando ntsnd y ntrcv con el tamaño del búfer disponible 
     * para getsockopt. Por lo tanto, si el valor aleatorio es mayor o igual al 
     * tamaño necesario, la llamada tendrá éxito. De lo contrario, fallará con 
     * EINVAL. Desde la página del manual:
     * 
     * Los argumentos optval y optlen se utilizan para acceder a los valores de 
     * las opciones para setsockopt(). Para getsockopt(), identifican un búfer 
     * en el que se devolverá el valor de las opciones solicitadas. 
     * Para getsockopt(), optlen es un argumento de resultado de valor, que 
     * inicialmente contiene el tamaño del búfer al que apunta optval y se 
     * modifica al regresar para indicar el tamaño real del valor devuelto. 
     * Si no se debe proporcionar ni devolver ningún valor de opción, optval 
     * puede ser NULL.
     * 
     * Para solucionar esto, inicialícelos a ambos con sizeof(struct timeval).
     */
    struct timeval tout, tsnd, trcv;
    tout.tv_sec=0;
    tout.tv_usec=10000; // 10 ms
    socklen_t ntrcv = sizeof(ntrcv), ntsnd = sizeof(ntsnd);
    fd_set read_fds;

    if (csgui(setsockopt_t)(sockfd, SOL_SOCKET, SO_RCVTIMEO, &tout, sizeof(tout)) < 0) {
        perror("Error al establecer timeout de recepción");
    }
    if (csgui(setsockopt_t)(sockfd, SOL_SOCKET, SO_SNDTIMEO, &tout, sizeof(tout)) < 0) {
        perror("Error al establecer timeout de envío");
    }

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        printf("Error al conectar con el servidor");
        csgui(close_t)(sockfd);
        return EXIT_FAILURE;
    }
    printf("Conectado al servidor %s:%d\n", SERVER_IP, SERVER_PORT);
    printf("Escribe 'exit' para cerrar la conexión.\n");


    struct timeval timeout_select = tout;
    // Bucle interactivo
    while (1) {
        // Limpiar buffers
        memset(buffer, 0, BUFFER_SIZE);
        memset(input, 0, BUFFER_SIZE);

        FD_ZERO(&read_fds);
        FD_SET(sockfd, &read_fds);
    
        // restablecer timeout_select
        timeout_select = tout;
        int ready = select(sockfd + 1, &read_fds, NULL, NULL, &timeout_select);        
        if (ready < 0) {
            perror("Error en select");
            break;
        } else if (ready == 0) {
            printf("Timeout: no hay datos disponibles\n");
            send(sockfd, "\n", 2, 0);
            continue; // Puedes decidir si sigues esperando o cierras la conexión
        }

        // Recibir respuesta del servidor
        ssize_t bytes_recibidos = recv(sockfd, buffer, BUFFER_SIZE - 1, 0);
        if (bytes_recibidos < 0) {
            if (errno == EWOULDBLOCK || errno == EAGAIN) {
                printf("Timeout en recv(), intentando de nuevo...\n");
                continue;
            } else {
                perror("Error al recibir la respuesta del servidor");
                break;
            }
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

    }

    // Cerrar el socket
    csgui(close_t)(sockfd);
    printf("Conexión cerrada\n");

    return 0;

}