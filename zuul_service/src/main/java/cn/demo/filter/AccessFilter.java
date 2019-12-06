package cn.demo.filter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务请求过滤器
 */
@Component
public class AccessFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

	//路由之前
	@Override
	public String filterType() {
		return "pre";
	}

	//过滤的顺序
	@Override
	public int filterOrder() {
		return 0;
	}

	//这里可以写逻辑判断，是否要过滤，本文true,永远过滤
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		log.info("{} >>> {}", request.getMethod(), request.getRequestURL().toString());

		String access_token = request.getParameter("access_token");
		if(StringUtils.isBlank(access_token) || !"test".equals(access_token)){
			// zuul过滤该请求
			requestContext.setSendZuulResponse(false);//表示不继续转发该请求。
			requestContext.setResponseStatusCode(401);//返回的状态码，这里为401
			requestContext.setResponseBody("token invide无效");//返回的内容，可以指定为一串json
			log.info("the request {} is fail, the token is invide无效", request.getRequestURL().toString());
		} else {
			log.info("the request {} is ok", request.getRequestURL().toString());
		}
		return null;
	}
}
