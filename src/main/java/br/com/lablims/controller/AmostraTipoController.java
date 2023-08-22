package br.com.lablims.controller;

import br.com.lablims.config.EntityRevision;
import br.com.lablims.domain.AmostraTipo;
import br.com.lablims.domain.CustomRevisionEntity;
import br.com.lablims.model.AmostraTipoDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.GenericRevisionRepository;
import br.com.lablims.service.AmostraTipoService;
import br.com.lablims.service.UsuarioService;
import br.com.lablims.util.UserRoles;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/amostraTipos")
public class AmostraTipoController {

    private final AmostraTipoService amostraTipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public AmostraTipoController(final AmostraTipoService amostraTipoService) {
        this.amostraTipoService = amostraTipoService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final SimplePage<AmostraTipoDTO> amostraTipos = amostraTipoService.findAll(filter, pageable);
        model.addAttribute("amostraTipos", amostraTipos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(amostraTipos));
        return "amostraTipo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("amostraTipo") final AmostraTipoDTO amostraTipoDTO) {
        return "amostraTipo/add";
    }
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "', '" + UserRoles.POWERUSER + "')")
    @PostMapping("/add")
    public String add(@ModelAttribute("amostraTipo") @Valid final AmostraTipoDTO amostraTipoDTO, final Model model,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes,
                      Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            return "amostraTipo/add";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText("Novo Registro");
                amostraTipoService.create(amostraTipoDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amostraTipo.create.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.login.error"));
                return "amostraTipo/add";
            }
        }
        return "redirect:/amostraTipos";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("amostraTipo", amostraTipoService.get(id));
        return "amostraTipo/edit";
    }
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "')")
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("amostraTipo") @Valid final AmostraTipoDTO amostraTipoDTO,
                       final BindingResult bindingResult, final Model model,
                       final RedirectAttributes redirectAttributes, @ModelAttribute("motivo") String motivo,
                       Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            return "amostraTipo/edit";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                amostraTipoService.update(id, amostraTipoDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amostraTipo.update.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.login.error"));
                return "amostraTipo/edit";
            }
        }
        return "redirect:/amostraTipos";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes,
                         @ModelAttribute("motivo") String motivo,
                         Principal principal,
                         @ModelAttribute("password") String pass) {
        final String referencedWarning = amostraTipoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                amostraTipoService.delete(id);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("amostraTipo.delete.success"));
            } else {
                redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.login.error"));
            }
        }
        return "redirect:/amostraTipos";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<AmostraTipo>> revisoes = genericRevisionRepository.listaRevisoes(AmostraTipo.class);
        model.addAttribute("audits", revisoes);
        return "/amostraTipo/audit";
    }
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        AmostraTipo amostraTipo = amostraTipoService.findById(id);
        List<EntityRevision<AmostraTipo>> revisoes = genericRevisionRepository.listaRevisoesById(amostraTipo.getId(), AmostraTipo.class);
        model.addAttribute("audits", revisoes);
        return "/amostraTipo/audit";
    }

}
