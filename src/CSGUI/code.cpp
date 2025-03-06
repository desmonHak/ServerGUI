#include <iostream>

#include "csgui.hpp"

int main(){
    
    Csgui client(450, "127.0.0.1");
    if (client.is_error_connect(client.connect_to_server("root", "1234"))) {
        std::cout << "Error connecting to server" << std::endl;
        return 1;
    }


    std::cout << "Connected to server : " << client.ip << ":" << client.port << std::endl;

    for (unsigned int i = 0; i < 1024; i++) { 
        // enviar el comando para dibujar un pixel drawPixel<0, 0, (255, 0, 255)>
        // client.send_data(build_command(drawPixel, 0, 0, (255, 0, 255)) "\r\n");

        if (client.drawPixel(i, i, (rgb){255, 0, 255}) < 0) {
            std::cout <<  "Error al enviar el comando" << std::endl;
            break;
        }
        // se espera recibir el comando enviado del servidor si todo salio correctamente,
        // es necesario antres de enviar otro comando.
        std::cout << client.recv_data(LOCAL_STING_LENGTH) << std::endl;
    }

    std::cout << "exit sucessfully..." << std::endl;
    return 0;
}