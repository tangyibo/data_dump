package com.weishao.dump.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyTcpServer {

	private int portNumber;

	private NettyServerInitializer serverInitializer;

	public NettyTcpServer(int port, boolean compress) {
		this.portNumber = port;
		serverInitializer = new NettyServerInitializer(compress);
	}

	public void start() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			// 指定socket的一些属性
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // 指定是一个NIO连接通道
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(this.serverInitializer);

			// 绑定对应的端口号,并启动开始监听端口上的连接
			Channel ch = serverBootstrap.bind(this.portNumber).sync().channel();

			// 等待关闭,同步端口
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}