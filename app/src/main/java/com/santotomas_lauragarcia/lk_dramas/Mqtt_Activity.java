package com.santotomas_lauragarcia.lk_dramas;

import androidx.appcompat.app.AppCompatActivity;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.w3c.dom.Text;
public class Mqtt_Activity extends AppCompatActivity {
    private String clienteId="";
    //Conexión al servidor MQTT
    private static String mqttHost = "tcp://lkdramas-mqtt.cloud.shiftr.io:1883";
    private static String mqttUser = "lkdramas-mqtt";
    private static String mqttPass = "lBhAmkWFHPopsf1r";

    private MqttAndroidClient cliente;
    private MqttConnectOptions opciones;
    private static String topic = "LED";
    private static String topicMsgOn = "Encender";
    private static String topicMsgOff = "Apagar";

    private boolean permisoPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        Button btnOn = findViewById(R.id.btnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMensaje(topic, topicMsgOn);
            }
        });

        Button btnOff = findViewById(R.id.btnOff);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMensaje(topic, topicMsgOff);
            }
        });

        getNombreCliente();
        connectBroker();
    }

    private void checkConnection(){
        if(this.cliente.isConnected()){
            this.permisoPublicar = true;
        }

        else{
            this.permisoPublicar = false;
            connectBroker();
        }
    }

    private void enviarMensaje(String topic, String msg){
        checkConnection();
        try{
            int qos = 0; //puede ser 0 (envía una vez), 1(envía hasta recibir) o 2(envía hasta que el cliente confirme)
            this.cliente.publish(topic, msg.getBytes(), qos, false);
            Toast.makeText(getBaseContext(), topic + ":" + msg, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void connectBroker(){
        this.cliente = new MqttAndroidClient(this.getApplicationContext(), mqttHost, this.clienteId);
        this.opciones = new MqttConnectOptions();
        this.opciones.setUserName(mqttUser);
        this.opciones.setPassword(mqttPass.toCharArray());
        try{
            IMqttToken token = this.cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Mqtt_Activity.this, "Conectado", Toast.LENGTH_SHORT).show();
                    suscribirseTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Mqtt_Activity.this, "Falló conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }

        catch (MqttException e){
            e.printStackTrace();
        }
    }

    private void suscribirseTopic(){
        try {
            this.cliente.subscribe(topic, 0);
        }
        catch (MqttSecurityException e){
            e.printStackTrace();
        }
        catch (MqttException e){
            e.printStackTrace();
        }
        this.cliente.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getBaseContext(), "Se desconectó el servidor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView txtInfo = findViewById(R.id.txtInfo);
                if(topic.matches(topic)){
                    String msg = new String(message.getPayload());
                    if(msg.matches(topicMsgOn)){
                        txtInfo.setText(msg);
                        txtInfo.setBackgroundColor(GREEN);
                    }
                    else if(msg.matches(topicMsgOff)){
                        txtInfo.setText(msg);
                        txtInfo.setBackgroundColor(RED);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }
    private void getNombreCliente(){
        String manufacturer = Build.MANUFACTURER;
        String modelName = Build.MODEL;
        this.clienteId = manufacturer + " " + modelName;
        TextView txtIdCliente = findViewById(R.id.txtIdCliente);
        txtIdCliente.setText(this.clienteId);
    }



}
