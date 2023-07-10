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
import ru.yandex.practicum.filmorate.controller.ControllerFilm;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestFilm {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ControllerFilm controllerFilm;

	@Test
	public void test() throws Exception {
		assertThat(controllerFilm).isNotNull();
	}

	@Test
	public void testGet() throws Exception {
		mockMvc.perform(get("/films"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
	}

	@Test
	public void testPostNewFilm() throws Exception {
		Film film = new Film("StarWars", "interesting film",
				LocalDate.of(2018, 11, 18), 110);
		mockMvc.perform(post("/films")
						.content(jsonToString(film))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(200));
	}

	@Test
	public void testPostVerifyName() throws Exception {
		Film film = new Film(" ", "a comedy",
				LocalDate.of(2005, 5, 2), 100);
		mockMvc.perform(post("/films")
						.content(jsonToString(film))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(400));
	}

	@Test
	public void testPostVerifyMaxDescription() throws Exception {
		Film film = new Film("Интерстеллар", "Когда засуха, пыльные бури и вымирание растений " +
				"приводят человечество к продовольственному кризису,\n" + "коллектив исследователей и учёных " +
				"отправляется сквозь червоточину (которая предположительно соединяет области" +
				" пространства-времени через большое расстояние) в путешествие,\n" +
				"чтобы превзойти прежние ограничения для космических путешествий человека " +
				"и найти планету с подходящими для человечества условиями.",
				LocalDate.of(2014, 9, 15), 169);
		mockMvc.perform(post("/films")
						.content(jsonToString(film))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400));
	}

	@Test
	public void postTestVerifyOfDateRelease() throws Exception {
		Film film = new Film("Тайна Коко", "12-летний Мигель живёт в мексиканской деревушке" +
				" в семье сапожников и тайно мечтает стать музыкантом. ",
				LocalDate.of(1895, 12, 27), 117);
		mockMvc.perform(post("/films")
						.content(jsonToString(film))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is(400));
	}

	@Test
	public void putTestVerifyDuration() throws Exception {
		Film film = new Film("Тайна Коко", "12-летний Мигель живёт в мексиканской деревушке" +
				" в семье сапожников и тайно мечтает стать музыкантом. ",
				LocalDate.of(2017, 8, 15), -117);
		mockMvc.perform(post("/films")
						.content(jsonToString(film))
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
