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
import org.springframework.web.bind.annotation.RequestParam;

import br.com.curso.alura.cursoalura.model.Aluno;
import br.com.curso.alura.cursoalura.repository.AlunoRepository;

@Controller
@RequestMapping("aluno")
public class AlunoController {
	
	
	@Autowired private AlunoRepository alunoRepository;
	
	//@Autowired private AlunoRepositoryInterface alunoRepository;

	
	@GetMapping("/cadastrar")
	public String cadastar(Model model) {
		model.addAttribute("aluno",new Aluno());
		
		return "aluno/cadastrar";
	}
	
	@PostMapping("/salvar")
	public String salvar(@ModelAttribute Aluno aluno) {
		
		alunoRepository.salvar(aluno);
		return "redirect:/";
		
	}
	@GetMapping("/listar")
	public String listar(Model model) {
		List<Aluno> alunos = alunoRepository.obterTodosAlunos();
		model.addAttribute("alunos",alunos);
		return "aluno/listar";
	}
	@GetMapping("/visualizar/{id}")
	public String visualizar(@PathVariable String id, Model model) {
		Aluno aluno = alunoRepository.findById(id);
		model.addAttribute("aluno", aluno);
		return "aluno/visualizar";
	}
	@GetMapping("/pesquisarnome")
	public String pesquisarNome() {
		return "aluno/pesquisarnome";
	}
	
	@GetMapping("/pesquisar")
	public String pesquisar(@RequestParam("nome") String nome, Model model) {
		List<Aluno>alunos = alunoRepository.pesquisaPor(nome);
		model.addAttribute("alunos", alunos);
		
		return "aluno/pesquisarnome";
	}
	


}
