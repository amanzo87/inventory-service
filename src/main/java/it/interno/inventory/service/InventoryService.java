package it.interno.inventory.service;

import it.interno.common.lib.model.ProductDto;

import java.sql.Timestamp;
import java.util.List;

public interface InventoryService {

    boolean verificaDisponibilitaProdotti(List<ProductDto> elencoProdotti, Integer idOrdine, Timestamp tsInserimentoOrdine, String idUtente);

    void fallimentoOrdine(List<ProductDto> elencoProdotti, Integer idOrdine, Timestamp tsInserimentoOrdine, String idUtente);

}
