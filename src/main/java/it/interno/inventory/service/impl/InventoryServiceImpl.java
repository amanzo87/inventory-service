package it.interno.inventory.service.impl;

import it.interno.common.lib.model.ProductDto;
import it.interno.common.lib.util.Utility;
import it.interno.inventory.entity.Inventory;
import it.interno.inventory.entity.key.InventoryKey;
import it.interno.inventory.repository.InventoryRepository;
import it.interno.inventory.service.InventoryService;
import jakarta.transaction.Transactional;
import org.hibernate.engine.spi.EntityKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.beans.BeanProperty;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository ;

    @Transactional
    @Override
    public boolean verificaDisponibilitaProdotti(List<ProductDto> elencoProdotti, Integer idOrdine,
                                                 Timestamp tsInserimentoOrdine, String idUtente) {

        AtomicBoolean prodottiDisponibili = new AtomicBoolean(true);
        if(!CollectionUtils.isEmpty(elencoProdotti)) {
            elencoProdotti.forEach(productDto -> {
                if(prodottiDisponibili.get()) {
                    Timestamp tmspRif = Utility.getTimestamp();
                    InventoryKey inventoryKey = new InventoryKey();
                    inventoryKey.setIdProdotto(productDto.getIdProdotto());
                    inventoryKey.setTsInserimento(Utility.convertStringToTimestamp(productDto.getTsInserimento()));
                    inventoryRepository.findById(inventoryKey).ifPresentOrElse(inventory -> {
                                if("S".equals(inventory.getDisponibile())) {
                                    inventoryRepository.cancellazioneLogica(inventory.getIdProdotto(), tmspRif, idUtente);

                                    Inventory entityNew = inventory.clone();

                                    entityNew.setIdUtenteInserimento(idUtente);
                                    entityNew.setDisponibile("N");
                                    entityNew.setIdOrdine(idOrdine);
                                    entityNew.setTsInserimentoOrdine(tsInserimentoOrdine);
                                    entityNew.setTsInserimento(tmspRif);

                                    inventoryRepository.save(entityNew);

                                    productDto.setTsCancellazione(productDto.getTsInserimento());
                                    productDto.setTsInserimento(Utility.convertTimestampToString(tmspRif));
                                }else{
                                    prodottiDisponibili.set(false);
                                }
                            },
                            () -> prodottiDisponibili.set(false)
                    );
                }
            });
        }

        return prodottiDisponibili.get();
    }

    @Transactional
    @Override
    public void fallimentoOrdine(List<ProductDto> elencoProdotti, Integer idOrdine, Timestamp tsInserimentoOrdine,
                                    String idUtente) {

        if(!CollectionUtils.isEmpty(elencoProdotti)) {
            elencoProdotti.forEach(productDto -> {

                InventoryKey inventoryKey = new InventoryKey();

                inventoryKey.setIdProdotto(productDto.getIdProdotto());
                inventoryKey.setTsInserimento(Utility.convertStringToTimestamp(productDto.getTsInserimento()));

                inventoryRepository.deleteById(inventoryKey);

                inventoryRepository.riaperturaDisponibilitarProdotto(productDto.getIdProdotto(),
                        Utility.convertStringToTimestamp(productDto.getTsInserimento()));
            });
        }

//        return prodottiDisponibili.get();
    }

}
