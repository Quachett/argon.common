package uk.co.inc.argon.commons.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

public class ArgonHttpUtil {

	public Map<String, String> getRequestHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(SynapseConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        headers.put("Accept", MediaType.APPLICATION_JSON);
		return headers;
	}

	public Map<String, String> getRequestHeaders(String accept, String content, String authorization) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(SynapseConstants.CONTENT_TYPE, content);
        headers.put("Accept", accept);
        headers.put(SynapseConstants.AUTHORIZATION, authorization);
		return headers;
	}
}
