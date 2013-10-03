/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.netty;

import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.RequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
        LOG.info("New client from " + ctx.name());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.info("Client disconnect.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String i) throws Exception {
        Request request;
        Response response;
        
        try {
            request= this.factory.createRequest(i);
            response= request.execute(this.store);
        } catch (IllegalArgumentException e) {
            LOG.warning(e.toString());
            response= new Response(false, "SYNTAX");
        }
        
        ctx.write(response.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    
    
}
