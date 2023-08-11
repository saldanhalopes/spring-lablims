package br.com.lablims.service;

import br.com.lablims.domain.Arquivos;
import br.com.lablims.domain.ColunaLog;
import br.com.lablims.domain.ColunaUtil;
import br.com.lablims.domain.Equipamento;
import br.com.lablims.domain.EquipamentoLog;
import br.com.lablims.domain.MetodologiaVesao;
import br.com.lablims.domain.Reagente;
import br.com.lablims.model.ArquivosDTO;
import br.com.lablims.model.SimplePage;
import br.com.lablims.repos.ArquivosRepository;
import br.com.lablims.repos.ColunaLogRepository;
import br.com.lablims.repos.ColunaUtilRepository;
import br.com.lablims.repos.EquipamentoLogRepository;
import br.com.lablims.repos.EquipamentoRepository;
import br.com.lablims.repos.MetodologiaVesaoRepository;
import br.com.lablims.repos.ReagenteRepository;
import br.com.lablims.util.NotFoundException;
import br.com.lablims.util.WebUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ArquivosService {

    private final ArquivosRepository arquivosRepository;
    private final ColunaUtilRepository colunaUtilRepository;
    private final MetodologiaVesaoRepository metodologiaVesaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ColunaLogRepository colunaLogRepository;
    private final ReagenteRepository reagenteRepository;
    private final EquipamentoLogRepository equipamentoLogRepository;

    public ArquivosService(final ArquivosRepository arquivosRepository,
            final ColunaUtilRepository colunaUtilRepository,
            final MetodologiaVesaoRepository metodologiaVesaoRepository,
            final EquipamentoRepository equipamentoRepository,
            final ColunaLogRepository colunaLogRepository,
            final ReagenteRepository reagenteRepository,
            final EquipamentoLogRepository equipamentoLogRepository) {
        this.arquivosRepository = arquivosRepository;
        this.colunaUtilRepository = colunaUtilRepository;
        this.metodologiaVesaoRepository = metodologiaVesaoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.colunaLogRepository = colunaLogRepository;
        this.reagenteRepository = reagenteRepository;
        this.equipamentoLogRepository = equipamentoLogRepository;
    }

    public SimplePage<ArquivosDTO> findAll(final String filter, final Pageable pageable) {
        Page<Arquivos> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = arquivosRepository.findAllById(integerFilter, pageable);
        } else {
            page = arquivosRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(arquivos -> mapToDTO(arquivos, new ArquivosDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ArquivosDTO get(final Integer id) {
        return arquivosRepository.findById(id)
                .map(arquivos -> mapToDTO(arquivos, new ArquivosDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ArquivosDTO arquivosDTO) {
        final Arquivos arquivos = new Arquivos();
        mapToEntity(arquivosDTO, arquivos);
        return arquivosRepository.save(arquivos).getId();
    }

    public void update(final Integer id, final ArquivosDTO arquivosDTO) {
        final Arquivos arquivos = arquivosRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(arquivosDTO, arquivos);
        arquivosRepository.save(arquivos);
    }

    public void delete(final Integer id) {
        final Arquivos arquivos = arquivosRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        colunaUtilRepository.findAllByAnexos(arquivos)
                .forEach(colunaUtil -> colunaUtil.getAnexos().remove(arquivos));
        arquivosRepository.delete(arquivos);
    }

    private ArquivosDTO mapToDTO(final Arquivos arquivos, final ArquivosDTO arquivosDTO) {
        arquivosDTO.setId(arquivos.getId());
        arquivosDTO.setNome(arquivos.getNome());
        arquivosDTO.setTipo(arquivos.getTipo());
        arquivosDTO.setDescricao(arquivos.getDescricao());
        arquivosDTO.setTamanho(arquivos.getTamanho());
        arquivosDTO.setArquivo(arquivos.getArquivo());
        arquivosDTO.setDataCriacao(arquivos.getDataCriacao());
        return arquivosDTO;
    }

    private Arquivos mapToEntity(final ArquivosDTO arquivosDTO, final Arquivos arquivos) {
        arquivos.setNome(arquivosDTO.getNome());
        arquivos.setTipo(arquivosDTO.getTipo());
        arquivos.setDescricao(arquivosDTO.getDescricao());
        arquivos.setTamanho(arquivosDTO.getTamanho());
        arquivos.setArquivo(arquivosDTO.getArquivo());
        arquivos.setDataCriacao(arquivosDTO.getDataCriacao());
        return arquivos;
    }

    public String getReferencedWarning(final Integer id) {
        final Arquivos arquivos = arquivosRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final MetodologiaVesao anexoMetodologiaVesao = metodologiaVesaoRepository.findFirstByAnexo(arquivos);
        if (anexoMetodologiaVesao != null) {
            return WebUtils.getMessage("arquivos.metodologiaVesao.anexo.referenced", anexoMetodologiaVesao.getId());
        }
        final ColunaUtil certificadoColunaUtil = colunaUtilRepository.findFirstByCertificado(arquivos);
        if (certificadoColunaUtil != null) {
            return WebUtils.getMessage("arquivos.colunaUtil.certificado.referenced", certificadoColunaUtil.getId());
        }
        final ColunaUtil anexosColunaUtil = colunaUtilRepository.findFirstByAnexos(arquivos);
        if (anexosColunaUtil != null) {
            return WebUtils.getMessage("arquivos.colunaUtil.anexos.referenced", anexosColunaUtil.getId());
        }
        final Equipamento certificadoEquipamento = equipamentoRepository.findFirstByCertificado(arquivos);
        if (certificadoEquipamento != null) {
            return WebUtils.getMessage("arquivos.equipamento.certificado.referenced", certificadoEquipamento.getId());
        }
        final Equipamento manualEquipamento = equipamentoRepository.findFirstByManual(arquivos);
        if (manualEquipamento != null) {
            return WebUtils.getMessage("arquivos.equipamento.manual.referenced", manualEquipamento.getId());
        }
        final Equipamento procedimentoEquipamento = equipamentoRepository.findFirstByProcedimento(arquivos);
        if (procedimentoEquipamento != null) {
            return WebUtils.getMessage("arquivos.equipamento.procedimento.referenced", procedimentoEquipamento.getId());
        }
        final ColunaLog anexoColunaLog = colunaLogRepository.findFirstByAnexo(arquivos);
        if (anexoColunaLog != null) {
            return WebUtils.getMessage("arquivos.colunaLog.anexo.referenced", anexoColunaLog.getId());
        }
        final Reagente fdsReagente = reagenteRepository.findFirstByFds(arquivos);
        if (fdsReagente != null) {
            return WebUtils.getMessage("arquivos.reagente.fds.referenced", fdsReagente.getId());
        }
        final EquipamentoLog anexoEquipamentoLog = equipamentoLogRepository.findFirstByAnexo(arquivos);
        if (anexoEquipamentoLog != null) {
            return WebUtils.getMessage("arquivos.equipamentoLog.anexo.referenced", anexoEquipamentoLog.getId());
        }
        return null;
    }

}
