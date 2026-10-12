package br.com.curso.alura.cursoalura;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CursoaluraApplicationTests {

	@Test
	void mainIniciaAplicacaoComOsArgumentosRecebidos() {
		String[] args = { "--spring.profiles.active=test" };

		try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
			CursoaluraApplication.main(args);

			springApplication.verify(() -> SpringApplication.run(CursoaluraApplication.class, args));
		}
	}
}
