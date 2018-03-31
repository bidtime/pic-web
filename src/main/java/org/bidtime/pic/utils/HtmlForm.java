package org.bidtime.pic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.bidtime.pic.bean.KeyValBean;
import org.bidtime.pic.bean.UploadParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlForm {
	
  private static final Logger log = LoggerFactory.getLogger(HtmlForm.class);

	private static String getCtxOfReader(BufferedReader reader) throws ServletException,
			IOException {
		//StringBuilder raw = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		String input = null;
		while ( ( input = reader.readLine() ) != null ) {
			//raw.append(input);
			//raw.append("\n");
			if ( StringUtils.isEmpty(input) || input.startsWith(Signal.DBL_POLE) ) {
				continue;
			}
			if (input.startsWith(Signal.Content_Disposit)) {
				if (sb.length() > 0) {
					sb.append(Signal.WRAP);			// "\n"					
				}
				String tmp = input.substring(Signal.Content_Disposit.length(), input.length());
				sb.append( tmp.trim() );
			} else {
				sb.append(Signal.TAB);			// "\t"
				sb.append( input.trim() );
			}
		}
		//String tmp = raw.toString();
		//log.debug("raw: {}", tmp);
		String str = sb.toString();
		log.debug("content: {}", str);
		return str;
	}

	public static Map<String, UploadParam> getMapOfReader(BufferedReader reader) throws ServletException,
			IOException {
		Map<String, UploadParam> mapParam = new LinkedHashMap<>();
		String ctx = HtmlForm.getCtxOfReader(reader);
		HtmlForm.parserToMap(ctx, mapParam);
		return mapParam;
	}
	
	/*
	 * Content-Disposition: form-data; name="file1.name" 1-1.txt
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM 
	 * Content-Disposition: form-data; name="file1.content_type" text/plain 
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM
	 * Content-Disposition: form-data; name="file1.path" /home/web/pic/upload/2/0000000012
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM
	 * Content-Disposition: form-data; name="file1.md5" ab0393478db32f6c795ce2668c2cbb86
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM
	 * Content-Disposition: form-data; name="file1.size" 890 
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM 
	 * Content-Disposition: form-data; name="submit" Upload
	 * ------WebKitFormBoundaryLOf2Rcda7rJ8bSpM--
	 */
	private static void parserToMap(String ctx, Map<String, UploadParam> mapUpladParam) {
		if ( StringUtils.isEmpty(ctx) ) {
			return;
		}
		String[] lines = ctx.split(Signal.WRAP);		// "\n"
		if (lines == null || lines.length<=0) {
			return;
		}
		for (String str : lines) {
			log.debug("line: {}", str);
			// name="file1.path" #9 /home/web/ul/store/1/0000005
			KeyValBean nameVBean = KeyValBean.indexOfSplt(str, Signal.TAB);	// "\t"
			if (nameVBean == null) {
				continue;
			}
			try {
				String strNameFieldKey = nameVBean.getKey();			// name="file1.path"
				String fieldKeyStr = KeyValBean.getRightStrOfSplt(strNameFieldKey, Signal.EQUAL);		// =
				KeyValBean fieldKeyBean = getSplitNameKeyVal( fieldKeyStr );							// file1 和 path
				try {
					if ( fieldKeyBean != null ) {				
						String key = fieldKeyBean.getKey();	// file1
						String val = fieldKeyBean.getVal();	// path
						// 从 map 中获取 对应 key 的 UploadParam
						UploadParam upBean = mapUpladParam.get(key);
						if ( upBean == null ) {
							upBean = new UploadParam();
						}
						String propValue = nameVBean.getVal();			// /home/web/ul/store/1/0000005
						upBean.setPropValue(val, propValue);
						mapUpladParam.put(key, upBean);
					}
				} finally {
					fieldKeyBean = null;
				}
			} finally {
				nameVBean = null;
			}
		}
	}
	
	/*
	 * "file1.path"
	 */
	private static KeyValBean getSplitNameKeyVal(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		// 判断是否有 "\""
		String value = ( str.startsWith(Signal.QUOTA) && str.endsWith(Signal.QUOTA) ) ? str.substring(1, str.length() - 1) : str;
		if ( StringUtils.isEmpty(value) ) {
			return null;
		} else {
			KeyValBean keyBean = KeyValBean.indexOfSplt(value, Signal.DOT);	// '.'
			return keyBean;
		}
	}

}
