package it.interno.inventory.repository;

import it.interno.inventory.entity.Inventory;
import it.interno.inventory.entity.key.InventoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryKey> {

    @Modifying
    @Query(value =
            "UPDATE SHOP1.INVENTORY " +
            "SET TS_CANCELLAZIONE = ?2 , " +
            "    ID_UTE_CAN = ?3 " +
            "WHERE" +
            "     ID_PRODUCT = ?1 " +
            " AND TS_CANCELLAZIONE IS NULL ", nativeQuery = true)
    void cancellazioneLogica(Integer idProduct, Timestamp tsCancellazione, String idUtenteCancellazione);

    @Modifying
    @Query(value =
            "UPDATE SHOP1.INVENTORY " +
            "SET TS_CANCELLAZIONE = NULL , " +
            "    ID_UTE_CAN = NULL " +
            "WHERE" +
            "     ID_PRODUCT = ?1 " +
            " AND TS_CANCELLAZIONE = ?2 ", nativeQuery = true)
    void riaperturaDisponibilitarProdotto(Integer idProduct, Timestamp tsCancellazione);

}
