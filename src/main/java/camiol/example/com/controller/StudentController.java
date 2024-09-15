package camiol.example.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import camiol.example.com.entity.Student;
import camiol.example.com.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService service;
	
	@GetMapping("/addRandom")
	public String addRandomStudent() {
		for(int i=0;i<10;i++) {
			service.addStudent();
		}
		
		return service.getAll().toString();
	}
	
	@GetMapping("/get")
	public String getStudent(@RequestParam("id") long id) {
		return service.getStudent(id).toString();
	}
	
	@GetMapping("/add")
	public String addStudent(@RequestParam("name") String name,@RequestParam("score") int score) {
		Student s = new Student();
		s.setName(name);
		s.setMathScore(score);
		
		service.addStudent(s);
		
		List<Student> list = service.getAll();
		
		return list.get(list.size()-1).toString();
	}

}
