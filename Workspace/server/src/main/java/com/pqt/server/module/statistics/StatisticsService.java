package com.pqt.server.module.statistics;

import com.pqt.core.entities.product.LightweightProduct;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.sale.Sale;
import com.pqt.server.module.sale.listeners.SaleListenerAdapter;
import com.pqt.server.module.stock.StockService;
import com.pqt.server.module.sale.SaleService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//TODO Issue #6 : Ajouter logs

/**
 * Cette classe correspond au sservices de statistiques du serveur, chargé de calculer et de mettre à disposition
 * diverses données concernant les ventes effectuées et les produits vendus.
 */
public class StatisticsService {

    private StockService stockService;

    private int totalSaleAmount, staffSaleAmount, guestSaleAmount;
    private double totalMoneyMade, totalSaleWorth, staffSaleWorth, guestSaleWorth;

    public StatisticsService(StockService stockService, SaleService saleService) {
        this.stockService = stockService;

        totalSaleAmount = 0;
        staffSaleAmount = 0;
        guestSaleAmount = 0;

        totalMoneyMade = 0;
        totalSaleWorth = 0;
        staffSaleWorth = 0;
        guestSaleWorth = 0;

        saleService.addListener(new SaleListenerAdapter() {
            @Override
            public void onSaleValidatedEvent(Sale sale) {
                double price = sale.getTotalPrice(), worth = sale.getTotalWorth();
                totalSaleWorth+=worth;
                totalMoneyMade+=price;
                totalSaleAmount++;
                switch (sale.getType()){
                    case OFFERED_GUEST:
                        guestSaleAmount++;
                        guestSaleWorth+=worth;
                        break;
                    case OFFERED_STAFF_MEMBER:
                        staffSaleAmount++;
                        staffSaleWorth+=worth;
                        break;
                }
            }
        });
    }

    public int getTotalAmountSale() {
		return totalSaleAmount;
	}

	public double getTotalMoneyMade() {
		return totalMoneyMade;
	}

	public double getTotalSaleWorth() {
		return totalSaleWorth;
	}

	public List<LightweightProduct> getTopPopularProducts(int amount) {
		return stockService.getProductList().stream()
                .sorted((prod1, prod2)->prod2.getAmountSold()-prod1.getAmountSold())
                .limit(amount)
                .map(LightweightProduct::new)
                .collect(Collectors.toList());
	}

	public int getStaffSaleAmount() {
		return staffSaleAmount;
	}

    public double getStaffSaleWorth() {
        return staffSaleWorth;
    }

    public int getGuestSaleAmount() {
        return guestSaleAmount;
    }

    public double getGuestSaleWorth() {
        return guestSaleWorth;
    }
}
