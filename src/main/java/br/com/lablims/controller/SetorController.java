package br.com.lablims.controller;

import br.com.lablims.domain.Departamento;
import br.com.lablims.domain.Setor;
import br.com.lablims.model.SetorDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.DepartamentoRepository;
import br.com.lablims.service.SetorService;
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
import org.springframework.beans.factory.annotation.Autowired;
import br.com.lablims.repos.*;
import br.com.lablims.config.EntityRevision;

import java.util.List;


@Controller
@RequestMapping("/setors")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class SetorController {

    private final SetorService setorService;
    private final DepartamentoRepository departamentoRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;
    public SetorController(final SetorService setorService,
            final DepartamentoRepository departamentoRepository) {
        this.setorService = setorService;
        this.departamentoRepository = departamentoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("departamentoValues", departamentoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Departamento::getId, Departamento::getDepartamento)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<SetorDTO> setors = setorService.findAll(filter, pageable);
        model.addAttribute("setors", setors);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(setors));
        return "setor/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("setor") final SetorDTO setorDTO) {
        return "setor/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("setor") @Valid final SetorDTO setorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "setor/add";
        }
        setorService.create(setorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("setor.create.success"));
        return "redirect:/setors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("setor", setorService.get(id));
        return "setor/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("setor") @Valid final SetorDTO setorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "setor/edit";
        }
        setorService.update(id, setorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("setor.update.success"));
        return "redirect:/setors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = setorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            setorService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("setor.delete.success"));
        }
        return "redirect:/setors";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<Setor>> revisoes = genericRevisionRepository.listaRevisoes(Setor.class);
        model.addAttribute("audits", revisoes);
        return "/setor/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        Setor setor = setorService.findById(id);
        List<EntityRevision<Setor>> revisoes = genericRevisionRepository.listaRevisoesById(setor.getId(), Setor.class);
        model.addAttribute("audits", revisoes);
        return "/setor/audit";
    }

}
