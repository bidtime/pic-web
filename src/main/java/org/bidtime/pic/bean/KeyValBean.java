package org.bidtime.pic.bean;

public class KeyValBean {
	
	public KeyValBean(String key, String val) {
		this.key = key;
		this.val = val;
	}
	
//	public KeyValBean(String str, char splt) {
//		indexOfSplt(str, splt);
//	}

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	private String val;
	
	public static KeyValBean indexOfSplt(String str, char splt) {
		int pos = str.indexOf(splt);
		if (pos<0) {
			return null;
		}
		String key = str.substring(0, pos);
		String val = str.substring(pos + 1, str.length());
		return new KeyValBean(key, val);
	}
	
	public static KeyValBean indexOfSplt(String str, String splt) {
		int pos = str.indexOf(splt);
		if (pos<0) {
			return null;
		}
		String key = str.substring(0, pos);
		String val = str.substring(pos + 1, str.length());
		return new KeyValBean(key, val);
	}

	/*
	param: str
		Content-Disposition: form-data; name="key.name"
	return:
		name="key.name
	*/
	public static String getRightStrOfSplt(String str, char splt) {
		int pos = str.indexOf(splt);
		if (pos<0) {
			return null;
		}
		String s = str.substring(pos + 1, str.length());
		return s;
	}

	public static String startsWithSub(String str, String splt) {
		if ( str.startsWith(splt) ) {
			String tmp = str.substring(splt.length(), str.length());
			return tmp;
		} else {
			return str;
		}
	}

	public static String endsWithSub(String str, String splt) {
		if ( str.endsWith(splt) ) {
			String tmp = str.substring(0, str.length() - splt.length());
			return tmp;
		} else {
			return str;
		}
	}

}
