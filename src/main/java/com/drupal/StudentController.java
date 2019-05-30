package com.drupal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
	public List<Student> getAllStudents() {
		return repo.findAll();
	}

	@RequestMapping(path = "students/create", method = RequestMethod.POST)
	@ResponseBody
	public Student postStudent(Student student) {
		System.out.println("inside post");
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
	public Optional<Student> students(@PathVariable("sid") int sid) {
		System.out.println(repo.findById(sid));
		return repo.findById(sid);
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
