package com.pqt.client.gui.main_frame;

import com.pqt.client.gui.ressources.components.AccountManager;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.others.SideBar;
import com.pqt.client.gui.ressources.components.generics.others.listeners.ISideBarListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.user_account.Account;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.Collection;

class MainFrameView implements IFXComponent{

    private final MainFrameController ctrl;

    private BorderPane mainPane;
    private ToolBar moduleListToolbar;
    private AccountManager accountManager;

    MainFrameView(MainFrameController ctrl) {

        this.ctrl = ctrl;
        initGui();
    }

    private void initGui(){
        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("main-module-pane", "main-frame");

        moduleListToolbar = new ToolBar();
        moduleListToolbar.setOrientation(Orientation.VERTICAL);

        SideBar sidebar = new SideBar();
        sidebar.setFillWidth(true);
        SideBar.setVgrow(moduleListToolbar, Priority.ALWAYS);
        sidebar.getChildren().add(moduleListToolbar);

        accountManager = new AccountManager();
        accountManager.addListener(ctrl.getAccountManagerValidatorListener());
        accountManager.addListener(ctrl.getAccountManagerAccountListener());
        sidebar.getChildren().add(accountManager.getPane());

        mainPane.setLeft(sidebar);

        Button sidebarCtrl = new Button();
        if(sidebar.isExpanded())
            sidebarCtrl.setText(GUIStringTool.getSideBarCollapseButtonLabel());
        else
            sidebarCtrl.setText(GUIStringTool.getSideBarExpandButtonLabel());
        sidebarCtrl.setOnMouseClicked(event -> {
            if(sidebar.isExpanded())
                sidebar.collapse();
            else if(sidebar.isCollapsed())
                sidebar.expand();
        });
        sidebar.addListener(new ISideBarListener() {
            @Override
            public void onCollapsedFinished() {
                sidebarCtrl.setText(GUIStringTool.getSideBarExpandButtonLabel());
            }

            @Override
            public void onExpandFinished() {
                sidebarCtrl.setText(GUIStringTool.getSideBarCollapseButtonLabel());
            }
        });
        mainPane.setTop(sidebarCtrl);
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }

    void addGuiModule(String moduleName, Pane moduleContent){
        Button button = new Button(moduleName);
        button.setOnMouseClicked(event->mainPane.setCenter(moduleContent));
        moduleListToolbar.getItems().add(button);
    }

    boolean isAccountCreationPossible(){
        return accountManager.isCreationPossible();
    }

    Account create(){
        return accountManager.create();
    }

    void setCurrentAccount(Account account){
        accountManager.setCurrentAccount(account);
    }

    void feedAccountCollectionToManager(Collection<Account> accounts){
        accountManager.display(accounts);
    }
}
