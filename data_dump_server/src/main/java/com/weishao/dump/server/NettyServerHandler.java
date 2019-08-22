package com.weishao.dump.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weishao.dump.protocol.MessagePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息处理器
 * @author Tang
 *
 */
@ChannelHandler.Sharable 
public class NettyServerHandler extends SimpleChannelInboundHandler<MessagePacket> {

	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessagePacket msg) throws Exception {
		logger.info("recv message:{}",msg.toString());
		MessagePacket response=new MessagePacket();
		response.setDomainSchool(msg.getDomainSchool());
		response.setTableName(msg.getTableName());
		response.setErrorCode(0);
		response.setBodyContent("this is a test".getBytes());
		logger.info("send message:{}",response.toString());
		ctx.write(response);
		ctx.flush();
	}

}
