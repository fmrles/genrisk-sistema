package com.genrisk.sistema.services;

import com.genrisk.sistema.model.entity.*;
import com.genrisk.sistema.model.entity.weakEntityKey.*;
import com.genrisk.sistema.repository.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class WordImportService {

    private final PacienteRepository pacienteRepository;
    private final FormularioRepository formularioRepository;
    private final DatosGeneralesRepository datosGeneralesRepository;
    private final DatosClinicosRepository datosClinicosRepository;
    private final HabitosPacienteRepository habitosPacienteRepository;
    private final FactDietariosAmbientalesRepository factDietariosAmbientalesRepository;
    private final HistopatologiaRepository histopatologiaRepository;

    public WordImportService(PacienteRepository pr, FormularioRepository fr, 
                             DatosGeneralesRepository dgr, DatosClinicosRepository dcr, 
                             HabitosPacienteRepository hpr, FactDietariosAmbientalesRepository fdar,
                             HistopatologiaRepository hr) {
        this.pacienteRepository = pr;
        this.formularioRepository = fr;
        this.datosGeneralesRepository = dgr;
        this.datosClinicosRepository = dcr;
        this.habitosPacienteRepository = hpr;
        this.factDietariosAmbientalesRepository = fdar;
        this.histopatologiaRepository = hr;
    }

    @Transactional
    public void importarWordPaciente(MultipartFile file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            List<XWPFTable> tables = doc.getTables();
            if (tables.isEmpty()) throw new RuntimeException("El documento no contiene tablas de datos.");

            // ========================================================
            // TABLA 1: IDENTIFICACIÓN (Índices fijos)
            // ========================================================
            XWPFTable tInfo = tables.get(0);
            String idPaciente = getValor(tInfo, 1);
            
            Paciente paciente = pacienteRepository.findById(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Paciente " + idPaciente + " no existe."));

            paciente.setNombrePaciente(getValor(tInfo, 2));
            paciente.setCorreoPaciente(getValor(tInfo, 3));
            paciente.setDireccionPaciente(getValor(tInfo, 4));
            paciente.setTipoPaciente(getValor(tInfo, 5));
            try { paciente.setFechaInclusion(LocalDate.parse(getValor(tInfo, 6))); } catch (Exception e) {}
            pacienteRepository.save(paciente);

            Formulario formulario = formularioRepository.findTopByPacienteOrderByIdFormularioDesc(paciente)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("Paciente sin formulario activo."));
            Integer fId = formulario.getIdFormulario();

            // ========================================================
            // TABLA 2: DATOS GENERALES (13 Filas mapeadas)
            // ========================================================
            if (tables.size() > 1) {
                XWPFTable t = tables.get(1);
                DatosGenerales dg = datosGeneralesRepository.findByIdDatosGen_FormularioId(fId)
                    .orElse(new DatosGenerales(new DatosGeneralesID(1, fId), null, null, null, null, null, null, null, null, null, null, null, null, null, null));
                
                dg.setEdad(parseInt(getValor(t, 1)));
                dg.setSexo(getValor(t, 2));
                dg.setPeso(parseDouble(getValor(t, 3)));
                dg.setEstatura(parseDouble(getValor(t, 4)));
                dg.setZonaResidencial(getValor(t, 5));
                dg.setAniosResiActual(getValor(t, 6));
                dg.setEducacion(getValor(t, 7));
                dg.setOcupacion(getValor(t, 8));
                dg.setPrevisionSalud(getValor(t, 9));
                dg.setNacionalidad(getValor(t, 10));
                dg.setDireccion(getValor(t, 11));
                dg.setComuna(getValor(t, 12));
                dg.setCiudad(getValor(t, 13));
                
                dg.calcularIMC();
                datosGeneralesRepository.save(dg);
            }

            // ========================================================
            // TABLA 3: DATOS CLÍNICOS (18 Filas mapeadas)
            // ========================================================
            if (tables.size() > 2) {
                XWPFTable t = tables.get(2);
                DatosClinicos dc = datosClinicosRepository.findByIdDatosCli_FormularioId(fId).orElse(new DatosClinicos());
                if(dc.getIdDatosCli() == null) dc.setIdDatosCli(new DatosClinicosID(1, fId));

                dc.setAdenoGastrico(getValor(t, 1));
                try { dc.setFechaAdenoGastrico(LocalDate.parse(getValor(t, 2))); } catch (Exception e) {}
                dc.setAntFamCancerGast(getValor(t, 3));
                dc.setAntFamOtroCancer(getValor(t, 4));
                dc.setMedicamentos(getValor(t, 5));
                dc.setOtrasEnfermedades(getValor(t, 6));
                dc.setCirugiaGastricaPrevia(getValor(t, 7)); 
                
                dc.setHpyloriResultado(getValor(t, 8));
                dc.setHpyloriPrueba(getValor(t, 9));
                dc.setHpyloriTiempoTest((getValor(t, 10))); 
                
                dc.setPositivoPasadoHPylori(getValor(t, 11));
                dc.setTipoExamenPasadoHPy(getValor(t, 12));
                dc.setAnioPositivoPasado(parseInt(getValor(t, 13))); 
                
                dc.setTrataErradicacion(getValor(t, 14));
                dc.setEsquemaTratamientoErra(getValor(t, 15)); 
                dc.setAnioTrataEradica(parseInt(getValor(t, 16))); 
                dc.setAntibioticosIBP((getValor(t, 17))); 
                
                datosClinicosRepository.save(dc);
            }

            // ========================================================
            // TABLA 4: HÁBITOS (9 Filas mapeadas)
            // ========================================================
            if (tables.size() > 3) {
                XWPFTable t = tables.get(3);
                HabitosPaciente hp = habitosPacienteRepository.findByIdHabPaciente_FormularioId(fId).orElse(new HabitosPaciente());
                if(hp.getIdHabPaciente() == null) hp.setIdHabPaciente(new HabitosPacienteID(1, fId));

                hp.setEstadoConsumoTabaco(getValor(t, 1));
                hp.setCantPromTabaco(parseInt(getValor(t, 2)));
                hp.setTiempoTabaco(getValor(t, 3));
                hp.setExConsumidorTabaco(parseInt(getValor(t, 4)));
                hp.setEstadoConsumoAlcohol(getValor(t, 5));
                hp.setFrecuenciaAlcohol(getValor(t, 6));
                hp.setCantidadAlcohol(parseInt(getValor(t, 7)));
                hp.setAniosConsumoAlcohol(parseInt(getValor(t, 8)));
                hp.setExConsumidorAlcohol(parseInt(getValor(t, 9))); 

                habitosPacienteRepository.save(hp);
            }

            // ========================================================
            // TABLA 5: FACTORES DIETARIOS (11 Filas mapeadas)
            // ========================================================
            if (tables.size() > 4) {
                XWPFTable t = tables.get(4);
                FactDietariosAmbientales fda = factDietariosAmbientalesRepository.findByIdFact_FormularioId(fId).orElse(new FactDietariosAmbientales());
                if(fda.getIdFact() == null) fda.setIdFact(new FactDietariosAmbientalesID(1, fId));

                fda.setDietaCarnesCecinas(getValor(t, 1));
                fda.setDietaAgregaSal(getValor(t, 2));
                fda.setDietaFrituras(getValor(t, 3));
                fda.setAliCondimentado(getValor(t, 4));
                fda.setDietaFrutasVerduras(getValor(t, 5));
                fda.setInfusionesBebidas(getValor(t, 6));
                fda.setAguaConsumoZona(getValor(t, 7));
                fda.setTratamientoAgua(getValor(t, 8));
                fda.setFumigaciones(getValor(t, 9));
                fda.setExposicionPesticidas(getValor(t, 10));
                fda.setCombusLenaDiario(getValor(t, 11));

                factDietariosAmbientalesRepository.save(fda);
            }

            // ========================================================
            // TABLA 6: HISTOPATOLOGÍA (3 Filas)
            // ========================================================
            if (tables.size() > 5) {
                XWPFTable t = tables.get(5);
                String tipo = getValor(t, 1);
                String estadio = getValor(t, 2);
                String localizacion = getValor(t, 3);
                boolean wordTieneDatos = (tipo != null || estadio != null || localizacion != null);
                boolean esCaso = "Caso".equalsIgnoreCase(paciente.getTipoPaciente());
                
                if (esCaso || wordTieneDatos) {
                    if (esCaso && (estadio == null || estadio.isEmpty())) {
                        System.out.println("Advertencia: Se omitió Histopatología para el Caso " + idPaciente + " por falta de Estadio Clínico.");
                    } else {
                        Histopatologia h = histopatologiaRepository.findByHistoID_FormularioId(fId)
                                .orElse(new Histopatologia());
                        if(h.getHistoID() == null) h.setHistoID(new HistopatologiaID(1, fId));
                        if (tipo != null) h.setTipo(tipo);
                        if (estadio != null) h.setEstadoClinico(estadio);
                        if (localizacion != null) h.setLocaliTumoral(localizacion);

                        histopatologiaRepository.save(h);
                    }
                }
            }
        }
    }

    private String getValor(XWPFTable table, int rowIndex) {
        try {
            if (table.getRows().size() > rowIndex) {
                XWPFTableRow row = table.getRow(rowIndex);
                if (row.getTableCells().size() > 1) {
                    String val = row.getCell(1).getText();
                    return (val == null || val.trim().isEmpty()) ? null : val.trim();
                }
            }
            return null;
        } catch (Exception e) { return null; }
    }

    private Integer parseInt(String val) {
        if (val == null) return null;
        try { return Integer.parseInt(val.replaceAll("[^0-9-]", "")); } catch (Exception e) { return null; }
    }

    private Double parseDouble(String val) {
        if (val == null) return null;
        try { return Double.parseDouble(val.replace(",", ".")); } catch (Exception e) { return null; }
    }
}