package com.pqt.client.module.gui.ressources.strings;

import com.pqt.core.entities.product.Product;

//TODO faire ça un peu mieux
public class GUIStringTool {
    public static String getValidationButtonLabel() {
        return "Valider";
    }

    public static String getConfirmationValidationButtonLabel() {
        return "Confirmer";
    }

    public static String getCancelButtonLabel() {
        return "Annuler";
    }

    public static String getConfirmationCancelButtonLabel() {
        return "Confirmer";
    }

    public static String getCategorytabStockDisplayerTitle() {
        return "Produits";
    }

    public static IObjectStringRenderer<Product> getProductStringRenderer(){
        return product->String.format("%s - %.2f€ (%s)", product.getName(), product.getPrice(), (product.getAmountRemaining()>=30?"30+": Integer.toString(product.getAmountRemaining())));
    }

    public static String getCommandComposerTitleTitle() {
        return "Commande";
    }

    public static IObjectWithQuantityStringRenderer<Product> getSaleItemStringRenderer(){
        return (product, qté)->String.format("%s - %.2f€ (%s)", product.getName(), product.getPrice(), (product.getAmountRemaining()>=30?"30+": Integer.toString(product.getAmountRemaining())));
    }
}


