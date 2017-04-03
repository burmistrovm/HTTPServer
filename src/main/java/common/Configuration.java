package common;

public class Configuration {
    private String host = "127.0.0.1";
    private int threads = Runtime.getRuntime().availableProcessors();
    private String directory = System.getProperty("user.dir")+'/';

    public Configuration(String[] args){

    }

}
