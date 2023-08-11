package br.com.lablims.controller;

import br.com.lablims.model.AnaliseDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.AnaliseService;
import br.com.lablims.util.UserRoles;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/analises")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class AnaliseController {

    private final AnaliseService analiseService;

    public AnaliseController(final AnaliseService analiseService) {
        this.analiseService = analiseService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<AnaliseDTO> analises = analiseService.findAll(filter, pageable);
        model.addAttribute("analises", analises);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(analises));
        return "analise/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("analise") final AnaliseDTO analiseDTO) {
        return "analise/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("analise") @Valid final AnaliseDTO analiseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "analise/add";
        }
        analiseService.create(analiseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("analise.create.success"));
        return "redirect:/analises";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("analise", analiseService.get(id));
        return "analise/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("analise") @Valid final AnaliseDTO analiseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "analise/edit";
        }
        analiseService.update(id, analiseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("analise.update.success"));
        return "redirect:/analises";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = analiseService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            analiseService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("analise.delete.success"));
        }
        return "redirect:/analises";
    }

}
