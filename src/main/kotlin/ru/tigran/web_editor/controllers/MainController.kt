package ru.tigran.web_editor.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping
    fun mainPage() : String {
        return "index";
    }
    @GetMapping("/editor")
    fun editorFrame() : String {
        return "editor";
    }
}