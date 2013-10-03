/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.netty;

import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahRequestFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahServerHandler extends SimpleChannelInboundHandler<String> {
    private SessionStore store;
    
    public HannahServerHandler(SessionStore store) {
        this.store= store;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String i) throws Exception {
        HannahRequestFactory f= new HannahRequestFactory();
        HannahRequest r= f.createRequest(i);
        r.execute(this.store);
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
