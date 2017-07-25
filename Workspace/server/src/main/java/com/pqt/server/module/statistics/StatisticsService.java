package com.pqt.server.module.statistics;

import com.pqt.core.entities.product.Product;
import com.pqt.server.module.stock.StockService;
import com.pqt.server.module.sale.SaleService;

import java.util.List;

//TODO écrire Javadoc
//TODO Ajouter logs
//TODO écrire contenu méthodes
public class StatisticsService {

    private StockService stockService;
    private SaleService saleService;

    public StatisticsService(StockService stockService, SaleService saleService) {
        this.stockService = stockService;
        this.saleService = saleService;
    }

    public int getTotalAmountSale() {
		return 0;
	}

	public double getTotalMoneyMade() {
		return 0;
	}

	public double getTotalSaleWorth() {
		return 0;
	}

	public List<Product> getTopPopularProducts(int amount) {
		return null;
	}

	public int getTotalAmountStaffSales() {
		return 0;
	}

	public int getTotalAmountGuestSale() {
		return 0;
	}

}
