package it.unisa.application.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sede {
    private String nome;
    private String indirizzo;
    private int id;
    private Set<Sala> sale;

    public Sede(){
        sale = new HashSet<>();
    }
    public Sede(int id){
        this.id = id;
        sale = new HashSet<>();
    }
    public Sede(int id, String nome, String indirizzo){
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.sale = new HashSet<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Sala> getSale() {
        return sale;
    }

    public void setSale(Set<Sala> sale) {
        this.sale = sale;
    }

    public List<Proiezione> getProgrammazione() {
        return sale.stream().
                flatMap(sala -> sala.getProiezioni().stream())
                .collect(Collectors.toList());
    }

    public List<Proiezione> getProiezioniFilm(Film film) {
        return sale.stream()
                .flatMap(sala -> sala.getProiezioni().stream())
                .filter(proiezione -> proiezione.getFilmProiezione().equals(film))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Sede{" +
                "nome='" + nome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", id=" + id +
                ", sale=" + sale +
                '}';
    }
}
