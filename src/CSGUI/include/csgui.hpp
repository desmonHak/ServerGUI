#ifndef CSGUI_HPP
#define CSGUI_HPP

#include <iostream>
#include <cassert>

#ifndef CSGUI_H
#include "csgui.h"
#endif

class Csgui {
    public:
        socket_t             client;
        sockaddr_in_t   server_addr;
        std::string              ip;
        uint16_t               port;

        Csgui(int port, std::string ip);
        //~Csgui();
        int connect_to_server();
        bool is_error_connect(int status_connect);
        int close();
        int send_data(const char* data, int size);
        std::string recv_data(int size);
        int send_data(std::string data);

        int drawPixel(uint64_t x, uint64_t y, rgb color);
};

#endif