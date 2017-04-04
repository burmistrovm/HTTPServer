package server;

import common.Configuration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HTTPServer {
    private Configuration config;
    private final EventLoopGroup workers;
    public HTTPServer(Configuration configuration){
        this.config = configuration;
        workers = new NioEventLoopGroup(this.config.getNumOfThreads());
    }

    public void start(){
        ServerBootstrap server = new ServerBootstrap();
        server.group(workers)
                .channel(NioServerSocketChannel.class);
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel nioServerSocketChannel) throws Exception {
                nioServerSocketChannel.pipeline()
                        .addLast(new ServerHandler());

            }
        });
        server.option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture future = server.bind(config.getPort()).sync();
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {

        }
        finally {
            workers.shutdownGracefully();
        }
    };
}
