package com.cryptobot.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.cryptobot.dto.CoinPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BinanceClient {
    private final HttpGet httpGet;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    public BinanceClient(@Value("${binance.api.getPrice}") String uri) {
        httpGet = new HttpGet(uri);
        mapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }
    public CoinPrice getBitcoinPrice() throws IOException {
        log.info("Performing client call to binanceApi to get bitcoin price");
        try {
            String response = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
            return CoinPrice.builder()
                    .price(mapper.readTree(response).path("price").asDouble())
                    .closeTime(mapper.readTree(response).path("closeTime").longValue())
                    .build();
        } catch (IOException e) {
            log.error("Error while getting price from binance", e);
            throw e;
        }
    }
}
