package server;

import http.Header;
import http.Request;
import http.Response;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseBuilder {
    private Response response;
    private String rootDir;
    private String relFilePath;
    private Path filePath;
    private Header header;
    private Boolean noIndex = Boolean.FALSE;

    public ResponseBuilder(String directory) {
        rootDir = directory;
    }

    public void buildResponse (Request req) {
        relFilePath = req.getFile();
        response = new Response();
        header = new Header();
        if (req.getMethod() == ("GET")) {
            if (checkURI() == Boolean.TRUE) {
                setContentLength();
                setContentType();
                setFileInputStream();
                header.setStatus("200 OK\r\n");
            }
            else if (noIndex == Boolean.FALSE) {
                header.setStatus("404 Not Found\r\n");
                filePath = Paths.get(System.getProperty("user.dir")+'/' + "404.html");
                setContentLength();
                setContentType();
                setFileInputStream();
            }
            else {
                header.setStatus("403 Forbidden\r\n");
                filePath = Paths.get(System.getProperty("user.dir")+'/' + "403.html");
                setContentLength();
                setContentType();
                setFileInputStream();
            }
        }
        else if (req.getMethod() == "HEAD") {
            if (checkURI() == Boolean.TRUE) {
                setContentLength();
                setContentType();
                header.setStatus("200 OK\r\n");
            }
            else {
                header.setStatus("404 Not Found\r\n");
            }
        }
        else if (req.getMethod() == "FORBIDDEN") {
            header.setStatus("405 Method Not Allowed\r\n");
            filePath = Paths.get(System.getProperty("user.dir")+'/' + "405.html");
            setContentLength();
            setContentType();
            setFileInputStream();
        }
        header.setTime();
        header.setConnection();
        response.setHeader(header);
    }

    public Boolean checkURI() {
        final String[] check = relFilePath.split("\\.\\.\\/");
        if (check.length > 1) {
            return Boolean.FALSE;
        }
        relFilePath = relFilePath.split("\\?")[0];
        try {
            relFilePath = URLDecoder.decode(relFilePath, "UTF-8");
        }
        catch (Exception e){
            System.out.println(e);
        }
        filePath = Paths.get(rootDir + relFilePath);
        if (Files.isDirectory(filePath)) {
            filePath = Paths.get(rootDir + relFilePath, "index.html");
            if (Files.exists(filePath)) {
                relFilePath = relFilePath + "index.html";
                filePath = Paths.get(rootDir + relFilePath);
                return Boolean.TRUE;
            }
            else {
                noIndex = Boolean.TRUE;
                return Boolean.FALSE;
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
            response.setFileLength(file.length);
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

    private void setFileInputStream() {
        try {
            response.setFileInputStream(new FileInputStream(filePath.toFile()));
        }
        catch (Exception e){
        }
    }

    public Response getResponse() {
        return response;
    }
}
