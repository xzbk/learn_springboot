package com.group;

import com.group.entity.Student;
import com.group.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class LearnSpringbootApplicationTests {
	@Resource
	Student student;
	@Resource
	ApplicationContext  context;
	Logger logger = LoggerFactory.getLogger(LearnSpringbootApplicationTests.class);
	/**
	 * 测试student通过配置文件注入值
	 */
	@Test
	void contextLoads() {
		System.out.println(student);
	}
	/**
	 * 测试spring手动配置文件(非默认配置)
	 */
	@Test
	void configSpring() {
		StudentService studentService = context.getBean("studentService", StudentService.class);
		System.out.println(studentService);
	}
	/**
	 * 测试SpringBoot的日志
	 */
	@Test
	void testLog() {
		logger.trace("trace****************");
		logger.debug("debug*************");
		logger.info("info******************");
		logger.warn("warn****************");
		logger.error("error****************");
	}
}
