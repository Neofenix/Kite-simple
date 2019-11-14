package io.cosmosoftware.kite.simulcast.pages;

import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.simulcast.LoopbackStats;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public abstract class SimulcastPageBase extends BasePage {


  @FindBy(tagName="video")
  private List<WebElement> videos;

  protected SimulcastPageBase(Runner runner) {
    super(runner);
  }

  /**
   *
   * @return the list of video elements
   */
  public List<WebElement> getVideoElements() {
    return videos;
  }

  public abstract void clickButton(String rid, int tid);

  public abstract LoopbackStats getLoopbackStats();

}
