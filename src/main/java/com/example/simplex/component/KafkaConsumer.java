package com.example.simplex.component;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class KafkaConsumer {

	@Autowired
	private MatchEngine matcher;
	@Autowired
	private WebsocketPush pusher;

	@KafkaListener(topics = "orderTopic", containerFactory = "kafkaListenerContainerFactory")
	public void onOrderRequest(List<ConsumerRecord<String, String>> records) {
		System.out.println("Kafka consumer: receive order topic ...");
		for (int i = 0; i < records.size(); ++i) {
			ConsumerRecord<String, String> record = records.get(i);
			System.out.println(record.value());
			matcher.placeOrder(JSON.parseObject(record.value()));
		}
	}
	
	@KafkaListener(topics = "orderUpdate", containerFactory = "kafkaListenerContainerFactory")
	public void onOrderUpdate(List<ConsumerRecord<String, String>> records) {
		System.out.println("Kafka consumer: receive order update ...");
		for (int i = 0; i < records.size(); ++i) {
			ConsumerRecord<String, String> record = records.get(i);
			System.out.println(record.value());
			pusher.doOrderUpdate(record.value());
		}
	}
	
	@KafkaListener(topics = "matchUpdate", containerFactory = "kafkaListenerContainerFactory")
	public void onMatchUpdate(List<ConsumerRecord<String, String>> records) {
		System.out.println("Kafka consumer: receive match update ...");
		for (int i = 0; i < records.size(); ++i) {
			ConsumerRecord<String, String> record = records.get(i);
			System.out.println(record.value());
			pusher.doMatchUpdate(record.value());
		}
		
	}

}
