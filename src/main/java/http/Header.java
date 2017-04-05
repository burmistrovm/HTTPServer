package http;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Header {
    private String httpVersion = "HTTP/1.1 ";
    private String contentLength;
    private String contentType;
    private String connection;
    private String time;
    private String serverVersion = "Server: HL_1.0"+ "\r\n";
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
        final Calendar calendar = Calendar.getInstance();
        final String dayWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        year = year % 100;
        time = "Date: " + dayWeek + ", " + day + " " + month + " " + year + " " +
                (new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()) + " GMT\r\n");
    }

    public void setConnection() {
        connection = "Connection: close\r\n";
    }

    public void setContentType(String fileType) {
        contentType = "Content-Type: " + contentTypes.get(fileType) + "\r\n";
    }

    public void setContentLength(int length) {
        contentLength = "Content-Length: " +length + "\r\n";
    }

    public byte[] getBytes() {
        return (httpVersion + status + contentType + contentLength + connection + time + serverVersion).getBytes();
    }
}