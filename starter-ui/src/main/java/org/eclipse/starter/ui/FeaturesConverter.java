package org.eclipse.starter.ui;

import jakarta.faces.convert.EnumConverter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("featuresConverter")
public class FeaturesConverter extends EnumConverter {

  public FeaturesConverter() {
    super(ApplicationFeature.class);
  }
}
