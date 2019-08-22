package com.weishao.down.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.weishao.down.util.Base64Util;
import com.weishao.down.util.BigByteUtil;
import com.weishao.down.util.GZIPUtil;

/**
 * Message packet protocol definition
 * @author tang
 *
 */
public class MessageProtocol extends MessagePacket.Friend {

	/**
	 * Packet protocol definition
	 * ----------------------------------------------
	 * |        |   magic header field [byte(4)]     |
	 * |        |------------------------------------
	 * |        |  school domain field [byte(64)]    |
	 * | header |------------------------------------
	 * |        |   table name field [byte(64)]      |
	 * |        |------------------------------------
	 * |        |   error code field [int(4)]        |
	 * |        |------------------------------------
	 * |        |   body length field [int(4)]       |
	 * ----------------------------------------------
	 * | body   |  body content field [byte(n)]      |
	 * ----------------------------------------------
	 */
	
	/**
	 * magic header definition
	 */
	public static byte[] PROTOCOL_MAGIC_HEADER= {'d','u','m','p'};
	
	/**
	 * magic header field bytes size
	 */
	public static final Integer PROTOCOL_HEADER_MAGIC_LENGTH = 4;

	/**
	 * school domain field bytes size
	 */
	public static final Integer PROTOCOL_HEADER_DOMAIN_LENGTH = 64;

	/**
	 * table name field bytes size
	 */
	public static final Integer PROTOCOL_HEADER_TABLE_LENGTH = 64;
	
	/**
	 * error code field bytes size
	 */
	public static final Integer PROTOCOL_HEADER_ERRCODE_LENGTH = 4;

	/**
	 * body length field bytes size
	 */
	public static final Integer PROTOCOL_HEADER_BODYLEN_LENGTH = 4;
	
	/**
	 * packet header length bytes size
	 */
	public static final Integer PROTOCOL_HEADER_TOTAL_LENGTH = 4 + 64 + 64 + 4 + 4 ;
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * magic header field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_MAGIC = 0;
	
	/**
	 * school domain field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_DOMAIN = 0 + 4;
	
	/**
	 * table name field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_TABLE = 0 + 4 + 64 ;
	
	/**
	 * error code field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_ERRCODE = 0 + 4 + 64 + 64;
	
	/**
	 * body length field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_BODYLEN = 0 + 4 + 64 + 64 + 4;
	
	/**
	 * body content field start position
	 */
	public static final Integer PROTOCOL_HEADER_POSTION_BODY = 0 + 4 + 64 + 64 + 4 + 4;
	
	/** 
	 * Send packet
	 * @param packet         request message packet 
	 * @throws  IOException  if an I/O error occurs when sending data on the socket.
	 */
	public static void sendPacket(MessagePacket packet,DataOutputStream output) throws IOException {
		byte[] bytesBodyContent=packet.bodyContent;
		
		packet.bodyLength = bytesBodyContent.length;
		
		byte[] bytesDomainSchool=BigByteUtil.getStringBytes(packet.domainSchool);
		byte[] bytesTableName=BigByteUtil.getStringBytes(packet.tableName);
		byte[] bytesErrorCode=BigByteUtil.getIntBytes(packet.errorCode);
		byte[] bytesBodyLen=BigByteUtil.getIntBytes(packet.bodyLength);

		byte[] packetBytes = new byte[MessageProtocol.PROTOCOL_HEADER_TOTAL_LENGTH + packet.bodyLength];
		java.util.Arrays.fill(packetBytes, (byte) 0);
		
		System.arraycopy(packet.magicHeader,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_MAGIC,
				MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH);
		
		
		System.arraycopy(bytesDomainSchool,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_DOMAIN,
				bytesDomainSchool.length<MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH?bytesDomainSchool.length:MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH);
		
		
		System.arraycopy(bytesTableName,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_TABLE,
				bytesTableName.length<MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH?bytesTableName.length:MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH);
		
		
		System.arraycopy(bytesErrorCode,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_ERRCODE,
				MessageProtocol.PROTOCOL_HEADER_ERRCODE_LENGTH);
		
		
		System.arraycopy(bytesBodyLen,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_BODYLEN,
				MessageProtocol.PROTOCOL_HEADER_BODYLEN_LENGTH);
		
		
		System.arraycopy(bytesBodyContent,0,packetBytes,MessageProtocol.PROTOCOL_HEADER_POSTION_BODY,
				bytesBodyContent.length);
		
		output.write(packetBytes, 0, packetBytes.length);
		output.flush();
	}
	
	/**
	 * Receive packet
	 * @param inputStream    input stream for socket
	 * @param packet         response message packet
	 * @param useCompress    whether use compress for body content
     * @throws  IOException  if an I/O error occurs when receiving data on the socket.
	 */
	public static void recvPacket(DataInputStream inputStream,MessagePacket packet,boolean useCompress) throws IOException,RuntimeException {
		
		byte[] magic=new byte[MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH];
		MessageProtocol.recvPacketField(inputStream,magic);
		if(!Arrays.equals(magic,MessageProtocol.PROTOCOL_MAGIC_HEADER)) {
			throw new RuntimeException(String.format("Unkown magic header field :%s",BigByteUtil.getString(magic)));
		}
		
		byte[] domain=new byte[MessageProtocol.PROTOCOL_HEADER_DOMAIN_LENGTH];
		MessageProtocol.recvPacketField(inputStream,domain);
		packet.setDomainSchool(BigByteUtil.getString(domain).trim());
		
		byte[] table=new byte[MessageProtocol.PROTOCOL_HEADER_TABLE_LENGTH];
		MessageProtocol.recvPacketField(inputStream,table);
		packet.setTableName(BigByteUtil.getString(table).trim());
		
		byte[] error=new byte[MessageProtocol.PROTOCOL_HEADER_ERRCODE_LENGTH];
		MessageProtocol.recvPacketField(inputStream,error);
		packet.setErrorCode(BigByteUtil.getInt(error));
		
		byte[] bodylen=new byte[MessageProtocol.PROTOCOL_HEADER_BODYLEN_LENGTH];
		MessageProtocol.recvPacketField(inputStream,bodylen);
		packet.setBodyLength(BigByteUtil.getInt(bodylen));
		
		byte[] body=new byte[packet.bodyLength];
		MessageProtocol.recvPacketField(inputStream,body);
		if (0 == packet.errorCode) {
			byte[] text = null;
			if (useCompress) {
				byte[] content = GZIPUtil.uncompress(body);
				text = Base64Util.decode(content);
			} else {
				text = body;
			}
			packet.setBodyContent(text);
		} else {
			packet.setBodyContent(body);
		}
	}

	/**
	 * Receive packet field
	 * @param inputStream    input stream for socket
	 * @param field          bytes field which for store data received
     * @throws  IOException  if an I/O error occurs when receiving data on the socket.
	 */
	public static void recvPacketField(DataInputStream inputStream,byte[] field) throws IOException {
		int totalBytesSize=field.length;
		int restBytesSize=totalBytesSize;
		do {
			int recvBytesBody=inputStream.read(field,totalBytesSize-restBytesSize,restBytesSize);
			if(recvBytesBody<0) {
				throw new RuntimeException("Recv field data failed!");
			}
			
			restBytesSize-=recvBytesBody;
		}while(restBytesSize>0);
	}
}
