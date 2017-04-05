package http;

/**
 * Created by mike on 05.04.17.
 */
public class Request {
    private String method;
    private String filePath;

    public Request(String msg){
        final String[] request = msg.split("\\n")[0].split(" ");
        filePath = request[1];
        if(request[0].equals("GET")) {
            method = "GET";
        }
        else if (request[0].equals("HEAD")) {
            method = "HEAD";
        }
        else {
            method = "FORBIDDEN";
        }
    }

    public String getMethod(){
        return method;
    }

    public String getFile() {
        return filePath;
    }
}
