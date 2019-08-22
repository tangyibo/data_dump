package com.weishao.dump.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@ChannelHandler.Sharable 
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

	private boolean compress;

	private NettyServerHandler handler;

	public NettyServerInitializer(boolean compress) {
		this.compress=compress;
		handler = new NettyServerHandler();
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new DumpMessageDecoder());
		pipeline.addLast("encoder", new DumpMessageEncoder(this.compress));
		pipeline.addLast(handler);
	}

}
