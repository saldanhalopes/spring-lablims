package br.com.lablims.controller;

import br.com.lablims.model.MaterialTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.MaterialTipoService;
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
@RequestMapping("/materialTipos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class MaterialTipoController {

    private final MaterialTipoService materialTipoService;

    public MaterialTipoController(final MaterialTipoService materialTipoService) {
        this.materialTipoService = materialTipoService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<MaterialTipoDTO> materialTipos = materialTipoService.findAll(filter, pageable);
        model.addAttribute("materialTipos", materialTipos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(materialTipos));
        return "materialTipo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("materialTipo") final MaterialTipoDTO materialTipoDTO) {
        return "materialTipo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("materialTipo") @Valid final MaterialTipoDTO materialTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "materialTipo/add";
        }
        materialTipoService.create(materialTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("materialTipo.create.success"));
        return "redirect:/materialTipos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("materialTipo", materialTipoService.get(id));
        return "materialTipo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("materialTipo") @Valid final MaterialTipoDTO materialTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "materialTipo/edit";
        }
        materialTipoService.update(id, materialTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("materialTipo.update.success"));
        return "redirect:/materialTipos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = materialTipoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            materialTipoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("materialTipo.delete.success"));
        }
        return "redirect:/materialTipos";
    }

}
