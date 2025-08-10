package com.valentin.reservacion_citas.web.config;

import com.valentin.reservacion_citas.web.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;

	public OAuthSuccessHandler(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;

		String email = oAuth2Token.getPrincipal().getAttribute("email");
		String jwtToken = jwtUtil.createJwt(email);

		/*Cookie cookie = new Cookie("token", jwtToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(3600);*/

		/*String cookieString = String.format("token=%s; Max-Age=%d; Path=/; HttpOnly", jwtToken, 3600);
		response.addHeader("Set-Cookie", cookieString);*/

		response.sendRedirect("https://proyecto-cafeteria-sooty.vercel.app/home?token=" + jwtToken);
	}
}
