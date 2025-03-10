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

// para argumentos de entrada
#ifndef ARG_IN
#define ARG_IN
#endif

// para argumentos de salida
#ifndef ARG_OUT
#define ARG_OUT
#endif

typedef struct {
    uint8_t r;
    uint8_t g;
    uint8_t b;
} rgb;

static inline int csgui(init_socket_t)();
static inline socket_t csgui(create_socket_t)(
    int ARG_IN af, 
    int ARG_IN type, 
    int ARG_IN protocol
);
inline int csgui(setsockopt_t)(
    socket_t ARG_IN self, 
    int ARG_IN level, 
    int ARG_IN optname, 
    const char * ARG_IN optval, 
    int ARG_IN optlen
);
inline int csgui(close_t)(socket_t ARG_IN self);
inline int csgui(bind_t)(
    sockaddr_in_t * ARG_IN addr, 
    short ARG_IN family, 
    unsigned short ARG_IN port, 
    const char * ARG_IN ip_address
);

int csgui(drawPixel)(
    socket_t ARG_IN client, 
    int ARG_IN x, 
    int ARG_IN y,
    rgb ARG_IN color
);

int csgui(autoUpdateScreen)(
    socket_t ARG_IN client, 
    bool ARG_IN status
);

int csgui(createFocus)(
    socket_t ARG_IN client, 
    const char* ARG_IN name_focus, 
    size_t ARG_IN id_focus
);

int csgui(createNewUser)(
    socket_t ARG_IN client, 
    const char* ARG_IN group, 
    const char* ARG_IN user,
    const char* ARG_IN password
);

int csgui(deleteFocus)(
    socket_t ARG_IN client, 
    const char* ARG_IN name_focus
);

int csgui(getAttribFocus)(
    socket_t ARG_IN client, 
    const char* ARG_IN name_focus,
    char* ARG_OUT output_AttribFocus_data, 
    size_t ARG_IN size_output_AttribFocus_data
);

int csgui(getAttribFocusNow)(socket_t ARG_IN client, 
    const char* ARG_IN name_focus,
    char* ARG_OUT output_AttribFocus_data, 
    size_t ARG_IN size_output_AttribFocus_data
);

int csgui(getKeyboard)(
    socket_t ARG_IN client, 
    char* ARG_OUT output_keyboard_data, 
    size_t ARG_IN size_output_keyboard_data
);

#include "command.h"

// indica que se activa el modo enlace, las funciones
// debe enlazarse
//#define LINKER_MODO_ON // no descomentar esto !!!
#include "../src/csgui.c"
#endif