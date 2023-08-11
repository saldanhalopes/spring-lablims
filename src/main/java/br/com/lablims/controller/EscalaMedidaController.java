package br.com.lablims.controller;

import br.com.lablims.model.EscalaMedidaDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.EscalaMedidaService;
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
@RequestMapping("/escalaMedidas")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class EscalaMedidaController {

    private final EscalaMedidaService escalaMedidaService;

    public EscalaMedidaController(final EscalaMedidaService escalaMedidaService) {
        this.escalaMedidaService = escalaMedidaService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<EscalaMedidaDTO> escalaMedidas = escalaMedidaService.findAll(filter, pageable);
        model.addAttribute("escalaMedidas", escalaMedidas);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(escalaMedidas));
        return "escalaMedida/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("escalaMedida") final EscalaMedidaDTO escalaMedidaDTO) {
        return "escalaMedida/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("escalaMedida") @Valid final EscalaMedidaDTO escalaMedidaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "escalaMedida/add";
        }
        escalaMedidaService.create(escalaMedidaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("escalaMedida.create.success"));
        return "redirect:/escalaMedidas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("escalaMedida", escalaMedidaService.get(id));
        return "escalaMedida/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("escalaMedida") @Valid final EscalaMedidaDTO escalaMedidaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "escalaMedida/edit";
        }
        escalaMedidaService.update(id, escalaMedidaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("escalaMedida.update.success"));
        return "redirect:/escalaMedidas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = escalaMedidaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            escalaMedidaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("escalaMedida.delete.success"));
        }
        return "redirect:/escalaMedidas";
    }

}
