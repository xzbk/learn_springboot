# 微服务与SpringBoot
## 相关概念
* 微服务：一个项目，可以有多个小型服务构成，此小型服务就称之为微服务。
* springboot可以快速开发微服务
	* 简化j2ee的开发
	* 整个spring技术栈的整合(spring、spring)
	* 整个j2ee技术的整合(mybatis、redis)

## 开发前准备工作
* 配置jdk：  
	1. JAVA_HOME：jdk根目录
	2. path：jdk根目录\bin
	3. classpath：.;jdk根目录\lib
* 配置maven：  
	1. MAVEN_HOME：maven根目录
	2. path：maven根目录\bin
	3. 配置本地仓库
	4. 在IDE中配置mvn
* SpringBoot开发工具：
	1. Eclipse(STS插件)
	2. STS软件
	3. IDEA

# 第一个SpringBoot程序及执行原理
* 目录结构
```
java：放置java文件
resources：
	static：放置静态资源(js css 图片 视频 音频)
	templates：放置模板文件(freemarker，thymeleaf,默认不支持jsp)
	application.properties：配置文件
```
* 为什么SpringBoot不需要tomcat
SpringBoot内置了tomcat，并且不需要打成war再执行。
可以在配置文件中application.properties进行端口号等服务端信息进行配置。
* SpringBoot整合三方依赖
SpringBoot将各个应用/三方框架，设置成了一个一个的“场景”starter，选完之后，以后需要用哪个，只需要引入场景即可。选完之后，SpringBoot会将该场景所需要的所有依赖自动注入。

# SpringBoot源码解读
@SpringBootApplication注解的类是SpringBoot的主配置类，此注解包含两个注解：
## @SpringBootConfiguration
其中包含@Configuration注解:  
* @Configuration注解表示此类是一个配置类
* 加了@Configuration注解的类会自动纳入spring容器

## @EnableAutoConfiguration注解
该注解开启了SpringBoot的自动配置(约定优于配置)，此注解包含了两个注解：
* @AutoConfigurationPackage注解  
次注解可以找到@SpringBootApplication注解的主配置类所在的包，就会将该包及所有的子包全部纳入spring容器(主要是引入自己写的jar)。
* @Import注解  
此注解可以将三方依赖(jar、配置)引入  
SpringBoot在启动时，会根据META-INF/spring.factories找到相应的三方依赖，并将这些依赖引入  
**总结：**  
1. 编写项目是，一般会对自己写的代码以及三方依赖进行配置。但是SpringBoot可以进行自动配置。
	* 自己写的代码，通过@SpringBootConfiguration自动帮我们进行配置
	* 三方依赖通过spring-boot-autoconfigure-2.0.3RELEASE.jar中的META-INF/spring.factories进行声明，然后开启使用
	* spring-boot-autoconfigure-2.0.3RELEASE.jar保重包含了J2EE整个体系中所需要的依赖

# SpringBoot自动装配原理
## 原理
研究org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\  
通过观察发现：
* @Configuration：  
标识此类是一个配置类  
将此类纳入springioc容器
* @EnableConfigurationProperties({ServerProperties.class})：  
通过HttpEncodingProperties将编码设置为了UTF_8(即自动装配为UTF_8)  
如何修改编码通过修改HttpEncodingProperties的prefix+属性名，在配置文件(yml/properties)中进行修改
* @ConditionalOnProperty(prefix = "server.servlet.encoding",value = {"enabled"},matchIfMissing = true)  
当属性满足条件时，注解生效  
要求：如果没有配置server.servlet.encoding.enable=xxx，则成立
**总结：**  
1. 每一个xxxAutoConfiguration都有很多条件@ConditionalOnXxx注解，当这些条件都满足时，则此配置自动装配生效。  
但是可以手工修改自动装配：XxxProperties文件中的prefix.属性名=xxx  
2. 全局配文件中的key，来源于某个Properties中的prefix+属性名

## 查看开启配置
如何查看SpringBoot中开启了哪些自动装配、禁止了哪些自动装配:  
只需在全局配置文件application.properties中书写debug=true即可  
启动时会打印在控制台上信息：  
Positive matches列表 表示SpringBoot自动开启的装配  
Negative matches列表 表示SpringBoot此时没有自动开启的装配

# 配置文件的使用
## 配置文件作用：  
SpringBoot自动配置(约定，8080等)，可以使用配置文件对默认的配置进行修改
## 默认全局配置文件种类
* application.properties  
写法：  
```
server.port=8888
```
* application.yml  
写法：  
```
# k:空格v
server:
	port: 8888
```

## 通过配置文件给配置类注入值
1. 书写配置类
	* 法一：
	此种方法通过@ConfigurationProperties(prefix = "student")连接到对应的配置文件进行取值
	```
	@ConfigurationProperties(prefix = "student")
	@Component
	public class Student {
	```
	* 法二：
	通过@Value直接配置值
	```
	@Value("lsss")
    private String name;
	```
	* 其他：  
	@Validated:次注解用户开启JSR303数据校验
	```
	@ConfigurationProperties(prefix = "student")
	@Component
	@Validated//开启JSR303数据校验的注解
	public class Student {
   		@Email//用于数据校验
    	private String email;
	```
	@PropertySource注解：次注解默认会加载applicaion.properties/application.yml文件中的数据
	```
	@ConfigurationProperties(prefix = "student")
	@Component
	@Validated//开启JSR303数据校验的注解
	@PropertySource({"conf.properties"})//此种配置即可加载conf.properties中的数据
	public class Student {
	```
2. 书写配置文件  
	* yml配置中此种注入写方法分两种:  
	写法一：行内写法
	```
	student:
	    name: zs
	    age: 23
	    sex: true
	    birthday: 2019/02/08
	    location: {province: 陕西,city: 西安,zone: 莲湖区}
	    hobbys: [足球,篮球]
	    skills: [足球,篮球]
	    pet: {nickName: wc,strain: hsq}
	```
	写法二
	```
	student:
    name: zs
    age: 23
    sex: true
    birthday: 2019/02/08
    location:
      province: 陕西
      city: 西安
      zone: 莲湖区
    hobbys:
      - 足球
      - 篮球
    skills:
      - 编程
      - 金融
    pet:
      nikcName: wc
      strain: hsq
	```
	* properties配置文件中的书写方式
	```
	student.name=ls
	student.age=90
	```

# @ImportResource、配置类、占位符表达式
## @ImportResource
SringBoot会自动装配，所以spring等配置文件默认会被SpringBoot配置好。  
所以自己编写的spring配置文件，SpringBoot默认是不识别的。  
想要开启识别，则只需在SpringBoot主配置类上，使用注解@ImportResource(locations={"classpath:spring.xml"})来指定配置文件的路径即可。  
但是不推荐手写spring配置文件(xml形式的配置)，推荐使用注解配置。  

## 配置类(注解方式配置)
1. 书写配置类并添加注解
2. @Configuration  
@Bean  
示例：
```
@Configuration//标明此类是一个配置类
public class AppConfig {
    @Bean//标明此为一个javaBean
    public StudentService studentService(){
        return new StudentService();
    }
}
```

## 占位符表达式
占位符可以直接使用于SpringBoot配置文件中进行取值，常用占位符表达式如下：
* ${Random.uuid}:用于获取uuid
* ${Random.value}:用于获取随机字符串
* ${Random.int}:用于获取随机整数
* ${Random.long}:用于获取随机长整数
* ${Random.int(10)}:用于获取10以内的整型数
* ${Random.int[1024,65536]}:用于获取指定随机数范围
* ${user.name:ls}:在yml中直接引用properties中的user.name的值，如果没引用到则默认为ls(:ls默认值可省略)

# SpringBoot多环境设置及切换
配置文件优先级：application.properties>application.yml  
多环境写法：  
application-环境名.yml
application-环境名.properties
application-dev.properties
application-test.propeties
## properties中的切换方式
默认SpringBoot会读取application.properties默认配置环境  
如果要选择某一个具体环境：  
application。properties中指定spring.profiles.active=环境名

## yml中的切换方式
**方式一：** 与properties切换方式相同
**方式二：** 单配置多环境  
```
# 单配置多环境
spring:
  profiles:
    active:  dev
---
spring:
  profiles:  dev
server:
  port: 90
---
spring:
  profiles:  test
```

# 配置文件的位置
properties和yml中的配置相互补充；如果冲突，则properties优先级高。  
SpringBoot默认能够读取application.properties/application.yml，这两个配置可以存在于以下四个位置：
* file(普通文件夹)：项目根目录/config
* file(普通文件夹)：项目根目录
* classpath(源码包类路径)：项目根目录/config
* classpath(源码包类路径)：项目根目录  
**注意：**
1. 如果配置冲突，则优先级从上往下
2. 如果不冲突，则互不结合使用

# SpringBoot日志处理
## 日志框架
JCL、JUL、jbos-logging、logback、log4j、log4j2、sl4j等  
SpringBoot默认选用的是sl4j，logback  
SpringBoot默认帮我们配置好了日志，我们直接使用即可。  
## 日志级别 
TRACE<DEBUG<INFO<WARN<ERROR<FATAL<OFF  
SpringBoot默认的日志级别是info(即只打印info及之后级别的信息)，也可以自定义级别：  
在配置文件中书写logging.level.com.group=warn  
将日志输出到文件中：  
在配置文件中书写logging.file=springBoot.log

# SpringBoot处理web资源
## 处理web资源
SpringBoot是一个jar，因此，静态资源不再是存放到wevapps中，而是封装成jar包，需要引用时直接添加maven的jar依赖即可。  
静态资源的存放路径通过WebMVCAutoConfiguration-addResourceHandlers()指定  
**静态资源引入路径的书写：**  
从jar目录结构的webjars开始向下写：`localhost:8080/webjars/jquery/3.3.1-1/jquery.js`

## 处理web静态资源
SpringBoot约定：SpringBoot将一些目录结构设置成静态资源存放目录，我们的静态资源直接放入这些目录即可。目录位置：
```
{
	"classpath:/META-INF/resources/","classpath:/resources/",
	"classpath:/static/","classpath:/public/"
}
```
**注意：**  
以上目录存放静态资源文件后，访问时不需要加前缀，直接范文即可：http//localhost:8080/hello.html  
**设置欢迎页：**  
将欢迎页其名为index.html放入静态资源目录中即可  
**修改网站图标：**  
将图标更名为favicon.ico放入静态资源目录即可  
**自定义静态资源目录：**  
在配置文件中书写`spring.resources.static-locations=classpath:/res/,classpath:/img/`,自定义静态资源目录后，之前默认的目录会失效

# ThymeLeaf模板引擎
SpringBoot默认不支持JSP，推荐使用模板引擎(ThymeLeaf,详细文档查看官网)  
模板引擎：网页=模板+数据  
**使用模板引擎前提：**  
1. 引入pom中的模板引擎的jar依赖
2. 书写头文件库(在templates文件夹中)
```
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:score="http://thymeleafexamples">
```
**常用集合操作:**  
```
<table>
  <tr>
    <th>NAME</th>
    <th>PRICE</th>
    <th>IN STOCK</th>
  </tr>
  <tr th:each="prod : ${prods}">
    <td th:text="${prod.name}">Onions</td>
    <td th:text="${prod.price}">2.41</td>
    <td th:text="${prod.inStock}? #{true} : #{false}">yes</td>
  </tr>
</table>
```

# SpringBoot整合外部tomcat以及使用jsp开发