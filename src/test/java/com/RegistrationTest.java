package com;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.RegistrationController;

@WebMvcTest(RegistrationController.class)
public class RegistrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void FormTest1() throws Exception {
		mockMvc.perform(get("/register"))
				.andExpect(content().string(containsString("Please fill the Application form")));
	}

	@Test
	public void formTest2() throws Exception {
			mockMvc.perform(get("/register"))
					.andExpect(content().string(containsString("Please enter your Name")));
	}
	
	@Test
	public void formTest3() throws Exception {
			mockMvc.perform(get("/register"))
					.andExpect(content().string(containsString("Please enter your Email")));
	}
	
	@Test
	public void submitsTest1() throws Exception {
		mockMvc.perform(post("/register").param("name", "lakshmanan").param("email", "devops@devops.com"))
				.andExpect(content().string(containsString("Application form successfully submitted")))
				.andExpect(content().string(containsString("Click here to go back to main page")));
	}

	@Test
	public void submitsTest2() throws Exception {
		mockMvc.perform(post("/register").param("name", "devops").param("email", "devops@test.com"))
				.andExpect(content().string(containsString("devops")))
				.andExpect(content().string(containsString("devops@test.com")));
	}

}