package io.cosmosoftware.kite.hangouts.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import java.util.ArrayList;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

import static io.cosmosoftware.kite.entities.Timeouts.*;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class MainPage extends BasePage {

  @FindBy(id = "gb_70")
  WebElement signinButton;

  @FindBy(id = "identifierId")
  WebElement emailInput;

  @FindBy(xpath = "//*[@id=\"password\"]/div[1]/div/div[1]/input")
  WebElement passwordInput;

  @FindBy(id = "identifierNext")
  WebElement idNextButton;

  @FindBy(id = "passwordNext")
  WebElement pwNextButton;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[8]/div[2]/div/div[3]")
  WebElement nextButton;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[4]/div[4]/div/div/ul/li[1]/div[1]")
  WebElement videoCallButton;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div[2]/div[2]/div/span/span")
  WebElement joinButton;

  @FindBy(tagName="video")
  private List<WebElement> videos;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[6]/div/div[2]/div[2]/div[3]/div/span")
  WebElement closePopup;

  public MainPage(Runner runner) {
    super(runner);
  }
  
  public void open(String url) {
    loadPage(webDriver, url, 20);
  }
  
  public void clickSignIn() throws KiteInteractionException {
    waitUntilVisibilityOf(signinButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    click(signinButton);    
  }
  
  public void clickJoin() throws KiteInteractionException {
    waitUntilVisibilityOf(joinButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    click(joinButton);
  }

  public void enterEmail(String email) throws KiteInteractionException {
    waitUntilVisibilityOf(emailInput, TEN_SECOND_INTERVAL_IN_SECONDS);
    sendKeys(emailInput, email);
    clickNext(idNextButton);
  }

  public void enterPassword(String password) throws KiteInteractionException {
    waitUntilVisibilityOf(passwordInput, TEN_SECOND_INTERVAL_IN_SECONDS);
    sendKeys(passwordInput, password);
    clickNext(pwNextButton);
    //if first time login, we need to skip the presentation
    skipPresentation();
  }

  private void clickNext(WebElement nextButton) throws KiteInteractionException {
    waitUntilVisibilityOf(nextButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    click(nextButton);
  }
  
  private void skipPresentation() {
    try {
      clickNext(nextButton);
      clickNext(nextButton);
      clickNext(nextButton);
      clickNext(nextButton);
    } catch (KiteInteractionException e) {
      logger.debug("No presentation to skip.");
    }
  }


  public void startVideoCall() throws KiteInteractionException {
    waitUntilVisibilityOf(videoCallButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    String parentWinHandle = webDriver.getWindowHandle();
    click(videoCallButton);
    waitAround(THREE_SECOND_INTERVAL);
    new WebDriverWait(webDriver,
      TEN_SECOND_INTERVAL_IN_SECONDS).until(
      d -> {
    Set<String> winHandles = d.getWindowHandles();
    for (String w:winHandles) {
      if (!w.equals(parentWinHandle)) {
        d.switchTo().window(w);
        waitAround(ONE_SECOND_INTERVAL);
        return true;
      }
    }
    return false;
    });
    try {
      waitUntilVisibilityOf(closePopup, TEN_SECOND_INTERVAL_IN_SECONDS);
      click(closePopup);
    } catch (KiteInteractionException e) {
      //ignore
      logger.debug("Unable to close the popup ", e);
    }
    
  }


  public void videoIsPublishing(int timeout) throws TimeoutException, KiteInteractionException {
    if (videos.size() < 1) {
      waitAround(timeout * ONE_SECOND_INTERVAL);
      if (videos.size() < 1) {
        throw new TimeoutException("videoIsPublishing: no <video> element found on the page");
      }
    }
    waitUntilVisibilityOf(videos.get(0), timeout);
  }

  public List<WebElement> getVideoElements() {
    return videos;
  }

  /**
   * Gets the index of the videos that are actually displayed (height & width > 0)
   * @return an array of valid video index
   */
  public List<Integer> getVideoIndex() {
    List<Integer> index = new ArrayList<>();
    List<WebElement> temp = this.videos;
    for (int i = 0; i < temp.size(); i ++) {
      try {
        WebElement video = temp.get(i);
        if (video.isDisplayed()) {
          if (parseCssValue(video.getCssValue("height")) > 5
          && parseCssValue(video.getCssValue("width")) > 5 ) {
            index.add(i);
          }
        }
      }catch (Exception e) {
        // ignore
      }
    }
    return index;
  }

  private int parseCssValue(String value) {
    return Integer.parseInt(value.split("px")[0]);
  }
}
