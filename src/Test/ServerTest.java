package src.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import src.*;
import src.ACL.SerializableObjects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);
    Server server;

    @BeforeEach
    void iniciar() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        server = new Server(8000);
        System.out.println("Server iniciado en el 8000 ");
    }

    @AfterEach
    void terminar() {
        server.stop();
        logger.info(() -> "Server en el 8000 detenido");
    }

    @Test
    @DisplayName("Test: inicio y detención del servidor con excepción esperada")
    void start() throws IOException, ClassNotFoundException {
        logger.info(() -> "Intentando abrir servidor en el puerto 9000");
        // Se espera que al ejecutar este método se lance una IllegalArgumentException
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            Server server_example2 = new Server(9000, "Archivo_pruebas_que_no_existe.dat");
            server_example2.start();
            logger.info(() -> "Cerrando servidor en el puerto 9000");
        });
        logger.info(() -> "Error generado con exito: " + exception.getMessage());

        // Opcional: Verificar el mensaje de la excepción
        // assertEquals("Mensaje esperado", exception.getMessage());

        // Se espera que el método se ejecute sin lanzar ninguna excepción
        assertDoesNotThrow(() -> {
            // crear un ACL por defecto:
            SerializableObjects acl = new SerializableObjects("Archivo_pruebas.dat");
            acl.load_default_users_and_groups();
            acl.write_users_and_groups();

            // iniciar el server con el nuevo ACL
            Server server_example2 = new Server(9000, "Archivo_pruebas.dat");

            // crear un hilo para ejecutar el servidor
            Thread thread_server = new Thread(new Runnable() {
                @Override
                public void run() {
                    server_example2.start();
                }
            });

            thread_server.start();
            Thread.sleep(2000); // dormir el hilo principal 2s y detener el servidor
            server_example2.stop();

            logger.info(() -> "Cerrando servidor en el puerto 9000");
        });
    }
    
}