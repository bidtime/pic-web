package org.bidtime.pic.params;

import org.bidtime.pic.bean.ArchvFileParam;
import org.bidtime.pic.bean.KeyValBean;
import org.bidtime.pic.bean.WebFileParam;
import org.bidtime.pic.utils.PropEx;

public class GlobalConst {

	// static {
	// instance = new GlobalConst();
	// }
	
	private WebFileParam webFile = new WebFileParam("web");
	private ArchvFileParam archvFile = new ArchvFileParam("archv");
	private Integer outPattern;
	private Boolean fdfs;

	private volatile static GlobalConst instance = null;

	public static GlobalConst getInstance() {
		if (instance == null) {
			synchronized (GlobalConst.class) {
				if (instance == null) {
					instance = new GlobalConst();
				}
			}
		}
		return instance;
	}

	public GlobalConst() {
		initial();
	}

	private void initial() {
		PropEx p = new PropEx();
		try {
			p.loadOfSrc("globalConst.properties");
			webFile.setProp(p);
			archvFile.setProp(p);
			//
			outPattern = Integer.parseInt(p.getString("outPattern"));
			//
			fdfs = Boolean.parseBoolean(p.getString("fdfs"));
		} finally {
			p = null;
		}
	}

//	public String mergeArchvUrl(String pathFile, String fOnlyName) {
//		return archvFile.mergeArchvUrl(pathFile, fOnlyName);
//	}

	public String mergeArchvUrl(String fileName) {
		return archvFile.mergeArchvUrl(fileName);
	}

	public String renameFile(String pathFile, String fOnlyName) {
		return archvFile.renameFile(pathFile, fOnlyName);
	}

	public String mergeWebUrl(String pathFile, String subDir, Boolean hashStore) {
		KeyValBean kv = archvFile.splitRoot_url(pathFile);
		if ( kv != null ) {
			String archFullFile = kv.getKey();		//archvFile.getWebRoot();
			String archSubFile = kv.getVal();		//archvFile.gerPathFileOfUrl(pathFile);
			if (hashStore) {
				int pos = archSubFile.indexOf("/");
				if (pos >= 0) {
					String tmp = archSubFile.substring(pos + 1);
					archSubFile = tmp;
				}
			}
			return webFile.mergeWebUrl(archFullFile, archSubFile, subDir);			
		} else {
			return "";
		}
	}

	public Integer getOutPattern() {
		return outPattern;
	}

	public void setOutPattern(Integer outPattern) {
		this.outPattern = outPattern;
	}

	public Boolean getFdfs() {
		return fdfs;
	}

	public void setFdfs(Boolean fdfs) {
		this.fdfs = (fdfs==null) ? false : fdfs;
	}

}
