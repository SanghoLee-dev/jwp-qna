package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questions;

	@Autowired
	private UserRepository users;

	private User writer;

	@BeforeEach
	void setUp() {
		writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
		users.save(writer);
	}

	@Test
	@DisplayName("Question 저장 테스트")
	void save() {
		Question question = new Question("title1", "contents1").writeBy(writer);
		Question saved = questions.save(question);
		assertThat(saved.getContents()).isEqualTo("contents1");
		assertThat(saved.getTitle()).isEqualTo("title1");
	}

	@Test
	@DisplayName("Question 조회 테스트")
	void findById() {
		Question question = new Question("title1", "contents1").writeBy(writer);
		Question saved = questions.save(question);
		questions.flush();
		Question actual = questions.findById(saved.getId()).get();
		assertThat(actual.getTitle()).isEqualTo(saved.getTitle());
		assertThat(actual.getContents()).isEqualTo(saved.getContents());
		assertThat(actual.getId()).isEqualTo(saved.getId());
		assertThat(actual.getCreateAt()).isEqualTo(saved.getCreateAt());
		assertThat(actual.getWriter().getEmail()).isEqualTo("javajigi@slipp.net");
		assertThat(actual.getWriter().getUserId()).isEqualTo("javajigi");
		assertThat(actual.getWriter().getPassword()).isEqualTo("password");
		assertThat(actual.getWriter().getName()).isEqualTo("name");
	}

	@Test
	@DisplayName("Question 수정 테스트")
	void update() {
		Question question = new Question("title1", "contents1").writeBy(writer);
		Question saved = questions.save(question);
		Question actual = questions.findById(saved.getId()).get();
		Date updateAt = new Date();
		actual.setUpdateAt(updateAt);
		actual.setContents("Question Contents 수정");
		actual.setTitle("Question Title 수정");
		questions.flush();
		assertThat(saved.getUpdateAt()).isEqualTo(updateAt);
		assertThat(saved.getContents()).isEqualTo("Question Contents 수정");
		assertThat(saved.getTitle()).isEqualTo("Question Title 수정");
	}

	@Test
	@DisplayName("Question 삭제 테스트")
	void delete() {
		Question question = new Question("title1", "contents1").writeBy(writer);
		Question saved = questions.save(question);
		questions.delete(saved);
		questions.flush();
		assertThat(questions.findById(saved.getId()).isPresent()).isFalse();
	}
}
