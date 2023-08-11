package br.com.lablims.controller;

import br.com.lablims.domain.SolucaoTipo;
import br.com.lablims.domain.UnidadeMedida;
import br.com.lablims.domain.Usuario;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.SolucaoRegistroDTO;
import br.com.lablims.repos.SolucaoTipoRepository;
import br.com.lablims.repos.UnidadeMedidaRepository;
import br.com.lablims.repos.UsuarioRepository;
import br.com.lablims.service.SolucaoRegistroService;
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
@RequestMapping("/solucaoRegistros")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class SolucaoRegistroController {

    private final SolucaoRegistroService solucaoRegistroService;
    private final SolucaoTipoRepository solucaoTipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;

    public SolucaoRegistroController(final SolucaoRegistroService solucaoRegistroService,
            final SolucaoTipoRepository solucaoTipoRepository,
            final UsuarioRepository usuarioRepository,
            final UnidadeMedidaRepository unidadeMedidaRepository) {
        this.solucaoRegistroService = solucaoRegistroService;
        this.solucaoTipoRepository = solucaoTipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.unidadeMedidaRepository = unidadeMedidaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("solucaoTipoValues", solucaoTipoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(SolucaoTipo::getId, SolucaoTipo::getSiglaSolucao)));
        model.addAttribute("criadorValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("conferenteValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("unidadeValues", unidadeMedidaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(UnidadeMedida::getId, UnidadeMedida::getUnidade)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<SolucaoRegistroDTO> solucaoRegistros = solucaoRegistroService.findAll(filter, pageable);
        model.addAttribute("solucaoRegistros", solucaoRegistros);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(solucaoRegistros));
        return "solucaoRegistro/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("solucaoRegistro") final SolucaoRegistroDTO solucaoRegistroDTO) {
        return "solucaoRegistro/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("solucaoRegistro") @Valid final SolucaoRegistroDTO solucaoRegistroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoRegistro/add";
        }
        solucaoRegistroService.create(solucaoRegistroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoRegistro.create.success"));
        return "redirect:/solucaoRegistros";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("solucaoRegistro", solucaoRegistroService.get(id));
        return "solucaoRegistro/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("solucaoRegistro") @Valid final SolucaoRegistroDTO solucaoRegistroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "solucaoRegistro/edit";
        }
        solucaoRegistroService.update(id, solucaoRegistroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("solucaoRegistro.update.success"));
        return "redirect:/solucaoRegistros";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = solucaoRegistroService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            solucaoRegistroService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("solucaoRegistro.delete.success"));
        }
        return "redirect:/solucaoRegistros";
    }

}
