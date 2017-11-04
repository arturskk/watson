package net.lipecki.watson;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("ittest")
@TestPropertySource("/it-test.properties")
@Transactional
public abstract class BaseItTest {

	@Autowired
	private TestEventStore testEventStore;

	@Before
	public void resetBeforeTest() {
		this.testEventStore.reset();
	}

}
