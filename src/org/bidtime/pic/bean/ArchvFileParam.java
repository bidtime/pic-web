package org.bidtime.pic.bean;

import java.util.UUID;

import org.bidtime.pic.utils.FileMv;
import org.bidtime.pic.utils.PropEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchvFileParam extends WebFileParam {

	private final Logger log = LoggerFactory.getLogger(WebFileParam.class);

	public ArchvFileParam(String flag) {
		super(flag);
	}

	public void setProp(PropEx p) {
		super.setProp(p);
		String key = getFlag() + "Store";
		storeRoot = p.getProperty(key);
		log.debug(key + ":" + storeRoot);
	}

	private String storeRoot;

	public String getStoreRoot() {
		return storeRoot;
	}

	public void setStoreRoot(String storeRoot) {
		this.storeRoot = storeRoot;
	}
	
  /**
   * 获得一个UUID
   * 
   * @return String UUID
   */
  public static String getUUID(boolean bDelMinus) {
    String s = UUID.randomUUID().toString();
    // 去掉“-”符号
    if (bDelMinus) {
      StringBuilder sb = new StringBuilder();
      sb.append(s.substring(0, 8));
      sb.append(s.substring(9, 13));
      sb.append(s.substring(14, 18));
      sb.append(s.substring(19, 23));
      sb.append(s.substring(24));
      return sb.toString();
    } else {
      return s;
    }
  }

	private String changeFileName(String oldFile, String extName) {
		String tmpFile = oldFile.replace(storeRoot, webRoot);
		int pos = tmpFile.lastIndexOf('/');
		StringBuilder sb = new StringBuilder();
		try {
			if (pos >= 0) {
				String befPos = tmpFile.substring(0, pos + 1);
				sb.append(befPos);
				sb.append(getUUID(true));
			} else {
				sb.append(tmpFile);
			}
			sb.append(extName);
			return sb.toString();
		} finally {
			sb.setLength(0);
			sb = null;
		}
	}

	private String mergeNewFile(String oldFile, String fOnlyName,
			StringBuilder sb) {
		sb.append(", ");
		sb.append("oldFile: " + oldFile);
		String newFile = changeFileName(oldFile, getExt(fOnlyName, "", true));
		sb.append(", ");
		sb.append("newFile: " + newFile);
		// rm file
		boolean b = FileMv.moveFile(oldFile, newFile);
		sb.append(", ");
		sb.append("mv: " + b);
		return newFile;
	}

	public String mergeArchvUrl(String pathFile, String fOnlyName) {
		StringBuilder sb = new StringBuilder();
		try {
			String fileName = mergeNewFile(pathFile, fOnlyName, sb);
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

}
