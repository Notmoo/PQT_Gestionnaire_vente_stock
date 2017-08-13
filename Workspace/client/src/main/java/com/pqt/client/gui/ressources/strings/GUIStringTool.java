package com.pqt.client.gui.ressources.strings;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.SaleStatus;
import com.pqt.core.entities.sale.SaleType;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.util.EnumSet;

//TODO faire ça un peu mieux
public class GUIStringTool {

    private static String saleIdLabel;

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
        return (product, qte)->String.format("%dx %s", qte, product.getName());
    }

    public static String getPasswordFieldPromptText() {
        return "mot de passe";
    }

    public static StringConverter<Account> getAccountStringConverter() {
        return new StringConverter<Account>() {
            @Override
            public String toString(Account object) {
                return String.format("%s - %s)", object.getUsername(), object.getPermissionLevel().name());
            }

            @Override
            public Account fromString(String string) {
                Account reply = new Account();

                String[] pieces = string.split(" - ");
                reply.setUsername(pieces[0]);
                if(pieces.length>1)
                    for(AccountLevel al : EnumSet.allOf(AccountLevel.class)){
                        if(al.name().equals(pieces[1]))
                            reply.setPermissionLevel(al);
                    }

                return reply;
            }
        };
    }

    public static String getLogoutButtonLabel() {
        return "Déconnexion";
    }

    public static String getLoginButtonLabel() {
        return "Connexion";
    }

    public static IObjectStringRenderer<Double> getPriceRenderer() {
        return price -> NumberFormat.getCurrencyInstance().format(price);
    }

    public static String getSaleMakerTextFieldPromptText() {
        return "Auteur";
    }

    public static String getSaleMakerTextFieldLabel() {
        return "Fait par : ";
    }

    public static String getSaleBeneficiaryTextFieldLabel() {
        return "Fait pour : ";
    }

    public static String getSaleTypeTextFieldLabel() {
        return "Type de paiement : ";
    }

    public static String getSalePriceTextFieldLabel() {
        return "Prix de la commande : ";
    }

    public static StringConverter<SaleType> getSaleTypeStringConverter() {
        return new StringConverter<SaleType>() {
            @Override
            public String toString(SaleType object) {
                return object.name();
            }

            @Override
            public SaleType fromString(String string) {
                return EnumSet.allOf(SaleType.class).stream().filter(type->type.name().equals(string)).findFirst().orElse(null);
            }
        };
    }

    public static String getSalePriceTextFieldPromptText() {
        return getPriceRenderer().render(0d);
    }

    public static String getCommandValidationErrorMessage() {
        return "La commande n'a pas pu être validée";
    }

    public static String getCommandValidationErrorMessage(Throwable cause) {
        return "La commande n'a pas pu être validée : "+cause.getMessage();
    }

    public static String getSaleIdLabel() {
        return "Numéro de commande : ";
    }

    public static String getSaleStatusLabel() {
        return "Etat actuel";
    }

    public static IObjectStringRenderer<SaleStatus> getSaleStatusRenderer() {
        return Enum::name;
    }

    public static String getOkButtonLabel() {
        return "OK";
    }
}


