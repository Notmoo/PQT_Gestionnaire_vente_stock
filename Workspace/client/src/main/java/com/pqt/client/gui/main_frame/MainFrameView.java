package com.pqt.client.gui.main_frame;

import com.pqt.client.gui.ressources.components.AccountManager;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.others.SideBar;
import com.pqt.client.gui.ressources.components.generics.others.listeners.ISideBarListener;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;

class MainFrameView implements IFXComponent{

    private final MainFrameController ctrl;

    private BorderPane mainPane;
    private AccountManager accountManager;
    private VBox buttonHolder;
    private ObjectProperty<AccountLevel> currentAccountLevel;

    MainFrameView(MainFrameController ctrl) {
        this.ctrl = ctrl;
        currentAccountLevel = new SimpleObjectProperty<>(AccountLevel.getLowest());
        initGui();
    }

    private void initGui(){
        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("main-module-pane", "main-frame");

        buttonHolder = new VBox();

        SideBar sidebar = new SideBar();
        sidebar.setFillWidth(true);
        SideBar.setVgrow(buttonHolder, Priority.ALWAYS);
        buttonHolder.prefWidthProperty().bind(sidebar.widthProperty());
        sidebar.getChildren().add(buttonHolder);

        accountManager = new AccountManager();
        accountManager.addListener(ctrl.getAccountManagerValidatorListener());
        accountManager.addListener(ctrl.getAccountManagerAccountListener());
        accountManager.getPane().prefWidthProperty().bind(sidebar.widthProperty());
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

    void addGuiModule(String moduleName, Pane moduleContent, AccountLevel requiredLevel){
        Button button = new Button(moduleName);
        button.getStyleClass().add("menu-button");
        button.setOnMouseClicked(event->{
            buttonHolder.getChildren()
                    .stream()
                    .filter(Button.class::isInstance)
                    .map(Button.class::cast)
                    .forEach(b-> b.getStyleClass().remove("menu-button-selected"));
            button.getStyleClass().add("menu-button-selected");
            Platform.runLater(()->{
                buttonHolder.getChildren().forEach(Node::applyCss);
                mainPane.setCenter(moduleContent);
            });
        });
        currentAccountLevel.addListener((obs, oldVal, newVal)->button.setDisable(requiredLevel.compareTo(newVal)>0));
        button.setDisable(requiredLevel.compareTo(currentAccountLevel.get())>0);
        buttonHolder.getChildren().add(button);
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

    void updateModuleButtonLock(AccountLevel level) {
        currentAccountLevel.setValue(level);
    }
}
