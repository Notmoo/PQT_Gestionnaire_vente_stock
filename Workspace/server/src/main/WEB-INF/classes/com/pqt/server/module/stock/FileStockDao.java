package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.server.tools.FileUtil;
import com.pqt.server.tools.entities.SaleContent;

import java.io.*;
import java.util.*;

//TODO écrire Javadoc
public class FileStockDao implements IStockDao {

    private static final String STOCK_FILE_NAME = "stock.pqt";
    private long nextProductId;
    private Random random;

	private Map<Long, Product> products;

    public FileStockDao() {
        random = new Random();
        loadFromFile();
        generateNextProductId();
    }

    private void generateNextProductId() {
        Long newId;
        do{
            newId = random.nextLong();
        }while (products.containsKey(newId));
        nextProductId = newId;
    }

    /**
	 * @see com.pqt.server.module.stock.IStockDao#getProductList()
	 */
	public List<Product> getProductList() {
        return copyOfProductList();
	}

    private List<Product> copyOfProductList() {
	    List<Product> copy = new ArrayList<>();
	    products.values().stream().forEach(p->copy.add(new Product(p)));
        return copy;
    }

    /**
	 * @see com.pqt.server.module.stock.IStockDao#getProduct(long)
	 */
	public Product getProduct(long id) {
        return products.get(id);
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#addProduct(com.pqt.core.entities.product.Product)
	 */
	public void addProduct(Product product) {
	    product.setId(nextProductId);
        this.products.put(nextProductId, product);
        generateNextProductId();
        save(this.products);
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#removeProduct(long)
	 */
	public void removeProduct(long id) {
        Product product = getProduct(id);
	    if(product!=null){
	        this.products.remove(product);
            save(this.products);
        }
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#modifyProduct(long, com.pqt.core.entities.product.Product)
	 */
	public void modifyProduct(long id, Product product) {
        if(this.products.containsKey(id)){
            product.setId(id);
            this.products.put(id, product);
        }
	}

    @Override
    public void applySale(SaleContent saleContent) throws IllegalArgumentException {
	    try {
            saleContent.getProductList().forEach(product -> applyRecursiveStockRemoval(product, saleContent.getProductAmount(product)));
        }catch (IllegalStateException e){
	        loadFromFile();
	        throw new IllegalArgumentException(e);
        }
    }

    private void applyRecursiveStockRemoval(Product product, int amount)throws IllegalStateException{
        Product correspondingProduct = getProduct(product.getId());
        if(correspondingProduct!=null) {
            correspondingProduct.setAmountSold(correspondingProduct.getAmountSold() + amount);
            correspondingProduct.setAmountRemaining(correspondingProduct.getAmountRemaining() - amount);
            correspondingProduct.getComponents().forEach(component -> applyRecursiveStockRemoval(component, amount));
        }else{
            StringBuffer sb = new StringBuffer("StockService>StockDao : Un produit vendu ne correspond pas à un produit connu : ");
            sb.append(product.getId()).append(" - ").append(product.getName()).append("(").append(product.getCategory()).append(")");
            throw new IllegalStateException(sb.toString());
        }
    }

    private void loadFromFile() {
        products = new HashMap<>(load());
    }

    private Map<Long, Product> load(){
        Map<Long, Product> loadedData = new HashMap<>();
        try{
            if(FileUtil.createFileIfNotExist(STOCK_FILE_NAME)){
                return loadedData;
            }
        }catch(IOException e){
            e.printStackTrace();
            return loadedData;
        }

        try(FileInputStream fis = new FileInputStream(STOCK_FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis)){

            boolean end = false;
            do{
                try{
                    Object obj = ois.readObject();
                    if(Product.class.isInstance(obj)){
                        Product p = Product.class.cast(obj);
                        loadedData.put(p.getId(), p);
                    }
                }catch (EOFException e){
                    end = true;
                }catch(ClassNotFoundException | InvalidClassException e){
                    e.printStackTrace();
                }
            }while(!end);
        }catch( IOException e){
                e.printStackTrace();
        }
	    return loadedData;
    }

    private void save(Map<Long, Product> products){
        try{
            FileUtil.createFileIfNotExist(STOCK_FILE_NAME);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        try(FileOutputStream fos = new FileOutputStream(STOCK_FILE_NAME);
                ObjectOutputStream oos = new ObjectOutputStream(fos)){

            products.values().stream().forEach(p -> {
                try {
                    oos.writeObject(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
