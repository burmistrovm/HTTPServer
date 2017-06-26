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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class HTTPServer {
    private final Configuration config;
    private final EventLoopGroup workers;
    private final EventLoopGroup bossGroup;

    public HTTPServer(Configuration configuration){
        this.config = configuration;
        workers = new NioEventLoopGroup(this.config.getNumOfThreads());
        bossGroup = new NioEventLoopGroup();
    }

    public void start(){
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workers)
                .channel(NioServerSocketChannel.class);

        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel nioServerSocketChannel) throws Exception {
                nioServerSocketChannel.pipeline()
                        .addLast(new StringDecoder(CharsetUtil.UTF_8))
                        .addLast(new ServerHandler(config.getDir()))
                        .addLast(new StringEncoder(CharsetUtil.UTF_8));
            }
        });

        server.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);

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
