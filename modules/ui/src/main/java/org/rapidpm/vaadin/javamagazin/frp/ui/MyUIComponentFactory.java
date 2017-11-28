package org.rapidpm.vaadin.javamagazin.frp.ui;

import javax.inject.Inject;

import org.rapidpm.vaadin.javamagazin.frp.ui.components.mainview.MainView;
import org.rapidpm.vaadin.javamagazin.frp.vaadin.JumpstartUIComponentFactory;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;

/**
 *
 */
public class MyUIComponentFactory implements JumpstartUIComponentFactory {


  @Inject private MainView view;

  @Override
  public Component createComponentToSetAsContent(VaadinRequest vaadinRequest) {
    return view;
  }
}
