package com.tmdt.shop_service.modules.auth.application.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.tmdt.shop_service.modules.auth.application.request.GoogleSsoRequest;

public interface SsoService {
    String getAuthorizationUrl(String logicType);

    Object googleVerify(GoogleSsoRequest request);
}
