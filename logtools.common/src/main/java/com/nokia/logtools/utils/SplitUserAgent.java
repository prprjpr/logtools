package com.nokia.logtools.utils;

import org.apache.spark.sql.api.java.UDF1;

import java.io.Serializable;

/**
 * Created by pactera on 2017/11/14.
 */
public class SplitUserAgent implements UDF1<String, String> {
	@Override
	public String call(String s) throws Exception {
		String normal = "-1:-1:-1:-1";
		try {
			if (s.isEmpty())
				return normal;
			s = s.trim();
			if (s.startsWith("Dalvik")) {
				normal = madeAgent(s);
			} else if (s.startsWith("Mozilla")) {
				if (s.startsWith("Mozilla/4.0")) {
					normal = "-1:windows:-1:-1";
				} else {
					normal = madeAgent(s);
				}
			} else if (s.startsWith("Android ")) {
				String version = s.substring(s.indexOf(" ") + 1).length() > 5 ? "-1" : s.substring(s.indexOf(" ") + 1);
				normal = "-1:Android:" + version + ":-1";
			} else if (s.startsWith("Alipay/")) {
				normal = s.split(" ")[1].split("/")[1] + "iPhone:" + "-1" + ":iPhone";
			} else if (s.startsWith("iPhone")) {
				normal = "-1:iPhone:-1:iPhone";
			} else if (s.contains("Darwin/")) {
				normal = "-1:iPhone:" + s.substring(s.indexOf("Darwin/") + 7) + ":iPhone";
			} else if (s.startsWith("qqlive4iphone")) {
				normal = "-1:iPhone:-1:iPhone";
			} else if (s.startsWith("QQMusiciPhone")) {
				normal = "-1:iPhone:-1:iPhone";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return normal;
		}
	}

	public String madeAgent(String s) {
		if (!s.contains("(") || !s.contains(")")) {
			return "-1:-1:-1:-1";
		}
		UserAgent result = new UserAgent();
		String value = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
		String[] fields = value.split(";");
		for (String f : fields) {
			f = f.trim();
			if (f.contains("Android")) {
				if (f.split(" ").length < 2) {
					result.setIosVersion("-1");
					result.setOs("Android");
					result.setVersion("-1");
				} else if (f.startsWith("Android ")) {
					try {
						result.setIosVersion("-1");
						result.setOs("Android");
						result.setVersion(
								f.split(" ")[1].contains(".") && f.split(" ")[1].length() < 7 ? f.split(" ")[1] : "-1");
					} catch (Exception e) {
						continue;
					}
				} else {
					result.setIosVersion("-1");
					result.setOs("Android");
					result.setVersion("-1");
				}
			} else if (f.contains("CPU iPhone OS")) {
				try {
					result.setIosVersion(f.split(" ")[3]);
					result.setOs("iPhone");
					result.setVersion("-1");
					result.setPhone("iPhone");
				} catch (Exception e) {
					continue;
				}

			} else if (f.contains("Windows NT")) {
				try {
					result.setIosVersion("-1");
					result.setOs("Windows");
					result.setVersion(f.substring(f.indexOf("Windows")));
					result.setPhone("Windows");
				} catch (Exception e) {
					continue;
				}
			} else if (f.contains("Windows 98")) {
				result.setIosVersion("-1");
				result.setOs("Windows");
				result.setVersion("98");
				result.setPhone("Windows");
			}
			if (f.contains("Build/")) {
				try {
					result.setPhone(f.substring(0, f.indexOf("Build/")));
				} catch (Exception e) {
					continue;
				}
			}
		}
		return result.toString();
	}

	public static void main(String args[]) throws Exception {
		// String s = "Dalvik/2.1.0 (Linux; U; Android 6.0.1; OPPO R9s Build/MMB29M)
		// aa";
		//String s = "Mozilla/5.0 (Linux; Android 7.0; MHA-AL00 Build/HUAWEIMHA-AL00; wv) AppleWebKit/537.36";
		// String s = " Android 7.0 ";
		// String s = " Alipay/10.1.5.102407 iOS/10.3.3 ";
		// String s = " iPhone7,2/10.3.3 (14G60) ";
		// String s = " qqlive4Android/7.2.5 Dalvik (Android 6.0.1;OPPO R9s) ";
		// String s = " QQMusic 7090107(android 6.0.1) ";
		// String s = "WeChat/6.5.21.33 CFNetwork/811.5.4 Darwin/16.7.0";
		// String s = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X)
		// AppleWebKit/603.3.8 ";
		String s="QQMusic 8080508(android 6.0.1)";
		SplitUserAgent agent = new SplitUserAgent();
		String call = agent.call(s);
		System.out.println(call);

		// String s = "Android 5.5 iphone asss".contains(".")&&"Android 5.5 iphone
		// asss".length()<7?"Android 5.5 iphone asss":"-1";
		// System.out.println(s);
	}
}

class UserAgent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String iosVersion = "-1";
	private String os = "-1";
	private String version = "-1";
	private String phone = "-1";

	public String getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return iosVersion + ":" + os + ":" + version + ":" + phone;
	}
}
