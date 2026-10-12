package br.com.curso.alura.cursoalura.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AdminControllerTest {

	private final AdminController controller = new AdminController();

	@Test
	void indexRetornaViewInicial() {
		assertThat(controller.index()).isEqualTo("index");
	}
}
