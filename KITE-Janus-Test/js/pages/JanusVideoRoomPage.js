const JanusBasepage = require('./JanusBasePage');
const {By, Key} = require('selenium-webdriver');
const {TestUtils} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

// Elements
const usernameInput = By.id('username');
const firstnameLocator = By.css('form > div:nth-child(1) > input')
const lastnameLocator = By.css('form > div:nth-child(2) > input')
const emailLocator = By.css('form > div:nth-child(3) > input')
const termsAndConditions = By.id('termsAndConditions')

class JanusVideoRoomPage extends JanusBasepage {
  constructor(driver) {
    super(driver);
  }

  async joinSession(sessionId) {
    //let start = await this.driver.findElement(this.startButton);
    let firstname = await this.driver.findElement(firstnameLocator);
    await firstname.click();
    await firstname.sendKeys("Test");
    let lastname = await this.driver.findElement(lastnameLocator);
    await lastname.click();
    await lastname.sendKeys("Stress");
    let email = await this.driver.findElement(emailLocator);
    await email.click();
    await email.sendKeys(Math.random().toString(36).substring(2,11).concat("@Stressqa.com"));
    let terms = await this.driver.findElement(termsAndConditions);
    await terms.click();
    //await start.click();
    //await waitAround(2000) // wait for element to be visible
    //let username = await this.driver.findElement(usernameInput);
    //await username.sendKeys(sessionId);
    await terms.sendKeys(Key.ENTER);
    await waitAround(9000);
  }

  // This will stop the video broadcasting properly
  async stopVideo() {
    // stop button and start button are the same
    let stop = await this.driver.findElement(this.startButton);
    await stop.click();
  }
}

module.exports = JanusVideoRoomPage;