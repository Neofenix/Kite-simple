package io.cosmosoftware.kite.janus.steps.videocall;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.idToString;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class RegisterUserToVideoCallStep extends TestStep {


  private final int runnerId;
  private final String testCaseName;
  private final JanusPage janusPage;

  public RegisterUserToVideoCallStep(Runner runner, int runnerId, String testCaseName) {
    super(runner);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;
    this.janusPage = new JanusPage(runner);

  }

  @Override
  public String stepDescription() {
    int runnersPeerId = runnerId/2;
    if (this.runnerId%2 == 0){
      return "Register the user Alice" + runnersPeerId + testCaseName;
    } else {
      return "Register the user Bob" + runnersPeerId + testCaseName ;
    }
  }

  @Override
  protected void step() throws KiteTestException {
    int runnersPeerId = runnerId/2;

    String callerName = (runnerId%2 == 0)? "Alice" + runnersPeerId + testCaseName : "Bob" + runnersPeerId + testCaseName;
    janusPage.fillCallerName(callerName);
    janusPage.registerUser();

  }
}
