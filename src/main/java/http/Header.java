package http;


import java.text.SimpleDateFormat;
import java.util.*;

public class Header {
    private String httpVersion = "HTTP/1.1 ";
    private String contentLength;
    private String contentType;
    private String connection;
    private String time;
    private String serverVersion = "Server: Highload/1.0.0 (UNIX)"+ "\r\n";
    private String status;

    private static HashMap<String, String> contentTypes;

    static {
        contentTypes = new HashMap<>();
        contentTypes.put("css", "text/css");
        contentTypes.put("html", "text/html");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("png", "image/png");
        contentTypes.put("swf", "application/x-shockwave-flash");
        contentTypes.put("txt", "text/txt");
        contentTypes.put("js", "text/javascript");
        contentTypes.put("xml", "text/xml");
    }

    public String getContentType() {
        return contentType;
    }

    public void setStatus(String stat) {
        status = stat;
    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        time = "Date: " + dateFormat.format(calendar.getTime()) + "\r\n";
    }

    public void setConnection() {
        connection = "Connection: close\r\n";
    }

    public void setContentType(String fileType) {
        contentType = "Content-Type: " + contentTypes.get(fileType) + "\r\n";
    }

    public void setContentLength(int length) {
        contentLength = "Content-Length: " + length + "\r\n";
    }

    public String getString() {
        return httpVersion + status + contentType + contentLength + connection + time + serverVersion + "\r\n";
    }
}