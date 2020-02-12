package org.flowable.ui.idm.rest.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.ui.idm.model.SSOUserInfo;
import org.springframework.util.MultiValueMap;

public interface SSOHandler {

    boolean isActive();
    SSOUserInfo handleSsoReturn(HttpServletRequest request, HttpServletResponse response, MultiValueMap<String, String> body);
    String getExternalUrl(String idmUrl);

}