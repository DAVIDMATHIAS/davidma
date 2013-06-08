package dav.dem;

import java.util.HashMap;
import java.util.Map;

public class PrivateChatManager {
	private static PrivateChatManager instance = new PrivateChatManager();
	public static PrivateChatManager getInstance(){
		if(instance==null){
			instance = new PrivateChatManager();
		}
		return instance;
	}
	private Map<String, PrivateChat> chats;
	private PrivateChatManager(){
		chats = new HashMap<String,PrivateChat>();
	}
	public void addChat(Client from, Client to,String type) {
		PrivateChat pChat = new PrivateChat(from, to , type);
		addChat(pChat);
	}

	public void addChat(PrivateChat pChat) {
		String from = pChat.getFrom().getName();
		String to = pChat.getTo().getName();
		addChat(from, to, pChat);
	}
	public void addChat(String from, String to, PrivateChat pChat){
		String key= from+Constants.HEADER_TERMINATOR+to+Constants.HEADER_TERMINATOR+pChat.getType();
		chats.put(key,pChat);
	}
	public void addChat(String from,String to, Client fromClient, Client toClient, String type){
		Util.assume(from!=null && to!=null,"Client name is null");
		PrivateChat pChat = new PrivateChat(fromClient,toClient, type);
		addChat(from,to,pChat);
		
	}
	public PrivateChat getPrivateChat(String from, String to, String type){
		Util.assume(from!=null && to!=null,"Client name is null");
		return chats.get(from+Constants.HEADER_TERMINATOR+to+Constants.HEADER_TERMINATOR+type);
	}
	
	public PrivateChat getPrivateChat(Client from, Client to, String type){
		String fromName = from.getName();
		String toName = to.getName();
		return getPrivateChat(fromName, toName, type);
	}
	
}
