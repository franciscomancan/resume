package com.cs.stadosweb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Used for secured (custom) connections from a mobile
 * client, necessary for transmitting very sensitive information
 * across the wifi network.  The assumption is that the network
 * is unknown and potentially unprotected.
 */
public class SecureEndpoint extends HttpServlet {

	private static final long serialVersionUID = 8092622954947744924L;
	private static final String INBOUND_CIPHER_KEY = "incip", OUTBOUND_CIPHER_KEY = "outcip";
	
	@Override
	public void init() throws ServletException {
		
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		StreamCipher inCipher = (StreamCipher)session.getAttribute(INBOUND_CIPHER_KEY);
		StreamCipher outCipher = (StreamCipher)session.getAttribute(OUTBOUND_CIPHER_KEY);
		
		if(inCipher == null || outCipher == null) {
			inCipher = new RC4Engine();
			outCipher = new RC4Engine();
			
			inCipher.init(true, new KeyParameter("from the stados client".getBytes()));
			outCipher.init(false, new KeyParameter("to the stados client".getBytes()));
			
			session.setAttribute(INBOUND_CIPHER_KEY, inCipher);
			session.setAttribute(OUTBOUND_CIPHER_KEY, OUTBOUND_CIPHER_KEY);
		}
		
		String clientHex = req.getParameter("message");
		byte[] cipherTxt = HexCodec.hexToBytes(clientHex);
		byte[] plainTxt = new byte[cipherTxt.length];
		inCipher.processBytes(cipherTxt, 0, cipherTxt.length, plainTxt, 0);
		
		String response = "something seems to be working if this is received";
		plainTxt = response.getBytes();
		cipherTxt = new byte[plainTxt.length];
		outCipher.processBytes(plainTxt, 0, plainTxt.length, cipherTxt, 0);
		char[] hexOut = HexCodec.bytesToHex(cipherTxt);
		
		resp.setContentType("text/plain");
		resp.setContentLength(hexOut.length);
		PrintWriter writer = resp.getWriter();
		writer.print(hexOut);
		
	}
	
}
