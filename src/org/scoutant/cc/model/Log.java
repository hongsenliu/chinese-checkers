//package org.scoutant.cc.model;
package org.scoutant.cc.model;
import java.util.List;

public class Log {
	
	public static void e(String tag, String msg) {
		d(tag, msg);
	}

	public static void d(String tag, String msg) {
		System.out.println(msg);
	}

	public static void d(String tag, Object o) {
		d(tag, ""+o);
	}

	public static void d(String tag, List<?> list) {
		String str = "";
		for (Object o : list) {
			str += o + ", ";
		}
		str += "\n";
		d(tag, str);
	}
	
}
