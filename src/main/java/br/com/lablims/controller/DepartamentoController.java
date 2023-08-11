package br.com.lablims.controller;

import br.com.lablims.model.DepartamentoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.DepartamentoService;
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
@RequestMapping("/departamentos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(final DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<DepartamentoDTO> departamentos = departamentoService.findAll(filter, pageable);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(departamentos));
        return "departamento/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("departamento") final DepartamentoDTO departamentoDTO) {
        return "departamento/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("departamento") @Valid final DepartamentoDTO departamentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "departamento/add";
        }
        departamentoService.create(departamentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("departamento.create.success"));
        return "redirect:/departamentos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("departamento", departamentoService.get(id));
        return "departamento/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("departamento") @Valid final DepartamentoDTO departamentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "departamento/edit";
        }
        departamentoService.update(id, departamentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("departamento.update.success"));
        return "redirect:/departamentos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = departamentoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            departamentoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("departamento.delete.success"));
        }
        return "redirect:/departamentos";
    }

}
