package com.ly.common.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Copyright (C) 2015 GO2.CN. All rights reserved. This computer program source code file is protected by copyright law and international
 * treaties. Unauthorized distribution of source code files, programs, or portion of the package, may result in severe civil and criminal
 * penalties, and will be prosecuted to the maximum extent under the law.
 * 
 * redis和连接池的封装 Jedis操作步骤如下： 获取Jedis实例需要从JedisPool中获取； 用完Jedis实例需要返还给JedisPool； 如果Jedis在使用过程中出错，则也需要还给JedisPool；
 * 
 * @author chengzl
 * @since 2015-11-11
 */

public class RedisUtil {
  private final static Logger log  = LoggerFactory.getLogger(RedisUtil.class);
  private static JedisPool    pool = null;

  /**
   * 构建redis连接池
   * 
   * @param ip
   * @param port
   * @return JedisPool
   */
  public static JedisPool createPool(String ip, int port) {
    if (pool == null) {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(300);
      config.setMaxIdle(100);
      
      config.setMaxWaitMillis(100000);
      config.setTimeBetweenEvictionRunsMillis(3000);
      config.setTestOnBorrow(true);
      pool = new JedisPool(config, ip, port,100000);
    }
    return pool;
  }

  /***
   * 密码验证登陆
   * @author yangxing
   * @param ip
   * @param password
   * @param port
   * @return
   *
   */
  public static JedisPool createPool(String ip, int port ,String password) {
    if (pool == null) {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(300);
      config.setMaxIdle(100);
      config.setMaxWaitMillis(100000);
      config.setTimeBetweenEvictionRunsMillis(3000);
      config.setTestOnBorrow(true);
      pool = new JedisPool(config, ip, port, 100000, password);
    }
    return pool;
  }
  /**
   * 获取数据
   * 
   * @param key
   * @return
   */
  public static String get(String key) {
    String value = null;
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      value = jedis.get(key);
    } catch (Exception e) {
    	 log.error(key+":"+value+"获取失败",e);
      pool.returnBrokenResource(jedis);
      log.error(null,e);
    } finally {
      pool.returnResource(jedis);
    }

    return value;
  }
  
  
  /**
   * 获取数据(选择db)
   * 
   * @param key
   * @return
   */
  public static String get(String key,int index) {
    String value = null;
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.select(index);
      value = jedis.get(key);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
    } finally {
      pool.returnResource(jedis);
    }

    return value;
  }
  
  /**
   * 获取某个数据库的所有数据
   * @param index
   * @return
   */
  public static Set<String> keys(int index) {
    Set<String> value = null;
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.select(index);
      value = jedis.keys("*");
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
    } finally {
      pool.returnResource(jedis);
    }

    return value;
  }


 

  /**
   * 设置 String
   * 
   * @param key
   * @param value
   */
  public static void set(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.set(key, value);
    } catch (Exception e) {
    	 log.error(key+":"+value+"设置失败",e);
      pool.returnBrokenResource(jedis);
     
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  public static void set(String key, String value,int seconds,int index) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.select(index);
      jedis.set(key, value);
      if(seconds!=0){
        jedis.expire(key, seconds);
      }
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(key+"获取失败",e);
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  public static void setExpire(String key, int seconds, int index){
  	Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.select(index);
      if(seconds!=0){
        jedis.expire(key, seconds);
      }
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(key+"设置失败",e);
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  /**
   * 设置 String
   * 
   * @param key
   * @param value
   */
  public static void setToCustomDb(String key, String value,int index) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.select(index);
      jedis.set(key, value);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  /**
   * 设置 String
   * 
   * @param key
   * @param value
   */
  public static void mset(String... keysAndValues) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.mset(keysAndValues);
    } catch (Exception e) {
    	if(keysAndValues!=null && keysAndValues.length>0){
      		log.error(keysAndValues.toString()+"设置失败",e);
      	}else{
      		log.error("mset keys 为空 ",e);
      	}
      pool.returnBrokenResource(jedis);  
    } finally {
    	pool.returnResource(jedis);
    }
  }
  /**
   * 设置 List<String>
   * 
   * @param key
   * @param value
   */
  public static void mset(List<String> keysAndValuesList) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      String[] keysAndValues = new String[keysAndValuesList.size()];
    		  keysAndValuesList.toArray(keysAndValues);
      jedis.mset(keysAndValues);
    } catch (Exception e) {
    	
      pool.returnBrokenResource(jedis);
      if(keysAndValuesList!=null && keysAndValuesList.size()>0){
          log.error(keysAndValuesList.toString()+"keys 设置失败",e);
      }else{
    	  log.error("keys 为空 设置失败",e);
      }
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  public static List<String> mget(String... keys) {
    List<String> value = null;
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      value = jedis.mget(keys);
    } catch (Exception e) {
    	if(keys!=null && keys.length>0){
    		log.error(keys.toString(),e);
    	}else{
    		log.error("mget keys 为空 ",e);
    	}
      pool.returnBrokenResource(jedis);
      
    } finally {
      pool.returnResource(jedis);
    }

    return value;
  }

  /**
   * 设置 hashMap
   * 
   * @param key
   * @param map
   */
  public static void setMap(String key, HashMap<String, String> map) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.hmset(key, map);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
    } finally {
      pool.returnResource(jedis);
    }
  }

  /**
   * 判断redis是否连接异常
   * 
   * @return 是否有异常
   */
  public static boolean isConnectRedis() {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
    } catch (Exception e) {
      log.error("Reids u Exception");
      pool.returnBrokenResource(jedis);
      return false;
    } finally {
      pool.returnResource(jedis);
    }

    return true;
  }

  /**
   * 判断key是否存在
   * 
   * @param key
   * @return true OR false
   */
  public static Boolean exists(String key) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.exists(key);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
      return false;
    } finally {
      pool.returnResource(jedis);
    }
  }

  /**
   * 删除指定的key,也可以传入一个包含key的数组
   * 
   * @param keys
   *          一个key 也可以使 string 数组
   * @return 返回删除成功的个数
   */
  public static Long del(String keys) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.del(keys);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
      return 0L;
    } finally {
      pool.returnResource(jedis);
    }
  }
  
  /**
   * 删除指定key 
   * @param keys 数组
   * @return
   */
  public static Long del(String... keys) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.del(keys);
    } catch (Exception e) {
      pool.returnBrokenResource(jedis);
      log.error(null,e);
      return 0L;
    } finally {
      pool.returnResource(jedis);
    }
  }

  /**
   * Hash
   */
  public static long hdel(String key, String fieid) {
    Jedis jedis = pool.getResource();
    long s = jedis.hdel(key, fieid);
    pool.returnResource(jedis);
    return s;
  }

  public static long hdel(String key) {
    Jedis jedis = pool.getResource();
    long s = jedis.del(key);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 测试hash中指定的存储是否存在
   * 
   * @param String
   *          key
   * @param String
   *          fieid 存储的名字
   * @return 1存在，0不存在
   * */
  public static boolean hexists(String key, String fieid) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    boolean s = jedis.hexists(key, fieid);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 返回hash中指定存储位置的值
   * 
   * @param String
   *          key
   * @param String
   *          fieid 存储的名字
   * @return 存储对应的值
   * */
  public static String hget(String key, String fieid) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    String s = jedis.hget(key, fieid);
    pool.returnResource(jedis);
    return s;
  }

  public static byte[] hget(byte[] key, byte[] fieid) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    byte[] s = jedis.hget(key, fieid);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 以Map的形式返回hash中的存储和值
   * 
   * @param String
   *          key
   * @return Map<Strinig,String>
   * */
  public static Map<String, String> hgetAll(String key) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    Map<String, String> map = jedis.hgetAll(key);
    pool.returnResource(jedis);
    return map;
  }

  /**
   * 添加一个对应关系
   * 
   * @param String
   *          key
   * @param String
   *          fieid
   * @param String
   *          value
   * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
   * **/
  public static long hset(String key, String fieid, String value) {
    Jedis jedis = pool.getResource();
    long s = jedis.hset(key, fieid, value);
    pool.returnResource(jedis);
    return s;
  }

  public static long hset(String key, String fieid, byte[] value) {
    Jedis jedis = pool.getResource();
    long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 添加对应关系，只有在fieid不存在时才执行
   * 
   * @param String
   *          key
   * @param String
   *          fieid
   * @param String
   *          value
   * @return 状态码 1成功，0失败fieid已存
   * **/
  public long hsetnx(String key, String fieid, String value) {
    Jedis jedis = pool.getResource();
    long s = jedis.hsetnx(key, fieid, value);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 获取hash中value的集合
   * 
   * @param String
   *          key
   * @return List<String>
   * */
  public List<String> hvals(String key) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    List<String> list = jedis.hvals(key);
    pool.returnResource(jedis);
    return list;
  }

  /**
   * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
   * 
   * @param String
   *          key
   * @param String
   *          fieid 存储位置
   * @param String
   *          long value 要增加的值,可以是负数
   * @return 增加指定数字后，存储位置的值
   * */
  public static long hincrby(String key, String fieid, long value) {
    Jedis jedis = pool.getResource();
    long s = jedis.hincrBy(key, fieid, value);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 返回指定hash中的所有存储名字,类似Map中的keySet方法
   * 
   * @param String
   *          key
   * @return Set<String> 存储名称的集合
   * */
  public Set<String> hkeys(String key) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    Set<String> set = jedis.hkeys(key);
    pool.returnResource(jedis);
    return set;
  }

  /**
   * 获取hash中存储的个数，类似Map中size方法
   * 
   * @param String
   *          key
   * @return long 存储的个数
   * */
  public long hlen(String key) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    long len = jedis.hlen(key);
    pool.returnResource(jedis);
    return len;
  }

  /**
   * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
   * 
   * @param String
   *          key
   * @param String
   *          ... fieids 存储位置
   * @return List<String>
   * */
  public static List<String> hmget(String key, String... fieids) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    List<String> list = jedis.hmget(key, fieids);
    pool.returnResource(jedis);
    return list;
  }

  public static List<byte[]> hmget(byte[] key, byte[]... fieids) {
    // ShardedJedis sjedis = getShardedJedis();
    Jedis jedis = pool.getResource();
    List<byte[]> list = jedis.hmget(key, fieids);
    pool.returnResource(jedis);
    return list;
  }

  /**
   * 添加对应关系，如果对应关系已存在，则覆盖
   * 
   * @param Strin
   *          key
   * @param Map
   *          <String,String> 对应关系
   * @return 状态，成功返回OK
   * */
  public static String hmset(String key, Map<String, String> map) {
    Jedis jedis = pool.getResource();
    String s = jedis.hmset(key, map);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 添加对应关系，如果对应关系已存在，则覆盖
   * 
   * @param Strin
   *          key
   * @param Map
   *          <String,String> 对应关系
   * @return 状态，成功返回OK
   * */
  public static String hmset(byte[] key, Map<byte[], byte[]> map) {
    Jedis jedis = pool.getResource();
    String s = jedis.hmset(key, map);
    pool.returnResource(jedis);
    return s;
  }

  /**
   * 仅供开发人员使用
   * 
   * @return
   */
  public static boolean flushDb(int index) {
    Jedis jedis = null;
    try{
      jedis = pool.getResource();
      jedis.select(index);
      String status = jedis.flushDB();
    if (status.equalsIgnoreCase("OK"))
      return true;
    return false;
    }finally{
    	pool.returnResource(jedis);
    }
    
  }

  /**
   * 获取redis信息
   * 
   * @param section
   * @return
   */
  public static String info(String section) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      if (section == null || StringUtil.isBlank(section))
        return jedis.info();
      else
        return jedis.info(section);
    }finally{
      pool.returnResource(jedis);
    }
    
   
  }

  public static Long dbSize() {
    Jedis jedis = null;
    try{
      jedis = pool.getResource();
      return jedis.dbSize();
    }finally{
      pool.returnResource(jedis);
    }
    
  }

  /**
   * 从jedis连接池中获取获取jedis对象
   * 
   * @return
   */
  public static Jedis getJedis() {
    return pool.getResource();
  }
  
  /**
   * 仅供开发人员使用
   * 
   * @return
   */
  public static boolean flushAll() {
	  Jedis jedis = pool.getResource();
	    String status = jedis.flushAll();
	    try{
	    if (status.equalsIgnoreCase("OK"))
	      return true;
	    return false;
	    }finally{
	    	pool.returnResource(jedis);
	    }
	    
  }

  /**
   * 原子操作增加1
   * @param key
   * @return
   */
  public static long incr(String key){
	  Jedis jedis = pool.getResource();
	   long s = jedis.incr(key);
	   pool.returnResource(jedis);
		return s;
  } 
  
  /**
   * 原子操作减1
   * @param key
   * @return
   */
  public static long decr(String key){
	  Jedis jedis = pool.getResource();
	   long s = jedis.decr(key);
	   pool.returnResource(jedis);
		return s;
  } 
}
