package common;
import org.apache.commons.cli.*;

public class Configuration {
    private String host = "127.0.0.1";
    private int threads = Runtime.getRuntime().availableProcessors();
    private String directory = System.getProperty("user.dir")+'/';
    private int port = 8081;

    public Configuration(String[] args){
        Options options = new Options();
        options.addOption(new Option("r", true, "root_directory"));
        options.addOption(new Option("c", true, "cpu"));
        options.addOption(new Option("p", true, "port"));

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        String parsedDir = cmd.getOptionValue("r");
        if (parsedDir != null) {
            directory = parsedDir;
        }

        String parsedCPUStr = cmd.getOptionValue("c");
        if (parsedCPUStr != null) {
            threads = Integer.parseInt(parsedCPUStr);
        }

        String parsedPortStr = cmd.getOptionValue("p");
        if (parsedCPUStr != null) {
            port = Integer.parseInt(parsedPortStr);
        }
    }

    public int getNumOfThreads() {
        return threads;
    }

    public int getPort() {
        return port;
    }

    public String getDir() {
        return directory;
    }
}
