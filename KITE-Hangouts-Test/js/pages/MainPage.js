const {By, Key, until} = require('selenium-webdriver');
const {TestUtils, KiteTestError, Status} = require('kite-common'); 

const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;
const waitAround = TestUtils.waitAround;


const signinButton = By.id('gb_70');
const emailInput = By.id('identifierId');
const passwordInput = By.xpath('//*[@id=\"password\"]/div[1]/div/div[1]/input');
const idNextButton = By.id('identifierNext');
const pwNextButton = By.id('passwordNext');
const nextButton = By.xpath('//*[@id=\"yDmH0d\"]/div[8]/div[2]/div/div[3]');
const videoCallButton = By.xpath('//*[@id=\"yDmH0d\"]/div[4]/div[4]/div/div/ul/li[1]/div[1]');
const joinButton = By.xpath('//*[@id=\"yDmH0d\"]/div[4]/div[2]/div/div/div[2]/div[2]/div/span/span');

const closePopup = By.xpath('//*[@id=\"yDmH0d\"]/div[6]/div/div[2]/div[2]/div[3]/div/span');
const videoElements = By.css('video');

const FIVE_SECONDS = 5000;

  
const skipPresentation = async function(driver) {
  try {
    console.log("skipPresentation");
    await clickButton(driver, nextButton);
    await clickButton(driver, nextButton);
    await clickButton(driver, nextButton);
    await clickButton(driver, nextButton);
  } catch (err) {
    console.log("no presentation to skip");
  }
}

const clickButton = async function(driver, but) {
  await driver.wait(until.elementLocated(but), FIVE_SECONDS);
  let b = await driver.findElement(but);
  await b.click();   
}


class MainPage {
  constructor(driver) {
    this.driver = driver;
  }

  async open(url) {
    await TestUtils.open(url);
  }
  
  async clickSignIn() {
    console.log("clickSignIn");
    await clickButton(this.driver, signinButton);
  }
  
  async clickJoin() {
    console.log("clickJoin");
    await clickButton(this.driver, joinButton);
  }
  
  async enterEmail(email) {
    console.log("enterEmail(" + email + ")");
    await this.driver.wait(until.elementLocated(emailInput), FIVE_SECONDS);
    let input = await this.driver.findElement(emailInput);
    await input.sendKeys(email);  
    await clickButton(this.driver, idNextButton);
  }
  
  async enterPassword(password) {
    console.log("enterPassword(" + password + ")");
    await waitAround(1000);
    await this.driver.wait(until.elementLocated(passwordInput), FIVE_SECONDS);
    let input = await this.driver.findElement(passwordInput);
    await input.sendKeys(password);
    await waitAround(1000);
    console.log("click pwNextButton");
    await clickButton(this.driver, pwNextButton);
    await skipPresentation(this.driver);
  }
  

  // VideoCheck with verifyVideoDisplayByIndex
  async videoCheck(stepInfo, index) {
    console.log('videoCheck(' + index + ')');
    let timeout = stepInfo.timeout;

    // Waiting for the videos
    await TestUtils.waitForVideos(stepInfo, videoElements);

    // Check the status of the video
    // checked.result = 'blank' || 'still' || 'video'
    let i = 0;
    let checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
    while(checked.result === 'blank' || typeof checked.result === "undefined" && i < timeout) {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(1000);
    }

    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index);
      i++;
      await waitAround(3 * 1000); // waiting 3s after each iteration
    }
    return checked.result;
  }
  
  async getVideos() {
    return videoElements;
  }
  
  async startVideoCall() {
    console.log("startVideoCall");
    await clickButton(this.driver, videoCallButton);
    //await waitAround(3 * 1000);    
    const d = this.driver;
    await this.driver.wait(function () {
      return d.getAllWindowHandles().then(async function (handles) {
        var isHandleCount2 = (handles.length == 2);
        if (isHandleCount2) {
          d.switchTo().window(handles[1]);
        }
        return isHandleCount2;
      });
    }).then(async function () {
    // Now do some stuff in new tab
      try {       
        await clickButton(d, closePopup);
      } catch (err) {
        console.log('no popup to close');
      }
    });    
  }
  
  /*


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
  
  */
  
  
}

module.exports = MainPage;