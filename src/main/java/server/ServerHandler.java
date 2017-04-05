package server;

import http.Request;
import http.Response;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


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
        final ChannelFuture channelfuture = ctx.writeAndFlush(Unpooled.copiedBuffer(response.getByte()));
        channelfuture.addListener(ChannelFutureListener.CLOSE);
    }
}
