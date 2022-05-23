package org.oneandone.idev.johanna.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.RequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class JohannaServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = Logger.getLogger(JohannaServerHandler.class.getName());
    private SessionStore store;
    private RequestFactory factory;
    
    public JohannaServerHandler(SessionStore store) {
        this.store= store;
        this.factory= new RequestFactory();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        InetSocketAddress addr= (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.log(Level.FINEST, "> New connection from {0}", new Object[]{addr.getHostString()});
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        
        InetSocketAddress addr= (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.log(Level.FINEST, "< Closed connection from {0}", new Object[]{addr.getHostString()});
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String in) throws Exception {
        InetSocketAddress addr= (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.log(Level.FINER, ">>> [{0}] {1}", new Object[]{ addr.getHostString(), in });
                
        Request request;
        Response response;
        
        try {
            request= this.factory.createRequest(in);
            request.setPrefix(this.prefixFor(addr.getAddress()));
            response= request.process(this.store);
        } catch (IllegalArgumentException e) {
            LOG.warning(e.toString());
            response= new Response(false, "SYNTAX");
        }

        LOG.log(Level.FINER, "<<< [{0}] {1}", new Object[] { addr.getHostString(), response.toString() });
        ChannelFuture future= ctx.writeAndFlush(response.toString());
        
        if (response.getClose()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warning(cause.toString());
        ctx.close();
    }

    private String prefixFor(final InetAddress address) {
        return Hex.encodeHexString(address.getAddress());
    }
    
}
