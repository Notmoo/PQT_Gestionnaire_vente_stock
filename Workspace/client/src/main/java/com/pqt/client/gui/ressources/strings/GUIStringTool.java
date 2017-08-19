package com.pqt.client.gui.ressources.strings;

import com.pqt.core.entities.product.Category;
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

    public static IObjectStringRenderer<Product> getDetailledProductStringRenderer(){
        return product->{
            if(product!=null){
                String amountStr;
                if(product.getAmountRemaining()<=0){
                    amountStr = "OUT OF STOCK";
                }else if(product.getAmountRemaining()>=30){
                    amountStr = "30+";
                }else{
                    amountStr = Integer.toString(product.getAmountRemaining());
                }
                return String.format("%s - %.2f€ (%s)", product.getName(), product.getPrice(), amountStr);
            }else
                return "null";
        };
    }

    public static IObjectStringRenderer<Product> getSimpleProductStringRenderer(){
        return product->{
            if(product!=null)
                return String.format("%s - %.2f€", product.getName(), product.getPrice());
            else
                return "null";
        };
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
                if(object!=null)
                    return String.format("%s - (%s)", object.getUsername(), object.getPermissionLevel());

                return "null";
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

    public static String getAppTitle() {
        return "Client PQT - Gargotte";
    }

    public static String getSideBarCollapseButtonLabel() {
        return "Réduire";
    }

    public static String getSideBarExpandButtonLabel() {
        return "Menu";
    }

    public static String getSaleGuiModuleName() {
        return "Vente";
    }

    public static String getAddButtonLabel() {
        return "Ajouter";
    }

    public static String getDetailButtonLabel() {
        return "Détail";
    }

    public static String getRemoveButtonLabel() {
        return "Supprimer";
    }

    public static String getRefreshButtonLabel() {
        return "Rafraichir";
    }

    public static String getProductNameColumnHeader() {
        return "Nom";
    }

    public static String getProductCategoryColumnHeader() {
        return "Catégorie";
    }

    public static String getProductAmountRemainingColumnHeader() {
        return "Stock";
    }

    public static String getProductAmountSoldColumnHeader() {
        return "Vendu";
    }

    public static String getProductPriceColumnHeader() {
        return "Prix";
    }

    public static String getProductIsSellableColumnHeader() {
        return "Vendable";
    }

    public static IObjectStringRenderer<Boolean> getBooleanRenderer() {
        return bool->bool?"Oui":"Non";
    }

    public static String getStatGuiModuleName() {
        return "Statistiques";
    }

    public static String getProductNameLabel() {
        return "Nom : ";
    }

    public static String getProductCategoryLabel() {
        return "Catégorie : ";
    }

    public static String getProductAmountRemainingLabel() {
        return "En stock : ";
    }

    public static String getProductAmountSoldLabel() {
        return "Vendu : ";
    }

    public static String getProductSellableLabel() {
        return "Vendable : ";
    }

    public static String getProductPriceLabel() {
        return "Prix : ";
    }

    public static StringConverter<Category> getCategoryStringConverter() {
        return new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                if(object!=null)
                   return object.getName();
                else
                    return "";
            }

            @Override
            public Category fromString(String string) {
                if(string!=null)
                    return new Category(-1, string);
                else
                    return null;
            }
        };
    }

    public static String getComponentListTitleLabel() {
        return "Composants";
    }
}


