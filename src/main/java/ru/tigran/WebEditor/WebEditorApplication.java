package ru.tigran.WebEditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableDiscoveryClient
@SpringBootApplication
@Controller
@RequestMapping("/editor")
public class WebEditorApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebEditorApplication.class, args);
	}

	@GetMapping()
	public String index() {
		return "index";
	}

	@GetMapping("/monaco")
	public String get() {
		return "editor :: monaco";
	}
}
