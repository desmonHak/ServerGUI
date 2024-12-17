import src.GUI;
import src.Server;

public class Core  {
    public static void main(String[] args) {
        Server server = new Server();
        GUI gui = new GUI();
        server.start(450);
    }
}