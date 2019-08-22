package com.weishao.dump.protocol;

import java.util.Arrays;

/**
 * Message packet definition
 * @author tang
 *
 */
public class MessagePacket {
	
	/**
	 * Friend class definition
	 * @author tang
	 *
	 */
	public static abstract class Friend{
		private static final Class<? extends Friend> friend=MessageProtocol.class;
		
		public Friend() {
			if(this.getClass()!=friend) {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	/**
	 * magic header field
	 */
	protected byte[] magicHeader;
	
	/**
	 * school domain field
	 */
	protected String domainSchool;
	
	/**
	 * table name field
	 */
	protected String tableName;
	
	/**
	 * error code field
	 */
	protected int errorCode;
	
	/**
	 * body length field
	 */
	protected int bodyLength;
	
	/**
	 * body content field
	 */
	protected byte[] bodyContent;
	
	public MessagePacket() {
		magicHeader=new byte[MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH];
		System.arraycopy(MessageProtocol.PROTOCOL_MAGIC_HEADER,0,magicHeader,0,MessageProtocol.PROTOCOL_HEADER_MAGIC_LENGTH);
		
		errorCode=0;
		bodyLength=0;
	}
	
	public byte[] getHeaderMagic() {
		return this.magicHeader;
	}

	public String getDomainSchool() {
		return domainSchool;
	}

	public void setDomainSchool(String domainSchool) {
		this.domainSchool = domainSchool;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public boolean isSuccess() {
		return 0==errorCode;
	}
	
	public void setErrorCode(int code) {
		this.errorCode = code;
	}

	public void setBodyLength(int bodylen) {
		bodyLength=bodylen;
	}
	
	public int getBodyLength() {
		return bodyLength;
	}

	public byte[] getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(byte[] content) {
		this.bodyContent = content;
	}

	@Override
	public String toString() {
		return "MessagePacket [magicHeader=" + Arrays.toString(magicHeader) + ", domainSchool=" + domainSchool
				+ ", tableName=" + tableName + ", errorCode=" + errorCode + ", bodyLength=" + bodyContent.length
				+ ", bodyContent=" + new String(bodyContent) + "]";
	}
	
	
}
