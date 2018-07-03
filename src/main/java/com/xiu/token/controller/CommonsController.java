package com.xiu.token.controller;

import com.xiu.token.utils.IdFactory;
import com.xiu.token.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

/**
 * @Auther 创建者: Tc李
 * @Date 创建时间: 2018/06/28 10:29
 * @Description 类描述:
 */

@RestController
public class CommonsController {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/")
    public Object token() throws Exception {
        long currentTime = System.currentTimeMillis();
        long id = IdFactory.nextId();
        String s = currentTime + "" + id;
        String token = RsaUtils.encryptByPublicKey(s);
        String result = token.replaceAll("\\+", "%2B");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(s,id,1,TimeUnit.HOURS);
        return result;
    }
}
