package org.eclipse.starter.ui;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("featuresConverter")
public class FeaturesConverter extends EnumConverter {

  public FeaturesConverter() {
    super(ApplicationFeature.class);
  }
}
