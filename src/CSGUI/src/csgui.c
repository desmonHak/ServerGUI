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

static inline socket_t csgui(create_socket_t)(
        int ARG_IN af, 
        int ARG_IN type, 
        int ARG_IN protocol
    ) {
    /*
     * retorna un socket
     */

    // inicializamos un socket con error:
    socket_t self = (socket_t)(~0);
    self = socket(af, type, protocol);
    return self;
}

inline int csgui(setsockopt_t)(
        socket_t ARG_IN self, 
        int ARG_IN level, 
        int ARG_IN optname, 
        const char * ARG_IN optval, 
        int ARG_IN optlen
    ){
    /*
     * Si retorna algo distinto a 0 ocurrio error
     */
    return setsockopt(self, level, optname, optval, optlen);
}

inline int csgui(close_t)(socket_t ARG_IN self) {
    #ifdef __linux__
    return close(self);
    #elif defined(_WIN32) || defined(_WIN64)
    int val = closesocket(self);
    WSACleanup();
    return val;
    #endif
}

inline int csgui(bind_t)(
        sockaddr_in_t * ARG_IN addr, 
        short ARG_IN family, 
        unsigned short ARG_IN port, 
        const char * ARG_IN ip_address
    ) {
    addr->sin_family = family;
    addr->sin_port   = htons(port);

    return inet_pton(AF_INET, SERVER_IP, &(addr->sin_addr));
}

// si se define LINKER_MODO_ON en un header o por parametro,
// se debe generar el codigo objeto de este archivo
#ifndef LINKER_MODO_ON
#define LINKER_MODO_ON

int csgui(drawPixel)(
        socket_t ARG_IN client, 
        int ARG_IN x, 
        int ARG_IN y,
        rgb ARG_IN color
    ) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando drawPixel<x, y, (r,g,b)>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            drawPixel, %d, %d, (%hhu, %hhu, %hhu)) "\r\n", // Agregar delimitador de línea al mensaje
            x, y, color.r, color.g, color.b);

    return send(client, command, strlen(command), 0);
}

int csgui(autoUpdateScreen)(
        socket_t ARG_IN client, 
        bool ARG_IN status
    ) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando autoUpdateScreen<x, y, (r,g,b)>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            autoUpdateScreen, %s) "\r\n", // Agregar delimitador de línea al mensaje
            (status) ? "true" : "false");

    return send(client, command, strlen(command), 0);
}

int csgui(createFocus)(
        socket_t ARG_IN client, 
        const char* ARG_IN name_focus, 
        size_t ARG_IN id_focus
    ) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando createFocus<"root.Foco1", 1>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            createFocus, "%s", %d) "\r\n", // Agregar delimitador de línea al mensaje
            name_focus, id_focus);

    return send(client, command, strlen(command), 0);
}

int csgui(createNewUser)(
    socket_t ARG_IN client, 
    const char* ARG_IN group, 
    const char* ARG_IN user,
    const char* ARG_IN password
) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando createNewUser<"root", "user", "password">
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            createNewUser, "%s", "%s", "%s") "\r\n", // Agregar delimitador de línea al mensaje
            group, user, password);

    return send(client, command, strlen(command), 0);
}

int csgui(deleteFocus)(
        socket_t ARG_IN client, 
        const char* ARG_IN name_focus
    ) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando deleteFocus<"root.Foco1">
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            deleteFocus, "%s") "\r\n", // Agregar delimitador de línea al mensaje
            name_focus);

    return send(client, command, strlen(command), 0);
}

int csgui(getAttribFocus)(
        socket_t ARG_IN client, 
        const char* ARG_IN name_focus,
        char* ARG_OUT output_AttribFocus_data, 
        size_t ARG_IN size_output_AttribFocus_data
    ) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando 
    // getAttribFocus<"root.a.b">
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            getAttribFocus, "%s") "\r\n", // Agregar delimitador de línea al mensaje
            name_focus);

    int status = send(client, command, strlen(command), 0);
    // unir estados
    return recv(
        client, 
        output_AttribFocus_data, 
        size_output_AttribFocus_data, 0
    ) & status;
}
int csgui(getAttribFocusNow)(socket_t ARG_IN client, 
    const char* ARG_IN name_focus,
    char* ARG_OUT output_AttribFocus_data, 
    size_t ARG_IN size_output_AttribFocus_data) {
    return csgui(getAttribFocus)(
        client, " ", 
        output_AttribFocus_data, 
        size_output_AttribFocus_data
    );
}

int csgui(getKeyboard)(
    socket_t ARG_IN client, 
    char* ARG_OUT output_keyboard_data, 
    size_t ARG_IN size_output_keyboard_data
) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando 
    // getKeyboard<>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            getKeyboard) "\r\n" // Agregar delimitador de línea al mensaje
    );

    int status = send(client, command, strlen(command), 0);
    
    // unir estados
    return recv(
        client, 
        output_keyboard_data, 
        size_output_keyboard_data, 0
    ) & status;
}

int csgui(getMyPermissions)(
    socket_t ARG_IN client, 
    char* ARG_OUT output_getMyPermissions_data, 
    size_t ARG_IN size_output_getMyPermissions_data
) {
    char command[LOCAL_STING_LENGTH];

    // Crear el comando 
    // getMyPermissions<>
    snprintf(command, LOCAL_STING_LENGTH,
        build_command(
            getMyPermissions) "\r\n" // Agregar delimitador de línea al mensaje
    );

    int status = send(client, command, strlen(command), 0);
    
    // unir estados
    return recv(
        client, 
        output_getMyPermissions_data, 
        size_output_getMyPermissions_data, 0
    ) & status;
}


#endif // LINKER_MODO_ON

#endif