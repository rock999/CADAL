package com.cadal.core;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class ConnPool {

	public static CloseableHttpClient getHttpClient() {

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);

		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(20000).setConnectTimeout(20000)
				.setSocketTimeout(20000).setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true).build();

		return HttpClients.custom().setDefaultRequestConfig(config)
				.setConnectionManager(cm).build();

	}

}
