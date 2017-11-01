package com.pqt.server.module.stock;

import com.pqt.core.entities.product.Product;
import com.pqt.server.tools.entities.SaleContent;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;

import java.lang.IllegalStateException;
import java.util.*;

/**
 * Implémentation de l'interface {@link IStockDao} utilisant un fichier de sauvegarde pour assurer la persistance des
 * données liées aux produits vendus.
 * <p/>
 * Les données sont écrites et lues dans le fichier grâce au méchanisme de sérialisation/désérialisation. Elles ne sont
 * pas faites pour être lisibles directement par un humain.
 *
 * @author Guillaume "Cess" Prost
 */
public class FileStockDao implements IStockDao {

    //TODO to modify
    private static final String STOCK_FILE_NAME = "G:\\temp\\stock.pqt";
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
	    products.values().forEach(p->copy.add(new Product(p)));
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
	public long addProduct(Product product) {
	    product.setId(nextProductId);
        this.products.put(nextProductId, product);
        long reply = nextProductId;
        generateNextProductId();
        saveToFile();
        return reply;
	}

	/**
	 * @see com.pqt.server.module.stock.IStockDao#removeProduct(long)
	 */
	public void removeProduct(long id) {
        Product product = getProduct(id);
	    if(product!=null){
	        this.products.remove(product.getId());
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
	    if(saleContent==null)
	        return;

	    try {
	        for(Product product : saleContent.getProductList()){
                applyRecursiveStockRemoval(product, saleContent.getProductAmount(product));
                applySoldCounterIncrease(product, saleContent.getProductAmount(product));
            }
            saveToFile();
        }catch (IllegalStateException e){
	        loadFromFile();
	        throw new IllegalArgumentException(e);
        }
    }

    /**
     * Cette méthode augmente le compteur de vente pour un produit donné dans la BDD.
     *
     * @param product données à utiliser pour déterminer le produit correspondant dans la BDD dont les données doivent
     *                être manipulées.
     * @param amount montant à ajouter
     * @throws IllegalStateException exception levée si le produit donné ne peut pas être trouvé dans la base de donnée.
     */
    private void applySoldCounterIncrease(Product product, Integer amount) throws IllegalStateException{
	    Product correspondingProduct = getProduct(product.getId());
	    if(correspondingProduct!=null){
	        correspondingProduct.setAmountSold(correspondingProduct.getAmountSold() + amount);
        }else{
            StringBuilder sb = new StringBuilder("StockService>StockDao : Un produit vendu ne correspond pas à un produit connu : ");
            sb.append(product.getId()).append(" - ").append(product.getName()).append("(").append(product.getCategory()).append(")");
            throw new IllegalStateException(sb.toString());
        }
    }

    /**
     * Cette méthode retire à un produit donné de la BDD le montant spécifié (diminue la valeur de
     * {@link Product#amountRemaining}), puis effectue récursivement la même opération pour tous les composants de ce
     * produit.
     *
     * @param product données à utiliser pour déterminer le produit correspondant dans la BDD dont les données doivent
     *                être manipulées.
     * @param amount montant à déduire
     * @throws IllegalStateException exception levée si le produit donné ne peut pas être trouvé dans la base de donnée.
     */
    private void applyRecursiveStockRemoval(Product product, int amount) throws IllegalStateException {
        Product correspondingProduct = getProduct(product.getId());
        if(correspondingProduct!=null) {
            correspondingProduct.setAmountRemaining(correspondingProduct.getAmountRemaining() - amount);
            correspondingProduct.getComponents().forEach(component -> applyRecursiveStockRemoval(component, amount));
        }else{
            StringBuilder sb = new StringBuilder("StockService>StockDao : Un produit vendu ne correspond pas à un produit connu : ");
            sb.append(product.getId()).append(" - ").append(product.getName()).append("(").append(product.getCategory()).append(")");
            throw new IllegalStateException(sb.toString());
        }
    }

    /**
     * Cette méthode charge les données relatives aux produits depuis le fichier de sauvegarde. Si ce fichier n'existe
     * pas, il est créé et la liste des produits est vidée.
     */
    private void loadFromFile() {
        Map<Long, Product> loadedData = new HashMap<>();
        fileManager.loadListFromFile().forEach(product -> loadedData.put(product.getId(), product));
        products = new HashMap<>(loadedData);
    }

    /**
     * Cette méthode écrit les données relatives aux produits dans le fichier de sauvegarde, écrasant le contenu
     * précédent.
     */
    private void saveToFile() {
	    fileManager.saveListToFile(new ArrayList<>(products.values()));
    }
}
