/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.netty;

import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahRequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = Logger.getLogger(HannahServerHandler.class.getName());
    private SessionStore store;
    private HannahRequestFactory factory;
    
    public HannahServerHandler(SessionStore store) {
        this.store= store;
        this.factory= new HannahRequestFactory();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.log(Level.INFO, "New client from " + ctx.name());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.log(Level.INFO, "Client disconnect.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String i) throws Exception {
        HannahRequest request;
        HannahResponse response;
        
        try {
            request= this.factory.createRequest(i);
            response= request.execute(this.store);
        } catch (IllegalArgumentException e) {
            response= new HannahResponse(false, "SYNTAX");
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
