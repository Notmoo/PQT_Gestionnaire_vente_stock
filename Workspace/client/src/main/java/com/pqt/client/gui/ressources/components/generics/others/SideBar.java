package com.pqt.client.gui.ressources.components.generics.others;

import com.pqt.client.gui.ressources.components.generics.others.listeners.ISideBarListener;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.swing.event.EventListenerList;
import java.util.Arrays;
import java.util.List;

public class SideBar extends VBox {

    private static final StyleablePropertyFactory<SideBar> FACTORY = new StyleablePropertyFactory<>(VBox.getClassCssMetaData());

    private static final CssMetaData<SideBar, Number> EXPANDED_WIDTH =
            FACTORY.createSizeCssMetaData("-pqt-expanded-width",p->p.expandedWidth, 100, false);

    private StyleableProperty<Number> expandedWidth;
    private EventListenerList listenerList;
    private Animation collapseSideBar, expandSideBar;

    /** creates a sidebar containing a vertical alignment of the given nodes */
    public SideBar(Node... children) {
        listenerList = new EventListenerList();
        getStyleClass().add("sidebar");
        this.setMinWidth(0);
        this.expandedWidth = new SimpleStyleableDoubleProperty(EXPANDED_WIDTH, this, "expanded-width");
        this.setPrefWidth(expandedWidth.getValue().doubleValue());

        setAlignment(Pos.CENTER);
        getChildren().addAll(children);

        // create an animation to show a sidebar.
        expandSideBar = new Transition() {
            {
                setCycleDuration(Duration.millis(250));
            }
            protected void interpolate(double frac) {
                SideBar.this.setVisible(true);
                final double curWidth = expandedWidth.getValue().doubleValue() * frac;
                setPrefWidth(curWidth);
                setTranslateX(-expandedWidth.getValue().doubleValue() + curWidth);
            }
        };
        expandSideBar.onFinishedProperty().set(actionEvent ->
                Arrays.stream(listenerList.getListeners(ISideBarListener.class))
                        .forEach(ISideBarListener::onExpandFinished)
        );


        // create an animation to hide sidebar.
        collapseSideBar = new Transition() {
            {
                setCycleDuration(Duration.millis(250));
            }
            protected void interpolate(double frac) {
                final double curWidth = expandedWidth.getValue().doubleValue() * (1.0 - frac);
                setPrefWidth(curWidth);
                setTranslateX(-expandedWidth.getValue().doubleValue() + curWidth);
            }
        };
        collapseSideBar.onFinishedProperty().set(actionEvent ->{
            setVisible(false);
            Arrays.stream(listenerList.getListeners(ISideBarListener.class))
                    .forEach(ISideBarListener::onCollapsedFinished);
        });
        collapse();
    }

    public void expand(){
        if (expandSideBar.statusProperty().get() == Animation.Status.STOPPED
                && collapseSideBar.statusProperty().get() == Animation.Status.STOPPED) {
            if (!isVisible()) {
                expandSideBar.play();
            }
        }
    }

    public void collapse(){
        if (expandSideBar.statusProperty().get() == Animation.Status.STOPPED
                && collapseSideBar.statusProperty().get() == Animation.Status.STOPPED) {
            if (isVisible()) {
                collapseSideBar.play();
            }
        }
    }

    public boolean isExpanded(){
        return isVisible() && expandSideBar.statusProperty().get().equals(Animation.Status.STOPPED);
    }

    public boolean isCollapsed() {
        return !isVisible() && collapseSideBar.statusProperty().get().equals(Animation.Status.STOPPED);
    }

    public void addListener(ISideBarListener listener){
        listenerList.add(ISideBarListener.class, listener);
    }

    public void removeListener(ISideBarListener listener){
        listenerList.remove(ISideBarListener.class, listener);
    }


    public static  List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }
}
