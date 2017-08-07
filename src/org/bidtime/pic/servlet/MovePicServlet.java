package org.bidtime.pic.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bidtime.pic.params.GlobalConst;
import org.bidtime.pic.utils.MovePicDTO;
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
		  MovePicDTO movePic = paserToDTO(request);
		  ResultDTO<Map<String, String>> dto = getRetMap(movePic);
	    ResponseUtils.setResponseResult(dto, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("move error", response);
		}
	}
  
  private MovePicDTO paserToDTO(HttpServletRequest request) throws Exception {
    MovePicDTO dto = new MovePicDTO();
    String[] archvUrls = request.getParameterValues("archvUrl");
    dto.setArchvUrl(archvUrls);
    if (!ArrayUtils.isEmpty(archvUrls)) {
      String subDir = RequestUtils.getString(request, "subDir", null);
      if (StringUtils.isNotEmpty(subDir)) {
        char last = subDir.charAt(subDir.length() -1);
        if (last != '/') {
          subDir = subDir + '/';
        }
      }
      dto.setSubDir(subDir);
      Boolean hashStore = Boolean.parseBoolean(RequestUtils.getString(request, "hashStore"));
      dto.setHashStore(hashStore);
    }
    return dto;
  }
  

  /*
   * http://localhost:8080/mv
     archvUrl:/home/web/pic/upload/store/8/15e199c3c8b244fca81153320b8b062e.txt
     subDir:erpPic
     hashStore:true
   */
  private ResultDTO<Map<String, String>> getRetMap(MovePicDTO movePic) throws Exception {
    String[] picArchvUrls = movePic.getArchvUrl();
    if (ArrayUtils.isEmpty(picArchvUrls)) {
      ResultDTO<Map<String, String>> rst = new ResultDTO<>();
      rst.setSuccess(false);
      rst.setMsg("无图片");
      return rst;
    }
    
    String subDir = movePic.getSubDir();
    Boolean hashStore = movePic.getHashStore();
    Map<String, String> map = new HashMap<>(picArchvUrls.length);
    for (String picArchvUrl : picArchvUrls) {
      String picWebUrl = GlobalConst.getInstance().mergeWebUrl(picArchvUrl, subDir, hashStore);
      map.put(picArchvUrl, picWebUrl);
    }
    ResultDTO<Map<String, String>> dto = new ResultDTO<>();
    dto.setData(map);
    return dto;
  }

}
