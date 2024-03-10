package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Libro;
import com.example.demo.model.Genero;
import com.example.demo.service.GeneroService;
import com.example.demo.service.LibroService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("/libro")
public class LibroController {


	@Autowired
	private LibroService libroService;
	
	@Autowired
	private GeneroService generoService;
	
	@GetMapping("/libros")
	public String getAllLibros(Model model) {
		List<Libro> lisLibros = libroService.getAllLibros();
		model.addAttribute("libros", lisLibros);
        return "libroList";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("generos", generoService.getAllGeneros());
		return "libroRegister";
	}
	
	
	@PostMapping("/register")
	public String createLibro(@RequestParam("nombre") String nombre, 
			@RequestParam("nombreAutor") String nombreAutor,
			@RequestParam("fechaPublicacion") String fechaPublicacion,
			@RequestParam("idgenero") Long idgenero, Model model) {
		
		Libro libro = new Libro();
		libro.nombre = nombre;
		libro.nombreAutor = nombreAutor;
		libro.fechaPublicacion = fechaPublicacion;
		
		
		Genero genero = generoService.getGeneroById(idgenero);

		libro.genero = genero;
		
		libroService.createLibro(libro);
		
		model.addAttribute("libros", libroService.getAllLibros());
		model.addAttribute("generos", generoService.getAllGeneros());
		
		return "libroList";
	}
	
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		
		Libro libro = libroService.getLibroByID(id);
		
		model.addAttribute("libro", libro);
		model.addAttribute("generos", generoService.getAllGeneros());
		
		return "libroEdit";
	}
	
	
	@PostMapping("/edit")
	public String createLibro(@RequestParam("idlibro") Long idlibro, @RequestParam("nombre") String nombre, 
			@RequestParam("nombreAutor") String nombreAutor,
			@RequestParam("fechaPublicacion") String fechaPublicacion,
			@RequestParam("idgenero") Long idgenero, Model model) {
		
		Libro libro = libroService.getLibroByID(idlibro);
		libro.nombre = nombre;
		libro.nombreAutor = nombreAutor;
		libro.fechaPublicacion = fechaPublicacion;
		
		Genero genero = generoService.getGeneroById(idgenero);

		libro.genero = genero;
		
		libroService.createLibro(libro);
		
		model.addAttribute("libros", libroService.getAllLibros());
		model.addAttribute("generos", generoService.getAllGeneros());
		
		return "libroList";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id, Model model) {
		libroService.deleteLibro(id);
		
		model.addAttribute("libros", libroService.getAllLibros());
		model.addAttribute("generos", generoService.getAllGeneros());
		
		return "libroList";
	}
	
	@GetMapping("/report")
	public void report(HttpServletResponse response) throws JRException, IOException {

		// 1. Acceder al reporte 
		InputStream jasperStream = getClass().getResourceAsStream("/reportes/ReporteFinalAlmonacid.jasper");
		// 2. Preparadar los datos 
		Map<String, Object> params = new HashMap<>();
		
		
		params.put("cantidad","La cantidad de libros publicados antes de los 6 meses es de "+Long.toString(libroService.contarLibro()));
		List<Libro> lisLibro = libroService.getAllLibros();

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lisLibro);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

		// 3. Configuracion 
		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "filename=Reporte_cantidad_libros.pdf");
		// 4. Exportar reporte
		final OutputStream outputStream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

	}
}
