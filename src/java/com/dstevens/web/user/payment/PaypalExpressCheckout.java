package com.dstevens.web.user.payment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaypalExpressCheckout {

	@Value("${paypal.url:https://api-3t.sandbox.paypal.com/nvp}") private String url;
	@Value("${paypal.user:platfo_1255077030_biz_api1.gmail.com}") private String user;
	@Value("${paypal.password:1255077037}") private String password;
	@Value("${paypal.signature:Abg0gYcQyxQvnf2HDJkKtA-p6pqhA1k-KTYE0Gcy1diujFio4io5Vqjf}") private String signature;
	@Value("${paypal.version:78}") private String version;
	@Value("${paypal.returnHost:http://localhost:8080}") private String returnHost;
	
	public String setUpPayment(int amount) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("USER", user));
		nvps.add(new BasicNameValuePair("PWD", password));
		nvps.add(new BasicNameValuePair("SIGNATURE", signature));
		nvps.add(new BasicNameValuePair("METHOD", "SetExpressCheckout"));
		nvps.add(new BasicNameValuePair("VERSION", version));
		nvps.add(new BasicNameValuePair("PAYMENTREQUEST_0_PAYMENTACTION", "SALE"));
		nvps.add(new BasicNameValuePair("PAYMENTREQUEST_0_AMT", Integer.toString(amount)));
		nvps.add(new BasicNameValuePair("PAYMENTREQUEST_0_CURRENCYCODE", "USD"));
		nvps.add(new BasicNameValuePair("cancelUrl", returnHost + "/user/main"));
		nvps.add(new BasicNameValuePair("returnUrl", returnHost + "/user/page/patronage/payments/paypal/confirm"));
		
		try(CloseableHttpResponse response = client.execute(post)) {
			HttpEntity entity = response.getEntity();
			post.setEntity(new UrlEncodedFormEntity(nvps));
			List<NameValuePair> responsePairs = URLEncodedUtils.parse(EntityUtils.toString(entity), Charset.defaultCharset());
			EntityUtils.consume(entity);
			Map<String, String> responseMap = responsePairs.stream().collect(Collectors.toMap((NameValuePair nvp) -> nvp.getName(), (NameValuePair nvp) -> nvp.getValue()));
			if("Success".equalsIgnoreCase(responseMap.get("ACK"))) {
				return responseMap.get("TOKEN");
			}
			throw new IllegalStateException("Did not get a success message from Paypal. Response was " + responseMap);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
}