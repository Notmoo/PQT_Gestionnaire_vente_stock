package com.pqt.client.gui.modules.stock_screen;

import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.modules.stock_screen.product_manager_screen.ProductManagerScreen;
import com.pqt.client.gui.modules.stock_screen.product_manager_screen.ProductManagerScreenFactory;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.stock.StockService;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.scene.layout.Pane;

public class StockScreen implements IGuiModule {

    private StockScreenView view;

    public StockScreen(StockService stockService, AccountService accountService) {
        StockScreenModel model = new StockScreenModel(stockService, accountService);
        StockScreenController ctrl = new StockScreenController(model);
        view = new StockScreenView(ctrl, new ProductManagerScreenFactory(stockService));

        model.addListener(ctrl);
        ctrl.setView(view);
        ctrl.refreshView();
    }

    @Override
    public String getModuleName() {
        return "Stock";
    }

    @Override
    public AccountLevel getLowestRequiredAccountLevel() {
        return AccountLevel.WAITER;
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
