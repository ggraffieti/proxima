package org.gammf.proxima.dns;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class Postman {
    private static final String URL = "http://localhost:6041/dns/address";

    public static void main() {
        try {
            postRequest(computeJson("proxima.medical.firstAid", "192.168.0.1", 1406));
            postRequest(computeJson("proxima.medical.firstAid", "192.168.0.1", 1406));
            postRequest(computeJson("proxima.medical.exam", "192.168.0.3", 1406));
            postRequest(computeJson("proxima.commercial.supermarket.coop", "192.168.0.4", 1406));
            postRequest(computeJson("proxima.commercial.supermarket.conad", "192.168.0.5", 1406));
            postRequest(computeJson("proxima.commercial.supermarket.a&o", "192.168.0.6", 1406));
            postRequest(computeJson("proxima.commercial.restaurant.scottadito", "192.168.0.7", 1406));
            postRequest(computeJson("proxima.commercial.shop.prada", "192.168.0.8", 1406));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject computeJson(final String service, final String ip, final int port) {
        return new JSONObject().put("service", service).put("ip", ip).put("port", port);
    }

    private static void postRequest(final JSONObject message) throws IOException {
        final CloseableHttpClient client = HttpClientBuilder.create().build();
        final HttpPost request = new HttpPost(URL);
        request.setEntity(new StringEntity(message.toString()));
        client.execute(request);
    }
}