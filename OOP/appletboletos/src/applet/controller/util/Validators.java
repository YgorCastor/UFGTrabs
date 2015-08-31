package applet.controller.util;

public class Validators {
	
	public static int stringToInt(String param) {
		try {
			return Integer.valueOf(param);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}
