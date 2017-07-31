package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.server.tools.entities.SaleContent;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;

import java.util.*;

//TODO écrire Javadoc
public class FileStockDao implements IStockDao {

    private static final String STOCK_FILE_NAME = "stock.pqt";
    private ISerialFileManager<Product> fileManager;
    private long nextProductId;
    private Random random;

	private Map<Long, Product> products;

    public FileStockDao() {
        random = new Random();
        fileManager = SimpleSerialFileManagerFactory.getFileManager(Product.class, STOCK_FILE_NAME);
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
        saveToFile();
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#removeProduct(long)
	 */
	public void removeProduct(long id) {
        Product product = getProduct(id);
	    if(product!=null){
	        this.products.remove(product);
	        saveToFile();
        }
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#modifyProduct(long, com.pqt.core.entities.product.Product)
	 */
	public void modifyProduct(long id, Product product) {
        if(this.products.containsKey(id)){
            product.setId(id);
            this.products.put(id, product);
            saveToFile();
        }
	}

    @Override
    public void applySale(SaleContent saleContent) throws IllegalArgumentException {
	    try {
            saleContent.getProductList().forEach(product -> {
                applyRecursiveStockRemoval(product, saleContent.getProductAmount(product));
                applySoldCounterIncrease(product, saleContent.getProductAmount(product));
            });
            saveToFile();
        }catch (IllegalStateException e){
	        loadFromFile();
	        throw new IllegalArgumentException(e);
        }
    }

    private void applySoldCounterIncrease(Product product, Integer amount) {
	    Product correspondingProduct = getProduct(product.getId());
	    if(correspondingProduct!=null){
	        correspondingProduct.setAmountSold(correspondingProduct.getAmountSold() + amount);
        }else{
            StringBuffer sb = new StringBuffer("StockService>StockDao : Un produit vendu ne correspond pas à un produit connu : ");
            sb.append(product.getId()).append(" - ").append(product.getName()).append("(").append(product.getCategory()).append(")");
            throw new IllegalStateException(sb.toString());
        }
    }

    private void applyRecursiveStockRemoval(Product product, int amount)throws IllegalStateException{
        Product correspondingProduct = getProduct(product.getId());
        if(correspondingProduct!=null) {
            correspondingProduct.setAmountRemaining(correspondingProduct.getAmountRemaining() - amount);
            correspondingProduct.getComponents().forEach(component -> applyRecursiveStockRemoval(component, amount));
        }else{
            StringBuffer sb = new StringBuffer("StockService>StockDao : Un produit vendu ne correspond pas à un produit connu : ");
            sb.append(product.getId()).append(" - ").append(product.getName()).append("(").append(product.getCategory()).append(")");
            throw new IllegalStateException(sb.toString());
        }
    }

    private void loadFromFile() {
        Map<Long, Product> loadedData = new HashMap<>();
        fileManager.loadListFromFile().forEach(product -> loadedData.put(product.getId(), product));
        products = new HashMap<>(loadedData);
    }

    private void saveToFile() {
	    fileManager.saveListToFile(new ArrayList<>(products.values()));
    }
}
