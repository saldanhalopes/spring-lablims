package br.com.lablims.controller;

import br.com.lablims.model.AnaliseProdutividadeDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.service.AnaliseProdutividadeService;
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
@RequestMapping("/analiseProdutividades")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class AnaliseProdutividadeController {

    private final AnaliseProdutividadeService analiseProdutividadeService;

    public AnaliseProdutividadeController(
            final AnaliseProdutividadeService analiseProdutividadeService) {
        this.analiseProdutividadeService = analiseProdutividadeService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<AnaliseProdutividadeDTO> analiseProdutividades = analiseProdutividadeService.findAll(filter, pageable);
        model.addAttribute("analiseProdutividades", analiseProdutividades);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(analiseProdutividades));
        return "analiseProdutividade/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("analiseProdutividade") final AnaliseProdutividadeDTO analiseProdutividadeDTO) {
        return "analiseProdutividade/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("analiseProdutividade") @Valid final AnaliseProdutividadeDTO analiseProdutividadeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "analiseProdutividade/add";
        }
        analiseProdutividadeService.create(analiseProdutividadeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("analiseProdutividade.create.success"));
        return "redirect:/analiseProdutividades";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("analiseProdutividade", analiseProdutividadeService.get(id));
        return "analiseProdutividade/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("analiseProdutividade") @Valid final AnaliseProdutividadeDTO analiseProdutividadeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "analiseProdutividade/edit";
        }
        analiseProdutividadeService.update(id, analiseProdutividadeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("analiseProdutividade.update.success"));
        return "redirect:/analiseProdutividades";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = analiseProdutividadeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            analiseProdutividadeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("analiseProdutividade.delete.success"));
        }
        return "redirect:/analiseProdutividades";
    }

}
