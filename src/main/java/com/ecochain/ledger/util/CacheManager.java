/*package com.ecochain.ledger.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

*//**
 * 缓存操作
 * 
 * @author liuyang
 *
 *//*
@Component
public class CacheManager {
	private static Logger logger = Logger.getLogger(CacheManager.class);
	
	
	@Autowired
	private ShardedJedisPool jedisPool;
	
	private int defaultTimeOut = 60*60*24;//默认时间一天

	public void setJedisPool(ShardedJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public ShardedJedis getJedis() {
		return jedisPool.getResource();
	}

	public void returnJedis(ShardedJedis jedis) {
		jedisPool.returnResource(jedis);
	}

	*//**
	 * 获取对象
	 * 
	 * @param key
	 * @return Object
	 *//*
	public Object get(String key) {
		Object obj = null;
		ShardedJedis jedis = jedisPool.getResource();
		try {
			obj = byte2Object(jedis.get(getKey(key)));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		} finally {
			jedisPool.returnResource(jedis);
		}
		return obj;
	}

	*//**
	 * 判断对象是否存在
	 * 
	 * @param key
	 * @return true or false
	 *//*
	public boolean isExist(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} finally {
			jedisPool.returnResource(jedis);
//			jedis.disconnect();
		}
	}

	*//**
	 * 保存对象
	 * 
	 * @param key
	 *            键
	 * @param obj
	 *            值
	 * @param outTime
	 *            超时时间
	 *//*
	public boolean set(String key, Object obj, int outTime) {
		ShardedJedis jedis = jedisPool.getResource();
		boolean flag = true;
		try {
			jedis.set(getKey(key), object2Bytes(obj));
			if (outTime != 0) {
				jedis.expire(getKey(key), outTime);
			}else{
			    jedis.expire(getKey(key), defaultTimeOut);
			}
		} catch (Exception e) {
			flag = false;
			logger.error("===save key===" + key + "==="
					+ e.getLocalizedMessage());
		} finally {
			jedisPool.returnResource(jedis);
		}
		return flag;
	}

	*//**
	 * 删除对象
	 * 
	 * @param key
	 * @return Long
	 *//*
	public Long del(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
			// return 1L;
		} catch (Exception e) {
			logger.error("===del key===" + key + "==="
					+ e.getLocalizedMessage());
			return null;
		} finally {
			jedisPool.returnResource(jedis);

		}
	}

	*//**
	 * 字节转化为对象
	 * 
	 * @param bytes
	 * @return Object
	 *//*
	public static Object byte2Object(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			ObjectInputStream inputStream;
			inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			Object obj = inputStream.readObject();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	*//**
	 * 对象转化为字节
	 * 
	 * @param value
	 * @return byte[]
	 *//*
	public static byte[] object2Bytes(Object value) {
		if (value == null) {
			return null;
		}

		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(arrayOutputStream);

			outputStream.writeObject(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				arrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return arrayOutputStream.toByteArray();
	}

	public static byte[] getKey(String key) {
		return key.getBytes();
	}
}
*/