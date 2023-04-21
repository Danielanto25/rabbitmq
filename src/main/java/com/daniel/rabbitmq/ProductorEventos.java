package com.daniel.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorEventos {

    public static final String EVENTOS = "eventos";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //Abrir conexion amq y establecer canal
        try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {

            //crear fanout exchange "eventos"
            channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);
            //Enviar mensajes al fanout exchange "eventos"
            int i = 1;
            while (i<100000000) {
                String message = "Evento"+ i;
                System.out.println("este es el mensaje: "+message);
                channel.basicPublish(EVENTOS,"",null, message.getBytes());
                Thread.sleep(1000);
                i++;
            }
        }



    }

}
