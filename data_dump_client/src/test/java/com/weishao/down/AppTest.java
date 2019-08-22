package com.weishao.down;

import com.weishao.down.protocol.MessagePacket;
import com.weishao.down.DownloadClient;

public class AppTest {

	public static void main(String[] args) {
		DownloadClient client = null;

		try {
			client = new DownloadClient("127.0.0.1", 1234, false);
			MessagePacket request0 = new MessagePacket();
			request0.setDomainSchool("data");
			request0.setTableName("ofuser");
			request0.setBodyContent("e10adc3949ba59abbe56e057f20f883e".getBytes());

			MessagePacket response0 = client.request(request0);
			if (response0.isSuccess()) {
				byte[] content = response0.getBodyContent();
				System.out.println("read body content:" + new String(content));
			} else {
				System.out.println("error:" + new String(response0.getBodyContent()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != client) {
				client.close();
				client = null;
			}
		}
	}

}
