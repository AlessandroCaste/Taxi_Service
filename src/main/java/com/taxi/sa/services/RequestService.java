package com.taxi.sa.services;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.taxi.sa.parsing.output.user.UserRequest;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RequestService {

    public String request(String cityId,UserRequest userRequest) {
        TcpClient tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS));
                });

        WebClient client = WebClient.builder()
                .baseUrl("http://serviceB:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();

        ClientResponse clientResponse = Objects.requireNonNull(client.post()
                .uri("/process_request/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userRequest))
                .exchange()
                .block());

        // Properly formatting the response string
        String responseString = clientResponse.bodyToMono(String.class).block();
        return new Gson().toJson(JsonParser.parseString(responseString));
    }

}

