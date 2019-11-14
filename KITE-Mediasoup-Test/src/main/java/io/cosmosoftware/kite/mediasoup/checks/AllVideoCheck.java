package io.cosmosoftware.kite.mediasoup.checks;

import io.cosmosoftware.kite.entities.Timeouts;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class AllVideoCheck extends TestStep {

  
  private final int numberOfParticipants;
  private final MediasoupPage mediasoupPage;

  public AllVideoCheck(Runner runner, int numberOfParticipants) {
    super(runner);
    this.numberOfParticipants = numberOfParticipants;
    this.mediasoupPage = new MediasoupPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Check the other videos are being received OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      //wait a while to allow all videos to load.
      waitAround(numberOfParticipants * 3 * Timeouts.ONE_SECOND_INTERVAL);
      logger.info("Looking for video elements");
      List<WebElement> videos = mediasoupPage.getVideoElements();
      if (videos.size() < numberOfParticipants) {
        throw new KiteTestException(
            "Unable to find " + numberOfParticipants + " <video> element on the page. No video found = "
              + videos.size(), Status.FAILED);
      }
      String videoCheck = "";
      boolean error = false;
      for (int i = 1; i < numberOfParticipants; i++) {
        String v = videoCheck(webDriver, i);
        videoCheck += v;
        if (i < numberOfParticipants - 1) {
          videoCheck += "|";
        }
        if (!"video".equalsIgnoreCase(v)) {
          error = true;
        }
      }
      if (error) {
        reporter.textAttachment(report, "Reveived Videos", videoCheck, "plain");
        throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}
