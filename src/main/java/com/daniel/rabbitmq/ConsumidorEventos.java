package com.daniel.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventos {

    public static final String EVENTOS = "eventos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //abrir conexion
        Connection connection = connectionFactory.newConnection();
        //establecer canal
        Channel channel = connection.createChannel();
        //crear exchange eventos
        channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);
        //crear cola y asoiarla al exchange "eventos"
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EVENTOS,"");
        //crear subscripcion a una cola asociada al exchange "eventos"
        channel.basicConsume(queueName, true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje recibido: " + messageBody);
//                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
//                    System.out.println("Routing Key: " + message.getEnvelope().getRoutingKey());
//                    System.out.println("Delivery tag: " + message.getEnvelope().getDeliveryTag());
                }, consumerTag -> {
                    System.out.println("consumer " + consumerTag + " cancelado");
                });

    }
}
