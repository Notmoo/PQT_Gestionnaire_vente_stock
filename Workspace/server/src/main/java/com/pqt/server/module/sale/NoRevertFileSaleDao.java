package com.pqt.server.module.sale;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.LightweightSale;
import com.pqt.core.entities.sale.Sale;
import com.pqt.server.module.stock.StockService;
import com.pqt.server.tools.FileUtil;
import com.pqt.server.tools.entities.SaleContent;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implémentation de l'interface {@link ISaleDao} utilisant un fichier comme moyen pour assurer la persistance des
 * données relatives aux commandes validées. <b>Cette implémentation ne supporte pas le rollback de commandes</b>.
 * <p/>.
 * La persistance des données est faite de sorte que ces données soient lisibles par un humain lorsque le fichier est
 * ouvert avecc un éditeur de texte quelconque.
 * <p/>
 * Les identifiants attribués aux commandes validées sont incrémentés à chaque fois, en se basant soit sur la dernière
 * valeur attribuée (lue depuis le fichier de sauvegarde lors de l'instantiation), soit sur une valeur par défaut. Ils
 * sont tous <b>positifs et non-nuls</b>.
 */
public class NoRevertFileSaleDao implements ISaleDao {

    private static final String SALE_LOG_FILE_NAME = "sale_log.txt";
    private final String SALE_LOG_FILE_FOLDER_PATH;
    private static final long DEFAULT_SALE_ID = 0; //équivaut à la valeur du premier id - 1
    private StockService stockService;
    private long nextSaleId;
    private ISaleRenderer renderer;

    NoRevertFileSaleDao(StockService stockService, String ressourceFolderPathStr) {
        SALE_LOG_FILE_FOLDER_PATH = ressourceFolderPathStr;
        this.stockService = stockService;
        this.renderer = getRenderer();
        nextSaleId = readLastSaleIdFromFile()+1;
    }

    @Override
    public long submitSale(Sale sale) {
        boolean valid = true;
        Iterator<Product> it = sale.getProducts().keySet().iterator();
        while(valid && it.hasNext()){
            Product p = it.next();
            Product product = stockService.getProduct(p.getId());
            valid = product!=null
                    && p.equals(product)
                    && product.isSellable()
                    && product.getAmountRemaining()>=sale.getProducts().get(p);
        }

        if(!valid)
            return -1;

        long saleId = nextSaleId;
        stockService.applySale(new SaleContent(sale));
        logSale(sale, new Date(), saleId);
        generateNextSaleId();
        return saleId;
    }

    public Sale convert(LightweightSale lwSale){
        Map<Product, Integer> products = new HashMap<>();

        lwSale.getProducts().keySet()
                .forEach(pId->{
                    Product p = stockService.getProduct(pId);
                    if(p!=null)
                        products.put(p, lwSale.getProducts().get(pId));
                });
        if(products.keySet().size()!=lwSale.getProducts().keySet().size())
            return null;

        return new Sale(lwSale.getId(), products, lwSale.getOrderedWith(), lwSale.getOrderedBy(), lwSale.getOrderedFor(), lwSale.getType(), lwSale.getStatus());
    }

    private void generateNextSaleId() {
        nextSaleId++;
    }

    /**
     * Read the last sale id written in the log file with title {@link #SALE_LOG_FILE_NAME} or a default value if such id has not been found.
     * <p/>
     * Different reasons why this method may not find any id :<br/>
     *  - file does not exist<br/>
     *  - file is empty<br/>
     *  - file does not respect the expected syntax for writing sales' data<br/>
     * <p/>
     * The log file with title {@link #SALE_LOG_FILE_NAME} is not created by this method if it doesn't exist yet.
     * @return last sale id used in the log file, or -1 if none was found.
     */
    private long readLastSaleIdFromFile(){
        long id  = DEFAULT_SALE_ID;
        if(FileUtil.exist(getLogFilePath())){
            try(ReversedLinesFileReader rlfr = new ReversedLinesFileReader(new File("SALE_LOG_FILE_NAME"))){
                boolean stop = false;
                do{
                    try {
                        String line = rlfr.readLine();
                        if(line.matches("^[0-9]+$")){
                            id = Long.parseLong(line.substring(1));
                            stop = true;
                        }
                    }catch (EOFException e){
                        stop = true;
                    }
                }while(!stop);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }else{
            return id;
        }
    }

    @Override
    public boolean isSaleRevertSupported() {
        return false;
    }

    @Override
    public boolean submitSaleRevert(long id) {
        //TODO Créer un nouveau dao qui supporte le revert
        throw new UnsupportedOperationException("Le revert de commandes n'est pas supporté");
    }

    private void logSale(Sale sale, Date date, long saleId){
        try(FileOutputStream fos = new FileOutputStream(getLogFilePath());
            PrintWriter pw = new PrintWriter(fos)){

            pw.append(renderer.render(sale, date, saleId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ISaleRenderer getRenderer(){
        return(sale, date, id)->{
            StringBuffer sb = new StringBuffer("\n#").append(id).append("\n");
            String separator = "-----";
            DateFormat dateFormat = new SimpleDateFormat("<dd/ww/yyyy - HH:mm:ss>");

            sb.append("type : ").append(sale.getType().name()).append("\n");
            sb.append("at : ").append(dateFormat.format(date)).append("\n");
            if(sale.getOrderedBy()!=null)
                sb.append("by : ").append(sale.getOrderedBy().getUsername()).append("(").append(sale.getOrderedBy().getPermissionLevel().name()).append(")").append("\n");
            if(sale.getOrderedFor()!=null)
                sb.append("for : ").append(sale.getOrderedFor().getUsername()).append("(").append(sale.getOrderedFor().getPermissionLevel().name()).append(")").append("\n");
            sb.append(separator).append("\n");
            sb.append("Products : \n");
            sale.getProducts().keySet().forEach(p->{
                int productAmount = sale.getProducts().get(p);
                sb.append(String.format(" * %s (%du, %f€) : %d remaining in stock", p.getName(), productAmount, p.getPrice()*(double)productAmount, p.getAmountRemaining()-productAmount)).append("\n");
            });
            sb.append(separator);
            return sb.toString();
        };
    }

    private interface ISaleRenderer{
        String render(Sale sale, Date date, long saleId);
    }

    private String getLogFilePath(){
        return SALE_LOG_FILE_FOLDER_PATH + File.separator + SALE_LOG_FILE_NAME;
    }
}
