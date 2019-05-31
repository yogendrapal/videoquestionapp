package com.drupal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.dao.StudentRepo;
import com.drupal.models.Student;
import com.drupal.models.StudentHiddenPassword;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class StudentController {
	@Autowired
	StudentRepo repo;

//	@RequestMapping("getStudent")
//	public ModelAndView getStudent(@RequestParam int sid) {
//		ModelAndView mv = new ModelAndView("GetStudent");
//		Student student = repo.findById(sid).orElse(new Student());
//		mv.addObject(student);
//		System.out.println("Student: "+ student);
//		System.out.println(repo.findByName("Pratik Gupta"));
//		return mv;
//	}

	@RequestMapping(path = "students", method = RequestMethod.GET)
	@ResponseBody
	public List<StudentHiddenPassword> getAllStudents() {
		List<Student> students= repo.findAll();
		List<StudentHiddenPassword> studentsHiddenPassword = new ArrayList<StudentHiddenPassword>();
		for(Student s: students) {
			studentsHiddenPassword.add(new StudentHiddenPassword(s));
		}
		return studentsHiddenPassword;
		
	}

	@RequestMapping(path = "students/create", method = RequestMethod.POST)
	@ResponseBody
	public Student postStudent(Student student) {
		System.out.println("inside post");
		String password = student.getPassword();
		String encryptedPass = AES.encrypt(password, "This is secret");
		student.setPassword(encryptedPass);
		repo.save(student);
		// return "Home"; This also works
		return student;
	}

	@RequestMapping(path = "students/update/{sid}", method = RequestMethod.PUT, produces = {"appliation/json"})
	@ResponseBody
	public String saveOrUpdateStudent(@PathVariable("sid") int id,  @RequestPart(name="name") String name, @RequestPart String email, @RequestPart String password) {
		Student s = repo.findById(id).orElse(null);
		System.out.println("inside put");
		if(s==null) {
			return "{\"Error\":\"Student assiciated with the id is not present\"}";
		}
		s.setEmail(email);
		s.setName(name);
		s.setPassword(password);
		repo.save(s);
		
		ObjectMapper Obj = new ObjectMapper(); 
		String jsonStr = s.toString();
        try { 
            jsonStr = Obj.writeValueAsString(s); 
            System.out.println(jsonStr); 
        } 
  
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
		// return "Home"; This also works
		return jsonStr;
	}

	@RequestMapping(path = "students/{sid}", method = RequestMethod.GET)
	@ResponseBody
	public StudentHiddenPassword students(@PathVariable("sid") int sid) {
		System.out.println(repo.findById(sid));
		Student s = repo.findById(sid).orElse(null);
		StudentHiddenPassword toReturn = new StudentHiddenPassword(s);
//		if(s==null) {
//			return null;
//		}
//		else {
//			s.setPassword(AES.decrypt(s.getPassword(), "This is secret"));
//		}
		return toReturn;
	}

	@DeleteMapping(path = "students/{sid}")
	@ResponseBody
	public String deleteStudent(@PathVariable("sid") int id) {
		System.out.println("deleteing");
		Student s = repo.findById(id).orElse(null);
		if (s != null) {
			repo.deleteById(id);
			return "Deleted";
		} else {
			return ("Student not present");
		}
	}
}
