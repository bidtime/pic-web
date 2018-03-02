package org.bidtime.pic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bidtime.pic.utils.HtmlForm;
import org.bidtime.pic.utils.ResponseUtils;
import org.bidtime.pic.utils.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jss
 * 
 */
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(UploadServlet.class);
	
	public UploadServlet() {
	  log.debug("upload servlet create...");
	}
	
	@Override
	public void destroy() {
    log.debug("upload servlet destroy.");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ResponseUtils.setSuccessResponse("ok", response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String ctx = HtmlForm.getCtxOfReader(request.getReader());
			ResultDTO<?> dto = HtmlForm.parserToDTO(ctx);
			ResponseUtils.setResponseResult(dto, response);
		} catch (ServletException e) {
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("upload servlet error", response);
		} catch (IOException e) {
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("upload io error", response);
		} catch (Exception e) {	
			log.error(e.getMessage());
			ResponseUtils.setErrorMsgResponse("upload error", response);
		}
	}

}
