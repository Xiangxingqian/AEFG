package entryPointCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CallBacks {
	private List<String> callbacks;
	public CallBacks() {
		callbacks = new ArrayList<String>();
		File file = new File("AndroidCallbacks.txt");
		String s;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while((s=br.readLine())!=null){
				callbacks.add(s);
			}
			fr.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<String> getCallbacks() {
		return callbacks;
	}
	public void setCallbacks(List<String> callbacks) {
		this.callbacks = callbacks;
	}
	
	public static void main(String...strings ){
		CallBacks callBacks2 = new CallBacks();
		System.out.println(callBacks2.callbacks);
	}
	
}
