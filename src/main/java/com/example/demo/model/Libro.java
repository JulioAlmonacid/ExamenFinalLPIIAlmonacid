package com.example.demo.model;

import java.util.Date;

import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "libro")

public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlibro", nullable = false)
	public Long idlibro;
	
	@Column(name = "nombre", nullable = false)
	@Size(min=4, max=60)
	public String nombre;
	
	
	@Column(name = "nombreAutor", nullable = false)
	@Size(min=4, max=60)
	public String nombreAutor;
	
	@Column(name = "fechaPublicacion", nullable = false)
	public String fechaPublicacion;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date LastUpdate;
	
	@PrePersist
	private void onCreate() {
		LastUpdate = new Date();
	}
	
	@ManyToOne
	@JoinColumn(name = "idgenero", nullable = false)
	public Genero genero;
	
	
}
