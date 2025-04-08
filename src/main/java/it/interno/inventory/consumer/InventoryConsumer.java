package it.interno.inventory.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.interno.common.lib.model.OrderDto;
import it.interno.common.lib.model.OrderFailedEvent;
import it.interno.common.lib.model.OrderSuccessEvent;
import it.interno.common.lib.util.Utility;
import it.interno.inventory.producer.InventoryProducer;
import it.interno.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryConsumer {

    private static final String TOPIC = "order-receive";

    @Autowired
    private InventoryProducer inventoryProducer;

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = "order-send", groupId = "inventory-group")
    public void consumeOrderSend(ConsumerRecord<String, OrderDto> element) {

        OrderDto orderDto = element.value();

        try{

            boolean disponibilitaProdotti = inventoryService.verificaDisponibilitaProdotti(
                    orderDto.getElencoProdotti(),
                    orderDto.getIdOrdine(),
                    Utility.convertStringToTimestamp(orderDto.getTsInserimento()),
                    orderDto.getIdUteIns());

            if(disponibilitaProdotti) {
                orderDto.setIdStato(2);
                inventoryProducer.sendToPayment(orderDto);
            }else{
                OrderFailedEvent orderFailureDto = new OrderFailedEvent() ;
                orderDto.setIdStato(6);
                BeanUtils.copyProperties(orderDto, orderFailureDto);
                inventoryProducer.sendToOrder(orderFailureDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }

    }

    @KafkaListener(topics = "inventory-receive", groupId = "inventory-group")
    public void consumePaymentReceive(ConsumerRecord<String, OrderDto> element) {

        try{

//            if(element.value() instanceof OrderSuccessEvent) {
//                OrderSuccessEvent orderDto = (OrderSuccessEvent) element.value();
//                inventoryProducer.sendToOrder(orderDto);
//            }else if(element.value() instanceof OrderFailedEvent) {
            if(element.value() instanceof OrderFailedEvent) {
                OrderFailedEvent orderDto = (OrderFailedEvent) element.value();

                inventoryService.fallimentoOrdine(orderDto.getElencoProdotti(), orderDto.getIdOrdine(),
                        Utility.convertStringToTimestamp(orderDto.getTsInserimento()), orderDto.getIdUteIns());

//                inventoryProducer.sendToOrder(orderDto);
            }

            inventoryProducer.sendToOrder(element.value());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
