package br.com.lablims.controller;

import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.Material;
import br.com.lablims.domain.Metodologia;
import br.com.lablims.domain.MetodologiaStatus;
import br.com.lablims.model.MetodologiaVesaoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.ArquivosRepository;
import br.com.lablims.repos.MaterialRepository;
import br.com.lablims.repos.MetodologiaRepository;
import br.com.lablims.repos.MetodologiaStatusRepository;
import br.com.lablims.service.MetodologiaVesaoService;
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
@RequestMapping("/metodologiaVesaos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class MetodologiaVesaoController {

    private final MetodologiaVesaoService metodologiaVesaoService;
    private final MetodologiaRepository metodologiaRepository;
    private final ArquivosRepository arquivosRepository;
    private final MaterialRepository materialRepository;
    private final MetodologiaStatusRepository metodologiaStatusRepository;

    public MetodologiaVesaoController(final MetodologiaVesaoService metodologiaVesaoService,
            final MetodologiaRepository metodologiaRepository,
            final ArquivosRepository arquivosRepository,
            final MaterialRepository materialRepository,
            final MetodologiaStatusRepository metodologiaStatusRepository) {
        this.metodologiaVesaoService = metodologiaVesaoService;
        this.metodologiaRepository = metodologiaRepository;
        this.arquivosRepository = arquivosRepository;
        this.materialRepository = materialRepository;
        this.metodologiaStatusRepository = metodologiaStatusRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("metodologiaValues", metodologiaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Metodologia::getId, Metodologia::getCodigo)));
        model.addAttribute("anexoValues", arquivosRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arquivos::getId, Arquivos::getNome)));
        model.addAttribute("materialValues", materialRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Material::getId, Material::getFiscalizado)));
        model.addAttribute("statusValues", metodologiaStatusRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(MetodologiaStatus::getId, MetodologiaStatus::getStatus)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<MetodologiaVesaoDTO> metodologiaVesaos = metodologiaVesaoService.findAll(filter, pageable);
        model.addAttribute("metodologiaVesaos", metodologiaVesaos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(metodologiaVesaos));
        return "metodologiaVesao/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("metodologiaVesao") final MetodologiaVesaoDTO metodologiaVesaoDTO) {
        return "metodologiaVesao/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("metodologiaVesao") @Valid final MetodologiaVesaoDTO metodologiaVesaoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "metodologiaVesao/add";
        }
        metodologiaVesaoService.create(metodologiaVesaoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("metodologiaVesao.create.success"));
        return "redirect:/metodologiaVesaos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("metodologiaVesao", metodologiaVesaoService.get(id));
        return "metodologiaVesao/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("metodologiaVesao") @Valid final MetodologiaVesaoDTO metodologiaVesaoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "metodologiaVesao/edit";
        }
        metodologiaVesaoService.update(id, metodologiaVesaoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("metodologiaVesao.update.success"));
        return "redirect:/metodologiaVesaos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = metodologiaVesaoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            metodologiaVesaoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("metodologiaVesao.delete.success"));
        }
        return "redirect:/metodologiaVesaos";
    }

}
