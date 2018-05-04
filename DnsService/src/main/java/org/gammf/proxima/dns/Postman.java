package org.gammf.proxima.dns;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Simple java class that performs some post requests to the Domain Name System service, in order to submit the creation
 * of the addresses of a bunch of components that offer specifics services.
 */
public class Postman {
    private static final int PORT = 1406;
    private static final String PATH = "dns/address";


    public static void main(String[] args) {
        try {
            final String url = getUrl();
            postRequest(computeJson("proxima.medical.firstAid", "192.168.0.1", 1406), url);
            postRequest(computeJson("proxima.medical.firstAid", "192.168.0.2", 1407), url);
            postRequest(computeJson("proxima.medical.exam", "192.168.0.3", 1406), url);
            postRequest(computeJson("proxima.commercial.supermarket.coop", "192.168.0.4", 1406), url);
            postRequest(computeJson("proxima.commercial.supermarket.conad", "192.168.0.5", 1406), url);
            postRequest(computeJson("proxima.commercial.supermarket.a&o", "192.168.0.6", 1406), url);
            postRequest(computeJson("proxima.commercial.restaurant.scottadito", "192.168.0.7", 1406), url);
            postRequest(computeJson("proxima.commercial.shop.prada", "192.168.0.8", 1406), url);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static String getUrl() throws IOException {
        final JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("address.json"))));
        return "http://" + json.getString("ip") + ":" + PORT + "/" + PATH;
    }

    private static JSONObject computeJson(final String service, final String ip, final int port) {
        return new JSONObject().put("service", service).put("ip", ip).put("port", port);
    }

    private static void postRequest(final JSONObject message, final String url) throws IOException {
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(message.toString()));
        client.execute(request);
    }
}