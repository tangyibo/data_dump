package com.weishao.dump.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

	public static final String BASE64_ENCODE_UTF_8 = "UTF-8";
	
	public static byte[] encode(byte[] text) throws UnsupportedEncodingException {
		return encode(text,BASE64_ENCODE_UTF_8);
	}
	
	public static String encode(String text) throws UnsupportedEncodingException {
		return encode(text,BASE64_ENCODE_UTF_8);
	}
	
	public static byte[] decode(byte[] text) throws UnsupportedEncodingException {
		return decode(text,BASE64_ENCODE_UTF_8);
	}
	
	public static String decode(String text) throws UnsupportedEncodingException {
		return decode(text,BASE64_ENCODE_UTF_8);
	}
	
	public static byte[] encode(byte[] textBytes,String encoding) throws UnsupportedEncodingException {
		final Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encode(textBytes);
	}
	
	public static String encode(String text,String encoding) throws UnsupportedEncodingException {
		final Base64.Encoder encoder = Base64.getEncoder();
		final byte[] textBytes = text.getBytes(encoding);
		return encoder.encodeToString(textBytes);
	}
	
	public static byte[] decode(byte[] textBytes,String encoding) throws UnsupportedEncodingException {
		final Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(textBytes);
	}
	
	public static String decode(String text,String encoding) throws UnsupportedEncodingException {
		final Base64.Decoder decoder = Base64.getDecoder();
		final byte[] textBytes = text.getBytes(encoding);
		final byte[] srcBytes=decoder.decode(textBytes);
		return new String(srcBytes);
	}
	

}
