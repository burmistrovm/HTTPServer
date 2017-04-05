package http;

/**
 * Created by mike on 05.04.17.
 */
public class Response {
    private String status;
    private Header header;
    private String body;

    public void setHeader(Header head) {
        header = head;
    }

    public void setBody(String data) {
        body = data;
    }

    public byte[] getByte() {
        byte[] head = header.getBytes();
        return head;
    }

}
