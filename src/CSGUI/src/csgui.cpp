#ifndef CSGUI_CPP
#define CSGUI_CPP

#include "csgui.hpp"

Csgui::Csgui(int port, std::string ip){

    this->ip = ip;
    this->port = port;

    // la direccion ip no puede ser nula
    //assert(ip != nullptr);

    // el puerto debe ser mayor a 0
    assert(port > 0);

    // error al iniciar los sockets si no devuelve 0
    assert (csgui_init_socket_t() == 0);

    client = csgui_create_socket_t(AF_INET, SOCK_STREAM, 0);

    // Error al crear el socket
    assert(client >= 0);

    // si devuelve 0 o un numero negativo, error
    assert(csgui_bind_t(&server_addr, AF_INET, port, ip.c_str()) > 0);

    // Desactivar el algoritmo Nagle
    const char flag = 1;
    assert (
        csgui_setsockopt_t(
            client, IPPROTO_TCP,
            TCP_NODELAY, &flag,
            sizeof(int)) == 0
    );

}

int Csgui::connect_to_server() {
    return connect(client, (struct sockaddr*)&server_addr, sizeof(server_addr));
}

int Csgui::connect_to_server(std::string user, std::string password) {
    // realizar la conexion para despues realizar la autenticación
    int ret = this->connect_to_server();

    // Implementar autenticación
    this->send_data("root\n"); // usuario
    this->send_data("1234\n"); // password
    return ret;
}

int Csgui::connect_to_server(const char *user, const char *password) {
    return this->connect_to_server(std::string(user), std::string(password));
}

bool Csgui::is_error_connect(int status_connect) {
    /*
    Permite comprobar si Csgui::connect_to_server tuvo exito
    o devolvio un error
    */
    return status_connect < 0;
}

int Csgui::close() {
    return csgui(close_t)(client);
}

/*Csgui::~Csgui() {
    this->close();
}*/

int Csgui::send_data(const char* data, int size){
    return send(client, data, size, 0);
}
int Csgui::send_data(std::string data){
    return this->send_data(data.c_str(), data.size());
}

int Csgui::drawPixel(uint64_t x, uint64_t y, rgb color){
    return csgui_drawPixel(client, x, y, color);
}

std::string Csgui::recv_data(int size){
    std::string response(size, 0);
    recv(client, &response[0], size, 0);
    return response;
}

#endif
