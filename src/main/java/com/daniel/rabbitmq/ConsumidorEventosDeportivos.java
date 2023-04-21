package com.daniel.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventosDeportivos {

    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //abrir conexion
        Connection connection = connectionFactory.newConnection();
        //establecer canal
        Channel channel = connection.createChannel();
        //crear exchange eventos
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
        //crear cola y asoiarla al exchange "eventos-deportivos"
        String queueName = channel.queueDeclare().getQueue();
        // patron routingKey-> country.sport.eventType
        // +-> identifica una palabra
        // # -> identifica multiples palabras
        // recibir todos los mensajes relacionados con el deporte tenis
        // *.tenis.*
        //todos los eventos de colombia => col.# / col.*.*
        Scanner scanner= new Scanner(System.in);
        System.out.println("Digite el routingKey por favor");
        String routingKey = scanner.nextLine().trim();
        channel.queueBind(queueName, EXCHANGE, routingKey);
        //crear subscripcion a una cola asociada al exchange "eventos-deportivos"
        channel.basicConsume(queueName, true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje recibido: " + messageBody);
//                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
                    System.out.println("Routing Key: " + message.getEnvelope().getRoutingKey());
//                    System.out.println("Delivery tag: " + message.getEnvelope().getDeliveryTag());
                }, consumerTag -> {
                    System.out.println("consumer " + consumerTag + " cancelado");
                });

    }
}
