package server;


import com.sun.rmi.rmid.ExecOptionPermission;
import http.Header;
import http.Request;
import http.Response;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ResponseBuilder {
    private Response response;
    private String rootDir;
    private String relFilePath;
    private Path filePath;
    private Header header;

    public ResponseBuilder(String directory) {
        rootDir = directory;
    }

    public void buildResponse (Request req) {
        relFilePath = req.getFile();
        response = new Response();
        header = new Header();
        System.out.println(req.getMethod());
        if (req.getMethod().equals("GET")) {
            if (checkURI() == Boolean.TRUE) {
                setBody();
                setContentLength();
                setContentType();
                header.setStatus("200 OK\r\n");
            }
            else {
                header.setStatus("404 Not Found\r\n");
            }
            header.setTime();
            header.setConnection();
        }
        if (req.getMethod() == "HEAD") {
            if (checkURI() == Boolean.TRUE) {
                setContentLength();
                setContentType();
                header.setStatus("200 OK\r\n");
            }
            else {
                header.setStatus("404 Not Found\r\n");
            }
            header.setTime();
            header.setConnection();
            response.setHeader(header);
        }
        else {
            header.setStatus("405 Method Not Allowed\r\n");
            header.setTime();
            header.setConnection();
            response.setHeader(header);
        }
    }

    public Boolean checkURI() {
        relFilePath = relFilePath.split("\\?")[0];
        relFilePath = URLDecoder.decode(relFilePath);
        filePath = Paths.get(rootDir + relFilePath);
        if (Files.isDirectory(filePath)) {
            System.out.println("dir");
            filePath = Paths.get(rootDir + relFilePath, "index.html");
            if (Files.exists(filePath)) {
                relFilePath = relFilePath + "index.html";
                filePath = Paths.get(rootDir + relFilePath);
                return Boolean.TRUE;
            }
        }
        else if (Files.exists(filePath)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void setContentLength() {
        try {
            final byte[] file = Files.readAllBytes(filePath);
            header.setContentLength(file.length);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private void setContentType() {
        final String[] splitedPath = relFilePath.split("\\.");
        header.setContentType(splitedPath[splitedPath.length-1]);
    }

    private void setBody() {
        try {
            String body = "";
            if (header.getContentType().contains("text")) {
                final List<String> fileText = Files.readAllLines(filePath);
                for (String text : fileText) {
                    System.out.println(text);
                    body = body + text + "\n";
                }
            } else {
                final byte[] fileBytes = Files.readAllBytes(filePath);
                for (byte one : fileBytes) {
                    body = body + one;
                }
            }
            response.setBody(body);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public Response getResponse() {
        return response;
    }
}
