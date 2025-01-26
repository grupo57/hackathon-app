package br.com.fiap.soat07.hackathon.infra.rest.dto;

import java.util.List;
import java.util.ArrayList;

public class Grupo57DTO {
    private final String projeto;
    private String nome;
    private List<String> membros;

    public Grupo57DTO() {
        this.nome = "Grupo57";
        this.projeto = "Hackathon";
        this.membros = new ArrayList<>();

        membros.add("Fabio Henrique Peixoto da Silva - RM354678");
        membros.add("Marcello de Almeida Lima - RM355880");
        membros.add("Matheus Tadeu Moreira da Cunha - RM355524");
        membros.add("Eduardo Fabris - RM356333");
    }

    public String getProjeto() {
        return projeto;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getMembros() {
        return membros;
    }

}
