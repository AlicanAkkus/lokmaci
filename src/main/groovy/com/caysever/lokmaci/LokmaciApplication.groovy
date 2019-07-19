package com.caysever.lokmaci

import com.github.javafaker.Faker
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
    Mono<Map> createLokmaci() {
        def name = buildName()
        def address = buildAddress()

        return Mono.just([name: (name), address: (address.fullAddress())])
    }

    def buildAddress() {
        Faker faker = new Faker(new Locale("tr"))
        return faker.address()
    }

    def buildName() {
        def suffix = LokmaciRandom.gen(consonants, 1) + LokmaciRandom.gen(vowels, 1) + LokmaciRandom.gen(consonants, 1)
        return "LOKMA$suffix".toString().toUpperCase()
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
class LokmaciRandom {

    String pool
    int count

    static String gen(String pool, Integer count) {
        StringBuilder builder = new StringBuilder(8)

        count.times {
            builder.append(pool.getAt(new Random().nextInt(pool.length() - 1)))
        }

        return builder.toString()
    }
}