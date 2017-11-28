package org.rapidpm.vaadin.javamagazin.frp.ui.components.mainview;

import static org.rapidpm.vaadin.javamagazin.frp.ui.ComponentIDGenerator.comboboxID;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.rapidpm.ddi.DI;
import org.rapidpm.frp.model.Pair;
import org.rapidpm.vaadin.javamagazin.frp.ui.components.demo01.CheckBoxComp;
import org.rapidpm.vaadin.javamagazin.frp.ui.components.demo02.NotificationComp;
import org.rapidpm.vaadin.javamagazin.frp.ui.components.demo03.DemoIDComponent;
import org.rapidpm.vaadin.javamagazin.frp.ui.components.demo04.ReactiveComp;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class MainView extends Composite {


  private final VerticalLayout layout = new VerticalLayout();
  private final ComboBox<Pair<String, Supplier<Composite>>> demoSelector = new ComboBox<>();
  private final Panel demoPanel = new Panel();

  public MainView() {
    layout.addComponents(demoSelector , demoPanel);
    setCompositionRoot(layout);
  }

  private Function<Class<? extends Composite>, Pair<String, Supplier<Composite>>> nextPair() {
    return (clazz) -> Pair.next(clazz.getSimpleName() , () -> DI.activateDI(clazz));
  }

  @PostConstruct
  private void postConstruct() {

    demoPanel.setCaption("Demo Panel");
    demoPanel.setSizeFull();
    demoPanel.setId(comboboxID().apply(getClass() , "demopanel"));

    demoSelector.setItems(
        nextPair().apply(CheckBoxComp.class) ,
        nextPair().apply(NotificationComp.class) ,
        nextPair().apply(DemoIDComponent.class) ,
        nextPair().apply(ReactiveComp.class)
    );
    demoSelector.setItemCaptionGenerator(Pair::getT1);

    demoSelector.addValueChangeListener(event -> demoPanel.setContent(event.getValue().getT2().get()));

  }

}
