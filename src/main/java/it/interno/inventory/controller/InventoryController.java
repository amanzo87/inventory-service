package it.interno.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.interno.common.lib.StatoOrdine;
import it.interno.common.lib.model.OrderDto;
import it.interno.common.lib.util.Utility;
import it.interno.inventory.dto.ResponseDto;
import it.interno.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Operation(summary = "Verifica la disponibilità dei prodotti e in caso di esito positivo, prenota il prodotto")
    @PostMapping(path = "/verifica-disponibilita-prodotti")
    public ResponseEntity<ResponseDto<OrderDto>> verificaDisponibilitaProdotti(
            @RequestHeader String idUtente,
            @RequestBody OrderDto orderDto) {

        inventoryService.verificaDisponibilitaProdotti(orderDto, idUtente);

        ResponseDto<OrderDto> response = null;

        if(StatoOrdine.PRODOTTI_DISPONIBILI.getCodice() == orderDto.getIdStato()) {
            response = new ResponseDto<>(
                    HttpStatus.OK.value(), orderDto, null, null
            );
        }else if(StatoOrdine.PRODOTTO_NON_DISPONIBILE.getCodice() == orderDto.getIdStato()) {
            response = new ResponseDto<>(
                    HttpStatus.FAILED_DEPENDENCY.value(), orderDto, "Uno o più prodotti non sono disponibili", null
            );
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Callback per fallimento")
    @PostMapping(path = "/fallimento-ordine")
    public ResponseEntity<ResponseDto<String>> fallimentoOrdine(
            @RequestHeader String idUtente,
            @RequestBody OrderDto orderDto) {

        inventoryService.fallimentoOrdine(
                orderDto.getElencoProdotti(), orderDto.getIdOrdine(),
                Utility.convertStringToTimestamp(orderDto.getTsInserimento()), idUtente);

        ResponseDto<String> response = new ResponseDto<>(
                HttpStatus.OK.value(), "CANCELLAZIONE EFFETTUATA", null, null
        );

        return ResponseEntity.ok(response);
    }

}
