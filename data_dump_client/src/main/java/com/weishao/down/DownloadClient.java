package com.weishao.down;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.weishao.down.protocol.MessagePacket;
import com.weishao.down.protocol.MessageProtocol;

/**
 * Download client for dump file communication
 * @author tang
 *
 */
public class DownloadClient {
	
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private boolean useCompress;
	
	/**
	 * Constructor function
	 * @param host   server host address
	 * @param port   server host port number
	 * @throws Exception
	 */
	public DownloadClient(String host,int port) throws Exception {
		this(host,port,true);
	}

	/**
	 * Constructor function
	 * @param host       server host address
	 * @param port       server host port number
	 * @param compress   whether use compress for body content
	 * @throws Exception
	 */
	public DownloadClient(String host,int port,boolean compress) throws Exception {
		serverAddress=host;
		serverPort=port;
		socket = null;
		useCompress=compress;
		
		connect();
	}
	
	/**
	 * Request to server by packet
	 * @param msg   request packet message
	 * @return  response packet message
	 * @throws IOException
	 */
	public MessagePacket request(MessagePacket msg) throws IOException {
		send(msg);
		return recv();
	}
	
	/**
	 * Disconnect with remote server
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get remote server address
	 * @return remote server address
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Get remote server port number
	 * @return remote server port number
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Whether use compress with remote server communication
	 * @return compress status
	 */
	public boolean isUseCompress() {
		return useCompress;
	}

	/**
	 * connect to remote server
     * @throws  UnknownHostException if the IP address of the host could not be determined.
     * @throws  IOException  if an I/O error occurs when creating the socket.
	 */
	protected void connect() throws UnknownHostException, IOException {
		this.socket = new Socket(this.serverAddress, this.serverPort);
	}
	
	/**
	 * Send message packet to remote server
	 * @param packet      request message packet
     * @throws  IOException  if an I/O error occurs when sending data on the socket.
	 */
	protected void send(MessagePacket packet) throws IOException {
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		MessageProtocol.sendPacket(packet, outputStream);
	}
	
	/**
	 * Receive message packet from remote server
	 * @return  response message packet
     * @throws  IOException  if an I/O error occurs when receiving data on the socket.
	 */
	protected MessagePacket recv() throws IOException {
		DataInputStream inputStream =new DataInputStream(socket.getInputStream());
		MessagePacket packet = new MessagePacket();
		MessageProtocol.recvPacket(inputStream, packet,this.useCompress);
		return packet;
	}

}