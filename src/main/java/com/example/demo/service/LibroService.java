	package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Libro;
import com.example.demo.repository.LibroRepository;


@Service
public class LibroService {


	@Autowired
	private LibroRepository libroRepository;
	
	public List<Libro> getAllLibros() {
		return libroRepository.findAll();
	}
	
 	public Long contarLibro() {
 		return libroRepository.count();
 	}
	
	public Libro createLibro(Libro libro) {
		return libroRepository.save(libro);
	}
	
	public Libro getLibroByID(Long idlibro) {
		return libroRepository.findById(idlibro).orElse(null);
	}
	
	public void deleteLibro(Long idlibro) {
		libroRepository.deleteById(idlibro);
	}
	
	
	
}
