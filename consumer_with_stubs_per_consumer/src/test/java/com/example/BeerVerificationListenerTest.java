package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
//remove::start[]
@SpringBootTest(webEnvironment = WebEnvironment.MOCK,
		properties = {"spring.application.name=foo-consumer"})
@AutoConfigureStubRunner(workOffline = true,
		ids = "com.example:beer-api-producer-with-stubs-per-consumer",
		stubsPerConsumer = true)
//remove::end[]
@DirtiesContext
public class BeerVerificationListenerTest extends AbstractTest {

	//remove::start[]
	@Autowired StubTrigger stubTrigger;
	//remove::end[]
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = listener.eligibleCounter.get();

		//remove::start[]
		stubTrigger.trigger("accepted_verification");
		//remove::end[]

		then(listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = listener.notEligibleCounter.get();

		//remove::start[]
		stubTrigger.trigger("rejected_verification");
		//remove::end[]

		then(listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}