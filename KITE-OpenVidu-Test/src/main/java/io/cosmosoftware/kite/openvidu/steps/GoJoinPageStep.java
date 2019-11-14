package io.cosmosoftware.kite.openvidu.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;
import static io.cosmosoftware.kite.util.WebDriverUtils.waitForElement;

public class GoJoinPageStep extends TestStep {
  private final String roomUrl;

  public GoJoinPageStep(Runner runner, String roomUrl) {
    super(runner);
    this.roomUrl = roomUrl;
  }

  @Override
  public String stepDescription() {
    return "Open " + roomUrl;
  }

  @Override
  protected void step() throws KiteTestException {
    goJoinPage(roomUrl);
  }

  public void goJoinPage(String url) {
    webDriver.manage().window().maximize();
    loadPage(webDriver, url, 10);
    logger.info("Open " + url);
    try {
      waitForElement(webDriver, By.id("mat-input-0"), 10000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
