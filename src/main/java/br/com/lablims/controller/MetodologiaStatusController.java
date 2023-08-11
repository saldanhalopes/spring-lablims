package br.com.lablims.controller;

import br.com.lablims.model.MetodologiaStatusDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.MetodologiaStatusService;
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
@RequestMapping("/metodologiaStatuss")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class MetodologiaStatusController {

    private final MetodologiaStatusService metodologiaStatusService;

    public MetodologiaStatusController(final MetodologiaStatusService metodologiaStatusService) {
        this.metodologiaStatusService = metodologiaStatusService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<MetodologiaStatusDTO> metodologiaStatuss = metodologiaStatusService.findAll(filter, pageable);
        model.addAttribute("metodologiaStatuss", metodologiaStatuss);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(metodologiaStatuss));
        return "metodologiaStatus/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("metodologiaStatus") final MetodologiaStatusDTO metodologiaStatusDTO) {
        return "metodologiaStatus/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("metodologiaStatus") @Valid final MetodologiaStatusDTO metodologiaStatusDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "metodologiaStatus/add";
        }
        metodologiaStatusService.create(metodologiaStatusDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("metodologiaStatus.create.success"));
        return "redirect:/metodologiaStatuss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("metodologiaStatus", metodologiaStatusService.get(id));
        return "metodologiaStatus/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("metodologiaStatus") @Valid final MetodologiaStatusDTO metodologiaStatusDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "metodologiaStatus/edit";
        }
        metodologiaStatusService.update(id, metodologiaStatusDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("metodologiaStatus.update.success"));
        return "redirect:/metodologiaStatuss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = metodologiaStatusService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            metodologiaStatusService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("metodologiaStatus.delete.success"));
        }
        return "redirect:/metodologiaStatuss";
    }

}
