package br.com.lablims.service;

import br.com.lablims.domain.EscalaMedida;
import br.com.lablims.domain.PlanoAnaliseColuna;
import br.com.lablims.domain.Reagente;
import br.com.lablims.domain.SolucaoReagente;
import br.com.lablims.domain.SolucaoRegistro;
import br.com.lablims.domain.UnidadeMedida;
import br.com.lablims.model.SimplePage;
import br.com.lablims.model.UnidadeMedidaDTO;
import br.com.lablims.repos.EscalaMedidaRepository;
import br.com.lablims.repos.PlanoAnaliseColunaRepository;
import br.com.lablims.repos.ReagenteRepository;
import br.com.lablims.repos.SolucaoReagenteRepository;
import br.com.lablims.repos.SolucaoRegistroRepository;
import br.com.lablims.repos.UnidadeMedidaRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UnidadeMedidaService {

    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final EscalaMedidaRepository escalaMedidaRepository;
    private final PlanoAnaliseColunaRepository planoAnaliseColunaRepository;
    private final ReagenteRepository reagenteRepository;
    private final SolucaoRegistroRepository solucaoRegistroRepository;
    private final SolucaoReagenteRepository solucaoReagenteRepository;

    public UnidadeMedidaService(final UnidadeMedidaRepository unidadeMedidaRepository,
            final EscalaMedidaRepository escalaMedidaRepository,
            final PlanoAnaliseColunaRepository planoAnaliseColunaRepository,
            final ReagenteRepository reagenteRepository,
            final SolucaoRegistroRepository solucaoRegistroRepository,
            final SolucaoReagenteRepository solucaoReagenteRepository) {
        this.unidadeMedidaRepository = unidadeMedidaRepository;
        this.escalaMedidaRepository = escalaMedidaRepository;
        this.planoAnaliseColunaRepository = planoAnaliseColunaRepository;
        this.reagenteRepository = reagenteRepository;
        this.solucaoRegistroRepository = solucaoRegistroRepository;
        this.solucaoReagenteRepository = solucaoReagenteRepository;
    }

    public SimplePage<UnidadeMedidaDTO> findAll(final String filter, final Pageable pageable) {
        Page<UnidadeMedida> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = unidadeMedidaRepository.findAllById(integerFilter, pageable);
        } else {
            page = unidadeMedidaRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(unidadeMedida -> mapToDTO(unidadeMedida, new UnidadeMedidaDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public UnidadeMedidaDTO get(final Integer id) {
        return unidadeMedidaRepository.findById(id)
                .map(unidadeMedida -> mapToDTO(unidadeMedida, new UnidadeMedidaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UnidadeMedidaDTO unidadeMedidaDTO) {
        final UnidadeMedida unidadeMedida = new UnidadeMedida();
        mapToEntity(unidadeMedidaDTO, unidadeMedida);
        return unidadeMedidaRepository.save(unidadeMedida).getId();
    }

    public void update(final Integer id, final UnidadeMedidaDTO unidadeMedidaDTO) {
        final UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(unidadeMedidaDTO, unidadeMedida);
        unidadeMedidaRepository.save(unidadeMedida);
    }

    public void delete(final Integer id) {
        unidadeMedidaRepository.deleteById(id);
    }

    private UnidadeMedidaDTO mapToDTO(final UnidadeMedida unidadeMedida,
            final UnidadeMedidaDTO unidadeMedidaDTO) {
        unidadeMedidaDTO.setId(unidadeMedida.getId());
        unidadeMedidaDTO.setUnidade(unidadeMedida.getUnidade());
        unidadeMedidaDTO.setEscalaMedida(unidadeMedida.getEscalaMedida() == null ? null : unidadeMedida.getEscalaMedida().getId());
        return unidadeMedidaDTO;
    }

    private UnidadeMedida mapToEntity(final UnidadeMedidaDTO unidadeMedidaDTO,
            final UnidadeMedida unidadeMedida) {
        unidadeMedida.setUnidade(unidadeMedidaDTO.getUnidade());
        final EscalaMedida escalaMedida = unidadeMedidaDTO.getEscalaMedida() == null ? null : escalaMedidaRepository.findById(unidadeMedidaDTO.getEscalaMedida())
                .orElseThrow(() -> new NotFoundException("escalaMedida not found"));
        unidadeMedida.setEscalaMedida(escalaMedida);
        return unidadeMedida;
    }

    public String getReferencedWarning(final Integer id) {
        final UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final PlanoAnaliseColuna unidadePlanoAnaliseColuna = planoAnaliseColunaRepository.findFirstByUnidade(unidadeMedida);
        if (unidadePlanoAnaliseColuna != null) {
            return WebUtils.getMessage("unidadeMedida.planoAnaliseColuna.unidade.referenced", unidadePlanoAnaliseColuna.getId());
        }
        final Reagente unidadeReagente = reagenteRepository.findFirstByUnidade(unidadeMedida);
        if (unidadeReagente != null) {
            return WebUtils.getMessage("unidadeMedida.reagente.unidade.referenced", unidadeReagente.getId());
        }
        final SolucaoRegistro unidadeSolucaoRegistro = solucaoRegistroRepository.findFirstByUnidade(unidadeMedida);
        if (unidadeSolucaoRegistro != null) {
            return WebUtils.getMessage("unidadeMedida.solucaoRegistro.unidade.referenced", unidadeSolucaoRegistro.getId());
        }
        final SolucaoReagente unidadeSolucaoReagente = solucaoReagenteRepository.findFirstByUnidade(unidadeMedida);
        if (unidadeSolucaoReagente != null) {
            return WebUtils.getMessage("unidadeMedida.solucaoReagente.unidade.referenced", unidadeSolucaoReagente.getId());
        }
        return null;
    }

}
