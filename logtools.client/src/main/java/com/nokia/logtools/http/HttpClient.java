package com.nokia.logtools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * 通信连接器工具类
 *
 * @author 
 * @date 
 */
public class HttpClient {

	public static final String POST = "POST";

	public static final String GET = "GET";

	public static final int HTTP_OK = 200;

	private static final RequestConfig config = RequestConfig.custom().setConnectTimeout( 10000 ).setSocketTimeout( 5000 ).build();

	/**
	 * use HttpURLConnection/HttpsURLConnection way.
	 * @param method : GET / POST
	 * @param url
	 * @return
	 */
	public static String service( String method, String url ) {
		return service( method, url, "", null );
	}

	/**
	 * use HttpURLConnection/HttpsURLConnection way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary. 
	 * @return
	 */
	public static String service( String method, String url, Map<String, String> data ) {
		return service( method, url, data, null );
	}

	/**
	 * use HttpURLConnection/HttpsURLConnection way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary. eg: id=111&name=Jason
	 * @return
	 */
	public static String service( String method, String url, String data ) {
		return service( method, url, data, null );
	}

	/**
	 * use HttpURLConnection/HttpsURLConnection way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary. 
	 * @param header : you can set parameter into header that server endpoint use request.getHeader can get your parameter. such as content-type.
	 * @return 
	 */
	public static String service( String method, String url, Map<String, String> data, Map<String, String> header ) {
		StringBuffer buffer = new StringBuffer();
		if( MapUtils.isNotEmpty( data ) ) {
			for( Map.Entry<String, String> entry : data.entrySet() ) {
				buffer.append( "&" ).append( entry.getKey() ).append( "=" ).append( entry.getValue() );
			}
		}

		String param = "";
		if( buffer.length() > 0 ) {
			param = buffer.substring( 1 );

		}

		return service( method, url, param, header );
	}

	/**
	 * use HttpURLConnection/HttpsURLConnection way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary. eg: id=111&name=Jason
	 * @param header : you can set parameter into header that server endpoint use request.getHeader can get your parameter. such as content-type.
	 * @return 
	 */
	public static String service( String method, String url, String data, Map<String, String> header ) {
		if( StringUtils.isAnyBlank( method, url ) ) {
			return "";
		}

		method = method.toUpperCase();
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URLConnection conn = null;

			if( !StringUtils.startsWithIgnoreCase( url, "https://" ) ) {
				conn = new URL( url ).openConnection();
				( ( HttpURLConnection )conn ).setRequestMethod( method );
			} else {
				HttpsURLConnection.setDefaultHostnameVerifier( hostnameVerifier );
				conn = new URL( url ).openConnection();
				( ( HttpsURLConnection )conn ).setRequestMethod( method );
				( ( HttpsURLConnection )conn ).setSSLSocketFactory( getSSLContext().getSocketFactory() );
			}

			setHeader( header, conn );
			conn.setDoInput( true );
			conn.setDoOutput( true );
			conn.setUseCaches( false );
			conn.setReadTimeout( 60000 );
			conn.setConnectTimeout( 60000 );

			if( StringUtils.isNotBlank( data ) && HttpClient.POST.equals( method ) ) {
				out = new PrintWriter( conn.getOutputStream() );
				out.print( data );
				out.flush();
			}

			int responseCode = 0;
			if( !StringUtils.startsWithIgnoreCase( url, "https://" ) ) {
				responseCode = ( ( HttpURLConnection )conn ).getResponseCode();
			} else {
				responseCode = ( ( HttpsURLConnection )conn ).getResponseCode();
			}

			if( HttpClient.HTTP_OK == responseCode ) {
				in = new BufferedReader( new InputStreamReader( conn.getInputStream(), "UTF-8" ) );
				StringBuffer buffer = new StringBuffer();
				String line;
				while( ( line = in.readLine() ) != null ) {
					buffer.append( line );
				}
				result = buffer.toString();
			}
		} catch( Exception e ) {
			throw new RuntimeException( e );
		} finally {
			if( null != out ) {
				out.close();
			}
			if( null != in ) {
				try {
					in.close();
				} catch( IOException e ) {
				}
			}
		}
		return result;
	}

	/**
	 * use HttpClientSec way.
	 * @param method : GET / POST
	 * @param url
	 * @return
	 */
	public static HttpResponse execute( String method, String url ) {
		return execute( method, url, new ArrayList<NameValuePair>(), null );
	}

	/**
	 * use HttpClientSec way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary.
	 * @return
	 */
	public static HttpResponse execute( String method, String url, Map<String, String> data ) {
		return execute( method, url, data, null );
	}

	/**
	 * use HttpClientSec way.
	 * @param method : GET / POST
	 * @param url
	 * @param nvpList : when method is post. it can contain param if necessary.
	 * @return
	 */
	public static HttpResponse execute( String method, String url, List<NameValuePair> nvpList ) {
		return execute( method, url, nvpList, null );
	}

	/**
	 * use HttpClientSec way.
	 * @param method : GET / POST
	 * @param url
	 * @param data : when method is post. it can contain param if necessary.
	 * @param header : you can set parameter into header that server endpoint use request.getHeader can get your parameter. such as content-type.
	 * @return
	 */
	public static HttpResponse execute( String method, String url, Map<String, String> data, Map<String, String> header ) {
		List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
		if( MapUtils.isNotEmpty( data ) ) {
			for( Map.Entry<String, String> entry : data.entrySet() ) {
				nvpList.add( new BasicNameValuePair( entry.getKey(), entry.getValue() ) );
			}
		}
		return execute( method, url, nvpList, header );
	}

	/**
	 * use HttpClientSec way.
	 * @param method : GET / POST
	 * @param url
	 * @param nvpList : when method is post. it can contain param if necessary.
	 * @param header : you can set parameter into header that server endpoint use request.getHeader can get your parameter. such as content-type.
	 * @return
	 */
	public static HttpResponse execute( String method, String url, List<NameValuePair> nvpList, Map<String, String> header ) {
		if( StringUtils.isBlank( url ) ) {
			return null;
		}
		try {
			org.apache.http.client.HttpClient httpClient = getHttpClient( url );
			HttpRequestBase request = null;
			if( HttpClient.GET.equalsIgnoreCase( method ) ) {
				request = new HttpGet( url );
			} else if( HttpClient.POST.equalsIgnoreCase( method ) ) {
				request = new HttpPost( url );
				( ( HttpPost )request ).setEntity( new UrlEncodedFormEntity( nvpList, "UTF-8" ) );
			} else {
				return null;
			}
			request.setConfig( config );
			setHeader( header, request );
			return httpClient.execute( request );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private static org.apache.http.client.HttpClient getHttpClient(String url ) throws Exception {
		org.apache.http.client.HttpClient httpclient = null;
		if( !StringUtils.startsWithIgnoreCase( url, "https://" ) ) {
			httpclient = HttpClientBuilder.create().build();
		} else {
			httpclient = HttpClientBuilder.create().setSSLContext( getSSLContext() ).setSSLHostnameVerifier( hostnameVerifier ).build();
		}
		return httpclient;
	}

	private static SSLContext getSSLContext() throws Exception {
		SSLContext context = SSLContext.getInstance( "TLS" );
		context.init( null, new TrustManager[] { X509TrustManager }, new SecureRandom() );
		return context;
	}

	private static <T> T setHeader( Map<String, String> header, T instance ) throws Exception {
		Class<?> clazz = instance.getClass();

		String methodName = "";
		if( instance instanceof HttpURLConnection || instance instanceof HttpsURLConnection ) {
			methodName = "setRequestProperty";
		} else if( instance instanceof HttpGet || instance instanceof HttpPost ) {
			methodName = "setHeader";
		}

		Method method = clazz.getMethod( methodName, String.class, String.class );
		if( MapUtils.isNotEmpty( header ) ) {
			for( Map.Entry<String, String> entry : header.entrySet() ) {
				method.invoke( instance, entry.getKey(), entry.getValue() );
			}
		}
		return instance;
	}

	private static TrustManager X509TrustManager = new X509TrustManager() {

		@Override
		public void checkClientTrusted( X509Certificate[] chain, String authType ) throws CertificateException {}

		@Override
		public void checkServerTrusted( X509Certificate[] chain, String authType ) throws CertificateException {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	};

	private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {

		@Override
		public boolean verify( String hostname, SSLSession session ) {
			return true;
		}

	};

}
