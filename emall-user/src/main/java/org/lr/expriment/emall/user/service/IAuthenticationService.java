package org.lr.expriment.emall.user.service;

import org.lr.expriment.emall.user.service.dto.AuthenticationRequest;
import org.lr.expriment.emall.user.service.dto.AuthenticationResponse;

/**
 * @author 李锐
 * @email rui.li04@mljr.com
 * @time 2017-10-12 20:06:17
 */
public interface IAuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request);
}
