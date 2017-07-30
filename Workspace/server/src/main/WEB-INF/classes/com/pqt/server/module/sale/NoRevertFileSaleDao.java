package com.pqt.server.module.sale;

import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.server.module.stock.StockService;
import com.pqt.server.utils.FileUtil;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class NoRevertFileSaleDao implements ISaleDao {

    private static final String SALE_LOG_FILE_NAME = "sale_log.txt";
    private StockService stockService;
    private long nextSaleId;
    private ISaleRenderer renderer;

    NoRevertFileSaleDao(StockService stockService) {
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
        stockService.applySale(sale.getProducts());
        logSale(sale, saleId);
        generateNextSaleId();
        return saleId;
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
        long id  = -1;
        if(FileUtil.exist(SALE_LOG_FILE_NAME)){
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
    public void submitSaleRevert(long id) {
        //TODO Créer un nouveau dao qui supporte le revert
        throw new UnsupportedOperationException("Le revert de commandes n'est pas supporté");
    }

    private void logSale(Sale sale, long saleId){
        try(FileOutputStream fos = new FileOutputStream(SALE_LOG_FILE_NAME);
            PrintWriter pw = new PrintWriter(fos)){

            pw.append(renderer.render(sale, saleId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ISaleRenderer getRenderer(){
        return(sale, id)->{
            StringBuffer sb = new StringBuffer("\n#").append(id).append("\n");
            String separator = "-----";
            DateFormat dateFormat = new SimpleDateFormat("<dd/ww/yyyy - HH:mm:ss>");

            sb.append("type : ").append(sale.getType().name()).append("\n");
            sb.append("at : ").append(dateFormat.format(sale.getOrderedAt())).append("\n");
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
        String render(Sale sale, long saleId);
    }
}
