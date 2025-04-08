package it.interno.inventory.entity;

import it.interno.inventory.entity.key.InventoryKey;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(schema = "SHOP1", name = "INVENTORY")
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(InventoryKey.class)
public class Inventory implements Cloneable {

    @Id
    @Column(name = "ID_PRODUCT", columnDefinition = "INTEGER", nullable = false)
    Integer idProdotto ;

    @Id
    @Column(name = "TS_INSERIMENTO", columnDefinition = "TIMESTAMP", scale = 6, nullable = false)
    Timestamp tsInserimento;

    @Column(name = "DESC_PRODUCT", columnDefinition = "VARCHAR2", length = 100, nullable = false)
    String descrizioneProdotto;

    @Column(name = "ID_UTE_INS", columnDefinition = "CHAR", length = 8, nullable = false)
    String idUtenteInserimento;

    @Column(name = "ID_PRODUCT_CATEGORY", columnDefinition = "INTEGER", nullable = false)
    Integer idCategoriaProdotto;

    @Column(name = "TS_CANCELLAZIONE", columnDefinition = "TIMESTAMP", scale = 6)
    Timestamp tsCancellazione;

    @Column(name = "ID_UTE_CAN", columnDefinition = "CHAR", length = 8)
    String idUtenteCancellazione;

    @Column(name = "AVAILABLE", columnDefinition = "CHAR", length = 1, nullable = false)
    String disponibile;

    @Column(name = "ID_ORDER", columnDefinition = "INTEGER")
    Integer idOrdine ;

    @Column(name = "TS_INSERIMENTO_ORDER", columnDefinition = "TIMESTAMP", scale = 6)
    Timestamp tsInserimentoOrdine;

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public Timestamp getTsInserimento() {
        return tsInserimento;
    }

    public void setTsInserimento(Timestamp tsInserimento) {
        this.tsInserimento = tsInserimento;
    }

    public String getDescrizioneProdotto() {
        return descrizioneProdotto;
    }

    public void setDescrizioneProdotto(String descrizioneProdotto) {
        this.descrizioneProdotto = descrizioneProdotto;
    }

    public String getIdUtenteInserimento() {
        return idUtenteInserimento;
    }

    public void setIdUtenteInserimento(String idUtenteInserimento) {
        this.idUtenteInserimento = idUtenteInserimento;
    }

    public Integer getIdCategoriaProdotto() {
        return idCategoriaProdotto;
    }

    public void setIdCategoriaProdotto(Integer idCategoriaProdotto) {
        this.idCategoriaProdotto = idCategoriaProdotto;
    }

    public Timestamp getTsCancellazione() {
        return tsCancellazione;
    }

    public void setTsCancellazione(Timestamp tsCancellazione) {
        this.tsCancellazione = tsCancellazione;
    }

    public String getIdUtenteCancellazione() {
        return idUtenteCancellazione;
    }

    public void setIdUtenteCancellazione(String idUtenteCancellazione) {
        this.idUtenteCancellazione = idUtenteCancellazione;
    }

    public String getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(String disponibile) {
        this.disponibile = disponibile;
    }

    public Integer getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Timestamp getTsInserimentoOrdine() {
        return tsInserimentoOrdine;
    }

    public void setTsInserimentoOrdine(Timestamp tsInserimentoOrdine) {
        this.tsInserimentoOrdine = tsInserimentoOrdine;
    }

    @Override
    public Inventory clone() {
        try {
            Inventory clone = (Inventory) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
