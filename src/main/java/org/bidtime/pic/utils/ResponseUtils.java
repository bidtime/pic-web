/**
 * 
 */
package org.bidtime.pic.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author jss
 * 
 *         提供对Response的操作,往Response输出各种对象
 *
 */
public class ResponseUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(ResponseUtils.class);

	public static void setSuccessResponse(String msg,
			HttpServletResponse r) {
		setStateMsgResponse(UserHeadState.SUCCESS, msg, r);
	}

	public static void setSuccessResponse(HttpServletResponse r) {
		setStateMsgResponse(UserHeadState.SUCCESS, "", r);
	}

	public static void setErrorMsgResponse(String msg,
			HttpServletResponse r) {
		setStateMsgResponse(UserHeadState.ERROR, msg, r);
	}

	public static void setErrorMsgResponse(Exception e, String msg, 
			HttpServletResponse r) {
		if (logger.isErrorEnabled()) {
			logger.error("setError", e);
		}
		if (e instanceof Exception) {
			setErrorMsgResponse(e.getLocalizedMessage(), r);
		} else {
			setErrorMsgResponse(msg, r);
		}
	}

//	public static void setErrorMsgResponse(Exception e,
//			HttpServletResponse r) {
//		if (logger.isErrorEnabled()) {
//			logger.error("setError", e);
//		}
//		setStateMsgResponse(UserHeadState.ERROR, e.getLocalizedMessage(), r);
//	}

	public static void setStateMsgResponse(int n, String msg,
			HttpServletResponse r) {
		ResultDTO<Object> d = new ResultDTO<Object>();
		d.setSuccess( n >= 1 ? true : false );
		setResponseResultString(JSON.toJSONString(d), r);
	}

	@SuppressWarnings("rawtypes")
	public static void setResponseResult(ResultDTO d, HttpServletResponse r) {
		if (d == null) {
			return;
		}
		setResponseResultString(JSON.toJSONString(d), r);
	}

	public static void setResponseResultObject(Object o, HttpServletResponse r) {
		if (o == null) {
			return;
		}
		setResponseResultString(o.toString(), r);
	}

	/**
	 * @param sReturn
	 * @param r
	 */
	public static void setResponseResultString(String s, HttpServletResponse r) {
		try {
			r.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
			r.setHeader("Pragma", "no-cache"); // HTTP 1.0
			r.setDateHeader("Expires", 0); // prevents caching at the proxy server
			r.setCharacterEncoding("UTF-8");
			r.setContentType("application/json;charset=UTF-8");
			// write string
			r.getWriter().write(s);
			// flush buffer
			r.flushBuffer();
			if (logger.isDebugEnabled()) {
				logger.debug(s);
			}
		} catch (IOException e) {
			logger.error("setResponse", e);
		}
	}

}