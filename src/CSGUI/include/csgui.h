#ifndef CSGUI_H
#define CSGUI_H

#ifdef __cplusplus   // si se compila con C++
#include "csgui.hpp" // añadir version c++
#else

#endif

#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>

#ifdef __linux__
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#elif defined(_WIN32) || defined(_WIN64)
#ifdef _WIN32_WINNT
#undef _WIN32_WINNT
#endif
// https://learn.microsoft.com/es-es/cpp/porting/modifying-winver-and-win32-winnt?view=msvc-170
// se requiere como minimo que el OS sea un sistema win Vista o superior
#define _WIN32_WINNT _WIN32_WINNT_VISTA
#include <winsock2.h>
#include <ws2tcpip.h>
#pragma comment(lib, "Ws2_32.lib")
#endif

#ifdef __linux__
typedef int socket_t;
#elif defined(_WIN32) || defined(_WIN64)
typedef SOCKET socket_t;
static WSADATA wsaData;
#endif

typedef struct sockaddr_in sockaddr_in_t;


#define SERVER_IP "127.0.0.1"  // Dirección IP del servidor (localhost)
#define SERVER_PORT 450        // Puerto del servidor
#define BUFFER_SIZE 1024       // Tamaño del buffer
#define LOCAL_STING_LENGTH 64  // Tamaño de la cadena de texto local para cada funcion

#ifdef __linux__
#endif

#define csgui(name) csgui_ ## name

typedef struct {
    uint8_t r;
    uint8_t g;
    uint8_t b;
} rgb;

static inline int csgui(init_socket_t)();
static inline socket_t csgui(create_socket_t)(int af, int type, int protocol);
inline int csgui(setsockopt_t)(socket_t self, int level, int optname, const char *optval, int optlen);
inline int csgui(close_t)(socket_t self);
inline int csgui(bind_t)(sockaddr_in_t *addr, short family, unsigned short port, const char *ip_address);

int csgui(drawPixel)(socket_t client, int x, int y, rgb color);

#include "command.h"

// indica que se activa el modo enlace, las funciones
// debe enlazarse
//#define LINKER_MODO_ON // no descomentar esto !!!
#include "../src/csgui.c"
#endif