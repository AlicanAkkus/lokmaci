package com.caysever.lokmaci

import groovy.transform.builder.Builder
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@SpringBootApplication
class LokmaciApplication {

    static void main(String[] args) {
        SpringApplication.run(LokmaciApplication, args)
    }
}

@RestController
@RequestMapping("/api/v1/lokmaci")
class LokmaciController {

    String vowels = "aeıioöuü"
    String consonants = "bcçdfghjklmnprsştvyz"

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<String> createLokmaci() {
        Mono.just(LokmaciRandom.builder().pool(consonants).count(1).build().gen())
                .map { it.concat(LokmaciRandom.builder().pool(vowels).count(1).build().gen()) }
                .map { it.concat(LokmaciRandom.builder().pool(consonants).count(1).build().gen()) }
                .map { "LOKMA$it" }
                .map { (it as String).toUpperCase() }
    }
}

@Component
class LokmaciWebFilter implements WebFilter {

    @Override
    Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.request.getURI().path == "/") {
            return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build())
        }

        return chain.filter(exchange)
    }
}

@Builder
@Component
class LokmaciRandom {

    String pool
    int count

    String gen() {
        StringBuilder builder = new StringBuilder(8)

        count.times {
            builder.append(pool.getAt(new Random().nextInt(pool.length() - 1)))
        }

        return builder.toString()
    }
}