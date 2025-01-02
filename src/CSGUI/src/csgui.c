#ifndef CSGUI_C
#define CSGUI_C

#include "csgui.h"

static inline int csgui(init_socket_t)() {
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

static inline socket_t csgui(create_socket_t)(int af, int type, int protocol) {
    /*
     * retorna un socket
     */

    // inicializamos un socket con error:
    socket_t self = (socket_t)(~0);
    self = socket(af, type, protocol);
    return self;
}

inline int csgui(setsockopt_t)(socket_t self, int level, int optname, const char *optval, int optlen){
    /*
     * Si retorna algo distinto a 0 ocurrio error
     */
    return setsockopt(self, level, optname, optval, optlen);
}

inline int csgui(close_t)(socket_t self) {
    #ifdef __linux__
    return close(self);
    #elif defined(_WIN32) || defined(_WIN64)
    int val = closesocket(self);
    WSACleanup();
    return val;
    #endif
}

inline int csgui(bind_t)(sockaddr_in_t *addr, short family, unsigned short port, const char *ip_address) {
    addr->sin_family = family;
    addr->sin_port   = htons(port);

    return inet_pton(AF_INET, SERVER_IP, &(addr->sin_addr));
}

// si se define LINKER_MODO_ON en un header o por parametro,
// se debe generar el codigo objeto de este archivo
#ifdef LINKER_MODO_ON
#define LINKER_MODO_ON

int csgui(drawPixel)(socket_t client, int x, int y, rgb color) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando drawPixel<x, y, (r,g,b)>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            drawPixel, %d, %d, (%hhu, %hhu, %hhu)) "\r\n", // Agregar delimitador de línea al mensaje
            x, y, color.r, color.g, color.b);

    return send(client, command, strlen(command), 0);
}

#endif // LINKER_MODO_ON

#endif