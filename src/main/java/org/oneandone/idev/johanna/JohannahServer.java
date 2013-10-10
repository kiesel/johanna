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
import net.xp_framework.jcli.cmd.Arg;
import net.xp_framework.jcli.cmd.Command;
import net.xp_framework.jcli.cmd.Default;
import org.oneandone.idev.johanna.netty.JohannaServerHandler;
import org.oneandone.idev.johanna.store.SessionStore;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;
import org.oneandone.idev.johanna.store.memory.MemorySessionStore;
import org.oneandone.idev.johanna.store.redis.RedisSessionStore;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
    
    
/**
 * Discards any incoming data.
 */
public class JohannahServer extends Command {
    private static final Logger LOG = Logger.getLogger(JohannahServer.class.getName());

    private int port;
    SessionStore store;
    private String host;
    private IdentifierFactory identifierFactory;
    
    @Arg
    public void setPort(@Default("2001") String port) {
        this.port = Integer.parseInt(port);
    }
    
    @Arg(name= "host", option= 'h')
    public void setHost(@Default("127.0.0.1") String host) {
        this.host= host;
    }
    
    @Arg(name= "ids", option= 'i')
    public void setIdentityMode(@Default("md5") String id) {
        this.identifierFactory= IdentifierFactory.valueOf(id.toUpperCase());
    }
    
    @Arg(name= "backend", option= 'b')
    public void setSessionBackend(@Default("memory") String backend) {
        switch (backend) {
            case "memory": {
                LOG.info("Using \"memory\" backend.");
                this.store= new MemorySessionStore(this.identifierFactory);
                break;
            }
                
            case "redis": {
                JedisPool pool= new JedisPool(new JedisPoolConfig(), this.host);
                this.store= new RedisSessionStore(this.identifierFactory, pool);

                LOG.log(Level.INFO, "Using \"redis\" backend: {0}", pool);
                break;
            }
                
            default: {
                throw new IllegalArgumentException("Backend must be one of 'memory' or 'redis'");
            }
        }
    }
    
    @Override
    public void run() {
        try {
            this.runInternal();
        } catch (Exception ex) {
            Logger.getLogger(JohannahServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runInternal() throws Exception {
        LOG.info("Server startup.");
        
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            LOG.info("===> Starting Johanna Server.");
            store.startAutomaticGarbageCollection();

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(
                             1024 * 1024 * 1024, Delimiters.lineDelimiter()
                     ));
                     ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("iso-8859-1")));
                     ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("iso-8859-1")));
                     ch.pipeline().addLast(new JohannaServerHandler(store));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .option(ChannelOption.TCP_NODELAY, true)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
    
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();
            
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
            LOG.info("===> Shutting down Johanna instance.");
        } finally {
            LOG.info("---> Cleanup.");
            store.stopAutomaticGarbageCollection();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}