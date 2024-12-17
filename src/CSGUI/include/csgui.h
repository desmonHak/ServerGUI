#ifndef CSGUI_H
#define CSGUI_H

#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdio.h>
#include <unistd.h>

#ifdef __linux__
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#elif defined(_WIN32) || defined(_WIN64)
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

#ifdef __linux__
#endif

#endif