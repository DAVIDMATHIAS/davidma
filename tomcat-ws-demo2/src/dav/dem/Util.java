package dav.dem;

import java.io.StringReader;
import java.util.Properties;

public class Util {
	public static void assume(boolean stmt,String... msg){
		String errMSG=null;
		if(msg!=null && msg.length>0){
			errMSG = msg[0];
		}
		if(!stmt){
			throw new IllegalArgumentException(errMSG);
		}
	}
	public static Properties messageProperties(String message){
		Properties p = new Properties();
		try{
		p.load(new StringReader(message));
		}catch(Exception e){
			e.printStackTrace();
		}
		return p;
	}
	
	public static void main(String args[]){
		String message=Constants.BROADCAST+"=true\n"+Constants.FROM+"=raju\n"+Constants.MESSAGE+"=Hello David\n";
		String prp = (String) messageProperties(message).get(Constants.MESSAGE);
		System.out.println(prp);
		
	}
}
