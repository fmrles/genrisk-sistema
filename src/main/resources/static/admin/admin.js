if (!localStorage.getItem("usuario")) {
  window.location.href = "/login/login.html";
}

const API_URL = "http://localhost:8080";

// Variable global para almacenar el último formulario ID
let ultimoFormularioId = null;
// Estado
let formularioCreado = false;
let panelDatos;



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
document.getElementById("btnSiguienteDatosGenerales")
  ?.addEventListener("click", async (e) => {

  e.preventDefault();

  // 1. Validar Paciente
  const pacienteId = document.getElementById("selectPacienteIngreso").value;
  if (!pacienteId) {
    alert("Selecciona un paciente primero");
    return;
  }

  // 2. Obtener Usuario Logueado
  const usuarioStr = localStorage.getItem("usuario");
  if (!usuarioStr) {
    alert("Error: No hay sesión activa.");
    return;
  }

  const objetoLocalStorage = JSON.parse(usuarioStr);
  const datosUsuario = objetoLocalStorage.usuario ?? objetoLocalStorage;
  const idMiembroEncontrado =
    datosUsuario.id_miembro || datosUsuario.idMiembro || datosUsuario.id;

  if (!idMiembroEncontrado) {
    alert("Error crítico: no se encontró el ID del miembro.");
    return;
  }

  try {
   if (!ultimoFormularioId) {
  alert("Primero debes crear el formulario con el botón 'Crear Formulario'");
  return;
}


    // 4. Cálculo IMC
    const peso = parseFloat(document.getElementById("peso").value);
    const estaturaCm = parseFloat(document.getElementById("estatura").value);
    let imc = null;

    if (peso && estaturaCm) {
      const estaturaM = estaturaCm / 100;
      imc = peso / (estaturaM * estaturaM);
    }

    // 5. Guardar Datos Generales
    const dataGenerales = {
      idDatosGen: {
        itemFormu: 3,
        formularioId: ultimoFormularioId
      },
      edad: parseInt(document.getElementById("edad").value) || null,
      sexo: document.getElementById("sexo").value || null,
      peso: peso || null,
      imc: imc ? parseFloat(imc.toFixed(2)) : null,
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

    if (!res.ok) {
      const error = await res.json();
      throw new Error(JSON.stringify(error));
    }

    alert("Datos generales guardados con éxito");
    new bootstrap.Tab(document.getElementById("habitos-tab")).show();

  } catch (err) {
    console.error(err);
    alert("Error: " + err.message);
    return;
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

// ==================== GESTIÓN DE MIEMBROS ====================

async function cargarMiembros() {
  try {
    const res = await fetch(`${API_URL}/miembro-equipo`);
    const miembros = await res.json();
    
    const tbody = document.querySelector("#tablaMiembros tbody");
    tbody.innerHTML = "";
    
    if (!miembros || miembros.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay miembros registrados</td></tr>';
      return;
    }
    
    miembros.forEach(m => {
      const id = m.idMiembro || m.id_miembro || m.idMiembroEquipo;
      const nombre = m.nombreMiembro || m.nombre || '';
      const rol = m.rolMiembro || m.rol || 'Sin Rol';
      const correo = m.correoMiembro || m.correo || '';

      const nombreSafe = nombre.replace(/'/g, "&#39;");
      const correoSafe = correo.replace(/'/g, "&#39;");
      const rolSafe = rol.replace(/'/g, "&#39;");

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${id}</td>
        <td>${nombre}</td>
        <td><span class="badge bg-purple">${rol}</span></td>
        <td>${correo}</td>
        <td>
          <button class="btn btn-sm btn-outline-primary me-2" 
            onclick="abrirModalEditar(${id}, '${nombreSafe}', '${correoSafe}', '${rolSafe}')" 
            title="Editar">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger" 
            onclick="eliminarMiembro(${id})" 
            title="Eliminar">
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

window.abrirModalCrear = function() {
  const form = document.getElementById("formModalMiembro");
  if(form) form.reset();
  
  document.getElementById("modalIdMiembro").value = ""; 
  document.getElementById("tituloModalMiembro").textContent = "Nuevo Miembro";
  
  const divClave = document.getElementById("divModalClave");
  const inputClave = document.getElementById("modalClave");
  const helpClave = document.getElementById("helpModalClave");
  
  divClave.classList.remove("d-none");
  inputClave.required = true;
  inputClave.value = "";
  helpClave.classList.add("d-none");

  const modalEl = document.getElementById('modalMiembro');
  const modal = new bootstrap.Modal(modalEl);
  modal.show();
}

window.abrirModalEditar = function(id, nombre, correo, rol) {
  document.getElementById("modalIdMiembro").value = id;
  document.getElementById("modalNombre").value = nombre;
  document.getElementById("modalCorreo").value = correo;
  document.getElementById("modalRol").value = rol;

  document.getElementById("tituloModalMiembro").textContent = "Editar Miembro";

  const divClave = document.getElementById("divModalClave");
  const inputClave = document.getElementById("modalClave");
  const helpClave = document.getElementById("helpModalClave");

  divClave.classList.remove("d-none");
  inputClave.required = false; 
  inputClave.value = "";
  helpClave.classList.remove("d-none");

  const modalEl = document.getElementById('modalMiembro');
  const modal = new bootstrap.Modal(modalEl);
  modal.show();
}

document.getElementById("formModalMiembro")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  
  const id = document.getElementById("modalIdMiembro").value;
  const nombre = document.getElementById("modalNombre").value;
  const correo = document.getElementById("modalCorreo").value;
  const rol = document.getElementById("modalRol").value;
  const clave = document.getElementById("modalClave").value;

  const datos = {
    nombreMiembro: nombre,
    correoMiembro: correo,
    rolMiembro: rol
  };

  if (clave && clave.trim() !== "") {
      datos.clave = clave;
  }

  try {
    let res;
    if (id) {
      res = await fetch(`${API_URL}/miembro-equipo/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
      });
    } else {
      if (!datos.clave) {
          alert("La contraseña es obligatoria para nuevos usuarios");
          return;
      }
      res = await fetch(`${API_URL}/miembro-equipo`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
      });
    }

    if (res.ok) {
      alert(id ? "Miembro actualizado correctamente" : "Miembro creado correctamente");
      const modalEl = document.getElementById('modalMiembro');
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
      cargarMiembros(); 
    } else {
      const error = await res.json();
      alert("Error: " + (error.message || "Error al procesar la solicitud"));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión con el servidor");
  }
});

window.eliminarMiembro = async function(id) {
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
      const id = c.idDicotConjunto || c.idDicotconjunto || c.id_dicotconjunto || c.id;
      
      const opt = document.createElement("option");
      opt.value = id;
      opt.textContent = `${c.nombre}`;
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
    
    if (!conjuntos || conjuntos.length === 0) {
      tbody.innerHTML = '<tr><td colspan="4" class="text-center">No hay conjuntos registrados</td></tr>';
      return;
    }
    
    conjuntos.forEach(c => {
      const id = c.idDicotConjunto || c.idDicotconjunto || c.id_dicotconjunto || c.id;
      
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${id !== undefined ? id : '<span class="text-danger">Error ID</span>'}</td>
        <td>${c.nombre || 'Sin nombre'}</td>
        <td>${c.descripcion || 'Sin descripción'}</td>
        <td class="text-center">
          <button class="btn btn-sm btn-outline-primary me-2" onclick="verReglas(${id})" title="Ver Reglas">
            <i class="bi bi-eye"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger" onclick="eliminarConjunto(${id})" title="Eliminar">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    console.error("Error cargando conjuntos", err);
    document.querySelector("#tablaConjuntos tbody").innerHTML = 
      '<tr><td colspan="4" class="text-center text-danger">Error de conexión</td></tr>';
  }
}

window.abrirModalConjunto = function() {
  const form = document.getElementById("formNuevoConjunto");
  if(form) form.reset();
  
  const modal = new bootstrap.Modal(document.getElementById('modalConjunto'));
  modal.show();
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
      
      const modalEl = document.getElementById('modalConjunto');
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
      
      cargarConjuntosVariables();
    } else {
      alert("Error al crear conjunto");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión");
  }
});

// ==================== VER REGLAS ====================
window.verReglas = async function(conjuntoId) {
  const tbody = document.getElementById("bodyReglas");
  
  tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando datos...</td></tr>';

  const modalEl = document.getElementById('modalVerReglas');
  const modal = new bootstrap.Modal(modalEl);
  modal.show();

  try {
    const res = await fetch(`${API_URL}/dicot-reglas`);

    if (!res.ok) {
      throw new Error("Error al consultar las reglas (DicotReglaController).");
    }

    const todasLasReglas = await res.json();

    const reglasDelConjunto = todasLasReglas.filter(r => r.dicotConjuntoID === conjuntoId);

    tbody.innerHTML = "";

    if (reglasDelConjunto.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="5" class="text-center text-muted">
            <i class="bi bi-folder-x me-2"></i>Este conjunto no tiene reglas asignadas.
          </td>
        </tr>`;
      return;
    }

    reglasDelConjunto.forEach(r => {
      const variable = r.atributoObj || "---";
      const operador = r.operador || "=";
      const valor = r.valorInf || r.valorSup || 0;
      const resultado = r.valorSiCumple;
      const categoria = r.valorCategoria || "";

      const tr = document.createElement("tr");
      tr.innerHTML = `
          <td><strong>${variable}</strong></td>
          <td class="text-center"><span class="badge bg-info text-dark">${operador}</span></td>
          <td class="text-center">${valor}</td>
          <td class="text-center fw-bold">${resultado}</td>
          <td>${categoria}</td>
      `;
      tbody.appendChild(tr);
    });

  } catch (err) {
    console.error(err);
    tbody.innerHTML = `
      <tr>
        <td colspan="5" class="text-center text-danger">
          <strong>Error:</strong> ${err.message}
        </td>
      </tr>`;
  }
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
    
   
}

// 1. Inhabilitación inicial
// Se ejecuta al final del script para asegurar que todos los elementos existan


// 2. MEJORA: Habilitar campos si hay un paciente seleccionado al cargar (esto se maneja en cargarPacientes/change event)

//aqui


// Estado


//  Bloquear todo al inicio
function bloquearTodo() {
  if (!panelDatos) return;

  panelDatos
    .querySelectorAll("input, select, textarea, button")
    .forEach(el => {
      if (
        el.id !== "selectPacienteIngreso" &&
        el.id !== "btnNuevoPaciente"
      ) {
        el.disabled = true;
      }
    });
}

//  Habilitar solo el panel superior (crear formulario)
function habilitarCrearFormulario() {
  if (!panelDatos) return;

  panelDatos
    .querySelectorAll("input, select, textarea, button")
    .forEach(el => {
      if (
        el.id === "selectPacienteIngreso" ||
        el.id === "btnNuevoPaciente"
      ) {
        el.disabled = false;
        return;
      }

      // Solo se habilita el botón crear formulario
      if (el.id === "btnCrearFormulario") {
        el.disabled = false;
      } else {
        el.disabled = true;
      }
    });
}

//  Habilitar tabs y formularios inferiores
function habilitarFormularioCompleto() {
  document
    .querySelectorAll(".tab-pane input, .tab-pane select, .tab-pane textarea, .tab-pane button")
    .forEach(el => el.disabled = false);

  document
    .querySelectorAll("[data-bs-toggle='tab']")
    .forEach(tab => tab.disabled = false);
}


 

// ==================== REFRESCAR PACIENTE EN CREAR FORMULARIO ====================
function refrescarPacienteEnCrearFormulario() {
  const select = document.getElementById("selectPacienteIngreso");
  const inputPaciente = document.getElementById("pacienteSeleccionadoForm");
  const inputMiembro = document.getElementById("miembroAsignadoForm");

  if (!select || !inputPaciente) return;

  const selectedOption = select.options[select.selectedIndex];

  if (!selectedOption || !select.value) {
    inputPaciente.value = "";
    if (inputMiembro) inputMiembro.value = "";
    return;
  }

  // Mostrar texto completo del paciente
  inputPaciente.value = selectedOption.textContent;

  // Mostrar miembro logueado
  const usuarioStr = localStorage.getItem("usuario");
  if (usuarioStr && inputMiembro) {
    const obj = JSON.parse(usuarioStr);
    const user = obj.usuario ?? obj;
    inputMiembro.value = user.nombreMiembro || user.nombre || "Usuario actual";
  }
}

document.addEventListener("DOMContentLoaded", () => {

   panelDatos = document.getElementById("panelIngresoDatos");
  const selectPaciente = document.getElementById("selectPacienteIngreso");
  const btnCrearFormulario = document.getElementById("btnCrearFormulario");

  // cargar pacientes
  cargarPacientes();

  // bloquear todo al inicio
  bloquearTodo();

  // al seleccionar paciente
  // al seleccionar paciente
selectPaciente?.addEventListener("change", function () {
  const pacienteId = this.value;

  if (!pacienteId) {
    bloquearTodo();
    return;
  }

  // AUTOCOMPLETAR CASO / CONTROL
  const selectTipoFormulario = document.getElementById("tipoFormulario");
  const texto = this.options[this.selectedIndex]?.textContent || "";

  if (selectTipoFormulario) {
    if (texto.includes("(Caso)")) {
      selectTipoFormulario.value = "CASO";
      selectTipoFormulario.disabled = true;
    } else if (texto.includes("(Control)")) {
      selectTipoFormulario.value = "CONTROL";
      selectTipoFormulario.disabled = true;
    } else {
      selectTipoFormulario.value = "";
      selectTipoFormulario.disabled = false;
    }
  }

  // COMPLETAR PACIENTE Y MIEMBRO
  habilitarCrearFormulario();
  refrescarPacienteEnCrearFormulario();
});


  });

  // botón crear formulario
  btnCrearFormulario?.addEventListener("click", () => {
    alert("Formulario creado correctamente ✅");
    formularioCreado = true;
    habilitarFormularioCompleto();

    const tabGenerales = document.getElementById("generales-tab");
    if (tabGenerales) {
      new bootstrap.Tab(tabGenerales).show();
    }
  });



