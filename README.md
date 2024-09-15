# 建立一個SpringBoot + Spring + JPA 的Web專案

[![hackmd-github-sync-badge](https://hackmd.io/i3T9xRyQR0OOVczCQmtkZQ/badge)](https://hackmd.io/i3T9xRyQR0OOVczCQmtkZQ)


- [Github位置](https://github.com/camioljoyce/springbootDemo)

建立一個Spring Boot專案，在Eclipse功能選單選擇 
File -> New -> Spring Starter Project
![](https://i.imgur.com/8Orv34K.jpg)

填好相關資訊按下next
![](https://i.imgur.com/TN7sDTO.jpg)

在SQL這邊選擇Spring Data JPA和MySQL Driver
然後在Web 選擇Spring Web 按Finish
![](https://i.imgur.com/txicpcr.jpg)


在專案上面按右鍵選Properties,選擇Project Facets設定後按下Apply and Close
![](https://i.imgur.com/CNeY8x7.jpg)


在src/main/resources下的application.properties設定檔，加入MySQL的datasource連線及JPA等設定

```\
# DataSource 配置
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mydb?useUnicode=yes&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=1qaz@WSX
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# hibernate 5.3.1後新增了MYSQL8Dialect
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

加入下列這些class和interface
![](https://i.imgur.com/nghIZbQ.jpg)

在entity層加入Student class
```java=\
package camiol.example.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name = "Student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name="Name")
	private String name;
	@Column(name="MathScore")
	private int mathScore;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMathScore() {
		return mathScore;
	}
	public void setMathScore(int mathScore) {
		this.mathScore = mathScore;
	}
	
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
}

```

在dao層加入StudentDao interface
```java=\
package camiol.example.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import camiol.example.com.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, Long>{

}

```

在service層 加入StudentService interface
```java=\
package camiol.example.com.service;

import java.util.List;

import camiol.example.com.entity.Student;

public interface StudentService {
	void addStudent();
	
	void addStudent(Student s);
	
	Student getStudent(long id);
	
	List<Student> getAll();
}

```

在service.impl層 加入StudentServiceImpl class
```java=\
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

```

在controller層 加入StudentController class
```java=\
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

```

最後在SpringbootDemoApplication 這邊右鍵
run as Java Application

即可看到console正在啟動
![](https://i.imgur.com/wIFJXze.jpg)

回到MySQL這邊,即可看到剛剛的Student table已建立
![](https://i.imgur.com/N09cTRx.jpg)

目前還沒有資料, 我們來測試加入資料
輸入網址:http://localhost:8080/student/addRandom

![](https://i.imgur.com/WcF7iX5.jpg)
可以看到已經隨機insert了10筆Student資料
![](https://i.imgur.com/6uLLhlD.jpg)

接著來找出剛剛新增的id=5的學生資料
輸入網址:http://localhost:8080/student/get?id=5
![](https://i.imgur.com/MFkLP3N.jpg)


最後我們來新增指定的學生資料
輸入網址:http://localhost:8080/student/add?name=John&score=100
![](https://i.imgur.com/giTRV13.jpg)

檢查一下DB, 可以發現已成功正確insert指定資料
![](https://i.imgur.com/LTMiFQC.jpg)

以上就是基本的SpringBoot + Spring + JPA + MySQL的專案設定

###### tags: `Spring boot`







