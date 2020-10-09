package it.gov.pagopa.bpd.common.connector.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import it.gov.pagopa.bpd.common.connector.PagoPaAppIORestClient;
import it.gov.pagopa.bpd.common.connector.model.PagoPaAppIOAuthResource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthAppIoInterceptor implements HandlerInterceptor{
	
	public static final String AUTHORIZATION_HEADER="Authorization";
	public static final String FISCAL_CODE_HEADER="fiscalCode";
	
	@Autowired
	private PagoPaAppIORestClient pagoPaAppIoRestClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		
		log.info("Interceptor preHandle start");
		
		if(request.getHeader(AUTHORIZATION_HEADER)!=null) {
			PagoPaAppIOAuthResource pagoPaAuthResponse = pagoPaAppIoRestClient.authorizeBearerToken(request.getHeader(AUTHORIZATION_HEADER));
			
			request.setAttribute(FISCAL_CODE_HEADER, pagoPaAuthResponse.getFiscalCode());
			
			((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).put(FISCAL_CODE_HEADER, pagoPaAuthResponse.getFiscalCode());
		}

		log.info("Interceptor preHandle end");
		
		return false;
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
			throws Exception {
		log.info("Interceptor postHandle end");
		return;
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) 
			throws Exception{
		log.info("Interceptor afterCompletion end");
		
	}
}