package org.bidtime.pic.bean;

import org.bidtime.pic.utils.FileMv;
import org.bidtime.pic.utils.PropEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebFileParam {
	
	private final Logger log = LoggerFactory.getLogger(WebFileParam.class);

	protected String flag;
	
	public WebFileParam(String flag) {
		this.flag = flag;
	}
	
	public void setProp(PropEx p) {
		String keyWebUrl = getFlag() + "Url";
		webUrl = p.getProperty(keyWebUrl);
		log.debug(keyWebUrl + ":" + webUrl);
		//
		String keyWebRoot = getFlag() + "Root";
		webRoot = p.getProperty(keyWebRoot);
		log.debug(keyWebRoot + ":" + webRoot);
	}

	protected String getFlag() {
		return flag;
	}
	
	protected String webUrl;

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	protected String webRoot;

	public static String getExt(String filename, String defExt, boolean includDot) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length() - 1))) {
				if (includDot) {
					return filename.substring(i);
				} else {
					return filename.substring(i + 1);					
				}
			}
		}
		return defExt;
	}
	
//	private String changeFilePath(String oldFile, String archvRoot, String subDir) {
//		String retFile = null;
//		int pos = oldFile.indexOf(archvRoot);
//		if ( pos >= 0) {
//			String tmpFile = oldFile.substring(pos + archvRoot.length(), oldFile.length());
//			int pp = tmpFile.indexOf('/');
//			if ( pp >= 0 ) {
//				String tmp = tmpFile.substring( pp, tmpFile.length() );
//				if ( StringUtils.isEmpty(subDir) ) {
//					retFile = archvRoot + tmp;
//				} else {
//					retFile = archvRoot + subDir + "/" + tmp;
//				}
//			}
//		} else {
//			retFile = oldFile;
//		}
//		return retFile;
//	}

	private String mergeNewFile(String archFullFile, String archSubFile, String subDir, StringBuilder sb) {
		String oldFile = archFullFile;
		sb.append(", ");
		sb.append("oldFile: " + oldFile);
		String newFile = webRoot + subDir + archSubFile;
		//String newFile = oldFile.replace(archvRoot, webRoot);
		sb.append(", ");
		sb.append("newFile: " + newFile);
		// rm file
		boolean b = FileMv.moveFile(oldFile, newFile, true);
		sb.append(", ");
		sb.append("mv: " + b);
		return newFile;
	}
	
	public String mergeWebUrl(String archFullFile, String archSubFile, String subDir) {
		StringBuilder sb = new StringBuilder();
		try {
			String fileName = mergeNewFile(archFullFile, archSubFile, subDir, sb);
			String url = fileName.replace(webRoot, webUrl);
			sb.append(", ");
			sb.append("url: " + url);
			log.debug(sb.toString());
			return url;
		} finally {
			sb.setLength(0);
			sb = null;
		}
	}

//	public String gerPathFileOfUrl(String url) {
//		String file = url.replace(webUrl, webRoot);
//		return file;
//	}
	
	public KeyValBean splitRoot_url(String url) {
		int idx = url.indexOf(webUrl);
		if ( idx >= 0 ) {
			String subDir = url.substring(idx + webUrl.length());
//			if (hashStore) {
//				int pos = subDir.indexOf("/");
//				if (pos >= 0) {
//					subDir = subDir.substring(pos + 1);
//				}
//			}
			String retFilePath = webRoot + subDir;
			return new KeyValBean(retFilePath, subDir);
		} else {
			return null;
		}
	}

}
