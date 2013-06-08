package dav.dem;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class WSServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private static ArrayList<WSInbound> noNameConnections = new ArrayList<WSInbound>();
	private static Map<WSInbound, String> namedConnections = new HashMap<WSInbound, String>();

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest arg1) {
		System.out.println("Inbound websocket created..");
		return new WSInbound();
	}

	class WSInbound extends MessageInbound {
		WsOutbound myoutbound;

		@Override
		public void onOpen(WsOutbound outbound) {
			try {
				System.out.println("Open Client.");
				this.myoutbound = outbound;
				noNameConnections.add(this);
				outbound.writeTextMessage(CharBuffer.wrap("Hello!"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onClose(int status) {
			System.out.println("Close Client.");
			noNameConnections.remove(this);
		}

		private CharBuffer formMessage(Properties prp) {
			String name = null;
			String message = null;
			CharBuffer cb = null;
			if (namedConnections.keySet().contains(this)) {
				name = namedConnections.get(this);
				message = name + " : " + prp.getProperty(Constants.MESSAGE);
			} else {
				message = null;
			}
			if (message != null) {
				cb = CharBuffer.allocate(message.length());
				for (int i = 0; i < message.length(); i++) {
					cb.put(message.charAt(i));
				}
			}
			return cb;
		}

		private void broadCast(CharBuffer msgBuffer)  throws IOException{
			if (msgBuffer != null) {
				for (WSInbound mmib : noNameConnections) {
					CharBuffer buffer = CharBuffer.wrap(msgBuffer);
					mmib.myoutbound.writeTextMessage(buffer);
					mmib.myoutbound.flush();
				}
			}
		}

		@Override
		public void onTextMessage(CharBuffer cb) throws IOException {
			String rawMessage = cb.toString();
			Properties msgPrp = Util.messageProperties(rawMessage);
			if (((String) msgPrp.get(Constants.BROADCAST)) != null) {
				CharBuffer msgBuffer = formMessage(msgPrp);
				broadCast(msgBuffer);
			} else {

			}
		}

		@Override
		public void onBinaryMessage(ByteBuffer bb) throws IOException {

			for (WSInbound mmib : noNameConnections) {
				if (mmib.myoutbound == this.myoutbound) {
					System.out
							.println("Binary data returned to the same connection");
				}
				mmib.myoutbound.writeBinaryMessage(bb);

				mmib.myoutbound.flush();
			}
		}
	}

}
