package com.ly.core.dao;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ly.bolg.core.user.dao.UserDao;
import com.ly.bolg.core.user.vo.User;
import com.ly.common.encrypt.MD5;

public class UserDaoTest {
	private static UserDao userDao;
	
	
	  @BeforeClass
	  public static void setUpBeforeClass() throws Exception {
	    ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:IOC.xml");
	    userDao = (UserDao) ac.getBean("userDao");
	  }
	  
	  
	  @Test
	  public void test() throws Exception {
		  for (int i = 0; i < 100; i++) {
			  User u = new User();
			  u.setUsername(getRandomString(7));
			  u.setPassword(MD5.md5Encode("12345"));
			  int rt = userDao.insert(u);
			  System.out.println(rt);
		  }
		 
	  }
	  
	  
	  //@Test
	  public void testFindById() throws Exception {
		  User u = userDao.findById(1);
		  System.out.println(u.getUsername());
	  }
	  
	  
	  
	  public static void main(String[] args) {
		//基本的词库，可以任意扩充
		  String[] s = {"我","你","他","她"};
		  String bev ="是";
		  String[] c = {"富有的","漂亮的","美丽的","伤心的","快乐的","有趣的","好笑的"};
		  String[] auxv = {"看起来","玩起来","杀起来"};
		  String[] adv = {"很","极度","非常"};
		  String[] n = {"汽车","飞机","大炮"};
		  String[] vt = {"拥有","驾驶","摧毁"};
		  String[] abstractn = {"刺激","爽快","开心"};
		  int srandom = (int) (Math.random() * 4);
		  //产生随机的话
		  int crandom = (int) (Math.random() * 7);
		  int auxvrandom = (int) (Math.random() * 3);
		  int advrandom = (int) (Math.random() * 3);
		  int nrandom = (int) (Math.random() * 3);
		  int vtrandom = (int) (Math.random() * 3);
		  System.out.println("简单句svc："+ s[srandom] + bev + c[crandom]);
		  System.out.println("简单句svo："+ s[srandom] + vt[vtrandom] + n[nrandom]);
		  System.out.println("简单句sv adv o："+ s[srandom] + bev + adv[advrandom] + abstractn[nrandom]);
		  System.out.println("简单句sv adv o："+ s[srandom] + auxv[auxvrandom] + adv[advrandom] + abstractn[nrandom]);
	  }
	  
	  
	  public static String getRandomString(int length){
		     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		     Random random=new Random();
		     StringBuffer sb=new StringBuffer();
		     for(int i=0;i<length;i++){
		       int number=random.nextInt(62);
		       sb.append(str.charAt(number));
		     }
		     return sb.toString();
		 }
}
