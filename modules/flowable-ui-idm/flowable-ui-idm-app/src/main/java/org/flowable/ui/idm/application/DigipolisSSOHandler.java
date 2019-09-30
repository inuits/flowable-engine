package org.flowable.ui.idm.application;

import java.util.AbstractMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.ui.idm.model.SSOUserInfo;
import org.flowable.ui.idm.rest.app.SSOHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class DigipolisSSOHandler implements SSOHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(DigipolisSSOHandler.class);

    @Value("${flowable.sso.digipolis.url.auth:#{null}}")
    private String ssoExternalUrl;

    @Value("${flowable.sso.digipolis.client-id:#{null}}")
    private String ssoClientId;

    @Value("${flowable.sso.digipolis.client-secret:#{null}}")
    private String ssoClientSecret;

    @Value("${flowable.sso.digipolis.url.token:#{null}}")
    private String ssoTokenUrl;

    @Value("${flowable.sso.digipolis.url.info:#{null}}")
    private String ssoUserInfoUrl;

    private String redirectUri;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public SSOUserInfo handleSsoReturn(HttpServletRequest request, HttpServletResponse response, MultiValueMap<String, String> body) {

        LOGGER.info("[mattydebie] THIS SSO IS HANDLED BY DIGIPOLIS");

        String access_token = getAccessToken(request.getParameter("code"));
        LOGGER.info("[mattydebie] access_token: {}", access_token);

        return getUserInfo(access_token);
    }

    @Override
    public String getExternalUrl(String redirectUri) {
        this.redirectUri = redirectUri;
        return String.format(
            "%s?response_type=code&service=astad.mprofiel.v1&client_id=%s&scope=all&redirect_uri=%s&save_consent=true",
            ssoExternalUrl, ssoClientId, redirectUri);
    }

    public SSOUserInfo getUserInfo(String accessToken) {
        RestTemplate template = new RestTemplate();
        JSONObject json;

        String resp = template.exchange(ssoUserInfoUrl, HttpMethod.GET, new HttpEntity<>(createMap(
            new AbstractMap.SimpleEntry<>("Authorization", "Bearer " + accessToken)
        )), String.class).getBody();
        LOGGER.info("[mattydebie] response from access token = {}", resp);
        json = new JSONObject(resp).getJSONObject("data");

        return new SSOUserInfo(
            json.getString("userName"),
            json.getString("firstName"),
            json.getString("lastName"),
            json.getString("emailWork"),
            accessToken
        );

    }

    public String getAccessToken(String code) {
        // request access token
        MultiValueMap<String, String> json = createMap(
            new AbstractMap.SimpleEntry<>("client_id", ssoClientId),
            new AbstractMap.SimpleEntry<>("client_secret", ssoClientSecret),
            new AbstractMap.SimpleEntry<>("code", code),
            new AbstractMap.SimpleEntry<>("grant_type", "authorization_code"),
            new AbstractMap.SimpleEntry<>("redirect_uri", redirectUri)
        );

        RestTemplate template = new RestTemplate();
        String s;
        try {
            s = template.postForObject(ssoTokenUrl,
                getHttpEntity(json),
                String.class);
        } catch (RestClientResponseException e) {
            LOGGER.error("INUITS Could not obtain access token: {}: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw e;
        }

        JSONObject response = new JSONObject(s);
        return response.getString("access_token");
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpEntity(MultiValueMap<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> createMap(AbstractMap.SimpleEntry<String, String>... values) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(values.length);

        for (AbstractMap.SimpleEntry<String, String> entry : values) {
            map.add(entry.getKey(), entry.getValue());
        }

        return map;
    }
}
