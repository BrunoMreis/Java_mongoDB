package br.com.curso.alura.cursoalura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Habilidade;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@Controller
@RequestMapping("habilidade")
public class HabilidadesController {

	@Autowired
	private AlunoRepository alunoRepository;

	@RequestMapping("/cadastrar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.findById(id);
		model.addAttribute("aluno", aluno);
		model.addAttribute("habilidade", new Habilidade());

		return "habilidades/cadastrar";

	}

	@PostMapping("/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Habilidade habilidade) {

		Aluno aluno = alunoRepository.findById(id);
		alunoRepository.salvar(aluno.adicionaHabilidade(aluno, habilidade));

		return "redirect:/aluno/listar";
	}

}
