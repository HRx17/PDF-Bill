package com.kepsfret.Models;

public class ItemsModel {
    String quantity;
    String itemCode;
    String poids;
    String volume;
    String designation;
    String prixunit;
    String remise;
    String acompte;
    String total;
    String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAcompte() {
        return acompte;
    }

    public void setAcompte(String acompte) {
        this.acompte = acompte;
    }

    public ItemsModel() {

    }

    public ItemsModel(String quantity, String itemCode, String poids, String volume, String designation, String prixunit, String remise, String acompte, String total) {
        this.quantity = quantity;
        this.itemCode = itemCode;
        this.poids = poids;
        this.volume = volume;
        this.designation = designation;
        this.prixunit = prixunit;
        this.remise = remise;
        this.acompte = acompte;
        this.total = total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPrixunit() {
        return prixunit;
    }

    public void setPrixunit(String prixunit) {
        this.prixunit = prixunit;
    }

    public String getRemise() {
        return remise;
    }

    public void setRemise(String remise) {
        this.remise = remise;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
