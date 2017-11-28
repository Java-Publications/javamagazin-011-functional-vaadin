package org.rapidpm.vaadin.javamagazin.frp.ui.components.demo04;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.frp.model.Pair;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class ReactiveComp extends Composite {

  private final Layout layout = new VerticalLayout();
  private final Button start = new Button();
  private final Button stop = new Button();
  private final TextField inputField = new TextField();
  private final Label resultLabel = new Label();

  private final List<Pair<CheckBox, Function<String, String>>> checkBoxes = new ArrayList<>();
  private final CheckBox cb01 = new CheckBox("f1" , true);
  private final CheckBox cb02 = new CheckBox("f2" , true);
  private final CheckBox cb03 = new CheckBox("f3" , true);
  private final CheckBox cb04 = new CheckBox("f4" , true);
  private final CheckBox cb05 = new CheckBox("f5" , true);


  private Function<String, String> fkt(String name , long ms) {
    return (v) -> {
      ((CheckedExecutor) () -> Thread.sleep(ms))
          .execute()
          .ifPresentOrElse(
              success -> System.out.println(" OK : " + name + " - " + LocalDateTime.now()) ,
              Notification::show
          );
      return v + " - " + name + " " + ms + "[ms]";
    };
  }


  public ReactiveComp() {

    checkBoxes.add(Pair.next(cb01 , fkt("f1" , 1_000)));
    checkBoxes.add(Pair.next(cb02 , fkt("f2" , 1_000)));
    checkBoxes.add(Pair.next(cb03 , fkt("f3" , 1_000)));
    checkBoxes.add(Pair.next(cb04 , fkt("f4" , 1_000)));
    checkBoxes.add(Pair.next(cb05 , fkt("f5" , 1_000)));

    checkBoxes.stream()
              .map(Pair::getT1)
              .forEach(layout::addComponent);
//    final Layout startStopButtons = new HorizontalLayout(start , stop);
//    layout.addComponent(startStopButtons);

    layout.addComponents(inputField , start , resultLabel);
    setCompositionRoot(layout);
  }


  @PostConstruct
  private void postConstruct() {

    start.setCaption("Start");

    start.addClickListener(e -> checkBoxes
        .stream()
        .filter(p -> p.getT1().getValue())
        .map(Pair::getT2)
        .reduce(Function::andThen)
        .ifPresent(f -> {
          final UI current = UI.getCurrent();
          final String inputFieldValue = inputField.getValue();

          CompletableFuture
              .supplyAsync(() -> f.apply(inputFieldValue))
              .whenCompleteAsync((result , throwable) -> {
                if (throwable != null)
                  current.access(() -> Notification.show(throwable.getMessage()));
                current.access(() -> resultLabel.setValue(result));
              });

//          CompletableFuture
//              .supplyAsync(() -> f.apply(inputFieldValue))
//              .whenCompleteAsync((result , throwable) -> {
//                if (throwable != null)
//                  current.access(() -> new Notification(throwable.getMessage()).show(current.getPage()));
//                current.access(() -> resultLabel.setValue(result));
//              });
        }));
  }


}
