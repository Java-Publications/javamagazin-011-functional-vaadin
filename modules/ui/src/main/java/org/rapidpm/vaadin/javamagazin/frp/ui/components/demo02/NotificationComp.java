package org.rapidpm.vaadin.javamagazin.frp.ui.components.demo02;

import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public class NotificationComp extends Composite {

  private final VerticalLayout layout = new VerticalLayout();

  private final List<CheckBox> checkBoxes = new ArrayList<>();
  private final CheckBox cb01 = new CheckBox("f1" , false);
  private final CheckBox cb02 = new CheckBox("f2" , false);
  private final CheckBox cb03 = new CheckBox("f3" , false);
  private final CheckBox cb04 = new CheckBox("f4" , false);
  private final CheckBox cb05 = new CheckBox("f5" , false);

  private final Button evaluate = new Button("evaluate");

  public NotificationComp() {
    checkBoxes.add(cb01);
    checkBoxes.add(cb02);
    checkBoxes.add(cb03);
    checkBoxes.add(cb04);
    checkBoxes.add(cb05);
    checkBoxes.forEach(layout::addComponent);

    layout.addComponents(evaluate);
    setCompositionRoot(layout);
  }

//  @PostConstruct
//  private void postConstruct(){
//    evaluate.addClickListener(e -> {
//      System.out.println("e = " + e);
//      Result<Boolean> result = Case
//          .match(
//              Case.matchCase(() -> Result.failure("unexpected combination")) ,
//              Case.matchCase(() -> cb01.getValue() &&  cb02.getValue(), () -> Result.success(true)) ,
//              Case.matchCase(() -> cb01.getValue() &&  cb03.getValue(), () -> Result.failure("really bad idea")) ,
//              Case.matchCase(() -> cb01.getValue() &&  cb04.getValue(), () -> Result.success(true))
//          );
//      result.ifPresentOrElse(
//          success -> {
//            System.out.println("success = " + success);
//          } ,
//          Notification::show
//      );
//    });
//  }


  @PostConstruct
  private void postConstruct() {
    evaluate.addClickListener(e -> match(
        matchCase(() -> failure("unexpected combination")) ,
        matchCase(() -> cb01.getValue() && cb02.getValue() , () -> success(true)) ,
        matchCase(() -> cb01.getValue() && cb03.getValue() , () -> failure("really bad idea")) ,
        matchCase(() -> cb01.getValue() && cb04.getValue() , () -> success(true))
    ).ifPresentOrElse(
        System.out::println ,
        Notification::show
    ));
  }

}
