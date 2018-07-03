package com.huatec.edu.mobileshop.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.huatec.edu.mobileshop.util.mail.MailSender;
import com.huatec.edu.mobileshop.util.mail.MailSenderInfo;

import net.sf.json.JSONObject;

public class MSUtil {
	//使用md5加密算法
	public static String md5(String msg){
		//摘要算法，将不同长度的字符串转换为等长的，不可逆
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] input=msg.getBytes();//input需要加密的信息
			byte[] output=md.digest(input);//output加密过的信息
			//将md5处理后的output结果转成字符串
			//利用Base64算法转成字符串
			String str=Base64.encodeBase64String(output);
			//String str=new String(output);//容易出现乱码，一般不使用
			return str;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("密码加密失败");
			return "";
		}
	}
	
	/**
	 * 调整图片大小
	 * @param srcImgPath 原图片路径
	 * @param distImgPath 转换大小后图片路径
	 * @param width 转换后图片宽度
	 * @param height 转换后图片高度
	 */
	public static void resizeImage(String srcImgPath,String distImgPath,int width,int height){
		try {
			File srcFile=new File(srcImgPath);
			Image srcImg=ImageIO.read(srcFile);
			//构建图片对象
			 BufferedImage buffImg=
					 new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			//绘制图片
			buffImg.getGraphics().drawImage(
					srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			ImageIO.write(buffImg, "JPEG", new File(distImgPath));
		} catch (IOException e) {
			System.out.println("图片转换异常"+e);
			e.printStackTrace();
		}
	}
	/**
	 * 生成不同尺寸的图片
	 * @param img 原图片
	 * @param url 资源路径
	 * @return map：缩略图、大图、小图、原图
	 */
	public static Map createImg(String img,String url){
		Map<String,Object> map=new HashMap<String,Object>();
		//图片名称
		int c=img.lastIndexOf("j");
		String s1=img.substring(0, c-1);
		String s2=img.substring(c,img.length());
//		String[] s=img.split("\\.");// "."用"\\."表示
//		String thumSRC=s[0]+"_thum."+s[1];
//		String bigSRC=s[0]+"_big."+s[1];
//		String smallSRC=s[0]+"_small."+s[1];
//		String origSRC=s[0]+"_orig."+s[1];
		String thumSRC=s1+"_thum."+s2;
		String bigSRC=s1+"_big."+s2;
		String smallSRC=s1+"_small."+s2;
		String origSRC=s1+"_orig."+s2;
		/*
		 * 图片尺寸转换
		 * 缩略图：60px*60px
		 * 大图：800px*800px
		 * 小图（列表页展示图）：220px*220px
		 * 原图：430px*430px
		 */
		MSUtil.resizeImage(img, thumSRC, 60, 60);
		MSUtil.resizeImage(img, bigSRC, 800, 800);
		MSUtil.resizeImage(img, smallSRC, 220, 220);
		MSUtil.resizeImage(img, origSRC, 430, 430);
		//转换成可访问的资源地址
		String path=MSUtil.getPath();
//		System.out.println("path"+path);
//		System.out.println("url:"+url);
//		String path="E:\\javashop\\wtpwebapps";
//		System.out.println("thumSRC:"+thumSRC);
		String thumSRC2=thumSRC.replace(path, url);
//		System.out.println("thumSRC2:"+thumSRC2);
		String bigSRC2=bigSRC.replace(path, url);
		String smallSRC2=smallSRC.replace(path, url);
		String origSRC2=origSRC.replace(path, url);
		//将资源路径中的\替换为/
		map.put("thum", thumSRC2.replace("\\", "/"));
		map.put("big", bigSRC2.replace("\\", "/"));
		map.put("small", smallSRC2.replace("\\", "/"));
		map.put("orig", origSRC2.replace("\\", "/"));
		return map;
	}
	
	//获取path.properties文件中path的路径
	public static String getPath(){
		String path="";
		try {
			Properties prop=new Properties();
			InputStream is=MSUtil.class.getClassLoader().getResourceAsStream("path.properties");
			prop.load(is);
			path=prop.getProperty("path");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载配置文件失败"+e);
		}
		return path;
	}
	
	/*public static String getPath2(HttpServletRequest request){
		String url="http://"+request.getServerName()+":"+request.getLocalPort()
		+request.getContextPath()+"/upload";
		System.out.println(url);
		return url;
	}*/
	//删除文件
	public static void deleteFile(String fileName){
		File file=new File(fileName);
		if(file.exists()){
			file.delete();
		}
	}
	/**
	 * 发送邮件
	 * @param pwd 动态生成的新密码
	 * @param email 邮箱地址
	 */
	public static void sendEmail(String pwd,String email){
		try {
			//获取mail.properties中的内容
			Properties prop=new Properties();
			InputStream is=MSUtil.class.getClassLoader().getResourceAsStream("mail.properties");
			prop.load(is);
			String host=prop.getProperty("host");
			String port=prop.getProperty("port");
			String username=prop.getProperty("username");
			String password=prop.getProperty("password");
			String fromAddress=prop.getProperty("from");
			//实例化“发送邮件所需各种信息”
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setMailServerHost(host);
			mailInfo.setMailServerPort(port);
			mailInfo.setValidate(true);
			mailInfo.setUserName(username);
			mailInfo.setPassword(password);
			mailInfo.setFromAddress(fromAddress);
			mailInfo.setToAddress(email);
			mailInfo.setSubject("移动电商平台【动态密码】");
			StringBuffer buffer = new StringBuffer();
			buffer.append("您的动态密码为 "+pwd+",请使用此密码登录系统，更改此密码请到个人中心");
			mailInfo.setContent(buffer.toString());
			// 发送邮件
			MailSender sms = new MailSender();
			sms.sendTextMail(mailInfo);
			System.out.println("邮件发送完毕");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载配置文件失败"+e);
		}
	}
	
	
	public static Map getParam(HttpServletRequest request){
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(
			        (ServletInputStream) request.getInputStream()));
			String line = null;
	        StringBuffer sb = new StringBuffer();
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
	        String appParam=sb.toString();
	        System.out.println(appParam);
	        JSONObject jsonParam=JSONObject.fromObject(appParam);
	        Map jmap=jsonParam;
			return jmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		String srcImgPath="E:/javashop/wtpwebapps/MobileShop/file/upload/Fz6tK9SEs+H2kJx0lSiXGA==.jpg";
		String[] s=srcImgPath.split("\\.");
		System.out.println(Arrays.toString(s));
		System.out.println(s.length);
		String distImgPath=s[0]+"_orig"+"."+s[1];
		System.out.println(distImgPath);
		resizeImage(srcImgPath,distImgPath,800,800);
	}*/
	
}
