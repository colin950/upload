package com.example.upload.utils;

import com.example.upload.base.model.RedisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisUtil {

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	private final String setConvertVideoProgressPercentKey = "cvt_progress_key:";

	public RedisUtil(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	// 저장
	public void setRedisData(RedisModel redisData) {
		ValueOperations<String, String> vop = redisTemplate.opsForValue();
		try {
			vop.set(redisData.getKey(), redisData.getValue());
		} catch (Exception e) {
			throw e;
		}
	}

	public void setConvertVideoProgressPercent(Long id, String percent) {
		RedisModel data = new RedisModel()
				.setKey(setConvertVideoProgressPercentKey + id)
						.setValue(percent);
		setRedisData(data);
	}

	public Optional<String> getConvertVideoProgressPercent(Long id) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(setConvertVideoProgressPercentKey + id));
	}

	
	// 조회
	public String getRedisData(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
}