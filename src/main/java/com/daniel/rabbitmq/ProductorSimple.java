package com.daniel.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorSimple {
    public static void main(String[] args) throws IOException, TimeoutException {
        String mensaje = "Hola amiguitos";
        //abrir conexion AMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();

        try
            //tiene valores por defecto, si nuestros valores son diferentes, posee metodos set para sobreescribirlos
                (Connection connection = connectionFactory.newConnection();
                 //Establecer un canal
                 //aporta los metodos para poder consumir mensajes bindings entre otras
                 Channel canal = connection.createChannel()) {
            //Crear la cola
            canal.queueDeclare("primera-cola", false, false, false, null);
            //Enviar mensaje al exchange
            //se debe enviar el mensaje en bytes
            canal.basicPublish("", "primera-cola", null, mensaje.getBytes());
        }
    }
}
