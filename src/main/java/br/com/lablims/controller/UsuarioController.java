package br.com.lablims.controller;

import br.com.lablims.domain.Grupo;
import br.com.lablims.domain.Usuario;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.UsuarioDTO;
import br.com.lablims.repos.GrupoRepository;
import br.com.lablims.service.UsuarioService;
import br.com.lablims.util.CustomCollectors;
import br.com.lablims.util.UserRoles;
import br.com.lablims.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;


@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final GrupoRepository grupoRepository;

    public UsuarioController(final UsuarioService usuarioService,
            final GrupoRepository grupoRepository) {
        this.usuarioService = usuarioService;
        this.grupoRepository = grupoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("gruposValues", grupoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Grupo::getId, Grupo::getGrupo)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<UsuarioDTO> usuarios = usuarioService.findAll(filter, pageable);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(usuarios));
        return "usuario/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("usuario") final UsuarioDTO usuarioDTO) {
        return "usuario/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("usuario") @Valid final UsuarioDTO usuarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && usuarioService.emailExists(usuarioDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.usuario.email");
        }
        if (!bindingResult.hasFieldErrors("username") && usuarioService.usernameExists(usuarioDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.usuario.username");
        }
        if (bindingResult.hasErrors()) {
            return "usuario/add";
        }
        usuarioService.create(usuarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("usuario.create.success"));
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("usuario", usuarioService.get(id));
        return "usuario/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("usuario") @Valid final UsuarioDTO usuarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final UsuarioDTO currentUsuarioDTO = usuarioService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !usuarioDTO.getEmail().equalsIgnoreCase(currentUsuarioDTO.getEmail()) &&
                usuarioService.emailExists(usuarioDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.usuario.email");
        }
        if (!bindingResult.hasFieldErrors("username") &&
                !usuarioDTO.getUsername().equalsIgnoreCase(currentUsuarioDTO.getUsername()) &&
                usuarioService.usernameExists(usuarioDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.usuario.username");
        }
        if (bindingResult.hasErrors()) {
            return "usuario/edit";
        }
        usuarioService.update(id, usuarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("usuario.update.success"));
        return "redirect:/usuarios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = usuarioService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("usuario.delete.success"));
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/validar")
    public void validarSenha(Principal principal, @RequestParam String pass){
        Usuario currentUsuario = usuarioService.findByUsername(principal.getName());
        boolean valid =  usuarioService.validarUser(currentUsuario, pass);
        System.out.println(valid);
    }

}
