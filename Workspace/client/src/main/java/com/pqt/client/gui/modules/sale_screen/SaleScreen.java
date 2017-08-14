package com.pqt.client.gui.modules.sale_screen;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.sale.SaleService;
import com.pqt.client.module.stock.StockService;
import javafx.scene.layout.Pane;

public class SaleScreen implements IFXComponent {

    private SaleScreenView view;

    public SaleScreen(AccountService accountService, StockService stockService, SaleService saleService) {
        SaleScreenModel model = new SaleScreenModel(accountService, stockService, saleService);
        SaleScreenController ctrl = new SaleScreenController(model);
        view = new SaleScreenView(ctrl);

        ctrl.setView(view);
        ctrl.updateView();
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
