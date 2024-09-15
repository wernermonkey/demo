package camiol.example.com.service;

import java.util.List;

import camiol.example.com.entity.Student;

public interface StudentService {
	void addStudent();
	
	void addStudent(Student s);
	
	Student getStudent(long id);
	
	List<Student> getAll();
}
