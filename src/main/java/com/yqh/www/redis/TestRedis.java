package com.yqh.www.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

/** 
 * Author:杨庆辉
 * Time:2017年6月7日 下午5:12:59 
 * Desp:
 */
public class TestRedis {
	
	public static void main(String[] args) {
//		GenericXmlApplicationContext context = new GenericXmlApplicationContext();  
//		context.setValidating(false);  
//		context.load("classpath:applicationContext.xml");  
//		context.refresh();
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		RedisTemplate redisTemplate = (RedisTemplate) context.getBean(RedisTemplate.class);
		HashOperations<String,String,String> opsForHash = redisTemplate.opsForHash();
		ValueOperations<String,String> opsForValue = redisTemplate.opsForValue();
		
		String result = opsForHash.get(RedisKey.REALTIME_AVARAGE_LINE_10_DAY.val(), "430009");
		
		System.out.println("结果："+ result);
		
	
	}
}
