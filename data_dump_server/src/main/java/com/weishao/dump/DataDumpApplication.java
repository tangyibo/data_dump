package com.weishao.dump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import com.weishao.dump.server.NettyTcpServer;

/**
 * 参考教程：https://blog.csdn.net/varyall/article/details/80699268
 * 
 * @author tang
 *
 */
@SpringBootApplication
@Configuration("DataConfig")
public class DataDumpApplication {

	private static final Logger logger = LoggerFactory.getLogger(DataDumpApplication.class);

	@Value("${netty.server.port}")
	public int port;

	@Value("${netty.server.compress}")
	public boolean compress;

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(DataDumpApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		ApplicationContext context = springApplication.run(args);
		DataDumpApplication app = (DataDumpApplication) context.getBean("DataConfig");
		try {
			NettyTcpServer server = new NettyTcpServer(app.port, app.compress);
			server.start();
		} catch (InterruptedException e) {
			logger.info("server is interrupted by error:", e);
		} catch (Exception e) {
			logger.info("server is stopped by error:", e);
		}

		logger.info("server is stopped now!");
	}

}
