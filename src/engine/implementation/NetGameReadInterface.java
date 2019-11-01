package engine.implementation;

import engine.net.client.Message;

public interface NetGameReadInterface {

    void readTCP(Message message);  //Engine: Различные действия с уникальными индексами с пакетами TCP
    void readUDP(Message message); //Engine: Различные действия с уникальными индексами с пакетами UDP
}
