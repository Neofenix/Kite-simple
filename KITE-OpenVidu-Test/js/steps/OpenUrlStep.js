const {TestStep} = require('kite-common');
const {CallPage, GetARoomPage, VideoConferencePage, WebinarPage, ClassroomPage} = require('../pages');

const description = function(stepInfo) {
  if (stepInfo.page instanceof CallPage ||
      stepInfo.page instanceof VideoConferencePage ||
      stepInfo.page instanceof WebinarPage) {
    return 'Open ' + stepInfo.url + ' and join the session';
  }
  if (stepInfo.page instanceof ClassroomPage) {
    return 'Open ' + stepInfo.url + ' and register';
  }
  if (stepInfo.page instanceof GetARoomPage) {
    return 'Open ' + stepInfo.url + ' and join a room';
  }
}

const executeStep = async function(stepInfo) {
  await stepInfo.page.open(stepInfo);
  if (stepInfo.page instanceof VideoConferencePage ||
      stepInfo.page instanceof WebinarPage ||
      stepInfo.page instanceof CallPage) {
    await stepInfo.page.joinSession('OpenVidu' + stepInfo.uuid);
  }
  if (stepInfo.page instanceof ClassroomPage) {
    await stepInfo.page.joinSession(stepInfo.id);
  }
}

/**
 * Class: OpenUrlStep
 * Extends: TestStep
 */
class OpenUrlStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
  }

  stepDescription() {
    return description(this);
  }

  async step() {
    await executeStep(this);
  }
}

module.exports = OpenUrlStep;