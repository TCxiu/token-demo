package com.xiu.token.interceptor;

import com.alibaba.fastjson.JSON;
import com.xiu.token.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @Auther 创建者: Tc李
 * @Date 创建时间: 2018/06/28 12:35
 * @Description 类描述:
 */

@Component
public class TokenInter implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if ("/".equals(requestURI)){
            return true;
        }

        boolean tokenFlag = checkToken(request, response);

        if (tokenFlag){
            return tokenFlag;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean checkToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        String token = request.getParameter("token");
        if (token != null && !token.trim().equals("")) {

            if (isLosed(token)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("code", -2);
                map.put("data", "");
                map.put("msg", "token失效");
                writer.write(JSON.toJSONString(map));
                return false;
            }

            return true;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("code",-1);
        map.put("data","");
        map.put("msg","无token");
        writer.write(JSON.toJSONString(map));
        return false;
    }


    private boolean isLosed(String token) throws Exception {
        String decrypt = RsaUtils.decryptByPrivateKey(token);

        ValueOperations valueOperations = redisTemplate.opsForValue();

        return redisTemplate.hasKey(decrypt);
    }
}
