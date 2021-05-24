package tests;

import api.configuration.AppConfiguration;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ConfigurationSetupHook implements BeforeAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) {
    AppConfiguration.initialize();
  }
}
