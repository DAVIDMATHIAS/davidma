package dav.dem;


public class PrivateChat {
	private Client from;
	private Client to;
	private String type;
	public PrivateChat(Client from, Client to, String type){
		this.from = from;
		this.to = to;
		this.type = type;
	}
	public Client getFrom() {
		return from;
	}
	public void setFrom(Client from) {
		this.from = from;
	}
	public Client getTo() {
		return to;
	}
	public void setTo(Client to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public boolean equals(Object obj) {
		PrivateChat that = (PrivateChat)obj;
		return that.from.equals(this.from) && that.to.equals(this.to);
	}
	
	@Override
	public int hashCode() {
		return from.hashCode()+to.hashCode();
	}
}
