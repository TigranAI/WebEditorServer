package ru.tigran.web_editor.config

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration
import ru.tigran.web_editor.WebEditorApplication

@Configuration
class ServletInitializer : SpringBootServletInitializer() {
	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(WebEditorApplication::class.java)
	}
}
