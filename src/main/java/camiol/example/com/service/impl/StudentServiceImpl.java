package camiol.example.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import camiol.example.com.dao.StudentDao;
import camiol.example.com.entity.Student;
import camiol.example.com.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentDao studentDao;
	
	private String[] firstNames = {"Camiol","Joyce","Lucy","Ruth","Louis"};
	
	private String[] lastNames = {"Chuang","Lin","Wang","Chen","Zhang"};
	
	@Override
	public void addStudent() {
		Student s = new Student();
		String firstName = firstNames[(int) (Math.random()*firstNames.length)];
		String lastName = lastNames[(int) (Math.random()*lastNames.length)];
		int score = (int)(Math.random()*100)+1;
		
		s.setName(firstName+" "+lastName);
		s.setMathScore(score);
		System.out.println(s);
		studentDao.save(s);
	}

	@Override
	public void addStudent(Student s) {
		studentDao.save(s);
	}

	@Override
	public Student getStudent(long id) {
		Student s = studentDao.findById(id).orElse(new Student());
		return s;

	}

	@Override
	public List<Student> getAll() {
		
		return studentDao.findAll();
	}

}
