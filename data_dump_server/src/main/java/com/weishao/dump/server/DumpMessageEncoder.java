package com.weishao.dump.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.weishao.dump.protocol.MessagePacket;
import com.weishao.dump.protocol.MessageProtocol;
import com.weishao.dump.util.Base64Util;
import com.weishao.dump.util.BigByteUtil;
import com.weishao.dump.util.GZIPUtil;

/**
 * 编码器
 * @author tang
 *
 */
public class DumpMessageEncoder extends MessageToByteEncoder<MessagePacket> {

	private boolean useCompress = false;

	public DumpMessageEncoder(boolean compress) {
		this.useCompress = compress;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, MessagePacket packet, ByteBuf out) throws Exception {
		byte[] bytesBodyContent=null;
		if(useCompress) {
			bytesBodyContent=GZIPUtil.compress(Base64Util.encode(packet.getBodyContent()));
		}else {
			bytesBodyContent=packet.getBodyContent();
		}
		
		packet.setBodyLength(bytesBodyContent.length);
		
		byte[] bytesDomainSchool=BigByteUtil.getStringBytes(packet.getDomainSchool());
		byte[] bytesTableName=BigByteUtil.getStringBytes(packet.getTableName());
		byte[] bytesErrorCode=BigByteUtil.getIntBytes(packet.getErrorCode());
		byte[] bytesBodyLen=BigByteUtil.getIntBytes(packet.getBodyLength());

		byte[] packetHeaderBytes = new byte[MessageProtocol.PROTOCOL_HEADER_TOTAL_LENGTH ];
		java.util.Arrays.fill(packetHeaderBytes, (byte) 0);
		
		System.arraycopy(packet.getHeaderMagic(),0,packetHeaderBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_MAGIC,
				MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH);
		
		
		System.arraycopy(bytesDomainSchool,0,packetHeaderBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_DOMAIN,
				bytesDomainSchool.length<MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH?bytesDomainSchool.length:MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH);
		
		
		System.arraycopy(bytesTableName,0,packetHeaderBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_TABLE,
				bytesTableName.length<MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH?bytesTableName.length:MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH);
		
		
		System.arraycopy(bytesErrorCode,0,packetHeaderBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_ERRCODE,
				MessageProtocol.PROTOCOL_HEADER_ERRCODE_LENGTH);
		
		
		System.arraycopy(bytesBodyLen,0,packetHeaderBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_BODYLEN,
				MessageProtocol.PROTOCOL_HEADER_BODYLEN_LENGTH);

		out.writeBytes(packetHeaderBytes, 0, packetHeaderBytes.length);
		out.writeBytes(bytesBodyContent, 0, bytesBodyContent.length);
	}

}
