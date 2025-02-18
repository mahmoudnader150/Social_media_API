package com.example.social_media;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class WelcomeController {

    private MessageSource messageSource;

    public WelcomeController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/welcome")
    public String welcome() {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage("welcome.message",null ,"Default message",locale);

    }
}
