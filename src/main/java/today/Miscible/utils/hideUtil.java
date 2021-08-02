package today.Miscible.utils;

import java.util.ArrayList;
import java.util.List;


public class hideUtil {
	
	public static hideUtil the;
	
	private ArrayList<String> hideList = new ArrayList();
	private String name;

	
	public void remove(String index) {
		if(hideList.contains(index))
		hideList.remove(index);
	}
	
	
	public void add(String name) {
		if(!hideList.contains(name))
		hideList.add(name);
	}
	
	public void removeAll(List<String> list) {
		if(hideList.contains(list))
		hideList.removeAll(list);
	}
	
	public String[] getAllname() {
		String[] res = new String[hideList.size()];
		int index = 0;
		for (String string : hideList) {
		    res[index] = string;
		    index++;
		}
		return res;
	}
}
