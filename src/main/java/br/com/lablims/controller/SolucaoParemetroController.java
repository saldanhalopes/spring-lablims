package br.com.lablims.controller;

import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoParemetroDTO;
import br.com.lablims.repos.SolucaoRegistroRepository;
import br.com.lablims.service.SolucaoParemetroService;
import br.com.lablims.util.CustomCollectors;
import br.com.lablims.util.UserRoles;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/solucaoParemetros")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class SolucaoParemetroController {

    private final SolucaoParemetroService solucaoParemetroService;
    private final SolucaoRegistroRepository solucaoRegistroRepository;

    public SolucaoParemetroController(final SolucaoParemetroService solucaoParemetroService,
            final SolucaoRegistroRepository solucaoRegistroRepository) {
        this.solucaoParemetroService = solucaoParemetroService;
        this.solucaoRegistroRepository = solucaoRegistroRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("solucaoRegistroValues", solucaoRegistroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(SolucaoRegistro::getId, SolucaoRegistro::getDescricao)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<SolucaoParemetroDTO> solucaoParemetros = solucaoParemetroService.findAll(filter, pageable);
        model.addAttribute("solucaoParemetros", solucaoParemetros);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(solucaoParemetros));
        return "solucaoParemetro/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("solucaoParemetro") final SolucaoParemetroDTO solucaoParemetroDTO) {
        return "solucaoParemetro/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("solucaoParemetro") @Valid final SolucaoParemetroDTO solucaoParemetroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoParemetro/add";
        }
        solucaoParemetroService.create(solucaoParemetroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoParemetro.create.success"));
        return "redirect:/solucaoParemetros";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("solucaoParemetro", solucaoParemetroService.get(id));
        return "solucaoParemetro/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("solucaoParemetro") @Valid final SolucaoParemetroDTO solucaoParemetroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoParemetro/edit";
        }
        solucaoParemetroService.update(id, solucaoParemetroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoParemetro.update.success"));
        return "redirect:/solucaoParemetros";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        solucaoParemetroService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("solucaoParemetro.delete.success"));
        return "redirect:/solucaoParemetros";
    }

}
