package com.Uber.Project.UberApp;

import com.Uber.Project.UberApp.Services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private  EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail("komeso4998@paxnw.com",
				"This is the testing email",
				"Body of the email");
	}


	@Test
	void sendEmail(){
		String[]email={
				"komeso4998@paxnw.com",
				"sirdon0205@gmail.com"
		};
		emailSenderService.sendEmail(email,"helloFrom uberApplication","Keep Codding");
	}

}
