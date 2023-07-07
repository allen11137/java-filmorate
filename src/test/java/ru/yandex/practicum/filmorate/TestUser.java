package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.ControllerUser;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUser {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ControllerUser controllerUser;

	@Test
	public void test() throws Exception {
		assertThat(controllerUser).isNotNull();
	}

	@Test
	public void getTest() throws Exception {
		mockMvc.perform(get("/user"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void postTestAddUser() throws Exception {
		User user = new User("lilia@yandex.ru", "Lilia", "Alisa",
				LocalDate.of(1900, 5, 15));
		mockMvc.perform(post("/user")
						.content(jsonToString(user))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(200));
	}

	@Test
	public void postTestVerifyEmail() throws Exception {
		User user = new User("svetasveta.com", "Waza", "Alisa",
				LocalDate.of(1921, 2, 20));
		mockMvc.perform(post("/user")
						.content(jsonToString(user))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(400));
	}

	@Test
	public void postTestVerifyLogin() throws Exception {
		User user = new User("alina@yandex.ru", "", "Alina",
				LocalDate.of(1990, 3, 1));
		mockMvc.perform(post("/user")
						.content(jsonToString(user))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(400));
	}

	@Test
	public void postTestVerifyName() throws Exception {
		User user = new User("alina@yandex.ru", "allen", " ",
				LocalDate.of(1990, 2, 15));
		mockMvc.perform(post("/user")
						.content(jsonToString(user))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(200));
	}

	@Test
	public void postTestVerifyBirthday() throws Exception {
		User user = new User("alina@yandex.ru", "allen", " ",
				LocalDate.of(3000, 5, 1));
		mockMvc.perform(post("/user")
						.content(jsonToString(user))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(400));
	}

	public static String jsonToString(final Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule((new JavaTimeModule()));
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
