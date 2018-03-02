package org.bidtime.pic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.bidtime.pic.bean.FilePicVO;
import org.bidtime.pic.bean.KeyValBean;
import org.bidtime.pic.bean.UploadParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlForm {
	
  private static final Logger log = LoggerFactory.getLogger(HtmlForm.class);
	
  @SuppressWarnings("unchecked")
  public static ResultDTO<List<FilePicVO>> parserToDTO(String ctx) {
		Map<String, UploadParam> mapParm = new LinkedHashMap<>();
		try {
			HtmlForm.parserToMap(ctx, mapParm);
			if (mapParm.isEmpty()) {
				return ResultDTO.error("无图片");
			} else {
				ResultDTO<List<FilePicVO>> dto = new ResultDTO<>();
				try {
					mapToDTO(mapParm, dto);
					return dto;
				} finally {
					dto = null;
				}
			}
		} finally {
			mapParm.clear();
			mapParm = null;
		}
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
	public static void parserToMap(String ctx, Map<String, UploadParam> mapUpladParam) {
		if ( StringUtils.isEmpty(ctx) ) {
			return;
		}
		String[] lines = ctx.split(Signal.WRAP);		// "\n"
		if (lines == null || lines.length<=0) {
			return;
		}
		for (String str: lines) {
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
						String fieldName = fieldKeyBean.getKey();	// file1
						String propName = fieldKeyBean.getVal();	// path
						// 从 map 中获取 对应 key 的 UploadParam
						UploadParam upBean = mapUpladParam.get(fieldName);
						if ( upBean == null ) {
							upBean = new UploadParam();
						}
						String propValue = nameVBean.getVal();			// /home/web/ul/store/1/0000005
						upBean.setPropValue(propName, propValue);
						mapUpladParam.put(fieldName, upBean);
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
	
	public static void mapToDTO(Map<String, UploadParam> map, ResultDTO<List<FilePicVO>> dto) {
		List<FilePicVO> list = new ArrayList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			list.add(new FilePicVO(entry.getKey(), entry.getValue().mergeArchvUrl()));
		}
		dto.setData(list);			
	}

	public static String getCtxOfReader(BufferedReader reader) throws ServletException,
			IOException {
		StringBuilder sb = new StringBuilder();
		String input = null;
		while ( ( input = reader.readLine() ) != null ) {
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
		String str = sb.toString();
		log.debug("content: {}", str);
		return str;
	}

//	public void setResponseString(HttpServletResponse r, String msg) {
//		try {
//			r.setCharacterEncoding("UTF-8");
//			r.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
//			r.setHeader("Pragma", "no-cache"); // HTTP 1.0
//			r.setDateHeader("Expires", 0); // prevents caching at the proxy
//											// server
//			r.setContentType("text/html;charset=UTF-8");
//			// set notLogin value
//			// write msg
//			r.getWriter().write(msg);
//			// flush buffer
//			r.flushBuffer();
//		} catch (IOException e) {
//			// log.error(e.getMessage());
//		}
//	}

}
