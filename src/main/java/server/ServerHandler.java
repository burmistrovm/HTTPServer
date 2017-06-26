package server;

import http.Request;
import http.Response;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    private String dir;
    private Request request;
    private ResponseBuilder builder;
    private Response response;

    public ServerHandler(String directory) {
        this.dir = directory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        request = new Request((String)msg);
        builder = new ResponseBuilder(dir);
        builder.buildResponse(request);
        response = builder.getResponse();
        ctx.write(Unpooled.copiedBuffer(response.getByte()));
        final ChannelFuture channelfuture;
        if (response.getFileInputStream() != null) {
            channelfuture = ctx.writeAndFlush(new DefaultFileRegion(response.getFileInputStream().getChannel(), 0, response.getFileLength()));
            channelfuture.addListener(ChannelFutureListener.CLOSE);
        }
        else {
            ctx.flush();
            ctx.close();
        }
    }
}
