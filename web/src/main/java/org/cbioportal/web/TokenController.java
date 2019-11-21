package org.cbioportal.web;

import org.cbioportal.service.OAuth2DataAccessTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    OAuth2DataAccessTokenServiceImpl tokenService;

    @RequestMapping("/token")
    @ResponseBody
    public final String token() {
        final String offlineToken = tokenService.getOfflineTokenFromContext();
        logger.info("Retrieved offline token for user: " + offlineToken);
        return "Token:\n" + offlineToken;
    }

}
