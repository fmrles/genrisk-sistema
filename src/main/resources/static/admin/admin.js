if (!localStorage.getItem("usuario")) {
  window.location.href = "/login/login.html";
}

const API_URL = "http://localhost:8081";

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
<<<<<<< HEAD

  const data = {
    // NO envíes idPaciente → el backend lo genera automáticamente
    nombrePaciente: document.getElementById("nombrePaciente").value.trim(),
    direccionPaciente: document.getElementById("direccionPaciente").value.trim(),
    correoPaciente: document.getElementById("correoPaciente").value.trim(),
    tipoPaciente: document.getElementById("tipoPacienteModal").value,
    fechaInclusion: document.getElementById("fechaInclusion").value  // Formato: YYYY-MM-DD
  };
=======
  
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
>>>>>>> 973a8c9fb9030f8cfb83154abdab02873d478159

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

      // Seleccionar automáticamente el nuevo paciente
      const selectPacienteIngreso = document.getElementById('selectPacienteIngreso');
      if (selectPacienteIngreso) {
        // Forzar recarga del select si es necesario
        selectPacienteIngreso.value = paciente.idPaciente;

        const idDisplay = document.getElementById('idPacienteDisplay');
        if (idDisplay) idDisplay.value = paciente.idPaciente;

        toggleFormFields(true); // Habilitar campos para ingreso de datos
      }
    } else {
<<<<<<< HEAD
      // Mejor manejo de errores: mostrar mensaje real del backend
      let errorMsg = "Error desconocido";
      try {
        const errorBody = await res.json();
        errorMsg = errorBody.message || errorBody.error || JSON.stringify(errorBody);
      } catch {
        errorMsg = await res.text();
      }
      alert("Error al crear paciente: " + errorMsg);
    }
  } catch (err) {
    console.error("Error de conexión:", err);
    alert("Error de conexión con el servidor");
=======
      const error = await res.json();
      alert("Error al crear paciente: " + (error.message || "Error desconocido"));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión al crear paciente");
>>>>>>> 973a8c9fb9030f8cfb83154abdab02873d478159
  }
});

// ==================== CARGAR MIEMBROS DEL EQUIPO ====================
async function cargarMiembrosEquipo() {
  try {
    const response = await fetch(`${API_URL}/miembro-equipo`);
    if (!response.ok) throw new Error("Error al cargar miembros");

    const miembros = await response.json();

    const selectMiembro = document.getElementById("selectMiembroAsignado"); 
    if (!selectMiembro) {
      console.warn("No se encontró el select de miembros (id: selectMiembroAsignado)");
      return;
    }

    selectMiembro.innerHTML = '<option value="">-- Seleccione miembro --</option>';
    miembros.forEach(miembro => {
      const opt = document.createElement("option");
      opt.value = miembro.idMiembro;
      opt.textContent = `${miembro.nombreMiembro || miembro.correoMiembro} (${miembro.rolMiembro})`;
      selectMiembro.appendChild(opt);
    });
  } catch (err) {
    console.error(err);
    alert("Error al cargar miembros del equipo. Revisa la consola.");
  }
}
 
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

// ==================== DIETARIOS / AMBIENTALES (CORREGIDO) ====================
document.getElementById("formDietarios")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  if (!ultimoFormularioId) {
    alert("Primero debes crear o seleccionar un formulario");
    return;
  }

  // ✅ CORRECCIÓN: IDs actualizados para coincidir con el HTML
  const dataDietarios = {
    id: {
      itemFormu: 5,
      formularioId: ultimoFormularioId,
    },
    aguaConsumoZona: document.getElementById("aguaConsumoZona")?.value || null,
    tratamientoAgua: document.getElementById("tratamientoAgua")?.value || null,
    fumigaciones: document.getElementById("fumigaciones")?.value || null,
    exposicionPesticidas: document.getElementById("exposicionPesticidas")?.value || null,
    combustionLenaDiario: document.getElementById("combusLenia")?.value || null,  // ← CORREGIDO
    exposicionQuimicos: document.getElementById("exposicionQuimicos")?.value || null,
    dietaFrutasVerduras: document.getElementById("dietaFrutasVerduras")?.value || null,
    alimentosCondimentados: document.getElementById("allCondimentado")?.value || null,  // ← CORREGIDO
    infusionesBebidas: document.getElementById("infusionesBebidas")?.value || null  // ← CORREGIDO (quitar la última "s" si tu backend espera "infusionesBebida")
  };

  try {
    const res = await fetch(`${API_URL}/factores-dietarios-ambientales`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dataDietarios)
    });

    if (res.ok) {
      alert("Datos dietarios y ambientales guardados con éxito");
      // Avanzar a Histopatología
      const nextTabElement = document.getElementById("histopatologia-tab");
      if (nextTabElement) {
        new bootstrap.Tab(nextTabElement).show();
      }
    } else {
      const error = await res.json();
      alert("Error al guardar dietarios: " + JSON.stringify(error));
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión al guardar dietarios");
  }
});

// ==================== HISTOPATOLOGÍA (CORREGIDO) ====================
document.getElementById("formHistopatologia")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  if (!ultimoFormularioId) {
    alert("Primero debes crear o seleccionar un formulario");
    return;
  }

  const dataHistopatologia = {
    id: {
      itemFormu: 1,
      formularioId: ultimoFormularioId
    },
    tipo: document.getElementById("tipoMuestra")?.value || null,
    estadoClinico: document.getElementById("estadoClinico")?.value || null,
    localiTumoral: document.getElementById("localizacionTumoral")?.value || null
  };

  try {
    const res = await fetch(`${API_URL}/histopatologia`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dataHistopatologia)
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err);
    }

    alert(" Formulario completo guardado correctamente");
    
    // Opcional: Limpiar formulario o redirigir
    // document.getElementById("formHistopatologia").reset();
    
  } catch (error) {
    console.error(error);
    alert(" Error al guardar histopatología: " + error.message);
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



// ==================== GESTIÓN DE REGLAS ====================

//diccionario de datos
const LIMITES_VARIABLES = {
    edad: { min: 18, max: 120, step: 1, msg: "Edad entre 18 y 120 años" },
    peso: { min: 30, max: 300, step: 0.1, msg: "Peso entre 30 y 300 kg" },
    estatura: { min: 100, max: 250, step: 1, msg: "Estatura en cm (100-250)" },
    imc: { min: 10, max: 60, step: 0.1, msg: "IMC válido (10-60)" },
    cantPromTabaco: { min: 0, max: 100, step: 1, msg: "Máximo 100 cigarros" },
    tiempoTabaco: { min: 0, max: 80, step: 1, msg: "Años fumando (0-80)" }
};

let conjuntoActualId = null;

window.verReglas = async function(conjuntoId) {
  conjuntoActualId = conjuntoId;
  
  document.getElementById("vistaTablaReglas").classList.remove("d-none");
  document.getElementById("vistaFormularioRegla").classList.add("d-none");
  
  const tbody = document.getElementById("bodyReglas");
  tbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';

  const modal = new bootstrap.Modal(document.getElementById('modalVerReglas'));
  modal.show();

  await cargarTablaReglas(conjuntoId);
}

async function cargarTablaReglas(id) {
  const tbody = document.getElementById("bodyReglas");
  try {
    const res = await fetch(`${API_URL}/dicot-reglas`);
    if (!res.ok) throw new Error("Error al obtener reglas");
    
    const todas = await res.json();
    const filtradas = todas.filter(r => r.dicotConjuntoID === id);

    tbody.innerHTML = "";
    if (filtradas.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No hay reglas definidas.</td></tr>';
      return;
    }

    filtradas.forEach(r => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
          <td><strong>${r.atributoObj || '---'}</strong></td>
          <td class="text-center"><span class="badge bg-info text-dark">${r.operador || '='}</span></td>
          <td class="text-center">${r.valorInf || r.valorSup || 0}</td>
          <td class="text-center fw-bold">${r.valorSiCumple}</td>
          <td>${r.valorCategoria || ''}</td>
      `;
      tbody.appendChild(tr);
    });

  } catch (err) {
    console.error(err);
    tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Error de conexión</td></tr>';
  }
}

window.mostrarFormularioRegla = function() {
    document.getElementById("vistaTablaReglas").classList.add("d-none");
    document.getElementById("vistaFormularioRegla").classList.remove("d-none");
    document.getElementById("formNuevaRegla").reset();
    
    document.getElementById("infoVariable").textContent = "Selecciona una variable";
    document.getElementById("reglaValor").classList.remove("is-invalid");
}

window.cancelarRegla = function() {
    document.getElementById("vistaFormularioRegla").classList.add("d-none");
    document.getElementById("vistaTablaReglas").classList.remove("d-none");
}

window.configurarInput = function() {
    const variable = document.getElementById("reglaVariable").value;
    const input = document.getElementById("reglaValor");
    const info = document.getElementById("infoVariable");
    
    input.value = "";
    input.classList.remove("is-invalid");

    const config = LIMITES_VARIABLES[variable];

    if (config) {
        input.min = config.min;
        input.max = config.max;
        input.step = config.step;
        input.placeholder = `${config.min} - ${config.max}`;
        info.textContent = config.msg; 
    } else {
        input.removeAttribute("min");
        input.removeAttribute("max");
        input.step = "any";
        input.placeholder = "Valor";
        info.textContent = "Variable sin rango estricto";
    }
}

window.invertirValor = function(origen) {
    const si = document.getElementById("reglaValorSi");
    const no = document.getElementById("reglaValorNo");
    
    if (origen === 'si') {
        no.value = (si.value === "1") ? "0" : "1";
    } else {
        si.value = (no.value === "1") ? "0" : "1";
    }
}

document.getElementById("formNuevaRegla").addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!conjuntoActualId) {
        alert("Error: No se ha identificado el conjunto.");
        return;
    }

    const variable = document.getElementById("reglaVariable").value;
    const operador = document.getElementById("reglaOperador").value;
    
    let entidadCorrecta = "";
    const varsGenerales = ['edad', 'peso', 'estatura', 'imc', 'sexo', 'zonaResidencial'];
    
    if (varsGenerales.includes(variable)) {
        entidadCorrecta = "datos_genericos";
    } else {
        entidadCorrecta = "habitos_paciente";
    }

    const valorRaw = parseFloat(document.getElementById("reglaValor").value);
    const valor = Math.round(valorRaw); 
    
    let valInf = null;
    let valSup = null;

    if (operador === '<=' || operador === '<') {
        valSup = valor;
    } else {
        valInf = valor;
    }

    const nuevaRegla = {
        entidadObj: entidadCorrecta,
        atributoObj: variable,
        operador: operador,
        metodo: "VALOR_FIJO",
        
        valorInf: valInf, 
        valorSup: valSup,
        
        valorSiCumple: parseInt(document.getElementById("reglaValorSi").value),
        valorNoCumple: parseInt(document.getElementById("reglaValorNo").value),
        valorCategoria: document.getElementById("reglaCategoria").value,
        dicotConjunto: { 
            idDicotConjunto: conjuntoActualId 
        }
    };

    console.log("Enviando regla:", nuevaRegla);

    try {
        const res = await fetch(`${API_URL}/dicot-reglas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevaRegla)
        });

        if (res.ok) {
            alert("Regla guardada correctamente");
            cancelarRegla();
            await cargarTablaReglas(conjuntoActualId);
        } else {
            const error = await res.json();
            alert("Error: " + (error.message || "No se pudo guardar"));
        }
    } catch (err) {
        console.error(err);
        alert("Error de conexión");
    }
});

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

// ==================== EXPORTAR A WORD ====================
async function descargarFichaWord(idPaciente) {

    if (!confirm(`¿Descargar ficha en Word para el paciente ${idPaciente}?`)) return;

    try {
        const url = `${API_URL}/api/export/paciente/${idPaciente}/word`;
        
        console.log("Descargando desde:", url);

        const res = await fetch(url);

        if (!res.ok) {
            throw new Error(`Error ${res.status}: No se encontró el reporte o falló el servidor.`);
        }

        const blob = await res.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        
        const a = document.createElement('a');
        a.href = downloadUrl;
        a.download = `Ficha_${idPaciente}.docx`;
        document.body.appendChild(a);
        a.click();
        
        document.body.removeChild(a);
        window.URL.revokeObjectURL(downloadUrl);

    } catch (err) {
        console.error(err);
        alert("Error: " + err.message);
    }
}

// ==================== LÓGICA EXPORTAR WORD ====================
async function cargarListaPacientesWord() {
    const select = document.getElementById("selectPacienteWord");
    if (!select) return;

    try {
        const res = await fetch(`${API_URL}/pacientes`);
        if (!res.ok) throw new Error("No se pudo cargar la lista");
        
        const pacientes = await res.json();

        select.innerHTML = '<option value="">-- Selecciona un paciente --</option>';

        pacientes.forEach(p => {
            const id = p.idPaciente || p.id; 
            const nombre = p.nombrePaciente || p.nombre || "Sin Nombre";
            
            const option = document.createElement("option");
            option.value = id;
            option.textContent = `${id} - ${nombre}`;
            select.appendChild(option);
        });

    } catch (err) {
        console.error(err);
        select.innerHTML = '<option value="">Error al cargar lista</option>';
    }
}

document.addEventListener("DOMContentLoaded", cargarListaPacientesWord);
cargarListaPacientesWord();

document.getElementById("btnDescargarWord")?.addEventListener("click", () => {
    const select = document.getElementById("selectPacienteWord");
    const idPaciente = select.value;

    if (!idPaciente) {
        alert("Por favor selecciona un paciente de la lista.");
        return;
    }

    descargarFichaWord(idPaciente);
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
      case 'pacientes':       endpoint = '/pacientes'; break;
      case 'formularios':     endpoint = '/formularios'; break;
      case 'datos_generales': endpoint = '/datos-generales'; break;
      case 'habitos':         endpoint = '/habitos-paciente'; break;
      case 'datos_clinicos':  endpoint = '/datos-clinicos'; break;
      case 'histopatologia':  endpoint = '/histopatologia'; break;
      case 'fact_ambientales': endpoint = '/factores-dietarios-hambientales'; break;
    }

    const res = await fetch(`${API_URL}${endpoint}`);
    if (res.status === 404) throw new Error(`Ruta no encontrada: ${endpoint}`);
    if (!res.ok) throw new Error(`Error ${res.status}: No se pudo cargar la tabla`);

    const datos = await res.json();
    
    if (!datos || datos.length === 0) {
      contenedor.innerHTML = '<div class="alert alert-warning">No hay datos registrados en esta tabla</div>';
      return;
    }

    const headers = Object.keys(datos[0]); 
    
    let html = '<table class="table table-striped table-hover table-bordered table-sm" style="font-size: 0.85rem;">';
    html += '<thead class="table-purple"><tr>';
    
    headers.forEach(h => {
      let titulo = h.replace(/([A-Z])/g, ' $1').toUpperCase(); 
      html += `<th>${titulo}</th>`;
    });
    html += '</tr></thead><tbody>';

    datos.forEach(row => {
      html += '<tr>';
      headers.forEach(h => {
        let value = row[h];
        let displayValue = '---';

        if (value !== null && value !== undefined) {
          if (typeof value === 'object') {
            if (value.formularioId) displayValue = `<span class="badge bg-secondary">Form: ${value.formularioId}</span>`;
            else if (value.idFormulario) displayValue = value.idFormulario;
            else if (value.idPaciente) displayValue = value.idPaciente;
            else if (value.id) displayValue = value.id;
            else displayValue = JSON.stringify(value).substring(0, 15) + '...';
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
    contenedor.innerHTML = `<div class="alert alert-danger">Error: ${err.message}</div>`;
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
      if (el.id === "btnCrearFormulario" || el.id === "selectFormularioExistente") {
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

// Refrescar los campos de paciente seleccionado y miembro asignado
function refrescarPacienteEnCrearFormulario() {
  const selectPaciente = document.getElementById("selectPacienteIngreso");
  const pacienteDisplay = document.getElementById("pacienteSeleccionadoForm");
  const miembroDisplay = document.getElementById("miembroAsignadoForm");
  
  // Mostrar nombre del paciente seleccionado
  if (selectPaciente && pacienteDisplay) {
    const textoSeleccionado = selectPaciente.options[selectPaciente.selectedIndex]?.textContent || "";
    pacienteDisplay.value = textoSeleccionado;
  }
  
  // Mostrar nombre del miembro logueado
  if (miembroDisplay) {
    const usuarioStr = localStorage.getItem("usuario");
    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr).usuario || JSON.parse(usuarioStr);
      miembroDisplay.value = usuario.nombre || usuario.nombreMiembro || "Usuario";
    }
  }
}

// ==================== REFRESCAR PACIENTE Y CARGAR FORMULARIOS EXISTENTES ====================
async function cargarFormulariosDelPaciente(pacienteId) {
  const selectFormulario = document.getElementById("selectFormularioExistente");
  const contenedorCrear = document.getElementById("contenedorCrearFormulario");

  if (!selectFormulario || !contenedorCrear) {
    console.error("No se encontraron elementos selectFormularioExistente o contenedorCrearFormulario");
    return;
  }

  // Habilitar el select mientras carga
  selectFormulario.disabled = false;
  selectFormulario.innerHTML = '<option value="">-- Cargando formularios... --</option>';

  if (!pacienteId) {
    selectFormulario.innerHTML = '<option value="">-- Seleccione un paciente --</option>';
    contenedorCrear.style.display = "block";
    return;
  }

  try {
    const response = await fetch(`${API_URL}/formularios`);
    if (!response.ok) throw new Error("Error al cargar formularios");

    const todosFormularios = await response.json();
    console.log("Todos los formularios:", todosFormularios);
    
    // El JSON devuelve paciente_id directamente (no anidado como paciente.idPaciente)
    const formulariosPaciente = todosFormularios.filter(f => f.paciente_id === pacienteId);
    console.log("Formularios del paciente " + pacienteId + ":", formulariosPaciente);

    selectFormulario.innerHTML = "";
    
    if (formulariosPaciente.length === 0) {
      selectFormulario.innerHTML = '<option value="">-- No tiene formularios, cree uno nuevo --</option>';
      contenedorCrear.style.display = "block";
      limpiarTodasLasPestanas();
    } else {
      // Agregar opción por defecto
      const optDefault = document.createElement("option");
      optDefault.value = "";
      optDefault.textContent = "-- Seleccione un formulario --";
      selectFormulario.appendChild(optDefault);
      
      // Agregar formularios existentes
      formulariosPaciente.forEach(form => {
        const opt = document.createElement("option");
        opt.value = form.idFormulario;
        opt.textContent = `ID ${form.idFormulario} - ${form.tipoFormulario} (${form.estadoFormulario || "En progreso"}) - ${form.fechaFormulario}`;
        selectFormulario.appendChild(opt);
      });
      
      // Ocultar botón crear si ya tiene formularios (opcional: puedes dejarlo visible)
      contenedorCrear.style.display = "none";
      
      // Seleccionar y cargar automáticamente el último formulario
      const ultimo = formulariosPaciente[formulariosPaciente.length - 1];
      selectFormulario.value = ultimo.idFormulario;
      
      // Cargar datos y habilitar formulario completo
      await cargarDatosFormulario(ultimo.idFormulario);
    }
  } catch (err) {
    console.error("Error cargando formularios:", err);
    selectFormulario.innerHTML = '<option value="">Error al cargar formularios</option>';
    contenedorCrear.style.display = "block";
  }
}
// ==================== CARGAR DATOS DEL FORMULARIO SELECCIONADO ====================
async function cargarDatosFormulario(formularioId) {
  if (!formularioId) {
    // Limpiar todas las pestañas
    limpiarTodasLasPestanas();
    return;
  }

  ultimoFormularioId = formularioId;
  habilitarFormularioCompleto();

  // Mostrar el ID del formulario
  const idFormularioDisplay = document.getElementById("idFormularioDisplay");
  if (idFormularioDisplay) {
    idFormularioDisplay.value = formularioId;
  }
  
  // Mostrar el ID del paciente desde el select
  const selectPaciente = document.getElementById("selectPacienteIngreso");
  const idPacienteDisplay = document.getElementById("idPacienteDisplay");
  if (selectPaciente && idPacienteDisplay) {
    idPacienteDisplay.value = selectPaciente.value || "";
  }

  try {
    // Cargar cada sección (itemFormu = 1 para todas tus weak entities)
    const promesas = [
      fetch(`${API_URL}/datos-generales/3/${formularioId}`),
      fetch(`${API_URL}/habitos-paciente/4/${formularioId}`),
      fetch(`${API_URL}/datos-clinicos/2/${formularioId}`),
      fetch(`${API_URL}/factores-dietarios-hambientales/5/${formularioId}`),
      fetch(`${API_URL}/histopatologia/1/${formularioId}`)
    ];

    const [genRes, habRes, cliRes, dietRes, histoRes] = await Promise.all(promesas);

    // === Datos Generales ===
    if (genRes.ok) {
      const datos = await genRes.json();
      document.getElementById("idPacienteDisplay").value = datos.idDatosGen?.pacienteId || "";
      document.getElementById("edad").value = datos.edad || "";
      document.getElementById("sexo").value = datos.sexo || "";
      document.getElementById("peso").value = datos.peso || "";
      document.getElementById("estatura").value = datos.estatura || "";
      document.getElementById("zonaResidencial").value = datos.zonaResidencial || "";
      document.getElementById("educacion").value = datos.educacion || "";
      document.getElementById("ocupacion").value = datos.ocupacion || "";
    }

    // === Datos Clínicos ===
    if (cliRes.ok) {
      const datos = await cliRes.json();
      document.getElementById("adenoGastrico").value = datos.adenoGastrico || "";
      document.getElementById("fechaAdenoGastrico").value = datos.fechaAdenoGastrico || "";
      document.getElementById("antFamCancerGast").value = datos.antFamCancerGast || "";
      document.getElementById("medicamentos").value = datos.medicamentos || "";
      document.getElementById("otrasEnfermedades").value = datos.otrasEnfermedades || "";
      document.getElementById("antFamOtroCancer").value = datos.antFamOtroCancer || "";
      document.getElementById("cirugiaGastrica").value = datos.cirugiaGastricaPrevia || "";
      document.getElementById("hpyloriPrueba").value = datos.hpyloriPrueba || "";
      document.getElementById("hpyloriResultado").value = datos.hpyloriResultado || "";
      document.getElementById("hpyloriTiempoTest").value = datos.hpyloriTiempoTest || "";
    }

    // === Hábitos ===
    if (habRes.ok) {
      const datos = await habRes.json();
      document.getElementById("estadoConsumoTabaco").value = datos.estadoConsumoTabaco || "";
      document.getElementById("cantPromTabaco").value = datos.cantPromTabaco || "";
      document.getElementById("exConsumidorTabaco").value = datos.exConsumidorTabaco || "";
      document.getElementById("tiempoTabaco").value = datos.tiempoTabaco || "";
      document.getElementById("estadoConsumoAlcohol").value = datos.estadoConsumoAlcohol || "";
      document.getElementById("frecuenciaAlcohol").value = datos.frecuenciaAlcohol || "";
      document.getElementById("cantidadAlcohol").value = datos.cantidadAlcohol || "";
      document.getElementById("aniosConsumoAlcohol").value = datos.aniosConsumoAlcohol || "";
      document.getElementById("exConsumidorAlcohol").value = datos.exConsumidorAlcohol || "";
    }

    // === Datos Clínicos, Dietarios, Histopatología ===
    // (Se agregan cuando los campos estén definidos en el HTML)

    // Abrir pestaña Generales
    new bootstrap.Tab(document.getElementById("generales-tab")).show();

  } catch (err) {
    console.error("Error cargando datos del formulario:", err);
    alert("Error al cargar los datos del formulario");
  }
}

function limpiarTodasLasPestanas() {
  // Limpiar campos de todas las pestañas
  document.querySelectorAll("#panelIngresoDatos input, #panelIngresoDatos select").forEach(el => {
    if (el.type !== "hidden" && !el.disabled) el.value = "";
  });

  // Limpiar también los campos de solo lectura
  const idFormularioDisplay = document.getElementById("idFormularioDisplay");
  if (idFormularioDisplay) idFormularioDisplay.value = "";
  
  const idPacienteDisplay = document.getElementById("idPacienteDisplay");
  if (idPacienteDisplay) idPacienteDisplay.value = "";

  
  bloquearTodo();  // Opcional: bloquear hasta crear/seleccionar formulario
}

// ==================== EVENTOS ====================
  document.addEventListener("DOMContentLoaded", () => {
  panelDatos = document.getElementById("panelIngresoDatos");
  const selectPaciente = document.getElementById("selectPacienteIngreso");
  const btnCrearFormulario = document.getElementById("btnCrearFormulario");
  const selectFormularioExistente = document.getElementById("selectFormularioExistente");

  cargarPacientes(); // Ya lo tienes
  bloquearTodo();

  // Cuando cambia el paciente
  selectPaciente?.addEventListener("change", async function () {
    const pacienteId = this.value;
    const texto = this.options[this.selectedIndex]?.textContent || "";

    if (!pacienteId) {
      bloquearTodo();
      return;
    }

    // Autocompletar tipo (Caso/Control)
    const selectTipoFormulario = document.getElementById("tipoFormulario");
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

    // Refrescar nombre paciente y miembro logueado
    refrescarPacienteEnCrearFormulario();

    habilitarCrearFormulario(); // Solo si no tiene formulario

    // Cargar formularios existentes del paciente
    await cargarFormulariosDelPaciente(pacienteId);
  });

  // Cuando cambia el formulario existente seleccionado
  selectFormularioExistente?.addEventListener("change", function () {
    const formularioId = this.value;
    if (formularioId) {
      cargarDatosFormulario(formularioId);
    } else {
      limpiarTodasLasPestanas();
    }
  });

  // ==================== CREAR FORMULARIO NUEVO ====================
  document.getElementById("btnCrearFormulario")?.addEventListener("click", async () => {
    // Obtener pacienteId directamente del select principal
    const selectPaciente = document.getElementById("selectPacienteIngreso");
    const pacienteId = selectPaciente?.value;

    // Obtener tipo de formulario
    const tipoFormulario = document.getElementById("tipoFormulario")?.value;

    // Obtener fecha del formulario
    const fechaFormulario = document.getElementById("fechaFormulario")?.value || new Date().toISOString().split('T')[0];

    // Obtener miembroId del localStorage (el usuario logueado)
    const usuarioStr = localStorage.getItem("usuario");
    const usuarioObj = usuarioStr ? JSON.parse(usuarioStr) : null;
    const usuario = usuarioObj?.usuario || usuarioObj;
    const miembroId = usuario?.id || usuario?.idMiembroEquipo || usuario?.idMiembro || null;

    console.log("Datos para crear formulario:", { pacienteId, tipoFormulario, fechaFormulario, miembroId });

    // Validaciones
    if (!pacienteId) {
      alert("Por favor, selecciona un paciente del listado superior.");
      return;
    }
    if (!tipoFormulario) {
      alert("Por favor, selecciona un Tipo de Formulario.");
      return;
    }
    if (!miembroId) {
      alert("No se detectó usuario logueado. Por favor, cierra sesión e inicia de nuevo.");
      return;
    }

    const data = {
      tipoFormulario: tipoFormulario,
      estadoFormulario: "En progreso",
      fechaFormulario: fechaFormulario,
      paciente: { idPaciente: pacienteId },
      miembroEquipo: { idMiembroEquipo: parseInt(miembroId) }
    };

    console.log("Enviando formulario:", data);

    try {
      const response = await fetch(`${API_URL}/formularios`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });

      if (response.ok) {
        const formulario = await response.json();
        ultimoFormularioId = formulario.idFormulario;
        
        alert(`¡Formulario creado exitosamente! ID: ${formulario.idFormulario}`);

        // Actualizar UI
        formularioCreado = true;
        habilitarFormularioCompleto();

        // Recargar formularios existentes
        await cargarFormulariosDelPaciente(pacienteId);

        // Seleccionar el nuevo formulario
        const selectExistente = document.getElementById("selectFormularioExistente");
        if (selectExistente) {
          selectExistente.value = formulario.idFormulario;
        }

        // Cargar datos del formulario (para mostrar el ID en el panel)
        await cargarDatosFormulario(formulario.idFormulario);

        // Ir a pestaña generales
        const tabGenerales = document.getElementById("generales-tab");
        if (tabGenerales) new bootstrap.Tab(tabGenerales).show();

      } else {
        const errorText = await response.text();
        console.error("Error del servidor:", errorText);
        alert("Error al crear formulario: " + errorText);
      }
    } catch (err) {
      console.error("Error de conexión:", err);
      alert("Error de conexión. Revisa la consola (F12)");
    }
  });
});
<<<<<<< HEAD
=======

// une el botón con la función de descarga
document.getElementById("btnExportarWord")?.addEventListener("click", () => {
    const id = document.getElementById("inputPacienteExportar").value;
    descargarFichaWord(id);
});
>>>>>>> 973a8c9fb9030f8cfb83154abdab02873d478159
