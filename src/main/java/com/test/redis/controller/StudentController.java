package com.test.redis.controller;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.redis.model.Student;

@Controller
@RequestMapping("/student")
public class StudentController {

	@PostMapping
	@CachePut(value="student", key="#student.id")
	public @ResponseBody Student save(@RequestBody Student student) {
		return student;
	}
	
	@GetMapping("/{id}")
	@Cacheable(value="student", key="#id")
	public @ResponseBody Student query(@PathVariable Integer id) {
		Student student = new Student();
		student.setId(1);
		student.setName("Jalo");
		student.setEmail("1@qq.com");
		return student;
	}
}
