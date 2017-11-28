package org.rapidpm.vaadin.javamagazin.frp.ui.components.demo01;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.rapidpm.frp.model.Pair;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class CheckBoxComp extends Composite {


  private final VerticalLayout layout = new VerticalLayout();

  private final List<Pair<CheckBox, Function<String, String>>> checkBoxes = new ArrayList<>();
  private final CheckBox cb01 = new CheckBox("f1" , true);
  private final CheckBox cb02 = new CheckBox("f2" , true);
  private final CheckBox cb03 = new CheckBox("f3" , true);
  private final CheckBox cb04 = new CheckBox("f4" , true);
  private final CheckBox cb05 = new CheckBox("f5" , true);

  private final Button calc = new Button("calc");

  private final Label resultLabel = new Label();
  private final TextField inputField = new TextField();

  public CheckBoxComp() {
    checkBoxes.add(Pair.next(cb01 , (i) -> i + " f1 "));
    checkBoxes.add(Pair.next(cb02 , (i) -> i + " f2 "));
    checkBoxes.add(Pair.next(cb03 , (i) -> i + " f3 "));
    checkBoxes.add(Pair.next(cb04 , (i) -> i + " f4 "));
    checkBoxes.add(Pair.next(cb05 , (i) -> i + " f5 "));
    checkBoxes.stream()
              .map(Pair::getT1)
              .forEach(layout::addComponent);
    layout.addComponents(inputField , calc , resultLabel);
    setCompositionRoot(layout);
  }

  @PostConstruct
  private void postConstruct() {

    calc.addClickListener(event -> checkBoxes.stream()
                                             .filter(p -> p.getT1().getValue())
                                             .map(Pair::getT2)
                                             .reduce(Function::andThen)
                                             .map(f -> f.apply(inputField.getValue()))
                                             .ifPresent(resultLabel::setValue));

  }

}
