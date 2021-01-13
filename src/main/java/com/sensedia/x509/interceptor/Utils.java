package com.sensedia.x509.interceptor;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sensedia.interceptor.externaljar.annotation.ApiSuiteInterceptor;
import com.sensedia.interceptor.externaljar.annotation.InterceptorMethod;
import com.sensedia.interceptor.externaljar.dto.ApiCallData;

/**
 * 
 * This class provides utility methods for handling x509 certificates.
 * 
 * @author Mario Mancuso
 * 
 */
@ApiSuiteInterceptor
public class Utils {
	
	/*
	 * Decodes client's x509 certificate from the x-forwarded-client-cert header of an api call.
	 */
	@InterceptorMethod
	public void decodeCerts(ApiCallData call) throws Exception {

		Map<String, Object> clientCertMap = getClientCertMap(call.request.getHeader("x-forwarded-client-cert"));
		clientCertMap.put("x509Info", readX509Cert(clientCertMap.get("cert").toString()));

		JSONObject clintCert = new JSONObject(clientCertMap);
		
		/*
		 * Set the client's certificate content on request body to be use on api flow.
		 * This is only an example of use case, the certificate information can be used
		 * for others cases, e.g.: set in a header, response body, content variable, etc.
		 */
		call.request.getBody().setString(clintCert.toString(), StandardCharsets.UTF_8.name());

	}
	
	/*
	 * Maps informations coming from x-forwarded-client-cert header.
	 */
	private Map<String, Object> getClientCertMap(String header) throws UnsupportedEncodingException {

		String[] clientCertHeader = header.replaceAll("\"", "").split(";");

		Map<String, Object> clientCertMap = new HashMap<String, Object>();

		for (int i = 0; i < clientCertHeader.length; i++) {
			if (!clientCertHeader[i].contains("Subject")) {
				String[] atribute = clientCertHeader[i].split("=");
				clientCertMap.put(atribute[0].toLowerCase(), atribute.length > 1 ? atribute[1] : "");
			}

		}
		
		return clientCertMap;
	}
	
	/*
	 * Reads and maps information form a X509 Certificate.
	 * This method can return maps to any information provided by a java.security.cert.X509Certificate class.
	 * For more information, see: 
	 * https://docs.oracle.com/javase/8/docs/api/java/security/cert/X509Certificate.html
	 */
	private Map<String, String> readX509Cert(String cert) throws Exception {

		cert = URLDecoder.decode(cert, StandardCharsets.UTF_8.name()).replace("-----BEGIN CERTIFICATE-----", "")
				.replaceAll(System.lineSeparator(), "").replace("-----END CERTIFICATE-----", "");

		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(cert));
		X509Certificate crtx509Cert = (X509Certificate) CertificateFactory.getInstance("X509")
				.generateCertificate(inputStream);

		Map<String, String> crtx509CertMap = new HashMap<String, String>();
		crtx509CertMap.put("subject", crtx509Cert.getSubjectX500Principal().toString());
		crtx509CertMap.put("signatureAlgotithm", crtx509Cert.getSigAlgName());
		crtx509CertMap.put("validFrom", crtx509Cert.getNotBefore().toString());
		crtx509CertMap.put("validUntil", crtx509Cert.getNotAfter().toString());
		crtx509CertMap.put("issuer", crtx509Cert.getIssuerX500Principal().toString());
		crtx509CertMap.put("serialNumber", crtx509Cert.getSerialNumber().toString());

		return crtx509CertMap;
	}

}
