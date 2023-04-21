package com.daniel.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ProductorEventosDeportivo {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexion amq y establecer canal
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            //clasificados por pais
            List<String> countries = Arrays.asList("col", "fr", "usa", "es");
            //clasificado por deporte
            List<String> sports = Arrays.asList("futbol", "tenis", "voleibol");
            //clasificado por un tipo de evento
            List<String> eventTypes = Arrays.asList("noticias", "envivos");
            //crear topic exchange "eventos-deportivos"
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            //Enviar mensajes al topic exchange "eventos-deportivos"
            int i = 1;
            while (i < 100000000) {


                shuffle(countries, sports, eventTypes);
                String country = countries.get(0);
                String sport = sports.get(0);
                String eventType = eventTypes.get(0);
                String message = "Evento" + i;
                //routingKey-> country.sport.eventType
                String routingKey = country + "." + sport + "." + eventType;
                System.out.println("Produciendo mensaje (" + country + "," + sport + "," + eventType + ") :" + message);
                message=message+sport;
                System.out.println("este es el mensaje: " + message+"-"+sport);
                channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes());
                Thread.sleep(1000);
                i++;

            }
        }


    }

    private static void shuffle(List<String> countries, List<String> sports, List<String> eventTypes) {
        Collections.shuffle(sports);
        Collections.shuffle(countries);
        Collections.shuffle(eventTypes);
    }

}
