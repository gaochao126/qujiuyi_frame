package com.jiuyi.frame.filter;

import org.apache.commons.io.input.TeeInputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;

public class RequestWrapper extends HttpServletRequestWrapper {

	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private long id;

	public RequestWrapper(Long requestId, HttpServletRequest request) {
		super(request);
		this.id = requestId;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			private TeeInputStream tee = new TeeInputStream(RequestWrapper.super.getInputStream(), bos);

			@Override
			public int read() throws IOException {
				return tee.read();
			}
		};
	}

	public byte[] toByteArray() {
		return bos.toByteArray();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
