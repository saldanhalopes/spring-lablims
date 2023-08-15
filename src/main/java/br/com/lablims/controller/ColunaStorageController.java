package br.com.lablims.controller;

import br.com.lablims.config.EntityRevision;
import br.com.lablims.domain.ColunaStorage;
import br.com.lablims.domain.ColunaStorageTipo;
import br.com.lablims.domain.Setor;
import br.com.lablims.model.ColunaStorageDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.ColunaStorageTipoRepository;
import br.com.lablims.repos.GenericRevisionRepository;
import br.com.lablims.repos.SetorRepository;
import br.com.lablims.service.ColunaStorageService;
import br.com.lablims.util.CustomCollectors;
import br.com.lablims.util.UserRoles;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;


@Controller
@RequestMapping("/colunaStorages")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaStorageController {

    private final ColunaStorageService colunaStorageService;
    private final SetorRepository setorRepository;
    private final ColunaStorageTipoRepository colunaStorageTipoRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ColunaStorageController(final ColunaStorageService colunaStorageService,
            final SetorRepository setorRepository,
            final ColunaStorageTipoRepository colunaStorageTipoRepository) {
        this.colunaStorageService = colunaStorageService;
        this.setorRepository = setorRepository;
        this.colunaStorageTipoRepository = colunaStorageTipoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("setorValues", setorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Setor::getId, Setor::getSetor)));
        model.addAttribute("tipoValues", colunaStorageTipoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaStorageTipo::getId, ColunaStorageTipo::getTipo)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaStorageDTO> colunaStorages = colunaStorageService.findAll(filter, pageable);
        model.addAttribute("colunaStorages", colunaStorages);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaStorages));
        return "colunaStorage/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaStorage") final ColunaStorageDTO colunaStorageDTO) {
        return "colunaStorage/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("colunaStorage") @Valid final ColunaStorageDTO colunaStorageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaStorage/add";
        }
        colunaStorageService.create(colunaStorageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaStorage.create.success"));
        return "redirect:/colunaStorages";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaStorage", colunaStorageService.get(id));
        return "colunaStorage/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaStorage") @Valid final ColunaStorageDTO colunaStorageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaStorage/edit";
        }
        colunaStorageService.update(id, colunaStorageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaStorage.update.success"));
        return "redirect:/colunaStorages";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaStorageService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaStorageService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaStorage.delete.success"));
        }
        return "redirect:/colunaStorages";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<ColunaStorage>> revisoes = genericRevisionRepository.listaRevisoes(ColunaStorage.class);
        model.addAttribute("audits", revisoes);
        return "/xxx/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        ColunaStorage colunaStorage = colunaStorageService.findById(id);
        List<EntityRevision<ColunaStorage>> revisoes = genericRevisionRepository.listaRevisoesById(colunaStorage.getId(), ColunaStorage.class);
        model.addAttribute("audits", revisoes);
        return "/colunaStorage/audit";
    }

}
