package br.com.lablims.controller;

import br.com.lablims.model.RegistrationRequest;
import br.com.lablims.service.RegistrationService;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute final RegistrationRequest registrationRequest) {
        return "registration/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid final RegistrationRequest registrationRequest,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("username") && registrationService.usernameExists(registrationRequest)) {
            bindingResult.rejectValue("username", "registration.register.taken");
        }
        if (bindingResult.hasErrors()) {
            return "registration/register";
        }
        registrationService.register(registrationRequest);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("registration.register.success"));
        return "redirect:/login";
    }

}
