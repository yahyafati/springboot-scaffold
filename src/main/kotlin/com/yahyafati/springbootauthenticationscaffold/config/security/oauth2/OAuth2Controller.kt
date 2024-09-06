package com.yahyafati.springbootauthenticationscaffold.config.security.oauth2

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/info")
class OAuth2Controller {

    @GetMapping
    fun info(
        @RequestParam token: String,
        @RequestParam(name = "new") isNew: Boolean,
        model: Model
    ): String? {
        model.addAttribute("token", token)
        model.addAttribute("new", isNew)
        return "oauth2-success"
    }


    private class Info {
        private val application: String? = null
        private val principal: Map<String, Any>? = null
    }
}