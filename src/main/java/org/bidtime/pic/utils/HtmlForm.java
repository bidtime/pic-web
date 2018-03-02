package org.bidtime.pic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.bidtime.pic.bean.FilePicVO;
import org.bidtime.pic.bean.KeyValBean;
import org.bidtime.pic.bean.FileParam;
import org.bidtime.pic.bean.UploadParam;
import org.bidtime.pic.params.GlobalConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlForm {
	
  private static final Logger log = LoggerFactory.getLogger(HtmlForm.class);
	
  @SuppressWarnings("unchecked")
public static ResultDTO<List<?>> parserToDTO(String ctx) throws Exception {
		Map<String, UploadParam> mapParm = new LinkedHashMap<>();
		try {
			HtmlForm.parserToMap(ctx, mapParm);
			if (mapParm.isEmpty()) {
				return ResultDTO.error("无上传文件");
			} else {
				return new ResultDTO<>(mapToList(mapParm));
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
	
	private static List<?> mapToList(Map<String, UploadParam> map) throws Exception {
		if (GlobalConst.getInstance().getOutPattern()==1) {
			return mapToListOutParam(map);
		} else {
			return mapToListPicVO(map);
		}
	}
	
	private static List<FilePicVO> mapToListPicVO(Map<String, UploadParam> map) {
		List<FilePicVO> list = new LinkedList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			FilePicVO bean = new FilePicVO(entry.getKey(), mergeArchvUrl(entry.getValue()));
			//
			list.add(bean);
		}
		return list;
	}
	
	private static List<FileParam> mapToListOutParam(Map<String, UploadParam> map) throws Exception {
		List<FileParam> list = new LinkedList<>();
		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
			UploadParam up = entry.getValue();
			//OutParam param = BeanUtils.copyProps(entry.getValue(), OutParam.class);
			FileParam param = new FileParam();
			param.setIndex(entry.getKey());
			param.setContent_type(up.getContent_type());
			param.setMd5(up.getMd5());
			param.setName(up.getName());
			param.setSize(up.getSize());
			//
			String url = mergeArchvUrl(entry.getValue());
			param.setUrl(url);
			//
			list.add(param);
		}
		return list;
	}
	
//	public static void mapToDTO(Map<String, UploadParam> map, ResultDTO<List<FilePicVO>> dto) {
//		List<FilePicVO> list = new ArrayList<>();
//		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
//			list.add(new FilePicVO(entry.getKey(), mergeArchvUrl(entry.getValue())));
//		}
//		dto.setData(list);			
//	}
//	
//	public static void mapToDTO(Map<String, UploadParam> map, ResultDTO<List<OutParam>> dto) throws Exception {
//		List<OutParam> list = new ArrayList<>();
//		for (Map.Entry<String, UploadParam> entry : map.entrySet()) {
//			OutParam param = new OutParam();
//			BeanUtils.copyProps(entry.getValue(), param);
//			String url = mergeArchvUrl(entry.getValue());
//			param.setUrl(url);
//		}
//		dto.setData(list);			
//	}
	
	public static String mergeArchvUrl(UploadParam bean) {
		return mergeArchvUrl(bean.getPath(), bean.getName());
	}
	
	public static String mergeArchvUrl(String path, String name) {
		return GlobalConst.getInstance().mergeArchvUrl(path, name);
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
