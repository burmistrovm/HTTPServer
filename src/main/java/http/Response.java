package http;

import java.io.FileInputStream;

/**
 * Created by mike on 05.04.17.
 */
public class Response {
    private String status;
    private Header header;
    private FileInputStream body;
    private int fileLength;
    public void setHeader(Header head) {
        header = head;
    }

    public void setFileInputStream(FileInputStream file) {
        body = file;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int len) {
        fileLength = len;
    }

    public FileInputStream getFileInputStream() {
        return body;
    }

    public byte[] getByte() {
        String  head = header.getString();
        return (head).getBytes();
    }

}
