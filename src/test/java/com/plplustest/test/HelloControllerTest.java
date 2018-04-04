package com.plplustest.test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloControllerTest {


	@Autowired
	private WebApplicationContext context;
	
	 @InjectMocks private HelloController mockHelloController;
	 private MockMvc mockMvc;
	 
	 @Before
	 public void setUp() {

	     this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

	 }
	 
	 @Test
	 public void testIndex() throws Exception {
		 mockMvc.perform(get("/")).andDo(print()).andExpect(view().name("templates/index.html")).andExpect(status().isOk());
	 }
	 
	 @Test
	 public void testIndexError() throws Exception {
		 mockMvc.perform(get("/foo")).andDo(print()).andExpect(status().isNotFound());
	 }
}
