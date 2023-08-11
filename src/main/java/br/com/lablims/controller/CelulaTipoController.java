package br.com.lablims.controller;

import br.com.lablims.model.CelulaTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.CelulaTipoService;
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
@RequestMapping("/celulaTipos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class CelulaTipoController {

    private final CelulaTipoService celulaTipoService;

    public CelulaTipoController(final CelulaTipoService celulaTipoService) {
        this.celulaTipoService = celulaTipoService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<CelulaTipoDTO> celulaTipos = celulaTipoService.findAll(filter, pageable);
        model.addAttribute("celulaTipos", celulaTipos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(celulaTipos));
        return "celulaTipo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("celulaTipo") final CelulaTipoDTO celulaTipoDTO) {
        return "celulaTipo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("celulaTipo") @Valid final CelulaTipoDTO celulaTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "celulaTipo/add";
        }
        celulaTipoService.create(celulaTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("celulaTipo.create.success"));
        return "redirect:/celulaTipos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("celulaTipo", celulaTipoService.get(id));
        return "celulaTipo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("celulaTipo") @Valid final CelulaTipoDTO celulaTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "celulaTipo/edit";
        }
        celulaTipoService.update(id, celulaTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("celulaTipo.update.success"));
        return "redirect:/celulaTipos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = celulaTipoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            celulaTipoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("celulaTipo.delete.success"));
        }
        return "redirect:/celulaTipos";
    }

}
