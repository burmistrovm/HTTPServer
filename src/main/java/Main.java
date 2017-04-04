
import common.Configuration;
import server.HTTPServer;

public class Main {
    public static final String STARTING_SERVER_MESSAGE = "Starting server at %s:%d...";
    public static Configuration config;
    public static void main(String[] args) throws Exception {
        try {
            config = new Configuration(args);
        } catch (Exception e) {
            System.out.println("Usage: -r: root directory, -c: number of cores.");
        }

        HTTPServer server = new HTTPServer(config);
        server.start();
    }
}
