package com.jiuyi.frame.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jiuyi.frame.util.StringUtil;

public class LoggingFilter extends OncePerRequestFilter {

	protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	private static final String REQUEST_PREFIX = "Request: ";
	private static final String RESPONSE_PREFIX = "Response: ";
	private AtomicLong id = new AtomicLong(1);

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private JsonParser parser = new JsonParser();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (logger.isDebugEnabled()) {
			long requestId = id.incrementAndGet();
			request = new RequestWrapper(requestId, request);
			response = new ResponseWrapper(requestId, response);
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			if (logger.isDebugEnabled()) {
				logRequest(request);
				logResponse((ResponseWrapper) response);
			}
		}

	}

	private void logRequest(HttpServletRequest request) {
		StringBuilder msg = new StringBuilder();
		msg.append(REQUEST_PREFIX);
		if (request instanceof RequestWrapper) {
			msg.append("request id=").append(((RequestWrapper) request).getId()).append("; ");
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			msg.append("session id=").append(session.getId()).append("; ");
		}
		msg.append("content type=").append(request.getContentType()).append("; ");
		msg.append("uri=").append(request.getRequestURI());
		msg.append('?').append(request.getQueryString());

		if (request instanceof RequestWrapper && !isMultipart(request)) {
			RequestWrapper requestWrapper = (RequestWrapper) request;
			try {
				String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() : "UTF-8";
				msg.append("; payload=").append(new String(requestWrapper.toByteArray(), charEncoding));
			} catch (UnsupportedEncodingException e) {
				logger.warn("Failed to parse request payload", e);
			}

		}
		logger.debug(msg.toString());
	}

	private boolean isMultipart(HttpServletRequest request) {
		return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
	}

	private void logResponse(ResponseWrapper response) {
		StringBuilder msg = new StringBuilder();
		msg.append(RESPONSE_PREFIX);
		msg.append("request id=").append((response.getId()));
		try {
			String contentLenStr = response.getHeader("Content-Length");
			Long contentLen = StringUtil.isNullOrEmpty(contentLenStr) ? 0 : Long.parseLong(response.getHeader("Content-Length"));
			String payload = "content is so long。。。";
			if (contentLen < 1024) {
				String content = new String(response.toByteArray(), response.getCharacterEncoding());
				String contentType = response.getContentType();
				if (contentType != null && contentType.contains("json")) {
					JsonElement el = parser.parse(content);
					content = gson.toJson(el);
				}
				payload = content.length() < 4096 ? content : payload;
			}
			msg.append("; payload=").append(payload);
		} catch (UnsupportedEncodingException e) {
			logger.warn("Failed to parse response payload", e);
		}
		logger.debug(msg.toString());
	}
}
