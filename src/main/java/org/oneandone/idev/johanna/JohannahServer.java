package org.oneandone.idev.johanna;
    
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.netty.JohannaServerHandler;
import org.oneandone.idev.johanna.store.SessionStore;
    
/**
 * Discards any incoming data.
 */
public class JohannahServer {
    private static final Logger LOG = Logger.getLogger(JohannahServer.class.getName());
    
    private int port;
    
    public JohannahServer(int port) {
        this.port = port;
    }
    
    public void run() throws Exception {
        LOG.info("Server startup.");
        final SessionStore store= new SessionStore();
        
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            System.out.println("===> Starting Johanna Server...");
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(
                             8192, Delimiters.lineDelimiter()
                     ));
                     ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("iso-8859-1")));
                     ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("iso-8859-1")));
                     ch.pipeline().addLast(new JohannaServerHandler(store));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
    
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();
    
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
            System.out.println("---> Shutting down server.");
        } finally {
            System.out.println("---> Cleaning up...");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 2001;
        }
        new JohannahServer(port).run();
    }
}