package br.com.lablims.controller;

import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoEquipamentoDTO;
import br.com.lablims.repos.SolucaoRegistroRepository;
import br.com.lablims.service.SolucaoEquipamentoService;
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
@RequestMapping("/solucaoEquipamentos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class SolucaoEquipamentoController {

    private final SolucaoEquipamentoService solucaoEquipamentoService;
    private final SolucaoRegistroRepository solucaoRegistroRepository;

    public SolucaoEquipamentoController(final SolucaoEquipamentoService solucaoEquipamentoService,
            final SolucaoRegistroRepository solucaoRegistroRepository) {
        this.solucaoEquipamentoService = solucaoEquipamentoService;
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
        final SimplePage<SolucaoEquipamentoDTO> solucaoEquipamentos = solucaoEquipamentoService.findAll(filter, pageable);
        model.addAttribute("solucaoEquipamentos", solucaoEquipamentos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(solucaoEquipamentos));
        return "solucaoEquipamento/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("solucaoEquipamento") final SolucaoEquipamentoDTO solucaoEquipamentoDTO) {
        return "solucaoEquipamento/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("solucaoEquipamento") @Valid final SolucaoEquipamentoDTO solucaoEquipamentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoEquipamento/add";
        }
        solucaoEquipamentoService.create(solucaoEquipamentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoEquipamento.create.success"));
        return "redirect:/solucaoEquipamentos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("solucaoEquipamento", solucaoEquipamentoService.get(id));
        return "solucaoEquipamento/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("solucaoEquipamento") @Valid final SolucaoEquipamentoDTO solucaoEquipamentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoEquipamento/edit";
        }
        solucaoEquipamentoService.update(id, solucaoEquipamentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoEquipamento.update.success"));
        return "redirect:/solucaoEquipamentos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        solucaoEquipamentoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("solucaoEquipamento.delete.success"));
        return "redirect:/solucaoEquipamentos";
    }

}
