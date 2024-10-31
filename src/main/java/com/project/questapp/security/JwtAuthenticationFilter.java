package com.project.questapp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.questapp.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//gelen isteğin authorized olup olmadığını kontrol edip ona göre isteği onaylıyoruz veya unauthorized dönüyoruz
		try {
			String jwtToken = extractJwtFromRequest(request);
			//token valid mi kontrol ettik
			if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
				//tokenin içinden idyi aldık
				Long id = jwtTokenProvider.getUserIdFromJwt(jwtToken);
				//bu id'ye ait user'ı getirdik
				UserDetails user = userDetailsService.loadUserById(id);
				//eger böyle bir user varsa artık bu isteği authorized edebiliriz
				if(user != null) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					//kullanıcının kimlik bilgileri, yetkileri ve güvenlik durumuyla ilgili bilgileri içerir.
					SecurityContextHolder.getContext().setAuthentication(auth); //isteğimizi authorized etmiş olduk
				}
			}
		} catch(Exception e) {
			//bu işlemler sırasında bir hata oluşursa mevcut filterda sonlanıyor ve unauthorized dönüyor
			return;
		}
		//Bir sonraki filtreyi çağır ve filtera devam et
		filterChain.doFilter(request, response);
	}

	/* Örnek bir header;
	GET /api/resource HTTP/1.1
	Host: example.com
	Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
	 */
	private String extractJwtFromRequest(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
			return bearer.substring("Bearer".length() + 1);
		return null;
	}

}
