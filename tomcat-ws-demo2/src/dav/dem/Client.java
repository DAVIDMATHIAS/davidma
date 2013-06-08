package dav.dem;
import dav.dem.WSServlet.WSInbound;
public class Client {
	private String name;
	private WSInbound connection;
	public Client(){
		
	}
	public Client(String name, WSInbound wsInbound){
		this.name = name;
		this.connection = wsInbound;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WSInbound getConnection() {
		return connection;
	}
	public void setConnection(WSInbound connection) {
		this.connection = connection;
	}
	@Override
	public boolean equals(Object obj) {
		Client that = (Client)obj;
		return (that.connection==this.connection && that.name.equals(this.name));
	}
	
	@Override
	public int hashCode() {
		return connection.hashCode()+name.hashCode();
	}
}
