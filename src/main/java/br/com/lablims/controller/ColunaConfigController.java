package br.com.lablims.controller;

import br.com.lablims.model.ColunaConfigDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.ColunaConfigService;
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
@RequestMapping("/colunaConfigs")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaConfigController {

    private final ColunaConfigService colunaConfigService;

    public ColunaConfigController(final ColunaConfigService colunaConfigService) {
        this.colunaConfigService = colunaConfigService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaConfigDTO> colunaConfigs = colunaConfigService.findAll(filter, pageable);
        model.addAttribute("colunaConfigs", colunaConfigs);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaConfigs));
        return "colunaConfig/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaConfig") final ColunaConfigDTO colunaConfigDTO) {
        return "colunaConfig/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("colunaConfig") @Valid final ColunaConfigDTO colunaConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaConfig/add";
        }
        colunaConfigService.create(colunaConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaConfig.create.success"));
        return "redirect:/colunaConfigs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaConfig", colunaConfigService.get(id));
        return "colunaConfig/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaConfig") @Valid final ColunaConfigDTO colunaConfigDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaConfig/edit";
        }
        colunaConfigService.update(id, colunaConfigDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaConfig.update.success"));
        return "redirect:/colunaConfigs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaConfigService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaConfigService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaConfig.delete.success"));
        }
        return "redirect:/colunaConfigs";
    }

}
