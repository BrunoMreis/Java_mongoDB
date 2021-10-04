package br.com.curso.alura.cursoalura.model;

import java.util.List;



public class Contato {

	private String telefone;

	private String email;

	private List<Double> coordinates;

	private String endereco;

	
	
	
	
	public Contato(String endereco, List<Double> coordinates) {
		this.coordinates = coordinates;
		this.endereco = endereco;
		
	}
	
	public Contato() {
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Double> getCordinates() {
		return coordinates;
	}

	public void setCordinates(List<Double> cordinates) {
		this.coordinates = cordinates;
	}

	@Override
	public String toString() {
		return "Contato [telefone=" + telefone + ", email=" + email + ", coordinates=" + coordinates + ", endereco="
				+ endereco + "]";
	}

}
