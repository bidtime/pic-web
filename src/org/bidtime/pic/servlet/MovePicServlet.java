package org.bidtime.pic.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.bidtime.pic.params.GlobalConst;
import org.bidtime.pic.utils.RequestUtils;
import org.bidtime.pic.utils.ResponseUtils;
import org.bidtime.pic.utils.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jss
 * 
 */
@SuppressWarnings("serial")
public class MovePicServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(MovePicServlet.class);

	public MovePicServlet() {
	  log.debug("mv pic servlet create...");
	}
	  
	@Override
	public void destroy() {
	    log.debug("mv pic servlet destroy.");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ResponseUtils.setSuccessResponse("ok", response);
	}

	@Override
	protected void doPost(HttpServletRequest request,	HttpServletResponse response) {
		try {
			doIt(request, response);
		} catch (ServletException e) {
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("move servlet error", response);
		} catch (IOException e) {
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("move io error", response);
		} catch (Exception e) {	
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("move error", response);
		}
	}

	/*
	 * http://localhost:8080/mv
	   archvUrl:/home/web/pic/upload/store/8/15e199c3c8b244fca81153320b8b062e.txt
	   subDir:erpPic
	   hashStore:true
	 */
	private void doIt(HttpServletRequest request,
			HttpServletResponse response)  throws ServletException, IOException {
		String picArchvUrl = RequestUtils.getString(request, "archvUrl");
		String subDir = RequestUtils.getString(request, "subDir");
		if (StringUtils.isNotEmpty(subDir)) {
			char last = subDir.charAt(subDir.length() -1);
			if (last != '/') {
				subDir = subDir + '/';
			}
		}
		Boolean hashStore = Boolean.parseBoolean(RequestUtils.getString(request, "hashStore"));
		Map<String, String> map = getRetMap(picArchvUrl, subDir, hashStore);
		//
		ResultDTO<Map<String, String>> dto = new ResultDTO<>();
		dto.setData(map);
		ResponseUtils.setResponseResult(dto, response);
	}
	
	private Map<String, String> getRetMap(String picArchvUrl, String subDir, Boolean hashStore) {
		String picWebUrl = GlobalConst.getInstance().mergeWebUrl(picArchvUrl, subDir, hashStore);
		Map<String, String> map = new HashMap<>(1);
		map.put("webUrl", picWebUrl);
		return map;
	}

}
