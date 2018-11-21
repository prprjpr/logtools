package com.nokia.logtools.http;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.nokia.logtools.encrypt.AES;
import com.nokia.logtools.encrypt.RSA;
import com.nokia.logtools.encrypt.SHA;

/**
 * client endpiont :
 * 
 * @see HttpClientSec 1.get AES.key String aesKey = Random.value(); 2.use
 *      AES.key encrypt data String encryptData = AESUtils.encrypt(aesKey,
 *      param); 3.use server.public_key encrypt AES.key String encryptKey =
 *      RSAUtils.encryptByPublicKey(SERVER_PUBLIC_KEY, aesKey); 4.get encryptKey
 *      and encryptData hash String source = SHAUtils.sha256(encryptKey +
 *      encryptData); 5.use client.private_key create signature from sources
 *      String signature = RSAUtils.encryptByPrivateKey(CLIENT_PRIVATE_KEY,
 *      source); 6.Package data map.put("key", encryptKey); map.put("data",
 *      encryptData); map.put("signature", signature);
 *
 *      server endpiont : verify param and decrypt data
 *
 * @author Junfeng.Tang
 * @date 2018.04.02
 */
public class HttpServerSec {

	private static Properties keys = new Properties();
	private static final String PRI_KEY = "priKey";
	private static final String KEY = "key";
	private static final String DATA = "data";
	private static final String SIGNATURE = "signature";

	// 初始化参数
	static {
		try {
			keys.load(HttpServerSec.class.getClassLoader().getResourceAsStream("http.properties"));
		} catch (Exception e) {
		}
	}

	/**
	 * use client.publicKey verify signature is true or false and param is original
	 * or not.
	 *
	 * @param pubKey
	 * @param encryptKey
	 * @param signature
	 * @return
	 */
	public static boolean verify(String pubKey, String encryptKey, String encryptData, String signature) {
		if (StringUtils.isAnyBlank(encryptKey, encryptData, signature, pubKey)) {
			return false;
		}
		return RSA.verify(pubKey, encryptData, signature);
	}

	/**
	 * use server.privateKey to decrypt AES.encryptKey
	 *
	 * @param priKey
	 * @param encryptKey
	 * @return AES.key
	 */
	public static String decryptKey(String priKey, String encryptKey) {
		return RSA.decryptStr(priKey, encryptKey);
	}

	/**
	 * use AES.key to decrypt encryptData
	 *
	 * @param aesKey
	 * @param encryptData
	 * @return
	 */
	public static String decryptData(String aesKey, String encryptData) {
		return AES.decryptStr(aesKey, encryptData);
	}

	// 如何返回比较好,异常的情况
	public static String parse(String pubKey, Map<String, String> map) {
		// 1 获取参数
		String encryKey, encryData, signature;

		encryKey = map.get(KEY);
		encryData = map.get(DATA);
		signature = map.get(SIGNATURE);

		// 1获取签名原始数据 (通过 encryKey+encryData hash256)
		String source = SHA.sha256(encryKey + encryData);

		// 2 判断签名 (原key,data 作为签名原始数据,client端priKey签名)
		if (!RSA.verify(pubKey, source, signature)) {
			throw new RuntimeException("签名失败");
		}

		// 3 获取AES key (原 RSA Server public key 加密
		String aesKey = RSA.decryptStr(keys.getProperty(PRI_KEY), encryKey);

		// 4 解析数据 (原 AES 加密得到 以AES key 作为向量 encryData)
		return AES.decryptStr(aesKey, encryData);
	}

}
