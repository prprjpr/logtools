package com.nokia.logtools.http;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.nokia.logtools.encrypt.AES;
import com.nokia.logtools.encrypt.RSA;
import com.nokia.logtools.encrypt.Random;
import com.nokia.logtools.encrypt.SHA;

/**
 * @author Junfeng.Tang
 * @date 2018.04.02
 */
public class HttpClientSec {

	private static String priKey;
	private static String pubKey;
	private static final String KEY = "key";
	private static final String DATA = "data";
	private static final String SIGNATURE = "signature";
	private static final String CLIENT_ID = "clientId";

	public static String service(String url, Data data, String companyId) {
		return service(url, new Gson().toJson(data), companyId);
	}

	public static String service(String url, String param, String companyId) {
		// 1.获取随机数
		String random = Random.value();

		// 2 生成AESkey
		String aesKey = AES.generateKey(random);

		// 3.use AES.key encrypt data
		String encryptData = AES.encryptStr(aesKey, param);

		// 4.use server.public_key encrypt AES.key
		String encryptKey = RSA.encryptStr(pubKey, aesKey);

		// 5.get encryptKey and encryptData hash
		String source = SHA.sha256(encryptKey + encryptData);

		// 6.use client.private_key create signature from sources
		String signature = RSA.sign(priKey, source);

		// 7.Package data
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY, encryptKey);
		map.put(DATA, encryptData);
		map.put(SIGNATURE, signature);
		map.put(CLIENT_ID, companyId);

		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/json;charset=UTF-8");
		// 7.send service
		return HttpClient.service(HttpClient.POST, url, new Gson().toJson(map), header);

	}

	public static void setKey(String priKey, String pubKey) {
		HttpClientSec.priKey = priKey;
		HttpClientSec.pubKey = pubKey;
	}

}
