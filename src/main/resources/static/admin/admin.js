if (!localStorage.getItem("usuario")) {
  window.location.href = "/login/login.html";
}

const API_URL = "http://localhost:8081";

// Variable global para almacenar el último formulario ID
let ultimoFormularioId = null;

// ==================== NAVEGACIÓN ENTRE SECCIONES ====================
document.querySelectorAll('.list-group-item').forEach(item => {
  item.addEventListener('click', function(e) {
    e.preventDefault();
    
    // Remover active de todos
    document.querySelectorAll('.list-group-item').forEach(i => i.classList.remove('active'));
    this.classList.add('active');
    
    // Ocultar todas las secciones
    document.querySelectorAll('.content-section').forEach(section => {
      section.classList.add('d-none');
    });
    
    // Mostrar sección seleccionada
    const section = this.getAttribute('data-section');
    document.getElementById(`section-${section}`).classList.remove('d-none');
    
    // Cargar datos según la sección
    switch(section) {
      case 'ingreso':
        cargarPacientes();
        // Al volver a la sección, re-evaluar la inhabilitación
        // Se asume que selectPacienteIngreso ya existe
        const pacienteSelect = document.getElementById('selectPacienteIngreso');
        if (pacienteSelect) {
            toggleFormFields(pacienteSelect.value !== '');
        }
        break;
      case 'roles':
        cargarMiembros();
        break;
      case 'dicotomizar':
        cargarConjuntosDicot();
        break;
      case 'variables':
        cargarConjuntosVariables();
        break;
      case 'editar':
        cargarPacientesEditar();
        break;
    }
  });
});

// ==================== CARGAR PACIENTES ====================
async function cargarPacientes() {
  try {
    const res = await fetch(`${API_URL}/pacientes`);
    const pacientes = await res.json();
    
    const selects = ['selectPacienteIngreso', 'selectPacienteEditar'];
    selects.forEach(selectId => {
      const select = document.getElementById(selectId);
      if (select) {
        select.innerHTML = '<option value="">Seleccionar paciente...</option>';
        pacientes.forEach(p => {
          const opt = document.createElement("option");
          opt.value = p.idPaciente;
          opt.textContent = `${p.idPaciente} - ${p.nombrePaciente} (${p.tipoPaciente})`;
          select.appendChild(opt);
        });
      }
    });
  } catch (err) {
    console.error("Error cargando pacientes", err);
  }
}

// Evento para actualizar el campo de ID cuando se selecciona un paciente
document.getElementById('selectPacienteIngreso')?.addEventListener('change', function() {
  const pacienteId = this.value;
  const idDisplay = document.getElementById('idPacienteDisplay');
  if (idDisplay) {
    idDisplay.value = pacienteId || '';
  }
  // Lógica para habilitar/inhabilitar el formulario
  toggleFormFields(pacienteId !== ''); 
});

async function cargarPacientesEditar() {
  await cargarPacientes();
}

// ==================== NUEVO PACIENTE ====================
document.getElementById("formNuevoPaciente").addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const data = {
    idPaciente: document.getElementById("nombrePaciente").value.substring(0, 2).toUpperCase() + Math.floor(Math.random() * 10000),
    nombrePaciente: document.getElementById("nombrePaciente").value,
    direccionPaciente: document.getElementById("direccionPaciente").value,
    correoPaciente: document.getElementById("correoPaciente").value,
    tipoPaciente: document.getElementById("tipoPacienteModal").value,
    fechaInclusion: document.getElementById("fechaInclusion").value
  };

  try {
    const res = await fetch(`${API_URL}/pacientes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });
    
    if (res.ok) {
      const paciente = await res.json();
      alert(`Paciente creado con éxito. ID: ${paciente.idPaciente}`);
      e.target.reset();
      
      // Cerrar modal
      const modal = bootstrap.Modal.getInstance(document.getElementById('modalNuevoPaciente'));
      modal.hide();
      
      // Recargar lista de pacientes
      await cargarPacientes();

      // Seleccionar el paciente recién creado y habilitar los campos
      const selectPacienteIngreso = document.getElementById('selectPacienteIngreso');
      if (selectPacienteIngreso) {
          selectPacienteIngreso.value = paciente.idPaciente;
          const idDisplay = document.getElementById('idPacienteDisplay');
          if (idDisplay) { idDisplay.value = paciente.idPaciente; }
          toggleFormFields(true); // Habilitar formulario
      }
    } else {
      const error = await res.json();
      alert("Error al crear paciente: " + (error.message || "Error desconocido"));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión al crear paciente");
  }
});

// ==================== DATOS GENERALES ====================
document.getElementById("btnSiguienteDatosGenerales")?.addEventListener("click", async (e) => {
  e.preventDefault();
  
  // 1. Validar Paciente
  const pacienteId = document.getElementById("selectPacienteIngreso").value;
  if (!pacienteId) {
    alert("Selecciona un paciente primero");
    return;
  }

  // 2. Obtener Usuario Logueado (Lógica Corregida)
  const usuarioStr = localStorage.getItem("usuario");
  if (!usuarioStr) {
      alert("Error: No hay sesión activa.");
      return;
  }
  
  const objetoLocalStorage = JSON.parse(usuarioStr);
  
  // Detectamos si el usuario viene anidado o plano
  const datosUsuario = objetoLocalStorage.usuario ? objetoLocalStorage.usuario : objetoLocalStorage;

  // Buscamos el ID con el nombre que viene de la BD (id_miembro)
  const idMiembroEncontrado = datosUsuario.id_miembro || datosUsuario.idMiembro || datosUsuario.id;

  if (!idMiembroEncontrado) {
      console.error("Usuario en sesión:", datosUsuario);
      alert("Error crítico: No se encuentra el ID del miembro logueado.");
      return;
  }

  // 3. Crear el Formulario
  try {
    const payloadFormulario = {
        paciente: { idPaciente: pacienteId },
        
        // --- CORRECCIÓN CRÍTICA PARA JAVA ---
        // Java espera 'miembroEquipo' (no 'miembro')
        // Java espera 'idMiembroEquipo' (no 'idMiembro')
        miembroEquipo: { idMiembroEquipo: idMiembroEncontrado }, 
        
        estadoFormulario: "En proceso",
        tipoFormulario: "Inicial",
        fechaFormulario: new Date().toISOString().split('T')[0]
    };

    const formRes = await fetch(`${API_URL}/formularios`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payloadFormulario)
    });

    if (!formRes.ok) {
      const error = await formRes.json();
      throw new Error("Error al crear formulario: " + (error.message || "Revisar consola Java"));
    }

    const formulario = await formRes.json();
    const formularioId = formulario.idFormulario;
    ultimoFormularioId = formularioId; // Guardar ID globalmente

    // 4. CÁLCULO DE IMC (Nueva lógica mantenida)
    const peso = parseFloat(document.getElementById("peso").value);
    const estaturaCm = parseFloat(document.getElementById("estatura").value);
    let imc = null;
    
    if (peso && estaturaCm) {
      const estaturaM = estaturaCm / 100; // Convertir cm a metros
      imc = peso / (estaturaM * estaturaM);
    }

    // 5. Guardar Datos Generales
    const dataGenerales = {
      idDatosGen: {
        itemFormu: 3, 
        formularioId: formularioId
      },
      edad: parseInt(document.getElementById("edad").value) || null,
      sexo: document.getElementById("sexo").value || null,
      peso: peso || null,
      imc: imc ? parseFloat(imc.toFixed(2)) : null, // Enviamos el IMC calculado
      estatura: estaturaCm || null, 
      zonaResidencial: document.getElementById("zonaResidencial").value || null,
      educacion: document.getElementById("educacion").value || null,
      ocupacion: document.getElementById("ocupacion").value || null
    };

    const res = await fetch(`${API_URL}/datos-generales`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dataGenerales)
    });

    if (res.ok) {
      alert("Datos generales guardados con éxito.");
      
      // Lógica para AVANZAR A LA PESTAÑA "Hábitos"
      const nextTabElement = document.getElementById('habitos-tab');
      if (nextTabElement) {
        // Usamos la API de Bootstrap para cambiar el tab visualmente
        const tabInstance = new bootstrap.Tab(nextTabElement);
        tabInstance.show();
      }
    } else {
      const error = await res.json();
      throw new Error("Error al guardar datos generales: " + JSON.stringify(error));
    }

  } catch (err) {
    console.error(err);
    alert("Error: " + err.message);
  }
});

// ==================== HÁBITOS ====================
document.getElementById("formHabitos")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  if (!ultimoFormularioId) {
    alert("Primero debes guardar los datos generales para crear un formulario");
    return;
  }

  try {
    const data = {
      idHabPaciente: {
        itemFormu: 4, // Valor corregido según la base de datos (4 para Habitos)
        formularioId: ultimoFormularioId
      },
      estadoConsumoTabaco: document.getElementById("estadoConsumoTabaco").value || null,
      cantPromTabaco: parseInt(document.getElementById("cantPromTabaco")?.value) || null,
      exConsumidorTabaco: parseInt(document.getElementById("exConsumidorTabaco")?.value) || null,
      tiempoTabaco: document.getElementById("tiempoTabaco").value || null,
      estadoConsumoAlcohol: document.getElementById("estadoConsumoAlcohol").value || null,
      frecuenciaAlcohol: document.getElementById("frecuenciaAlcohol").value || null,
      cantidadAlcohol: parseInt(document.getElementById("cantidadAlcohol")?.value) || null,
      aniosConsumoAlcohol: parseInt(document.getElementById("aniosConsumoAlcohol").value) || null,
      exConsumidorAlcohol: parseInt(document.getElementById("exConsumidorAlcohol")?.value) || null
    };

    const res = await fetch(`${API_URL}/habitos-paciente`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (res.ok) {
      alert("Hábitos guardados con éxito");
      // Avanzar a la pestaña "Datos Clínicos"
      const nextTabElement = document.getElementById('clinicos-tab');
      if (nextTabElement) {
        new bootstrap.Tab(nextTabElement).show();
      }
    } else {
      const error = await res.json();
      alert("Error al guardar hábitos: " + JSON.stringify(error));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión: " + err.message);
  }
});

// ==================== DATOS CLÍNICOS ====================
document.getElementById("formDatosClinicos")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  if (!ultimoFormularioId) {
    alert("Primero debes guardar los datos generales para crear un formulario");
    return;
  }

  try {
    const data = {
      idDatosCli: {
        itemFormu: 2, // Valor fijo según la base de datos
        formularioId: ultimoFormularioId
      },
      adenoGastrico: document.getElementById("adenoGastrico").value || null,
      fechaAdenoGastrico: document.getElementById("fechaAdenoGastrico").value || null,
      antFamCancerGast: document.getElementById("antFamCancerGast").value || null,
      medicamentos: document.getElementById("medicamentos").value || null,
      otrasEnfermedades: document.getElementById("otrasEnfermedades").value || null,
      antFamOtroCancer: document.getElementById("antFamOtroCancer")?.value || null,
      cirugiaGastricaPrevia: document.getElementById("cirugiaGastrica").value || null,
      hpyloriPrueba: document.getElementById("hpyloriPrueba").value || null,
      hpyloriResultado: document.getElementById("hpyloriResultado").value || null,
      hpyloriTiempoTest: document.getElementById("hpyloriTiempoTest").value || null,
      positivoPasadoHpylori: document.getElementById("positivoPasadoHpylori")?.value || null,
      anioPositivopasadoHpylori: parseInt(document.getElementById("anioPositivopasadoHpylori")?.value) || null,
      trataErradicacion: document.getElementById("trataErradicacion")?.value || null,
      anioTrataerradica: parseInt(document.getElementById("anioTrataerradica")?.value) || null,
      esquemaTrataerradica: document.getElementById("esquemaTrataerradica")?.value || null,
      antibioticosIbp: document.getElementById("antibioticosIbp")?.value || null,
      repeticionExamen: document.getElementById("repeticionExamen")?.value || null,
      fechaRepetiexamen: document.getElementById("fechaRepetiexamen")?.value || null,
      tipoExaPasadoHpy: document.getElementById("tipoExaPasadoHpy")?.value || null,
      resultadosExamen: document.getElementById("resultadosExamen")?.value || null
    };

    const res = await fetch(`${API_URL}/datos-clinicos`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (res.ok) {
      alert("Datos clínicos guardados con éxito");
      // Avanzar a la pestaña "Dietarios/Ambientales"
      const nextTabElement = document.getElementById('dietarios-tab');
      if (nextTabElement) {
        new bootstrap.Tab(nextTabElement).show();
      }
    } else {
      const error = await res.json();
      alert("Error al guardar datos clínicos: " + JSON.stringify(error));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión: " + err.message);
  }
});

// ==================== DIETARIOS ====================
document.getElementById("formDietarios")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  alert("Funcionalidad en desarrollo");
    
    // Avanzar a la pestaña "Histopatología"
    const nextTabElement = document.getElementById('histopatologia-tab');
    if (nextTabElement) {
      new bootstrap.Tab(nextTabElement).show();
    }
});

// ==================== HISTOPATOLOGÍA (ÚLTIMO PASO) ====================
// El botón aquí será para "Ingresar Formulario Completo" (Guardar Final)
document.getElementById("formHistopatologia")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  if (!ultimoFormularioId) {
    alert("Debe completar los pasos anteriores.");
    return;
  }

  alert("Funcionalidad en desarrollo: Aquí se debería guardar Histopatología y finalizar el formulario.");

  // Lógica para actualizar el estado del formulario a "Activo" o "Completo"
  try {
    const res = await fetch(`${API_URL}/formularios/${ultimoFormularioId}/estado`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ estadoFormulario: "Activo" }) // o "Completo"
    });
    if (res.ok) {
      alert(`Formulario ${ultimoFormularioId} ingresado y completado con éxito.`);
      
      // Limpiar y reiniciar formulario
      ultimoFormularioId = null;
      document.getElementById("formDatosGenerales").reset();
      document.getElementById("formHabitos").reset();
      document.getElementById("formDatosClinicos").reset();
      document.getElementById("formDietarios").reset();
      document.getElementById("formHistopatologia").reset();
      document.getElementById('idPacienteDisplay').value = '';
      document.getElementById('selectPacienteIngreso').value = '';

      // Volver a la primera pestaña e inhabilitar
      new bootstrap.Tab(document.getElementById('generales-tab')).show();
      toggleFormFields(false);

    } else {
      alert("Error al finalizar el formulario.");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión al finalizar el formulario.");
  }
});

// ==================== ROLES ====================
async function cargarMiembros() {
  try {
    const res = await fetch(`${API_URL}/miembro-equipo`);
    const miembros = await res.json();
    
    const tbody = document.querySelector("#tablaMiembros tbody");
    tbody.innerHTML = "";
    
    if (miembros.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay miembros registrados</td></tr>';
      return;
    }
    
    miembros.forEach(m => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${m.idMiembro}</td>
        <td>${m.nombreMiembro}</td>
        <td><span class="badge bg-purple">${m.rol}</span></td>
        <td>${m.correo || 'N/A'}</td>
        <td>
          <button class="btn btn-sm btn-danger" onclick="eliminarMiembro(${m.idMiembro})">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    console.error("Error cargando miembros", err);
  }
}

document.getElementById("formRoles")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const data = {
    nombreMiembro: document.getElementById("nombreMiembro").value,
    rol: document.getElementById("rolMiembro").value,
    correo: document.getElementById("correoMiembro").value
  };

  try {
    const res = await fetch(`${API_URL}/miembro-equipo`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (res.ok) {
      alert("Rol asignado con éxito");
      e.target.reset();
      cargarMiembros();
    } else {
      const error = await res.json();
      alert("Error al asignar rol: " + (error.message || "Error desconocido"));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
});

async function eliminarMiembro(id) {
  if (!confirm("¿Estás seguro de eliminar este miembro?")) return;
  
  try {
    const res = await fetch(`${API_URL}/miembro-equipo/${id}`, {
      method: "DELETE"
    });

    if (res.ok) {
      alert("Miembro eliminado");
      cargarMiembros();
    } else {
      alert("Error al eliminar");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
}

// ==================== DICOTOMIZAR ====================
async function cargarConjuntosDicot() {
  try {
    const res = await fetch(`${API_URL}/dicot-conjuntos`);
    const conjuntos = await res.json();
    
    const select = document.getElementById("selectConjuntoDicot");
    select.innerHTML = '<option value="">Seleccionar conjunto...</option>';
    
    conjuntos.forEach(c => {
      const opt = document.createElement("option");
      opt.value = c.idDicotconjunto;
      opt.textContent = `${c.nombre} - ${c.descripcion}`;
      select.appendChild(opt);
    });
  } catch (err) {
    console.error("Error cargando conjuntos", err);
  }
}

document.getElementById("btnDicotomizar")?.addEventListener("click", async () => {
  const conjuntoId = document.getElementById("selectConjuntoDicot").value;
  
  if (!conjuntoId) {
    alert("Selecciona un conjunto de reglas");
    return;
  }

  try {
    const res = await fetch(`${API_URL}/dicotomizacion/ejecutar/${conjuntoId}`, {
      method: "POST"
    });

    if (res.ok) {
      const resultado = await res.json();
      const mensajeDiv = document.getElementById("mensajeResultadoDicot");
      mensajeDiv.textContent = `Dicotomización completada. Se generaron ${resultado.length || 0} valores dicotomizados.`;
      document.getElementById("resultadoDicot").classList.remove("d-none");
    } else {
      alert("Error en la dicotomización");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
});

// ==================== VARIABLES DICOTOMIZACIÓN ====================
async function cargarConjuntosVariables() {
  try {
    const res = await fetch(`${API_URL}/dicot-conjuntos`);
    const conjuntos = await res.json();
    
    const tbody = document.querySelector("#tablaConjuntos tbody");
    tbody.innerHTML = "";
    
    if (conjuntos.length === 0) {
      tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay conjuntos registrados</td></tr>';
      return;
    }
    
    conjuntos.forEach(c => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${c.idDicotconjunto}</td>
        <td>${c.nombre}</td>
        <td>${c.descripcion}</td>
        <td>
          <button class="btn btn-sm btn-primary" onclick="verReglas(${c.idDicotconjunto})">
            <i class="bi bi-eye"></i> Ver Reglas
          </button>
          <button class="btn btn-sm btn-danger" onclick="eliminarConjunto(${c.idDicotconjunto})">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    console.error("Error cargando conjuntos", err);
  }
}

document.getElementById("formNuevoConjunto")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const data = {
    nombre: document.getElementById("nombreConjunto").value,
    descripcion: document.getElementById("descripcionConjunto").value
  };

  try {
    const res = await fetch(`${API_URL}/dicot-conjuntos`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (res.ok) {
      alert("Conjunto creado con éxito");
      e.target.reset();
      cargarConjuntosVariables();
    } else {
      alert("Error al crear conjunto");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
});

function verReglas(conjuntoId) {
  alert(`Ver reglas del conjunto ${conjuntoId} - Funcionalidad en desarrollo`);
}

async function eliminarConjunto(id) {
  if (!confirm("¿Estás seguro de eliminar este conjunto?")) return;
  
  try {
    const res = await fetch(`${API_URL}/dicot-conjuntos/${id}`, {
      method: "DELETE"
    });

    if (res.ok) {
      alert("Conjunto eliminado");
      cargarConjuntosVariables();
    } else {
      alert("Error al eliminar");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
}

// ==================== EXPORTAR DATOS ====================
document.getElementById("btnExportarExcel")?.addEventListener("click", async () => {
  try {
    const res = await fetch(`${API_URL}/api/export/dicotomizacion/excel`);
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `genrisk_dicotomizacion_${new Date().toISOString().split('T')[0]}.xlsx`;
    a.click();
    alert("Archivo Excel descargado exitosamente");
  } catch (err) {
    console.error(err);
    alert("Error al exportar a Excel");
  }
});

document.getElementById("btnExportarPdf")?.addEventListener("click", async () => {
  try {
    const res = await fetch(`${API_URL}/api/export/pacientes/pdf`);
    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `genrisk_pacientes_${new Date().toISOString().split('T')[0]}.pdf`;
    a.click();
    alert("Archivo PDF descargado exitosamente");
  } catch (err) {
    console.error(err);
    alert("Error al exportar a PDF");
  }
});

// ==================== LISTAR DATOS ====================
document.getElementById("selectTablaListar")?.addEventListener("change", async function() {
  const tabla = this.value;
  const contenedor = document.getElementById("contenedorTablaListar");
  
  if (!tabla) {
    contenedor.innerHTML = '<p class="text-muted">Selecciona una tabla para ver los datos</p>';
    return;
  }

  try {
    let endpoint = '';
    switch(tabla) {
      case 'pacientes':
        endpoint = '/pacientes';
        break;
      case 'formularios':
        endpoint = '/formularios';
        break;
      case 'datos_generales':
        endpoint = '/datos-generales';
        break;
      case 'habitos':
        endpoint = '/habitos-paciente';
        break;
      case 'datos_clinicos':
        endpoint = '/datos-clinicos';
        break;
    }

    const res = await fetch(`${API_URL}${endpoint}`);
    const datos = await res.json();
    
    if (datos.length === 0) {
      contenedor.innerHTML = '<p class="text-muted">No hay datos disponibles</p>';
      return;
    }

    // Crear tabla dinámica
    const headers = Object.keys(datos[0]).filter(k => !k.startsWith('id') || k === 'idPaciente' || k === 'idFormulario' || k === 'idMiembro');
    let html = '<table class="table table-striped table-hover"><thead class="table-purple"><tr>';
    headers.forEach(h => {
      html += `<th>${h}</th>`;
    });
    html += '</tr></thead><tbody>';
    
    datos.forEach(row => {
      html += '<tr>';
      headers.forEach(h => {
        const value = row[h];
        let displayValue = 'N/A';
        if (value !== null && value !== undefined) {
          if (typeof value === 'object') {
            displayValue = JSON.stringify(value);
          } else {
            displayValue = value;
          }
        }
        html += `<td>${displayValue}</td>`;
      });
      html += '</tr>';
    });
    
    html += '</tbody></table>';
    contenedor.innerHTML = html;
  } catch (err) {
    console.error(err);
    contenedor.innerHTML = '<div class="alert alert-danger">Error al cargar los datos: ' + err.message + '</div>';
  }
});

// ==================== CERRAR SESIÓN ====================
function cerrarSesion() {
  localStorage.clear();
  window.location.href = "/login/login.html";
}

// Cargar pacientes al iniciar
cargarPacientes();

// ==================== LÓGICA DE USABILIDAD (INHABILITACIÓN) ====================

// Identificadores (ESTOS COINCIDEN CON LOS IDs AGREGADOS EN admin.html)
const panelDatos = document.getElementById('panelIngresoDatos'); // Contenedor del Formulario
const selectPaciente = document.getElementById('selectPacienteIngreso'); 
const btnSiguienteGeneral = document.getElementById('btnSiguienteDatosGenerales'); 
const btnGuardarFinal = document.getElementById('btnGuardarFormularioCompleto'); 

// Función para habilitar/inhabilitar todos los campos del formulario
function toggleFormFields(enable) {
    // Si el panel de datos no existe (ej: estás en otra sección), sal de la función
    if (!panelDatos) return; 

    // Seleccionar todos los inputs, selects y textareas dentro del panel de ingreso
    const fields = panelDatos.querySelectorAll('input, select, textarea');
    fields.forEach(field => {
        // Excluir el campo de visualización del ID que siempre está inhabilitado por HTML
        if (field.id !== 'idPacienteDisplay') {
            field.disabled = !enable;
        }
    });
    
    // Controlar el botón "Siguiente" de Datos Generales
    if (btnSiguienteGeneral) {
        btnSiguienteGeneral.disabled = !enable;
    }

    // Controlar el botón "Ingresar Formulario Completo" (Guardar Final)
    if (btnGuardarFinal) {
         btnGuardarFinal.disabled = !enable; 
    }
}

// 1. Inhabilitación inicial
// Se ejecuta al final del script para asegurar que todos los elementos existan
toggleFormFields(false);

// 2. MEJORA: Habilitar campos si hay un paciente seleccionado al cargar (esto se maneja en cargarPacientes/change event)
if (selectPaciente && selectPaciente.value !== '') {
    toggleFormFields(true);
}
document.addEventListener("DOMContentLoaded", () => {
    const panelDatos = document.getElementById("panelIngresoDatos");
    const selectPaciente = document.getElementById("selectPacienteIngreso");

    if (!panelDatos) {
        console.error("❌ No se encontró panelIngresoDatos");
        return;
    }

    //  BLOQUEAR TODO AL CARGAR
    const bloquearFormulario = (bloquear) => {
        const campos = panelDatos.querySelectorAll("input, select, textarea, button");
        campos.forEach(el => {
            if (el.id !== "selectPacienteIngreso" && el.id !== "btnNuevoPaciente") {
                el.disabled = bloquear;
            }
        });
    };

    bloquearFormulario(true);
    console.log(" Formulario bloqueado al iniciar");

    //  DESBLOQUEAR CUANDO SE SELECCIONA PACIENTE
    selectPaciente?.addEventListener("change", () => {
        if (selectPaciente.value !== "") {
            bloquearFormulario(false);
            console.log("🔓 Paciente seleccionado, formulario habilitado");
        } else {
            bloquearFormulario(true);
        }
    });
});
