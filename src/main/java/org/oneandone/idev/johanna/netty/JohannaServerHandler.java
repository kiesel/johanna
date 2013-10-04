/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.netty;

import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.RequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        LOG.log(Level.INFO, "> New connection from {0}:{1}", new Object[]{addr.getHostString(), addr.getPort()});
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress addr= (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.log(Level.INFO, "< New connection from {0}:{1}", new Object[]{addr.getHostString(), addr.getPort()});
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String i) throws Exception {
        InetSocketAddress addr= (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.log(Level.FINE, ">> Received message from {0}:{1}", new Object[]{addr.getHostString(), addr.getPort()});
        LOG.log(Level.FINER, ">>> {0}", i);
                
        Request request;
        Response response;
        
        try {
            request= this.factory.createRequest(i);
            response= request.process(this.store);
        } catch (IllegalArgumentException e) {
            LOG.warning(e.toString());
            response= new Response(false, "SYNTAX");
        }

        LOG.log(Level.FINEST, "<<< {0}", response.toString());
        ctx.write(response.toString());
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
}
