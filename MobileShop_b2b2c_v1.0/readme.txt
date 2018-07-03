修改配置如下：
	com.mobileshop.mobile包名下的Constants.java文件，修改baseUrl值为项目所在的主机位置即可。
	示例：
		public static String baseUrl = "http://192.168.8.50:8080/mobile_shop";
		public static String baseUrl = "http://192.168.1.200/mobileshop";
	代码中也有相应的示例。
