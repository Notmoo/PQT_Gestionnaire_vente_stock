package com.pqt.client.gui.modules.stat_screen;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.core.common_resources.statistics.StatisticFields;
import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.communication.IObjectParser;
import com.pqt.core.entities.product.LightweightProduct;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.stream.Collectors;

class StatScreenView implements IFXComponent {

    private TextArea statTextArea;
    private Pane mainPane;

    StatScreenView() {
        initGui();
    }

    private void initGui() {
        mainPane = new Pane();
        mainPane.getStyleClass().add(GUICssTool.getMainModulePaneCssClass());

        statTextArea = new TextArea();
        mainPane.getChildren().add(statTextArea);
        statTextArea.setId("stat-screen-text-area");
        statTextArea.setWrapText(true);
        statTextArea.setEditable(false);
        statTextArea.prefWidthProperty().bind(mainPane.widthProperty());
        statTextArea.prefHeightProperty().bind(mainPane.heightProperty());
    }

    void display(Map<String, String> statistics){
        List<String> lines = new ArrayList<>();

        if(statistics!=null) {

            lines.addAll(EnumSet.allOf(StatisticFields.class)
                    .stream()
                    .map(field -> {
                        if(statistics.containsKey(field.getStr())){
                            switch (field.getType()){
                                case SIMPLE:
                                    return String.format("%s : %s", GUIStringTool.getStatisticFieldsRenderer().render(field), statistics.get(field.getStr()));
                                default :
                                    break;
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            if(statistics.containsKey(StatisticFields.TOP_POPULAR_PRODUCTS.getStr())){
                StringBuffer sb = new StringBuffer(GUIStringTool.getStatisticFieldsRenderer().render(StatisticFields.TOP_POPULAR_PRODUCTS)).append(" :");

                //TODO centraliser la récupération de la message factory, pour éviter les erreurs de aprsing quand il y aura plusieurs impl. de factory différentes
                IObjectParser<List<LightweightProduct>> parser =
                        new GSonMessageToolFactory().getListParser(LightweightProduct.class);

                List<String> products = parser.parse(statistics.get(StatisticFields.TOP_POPULAR_PRODUCTS.getStr()))
                                                .stream()
                                                .map(LightweightProduct::getName)
                                                .collect(Collectors.toList());

                for(int i = 1; i<products.size(); i++){
                    sb.append("\n").append(String.format("\t%d. %s", i, products.get(i-1)));
                }

                lines.add(sb.toString());
            }
        }

        Platform.runLater(()-> {
            statTextArea.setText("");
            lines.forEach(line -> {
                if(line!=null) statTextArea.appendText(" * "+line + "\n");
            });
        });
    }

    @Override
    public Pane getPane() {
        return mainPane;
    }
}
