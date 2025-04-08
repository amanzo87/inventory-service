package it.interno.inventory.producer;

import it.interno.common.lib.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryProducer {

    private static final String TOPIC_SEND_INVENTORY = "inventory-send";
    private static final String TOPIC_SEND_ORDER = "order-receive";

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToPayment(OrderDto orderDto) {

        try{
//            ObjectMapper objectMapper = new ObjectMapper();

            //orderDto.setIdStato(2) ; //"PRODOTTI PRESENTI IN MAGAZZINO PER ORDINE "+ orderDto.getIdOrdine());

            kafkaTemplate.send(TOPIC_SEND_INVENTORY, orderDto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void sendToOrder(OrderDto orderDto) {

        try{

            kafkaTemplate.send(TOPIC_SEND_ORDER, orderDto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
