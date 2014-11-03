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
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import java.nio.charset.Charset;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
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
public class JohannahServer {
    private static final Logger LOG = Logger.getLogger(JohannahServer.class.getName());
    private static final int MAX_THREADS = 1;
    
    @Option(name = "--help", help = true, usage = "This command line help")
    private boolean help;
    
    @Option(name = "--debug", usage = "Whether to log with DEBUG level")
    private boolean debug;
    
    @Option(name = "--port", aliases = "-p", usage = "The TCP/IP port to bind to")
    private int port = 2001;
        
    @Option(name = "--host", aliases = "-h", usage = "The IP of the REDIS server to bind to (see --backend, defaults to 127.0.0.1)", metaVar = "IP")
    private String host= "127.0.0.1";
    
    private enum Backend {
      MEMORY,
      REDIS
    };

    @Option(name = "--backend", aliases = "-b", usage = "The session storage backend to use")
    private Backend backend = Backend.MEMORY;
    
    @Option(name = "--identifier", aliases = "-i", usage = "The identifier factory to use")
    private IdentifierFactory identifierFactory = IdentifierFactory.MD5;    
    
    SessionStore store;
    
    private static void setDebug(boolean debug) {
        if (debug) {
            LOG.info("Enabling debug mode logging.");
            Logger log= Logger.getLogger("");
            for (Handler h : log.getHandlers()) {
                h.setLevel(Level.ALL);
            }
            Logger.getLogger("org.oneandone.idev.johanna").setLevel(Level.ALL);
        }
    }
    
    public void run() {
        try {
            this.runInternal();
        } catch (Exception ex) {
            Logger.getLogger(JohannahServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void runInternal() throws Exception {
        LOG.info("Server startup.");

        switch (backend) {
            case MEMORY: {
                LOG.info("Using \"memory\" backend.");
                this.store= new MemorySessionStore(this.identifierFactory);
                break;
            }
                
            case REDIS: {
                JedisPoolConfig config= new JedisPoolConfig();
                config.setMaxActive(MAX_THREADS);
                JedisPool pool= new JedisPool(config, this.host);
                
                this.store= new RedisSessionStore(this.identifierFactory, pool);

                LOG.log(Level.INFO, "Using \"redis\" backend: {0} @ {1}", new Object[] { pool, this.host });
                break;
            }
                
            default: {
                throw new IllegalArgumentException("Backend must be one of 'memory' or 'redis'");
            }
        }

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(MAX_THREADS);
        try {
            LOG.info("===> Starting Johanna Server.");
            store.scheduleMaintenanceTask();
            
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
                     ch.pipeline().addLast(executorGroup, new JohannaServerHandler(store));
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
            store.cancelMaintenanceTask();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            executorGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
        CmdLineParser cmdLineParser = null;
        try {
            JohannahServer johannahServer = new JohannahServer();
            cmdLineParser = new CmdLineParser(johannahServer);
            cmdLineParser.parseArgument(args);
            if (johannahServer.help) {
                cmdLineParser.printUsage(System.err);
                System.err.flush();
                return;
            }
            JohannahServer.setDebug(johannahServer.debug);
            johannahServer.run();
        } catch (CmdLineException ex) {
            System.err.println(ex);
            if (cmdLineParser != null) {
                cmdLineParser.printUsage(System.err);
                System.err.flush();
            }
        }
    }
}