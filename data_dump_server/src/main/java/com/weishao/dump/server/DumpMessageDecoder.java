package com.weishao.dump.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.weishao.dump.protocol.MessagePacket;
import com.weishao.dump.protocol.MessageProtocol;
import com.weishao.dump.util.BigByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码器
 * 
 * @author tang
 *
 */
public class DumpMessageDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() > MessageProtocol.PROTOCOL_HEADER_TOTAL_LENGTH) {

			//找到包头的标记位置
			byte[] magic = new byte[MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH];
			while (true) {
				in.readBytes(magic);
				if (Arrays.equals(magic, MessageProtocol.PROTOCOL_MAGIC_HEADER)) {
					break;
				}
			}
			
			try {
				MessagePacket packet =this.decodeOnePacket(in);
				out.add(packet);
			} catch (Exception e) {
				in.resetReaderIndex();
				e.printStackTrace();
			}
		}

	}

	private MessagePacket decodeOnePacket(ByteBuf input) throws IOException, RuntimeException {
		MessagePacket packet=new MessagePacket();

		byte[] domain = new byte[MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH];
		input.readBytes(domain);
		packet.setDomainSchool(BigByteUtil.getString(domain).trim());

		byte[] table = new byte[MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH];
		input.readBytes(table);
		packet.setTableName(BigByteUtil.getString(table).trim());

		byte[] error = new byte[MessageProtocol.PROTOCOL_HEADER_ERRCODE_LENGTH];
		input.readBytes(error);
		packet.setErrorCode(BigByteUtil.getInt(error));

		byte[] bodylen = new byte[MessageProtocol.PROTOCOL_HEADER_BODYLEN_LENGTH];
		input.readBytes(bodylen);
		packet.setBodyLength(BigByteUtil.getInt(bodylen));

		byte[] body = new byte[packet.getBodyLength()];
		input.readBytes(body);
		packet.setBodyContent(body);
		
		return packet;
	}

}
