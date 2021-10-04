package br.com.curso.alura.cursoalura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.model.Nota;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@Controller
public class NotasController {

	@Autowired
	public AlunoRepository alunoRepository;

	@RequestMapping(value = "/nota/cadastrar/{id}",method = RequestMethod.GET)
	public String cadastrar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.findById(id);
		model.addAttribute("aluno", aluno);
		model.addAttribute("nota", new Nota());

		return "/nota/cadastrar";
	}

	@PostMapping("/nota/salvar/{id}")
	public String cadastar(@PathVariable String id, @ModelAttribute Nota nota) {
		Aluno aluno = alunoRepository.findById(id);
		alunoRepository.salvar(aluno.adicionaNota(aluno, nota));
		return "redirect:/aluno/listar";
	}
	
	
	@GetMapping("/nota/iniciarpesquisa")
	public String iniciarPesquisar() {
		return "nota/pesquisar";
	}
	
	
	@GetMapping("/nota/pesquisar")
	public String pesquisar(@RequestParam String classificacao, @RequestParam String notacorte, Model model) {
		List<Aluno> alunos = alunoRepository.pesquisaPor(classificacao, Double.parseDouble(notacorte));
		model.addAttribute("alunos", alunos);
		return "nota/pesquisar";
	}

}
