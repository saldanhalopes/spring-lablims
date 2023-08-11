package br.com.lablims.controller;

import br.com.lablims.domain.Analise;
import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.Campanha;
import br.com.lablims.domain.ColunaUtil;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.Usuario;
import br.com.lablims.model.ColunaLogDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.AnaliseRepository;
import br.com.lablims.repos.ArquivosRepository;
import br.com.lablims.repos.CampanhaRepository;
import br.com.lablims.repos.ColunaUtilRepository;
import br.com.lablims.repos.EquipamentoRepository;
import br.com.lablims.repos.UsuarioRepository;
import br.com.lablims.service.ColunaLogService;
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
@RequestMapping("/colunaLogs")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaLogController {

    private final ColunaLogService colunaLogService;
    private final ColunaUtilRepository colunaUtilRepository;
    private final UsuarioRepository usuarioRepository;
    private final CampanhaRepository campanhaRepository;
    private final AnaliseRepository analiseRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ArquivosRepository arquivosRepository;

    public ColunaLogController(final ColunaLogService colunaLogService,
            final ColunaUtilRepository colunaUtilRepository,
            final UsuarioRepository usuarioRepository, final CampanhaRepository campanhaRepository,
            final AnaliseRepository analiseRepository,
            final EquipamentoRepository equipamentoRepository,
            final ArquivosRepository arquivosRepository) {
        this.colunaLogService = colunaLogService;
        this.colunaUtilRepository = colunaUtilRepository;
        this.usuarioRepository = usuarioRepository;
        this.campanhaRepository = campanhaRepository;
        this.analiseRepository = analiseRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.arquivosRepository = arquivosRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("colunaUtilValues", colunaUtilRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaUtil::getId, ColunaUtil::getCodigoColuna)));
        model.addAttribute("usuarioInicioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("usuarioFimValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("campanhaValues", campanhaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Campanha::getId, Campanha::getStatus)));
        model.addAttribute("analiseValues", analiseRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Analise::getId, Analise::getAnalise)));
        model.addAttribute("equipamentoValues", equipamentoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Equipamento::getId, Equipamento::getDescricao)));
        model.addAttribute("anexoValues", arquivosRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arquivos::getId, Arquivos::getNome)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaLogDTO> colunaLogs = colunaLogService.findAll(filter, pageable);
        model.addAttribute("colunaLogs", colunaLogs);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaLogs));
        return "colunaLog/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaLog") final ColunaLogDTO colunaLogDTO) {
        return "colunaLog/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("colunaLog") @Valid final ColunaLogDTO colunaLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaLog/add";
        }
        colunaLogService.create(colunaLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaLog.create.success"));
        return "redirect:/colunaLogs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaLog", colunaLogService.get(id));
        return "colunaLog/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaLog") @Valid final ColunaLogDTO colunaLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaLog/edit";
        }
        colunaLogService.update(id, colunaLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaLog.update.success"));
        return "redirect:/colunaLogs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        colunaLogService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaLog.delete.success"));
        return "redirect:/colunaLogs";
    }

}
