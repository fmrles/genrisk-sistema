-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-12-2025 a las 01:37:26
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `genriskbdd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `datos_clinicos`
--

CREATE TABLE `datos_clinicos` (
  `itemformu` int(11) NOT NULL,
  `formulario_id` int(11) NOT NULL,
  `adeno_gastrico` varchar(255) DEFAULT NULL,
  `fecha_adeno_gastrico` date DEFAULT NULL,
  `ant_fam_cancer_gast` varchar(255) DEFAULT NULL,
  `medicamentos` varchar(255) DEFAULT NULL,
  `otras_enfermedades` varchar(255) DEFAULT NULL,
  `ant_fam_otro_cancer` varchar(255) DEFAULT NULL,
  `cirugia_gastrica_previa` varchar(255) DEFAULT NULL,
  `hpylori_prueba` varchar(255) DEFAULT NULL,
  `hpylori_resultado` varchar(255) DEFAULT NULL,
  `hpylori_tiempo_test` varchar(255) DEFAULT NULL,
  `positivo_pasado_hpylori` varchar(50) DEFAULT NULL,
  `anio_positivopasado_hpylori` int(11) DEFAULT NULL,
  `trata_erradicacion` varchar(50) DEFAULT NULL,
  `anio_trataerradica` int(11) DEFAULT NULL,
  `esquema_trataerradica` varchar(100) DEFAULT NULL,
  `antibioticos_ibp` varchar(50) DEFAULT NULL,
  `repeticion_examen` varchar(50) DEFAULT NULL,
  `fecha_repetiexamen` date DEFAULT NULL,
  `tipo_exa_pasado_hpy` varchar(100) DEFAULT NULL,
  `resultados_examen` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `datos_clinicos`
--

INSERT INTO `datos_clinicos` (`itemformu`, `formulario_id`, `adeno_gastrico`, `fecha_adeno_gastrico`, `ant_fam_cancer_gast`, `medicamentos`, `otras_enfermedades`, `ant_fam_otro_cancer`, `cirugia_gastrica_previa`, `hpylori_prueba`, `hpylori_resultado`, `hpylori_tiempo_test`, `positivo_pasado_hpylori`, `anio_positivopasado_hpylori`, `trata_erradicacion`, `anio_trataerradica`, `esquema_trataerradica`, `antibioticos_ibp`, `repeticion_examen`, `fecha_repetiexamen`, `tipo_exa_pasado_hpy`, `resultados_examen`) VALUES
(2, 101, NULL, NULL, 'NO', 'IBP', 'NO', 'NO', 'NO', 'Test de Aliento', 'Negativo', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 102, NULL, NULL, 'NO', 'Ninguno', 'Anemia', 'NO', 'NO', 'Antigeno', 'Positivo', '8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 103, 'NO', '2024-12-05', 'SÍ', 'Ninguno', 'NO', 'NO', 'NO', 'Test de Aliento', 'Negativo', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 201, 'NO', '2019-07-11', 'NO', 'Clonixinato de Lisina', 'NO', 'Cáncer de Páncreas', 'NO', 'Test de Aliento', 'Negativo', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 202, 'NO', '2025-02-14', 'NO', 'Ninguno', 'Diabetes', 'NO', 'NO', 'Test de Aliento', 'Negativo', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 203, NULL, NULL, 'NO', 'NO', 'NO', 'NO', 'NO', 'Test de Aliento', 'Negativo', '12', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 209, 'SÍ', '2023-08-14', 'NO', 'Ninguno', 'Lupus', 'Cáncer de Estómago', 'NO', 'Test de Aliento', 'Negativo', '10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 213, NULL, NULL, 'NO', 'NO', 'NO', 'NO', 'NO', 'Endoscopia', 'Positivo', '24', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 217, 'NO', NULL, 'SÍ', 'Ninguno', NULL, NULL, NULL, 'Test de Aliento', 'Positivo', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `datos_generales`
--

CREATE TABLE `datos_generales` (
  `itemformu` int(11) NOT NULL,
  `formulario_id` int(11) NOT NULL,
  `edad` int(11) DEFAULT NULL,
  `sexo` varchar(255) DEFAULT NULL,
  `peso` double DEFAULT NULL,
  `imc` double DEFAULT NULL,
  `estatura` double DEFAULT NULL,
  `zona_residencial` varchar(255) DEFAULT NULL,
  `educacion` varchar(255) DEFAULT NULL,
  `ocupacion` varchar(255) DEFAULT NULL,
  `nacionalidad` varchar(100) DEFAULT NULL,
  `direccion` varchar(150) DEFAULT NULL,
  `comuna` varchar(100) DEFAULT NULL,
  `ciudad` varchar(150) DEFAULT NULL,
  `prevision_salud` varchar(100) DEFAULT NULL,
  `anios_resi_actual` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `datos_generales`
--

INSERT INTO `datos_generales` (`itemformu`, `formulario_id`, `edad`, `sexo`, `peso`, `imc`, `estatura`, `zona_residencial`, `educacion`, `ocupacion`, `nacionalidad`, `direccion`, `comuna`, `ciudad`, `prevision_salud`, `anios_resi_actual`) VALUES
(3, 101, 30, 'Femenino', 67, 23, 165, 'Urbana', 'Superior', 'Ingeniera Civil Informática', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 102, 35, 'Femenino', 61, 23, 161, 'Urbana', 'Superior', 'Arquitecta', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 103, 29, 'Femenino', 58, 23, 160, 'Urbana', 'Superior', 'Enfermera', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 201, 40, 'Femenino', 70, 26, 163, 'Rural', 'Media', 'Dueña de Casa', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 202, 20, 'Femenino', 55, 21, 158, 'Urbana', 'Superior', 'Estudiante Universitaria', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 203, 33, 'Masculino', 70, 26, 165, 'Urbana', 'Superior', 'Ejecutivo', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 209, 47, 'Masculino', 81, 25, 171, 'Rural', 'Superior', 'Ingeniero Agrónomo', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 213, 41, 'Masculino', 77, 24, 174, 'Rural', 'Media', 'Granjero', NULL, NULL, NULL, NULL, NULL, NULL),
(3, 217, 45, 'Masculino', 80.5, 26.1, 175, 'Urbana', 'Superior', 'Ingeniero', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dicot_conjunto`
--

CREATE TABLE `dicot_conjunto` (
  `id_dicotconjunto` int(100) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `dicot_conjunto`
--

INSERT INTO `dicot_conjunto` (`id_dicotconjunto`, `nombre`, `descripcion`) VALUES
(1, 'Estudio Cáncer Gástrico 2025', 'Dicotomías edad, tabaquismo, alcohol, zona y sexo'),
(2, 'Config Cliente MALV 2025', 'Cortes por >=60, IMC>=25, Frecuencia Frecuente, Sexo Mujer, Zona Rural, Fumador actual'),
(3, 'Prueba Mediana Edad 2025', 'Conjunto de prueba usando la mediana de edad y el sexo.'),
(4, 'Test Postman', 'Prueba completa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dicot_regla`
--

CREATE TABLE `dicot_regla` (
  `id_dicotregla` int(100) NOT NULL,
  `entidad_obj` varchar(255) DEFAULT NULL,
  `atributo_obj` varchar(255) DEFAULT NULL,
  `metodo` varchar(255) DEFAULT NULL,
  `valor_inf` int(100) DEFAULT NULL,
  `valor_sup` int(100) DEFAULT NULL,
  `operador` varchar(255) DEFAULT NULL,
  `valor_categoria` varchar(255) DEFAULT NULL,
  `valor_si_cumple` int(11) NOT NULL DEFAULT 1,
  `valor_no_cumple` int(11) NOT NULL DEFAULT 0,
  `dicotconjunto_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `dicot_regla`
--

INSERT INTO `dicot_regla` (`id_dicotregla`, `entidad_obj`, `atributo_obj`, `metodo`, `valor_inf`, `valor_sup`, `operador`, `valor_categoria`, `valor_si_cumple`, `valor_no_cumple`, `dicotconjunto_id`) VALUES
(1, 'datos_genericos', 'edad', 'VALOR_FIJO', 50, NULL, '>=', NULL, 1, 0, 1),
(2, 'habitos_paciente', 'estadoConsumoTabaco', 'VALOR_FIJO', NULL, NULL, '=', 'Fumador', 1, 0, 1),
(3, 'habitos_paciente', 'estadoConsumoAlcohol', 'VALOR_FIJO', NULL, NULL, '=', 'Bebedor', 1, 0, 1),
(4, 'datos_genericos', 'sexo', 'VALOR_FIJO', NULL, NULL, '=', 'Femenino', 1, 0, 1),
(5, 'datos_genericos', 'zonaResidencial', 'VALOR_FIJO', NULL, NULL, '=', 'Rural', 1, 0, 1),
(21, 'datos_genericos', 'edad', 'VALOR_FIJO', 60, NULL, '>=', NULL, 1, 0, 2),
(22, 'habitos_paciente', 'estado', 'VALOR_FIJO', NULL, NULL, '=', NULL, 1, 0, 2),
(23, 'habitos_paciente', 'frecuencia', 'VALOR_FIJO', NULL, NULL, '=', NULL, 1, 0, 2),
(24, 'datos_genericos', 'sexo', 'VALOR_FIJO', NULL, NULL, '=', NULL, 1, 0, 2),
(25, 'datos_genericos', 'zona', 'VALOR_FIJO', NULL, NULL, '=', NULL, 1, 0, 2),
(26, 'datos_genericos', 'imc', 'VALOR_FIJO', 25, NULL, '>=', NULL, 1, 0, 2),
(27, 'datos_genericos', 'edad', NULL, NULL, 34, '<=', NULL, 1, 0, 3),
(28, 'datos_genericos', 'sexo', NULL, NULL, NULL, '=', 'Femenino', 1, 0, 3),
(29, 'datos_genericos', 'edad', NULL, NULL, 34, '<=', NULL, 1, 0, 3),
(30, 'datos_genericos', 'edad', NULL, 35, NULL, '>=', NULL, 1, 0, 4),
(31, 'habitos_paciente', 'estado_consumo_tabaco', NULL, NULL, NULL, '=', 'Fumador', 1, 0, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dicot_valor`
--

CREATE TABLE `dicot_valor` (
  `id_dicotvalor` int(11) NOT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `valordicico` int(100) NOT NULL,
  `formulario_id` int(100) NOT NULL,
  `regladicot_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `dicot_valor`
--

INSERT INTO `dicot_valor` (`id_dicotvalor`, `categoria`, `valordicico`, `formulario_id`, `regladicot_id`) VALUES
(1, 'edad', 0, 101, 1),
(2, 'edad', 0, 102, 1),
(3, 'edad', 0, 103, 1),
(4, 'edad', 0, 201, 1),
(5, 'edad', 0, 202, 1),
(6, 'edad', 0, 203, 1),
(7, 'edad', 1, 209, 1),
(8, 'edad', 0, 213, 1),
(9, 'estado_cons_tabaco', 0, 101, 2),
(10, 'estado_cons_tabaco', 1, 102, 2),
(11, 'estado_cons_tabaco', 0, 103, 2),
(12, 'estado_cons_tabaco', 0, 201, 2),
(13, 'estado_cons_tabaco', 1, 202, 2),
(14, 'estado_cons_tabaco', 0, 203, 2),
(15, 'estado_cons_tabaco', 0, 209, 2),
(16, 'estado_cons_tabaco', 0, 213, 2),
(17, 'estado_cons_alcohol', 1, 101, 3),
(18, 'estado_cons_alcohol', 0, 102, 3),
(19, 'estado_cons_alcohol', 1, 103, 3),
(20, 'estado_cons_alcohol', 0, 201, 3),
(21, 'estado_cons_alcohol', 1, 202, 3),
(22, 'estado_cons_alcohol', 0, 203, 3),
(23, 'estado_cons_alcohol', 1, 209, 3),
(24, 'estado_cons_alcohol', 1, 213, 3),
(25, 'sexo', 1, 101, 4),
(26, 'sexo', 1, 102, 4),
(27, 'sexo', 1, 103, 4),
(28, 'sexo', 1, 201, 4),
(29, 'sexo', 1, 202, 4),
(30, 'sexo', 0, 203, 4),
(31, 'sexo', 0, 209, 4),
(32, 'sexo', 0, 213, 4),
(33, 'zona', 0, 101, 5),
(34, 'zona', 0, 102, 5),
(35, 'zona', 0, 103, 5),
(36, 'zona', 1, 201, 5),
(37, 'zona', 0, 202, 5),
(38, 'zona', 0, 203, 5),
(39, 'zona', 1, 209, 5),
(40, 'zona', 1, 213, 5),
(41, 'edad', 0, 101, 21),
(42, 'edad', 0, 102, 21),
(43, 'edad', 0, 103, 21),
(44, 'edad', 0, 201, 21),
(45, 'edad', 0, 202, 21),
(46, 'edad', 0, 203, 21),
(47, 'edad', 0, 209, 21),
(48, 'edad', 0, 213, 21),
(49, 'estado', 0, 101, 22),
(50, 'estado', 1, 102, 22),
(51, 'estado', 0, 103, 22),
(52, 'estado', 0, 201, 22),
(53, 'estado', 1, 202, 22),
(54, 'estado', 0, 203, 22),
(55, 'estado', 0, 209, 22),
(56, 'estado', 0, 213, 22),
(57, 'frecuencia', 0, 101, 23),
(58, 'frecuencia', 0, 102, 23),
(59, 'frecuencia', 0, 103, 23),
(60, 'frecuencia', 0, 201, 23),
(61, 'frecuencia', 0, 202, 23),
(62, 'frecuencia', 0, 203, 23),
(63, 'frecuencia', 1, 209, 23),
(64, 'frecuencia', 0, 213, 23),
(65, 'sexo', 1, 101, 24),
(66, 'sexo', 1, 102, 24),
(67, 'sexo', 1, 103, 24),
(68, 'sexo', 1, 201, 24),
(69, 'sexo', 1, 202, 24),
(70, 'sexo', 0, 203, 24),
(71, 'sexo', 0, 209, 24),
(72, 'sexo', 0, 213, 24),
(73, 'zona', 1, 101, 25),
(74, 'zona', 1, 102, 25),
(75, 'zona', 1, 103, 25),
(76, 'zona', 0, 201, 25),
(77, 'zona', 1, 202, 25),
(78, 'zona', 1, 203, 25),
(79, 'zona', 0, 209, 25),
(80, 'zona', 0, 213, 25),
(81, 'imc', 0, 101, 26),
(82, 'imc', 0, 102, 26),
(83, 'imc', 0, 103, 26),
(84, 'imc', 1, 201, 26),
(85, 'imc', 0, 202, 26),
(86, 'imc', 1, 203, 26),
(87, 'imc', 1, 209, 26),
(88, 'imc', 0, 213, 26),
(89, 'estadoConsumoTabaco', 0, 101, 2),
(90, 'estadoConsumoAlcohol', 1, 101, 3),
(91, 'estadoConsumoTabaco', 1, 102, 2),
(92, 'estadoConsumoAlcohol', 0, 102, 3),
(93, 'estadoConsumoTabaco', 0, 103, 2),
(94, 'estadoConsumoAlcohol', 1, 103, 3),
(95, 'estadoConsumoTabaco', 0, 201, 2),
(96, 'estadoConsumoAlcohol', 0, 201, 3),
(97, 'estadoConsumoTabaco', 1, 202, 2),
(98, 'estadoConsumoAlcohol', 1, 202, 3),
(99, 'estadoConsumoTabaco', 0, 203, 2),
(100, 'estadoConsumoAlcohol', 0, 203, 3),
(101, 'estadoConsumoTabaco', 0, 209, 2),
(102, 'estadoConsumoAlcohol', 1, 209, 3),
(103, 'estadoConsumoTabaco', 0, 213, 2),
(104, 'estadoConsumoAlcohol', 1, 213, 3),
(105, 'edad', 0, 101, 1),
(106, 'estadoConsumoTabaco', 0, 101, 2),
(107, 'estadoConsumoAlcohol', 1, 101, 3),
(108, 'sexo', 1, 101, 4),
(109, 'zonaResidencial', 0, 101, 5),
(110, 'edad', 0, 102, 1),
(111, 'estadoConsumoTabaco', 1, 102, 2),
(112, 'estadoConsumoAlcohol', 0, 102, 3),
(113, 'sexo', 1, 102, 4),
(114, 'zonaResidencial', 0, 102, 5),
(115, 'edad', 0, 103, 1),
(116, 'estadoConsumoTabaco', 0, 103, 2),
(117, 'estadoConsumoAlcohol', 1, 103, 3),
(118, 'sexo', 1, 103, 4),
(119, 'zonaResidencial', 0, 103, 5),
(120, 'edad', 0, 201, 1),
(121, 'estadoConsumoTabaco', 0, 201, 2),
(122, 'estadoConsumoAlcohol', 0, 201, 3),
(123, 'sexo', 1, 201, 4),
(124, 'zonaResidencial', 1, 201, 5),
(125, 'edad', 0, 202, 1),
(126, 'estadoConsumoTabaco', 1, 202, 2),
(127, 'estadoConsumoAlcohol', 1, 202, 3),
(128, 'sexo', 1, 202, 4),
(129, 'zonaResidencial', 0, 202, 5),
(130, 'edad', 0, 203, 1),
(131, 'estadoConsumoTabaco', 0, 203, 2),
(132, 'estadoConsumoAlcohol', 0, 203, 3),
(133, 'sexo', 0, 203, 4),
(134, 'zonaResidencial', 0, 203, 5),
(135, 'edad', 0, 209, 1),
(136, 'estadoConsumoTabaco', 0, 209, 2),
(137, 'estadoConsumoAlcohol', 1, 209, 3),
(138, 'sexo', 0, 209, 4),
(139, 'zonaResidencial', 1, 209, 5),
(140, 'edad', 0, 213, 1),
(141, 'estadoConsumoTabaco', 0, 213, 2),
(142, 'estadoConsumoAlcohol', 1, 213, 3),
(143, 'sexo', 0, 213, 4),
(144, 'zonaResidencial', 1, 213, 5),
(145, 'sexo', 1, 101, 28),
(146, 'sexo', 1, 102, 28),
(147, 'sexo', 1, 103, 28),
(148, 'sexo', 1, 201, 28),
(149, 'sexo', 1, 202, 28),
(150, 'sexo', 0, 203, 28),
(151, 'sexo', 0, 209, 28),
(152, 'sexo', 0, 213, 28),
(153, 'edad', 1, 101, 27),
(154, 'sexo', 1, 101, 28),
(155, 'edad', 0, 102, 27),
(156, 'sexo', 1, 102, 28),
(157, 'edad', 1, 103, 27),
(158, 'sexo', 1, 103, 28),
(159, 'edad', 0, 201, 27),
(160, 'sexo', 1, 201, 28),
(161, 'edad', 1, 202, 27),
(162, 'sexo', 1, 202, 28),
(163, 'edad', 1, 203, 27),
(164, 'sexo', 0, 203, 28),
(165, 'edad', 0, 209, 27),
(166, 'sexo', 0, 209, 28),
(167, 'edad', 0, 213, 27),
(168, 'sexo', 0, 213, 28),
(169, 'edad', 0, 101, 30),
(170, 'estado_consumo_tabaco', 0, 101, 31),
(171, 'edad', 1, 102, 30),
(172, 'estado_consumo_tabaco', 1, 102, 31),
(173, 'edad', 0, 103, 30),
(174, 'estado_consumo_tabaco', 0, 103, 31),
(175, 'edad', 1, 201, 30),
(176, 'estado_consumo_tabaco', 0, 201, 31),
(177, 'edad', 0, 202, 30),
(178, 'estado_consumo_tabaco', 1, 202, 31),
(179, 'edad', 0, 203, 30),
(180, 'estado_consumo_tabaco', 0, 203, 31),
(181, 'edad', 1, 209, 30),
(182, 'estado_consumo_tabaco', 0, 209, 31),
(183, 'edad', 1, 213, 30),
(184, 'estado_consumo_tabaco', 0, 213, 31);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fact_dietario_ambiental`
--

CREATE TABLE `fact_dietario_ambiental` (
  `itemformu` int(11) NOT NULL,
  `formulario_id` int(11) NOT NULL,
  `agua_consumo_zona` varchar(100) DEFAULT NULL,
  `tratamiento_agua` varchar(255) DEFAULT NULL,
  `fumigaciones` varchar(255) DEFAULT NULL,
  `exposicion_pesticidas` varchar(255) DEFAULT NULL,
  `combus_lena_diario` varchar(255) DEFAULT NULL,
  `exposicion_quimicos` varchar(255) DEFAULT NULL,
  `dieta_agregasal` varchar(255) DEFAULT NULL,
  `dieta_frutas_verduras` varchar(255) DEFAULT NULL,
  `dieta_frituras` varchar(255) DEFAULT NULL,
  `dieta_carnes_cecinas` varchar(255) DEFAULT NULL,
  `ali_condimentado` varchar(100) DEFAULT NULL,
  `infusiones_bebidas` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `fact_dietario_ambiental`
--

INSERT INTO `fact_dietario_ambiental` (`itemformu`, `formulario_id`, `agua_consumo_zona`, `tratamiento_agua`, `fumigaciones`, `exposicion_pesticidas`, `combus_lena_diario`, `exposicion_quimicos`, `dieta_agregasal`, `dieta_frutas_verduras`, `dieta_frituras`, `dieta_carnes_cecinas`, `ali_condimentado`, `infusiones_bebidas`) VALUES
(5, 101, 'Red pública', 'Ninguno', 'Nunca', 'No', 'Nunca/Rara vez', 'SÍ', '≥3/sem', '3-4 porcio', 'No', '1-2/sem', NULL, NULL),
(5, 102, 'Red pública', 'Filtro', 'Nunca', 'No', 'Nunca/Rara vez', 'No', 'No', '3-4 porcio', 'No', '<1/sem', NULL, NULL),
(5, 103, 'Red pública', 'Hervir', 'Nunca', 'No', '1-2/sem', 'Sí', '≥3/sem', '<2 porcion', 'Sí', '≥3/sem', NULL, NULL),
(5, 201, 'Pozo', 'Hervir', 'Frecuente', 'Sí', '≥3/sem', 'Estacional', 'No', '<2 porcion', 'No', '1-2/sem', NULL, NULL),
(5, 202, 'Red pública', 'Ninguno', 'Nunca', 'No', 'Nunca/Rara vez', 'Sí', '≥3/sem', '3-4 porcio', 'Sí', '≥3/sem', NULL, NULL),
(5, 203, 'Red pública', 'Filtro', 'Nunca', 'No', 'Nunca/Rara vez', 'No', 'No', '≥5 porcion', 'No', '<1/sem', NULL, NULL),
(5, 209, 'Pozo', 'Cloro', 'Ocasional', 'Sí', '1-2/sem', 'Diario', 'Sí', '<2 porcion', 'Sí', '1-2/sem', NULL, NULL),
(5, 213, 'Camión aljibe', 'Hervir', 'Frecuente', 'Sí', '≥3/sem', 'Estacional', 'No', '3-4 porcio', 'No', '1-2/sem', NULL, NULL),
(5, 217, 'Red pública', NULL, NULL, NULL, NULL, NULL, '≥3/sem', NULL, 'Sí', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `formulario`
--

CREATE TABLE `formulario` (
  `id_formulario` int(11) NOT NULL,
  `estado_formulario` varchar(255) DEFAULT NULL,
  `tipo_formulario` varchar(255) NOT NULL,
  `fecha_formulario` date NOT NULL,
  `paciente_id` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `miembro_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `formulario`
--

INSERT INTO `formulario` (`id_formulario`, `estado_formulario`, `tipo_formulario`, `fecha_formulario`, `paciente_id`, `miembro_id`) VALUES
(101, 'Activo', 'Control', '2025-10-01', 'pac1', 10),
(102, 'Activo', 'Control', '2025-10-02', 'pac2', 11),
(103, 'Activo', 'Caso', '2025-10-03', 'pac3', 12),
(201, 'Activo', 'Caso', '2025-10-07', 'pac4', 15),
(202, 'Activo', 'Caso', '2025-10-08', 'pac5', 10),
(203, 'Activo', 'Control', '2025-10-08', 'pac6', 11),
(209, 'Activo', 'Caso', '2025-10-10', 'pac7', 12),
(213, 'Activo', 'Control', '2025-10-11', 'pac8', 11),
(216, 'Activo', 'Caso', '2025-11-05', 'pac99', 15),
(217, 'En Progreso', 'Caso', '2025-11-05', 'CA0003', 10),
(218, 'Activo', 'Control', '2025-12-01', 'CA0002', 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `genotipificacion`
--

CREATE TABLE `genotipificacion` (
  `id_genotipificacion` int(11) NOT NULL,
  `paciente_id` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `muestra_nro` int(11) DEFAULT NULL,
  `estado_genoti` varchar(255) DEFAULT NULL,
  `fecha_genoti` date DEFAULT NULL,
  `tlr9rs5743836` varchar(255) DEFAULT NULL,
  `mir_146ars2910164` varchar(255) DEFAULT NULL,
  `mir_196a2rs11614913` varchar(255) DEFAULT NULL,
  `mthfrrs1801133` varchar(255) DEFAULT NULL,
  `dnmt3brs1569686` varchar(255) DEFAULT NULL,
  `tlr9rs187084` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `genotipificacion`
--

INSERT INTO `genotipificacion` (`id_genotipificacion`, `paciente_id`, `muestra_nro`, `estado_genoti`, `fecha_genoti`, `tlr9rs5743836`, `mir_146ars2910164`, `mir_196a2rs11614913`, `mthfrrs1801133`, `dnmt3brs1569686`, `tlr9rs187084`) VALUES
(1, 'pac2', 1, 'Completo', '2025-10-15', 'TT', 'GC', 'CT', 'CT', 'GG', 'TC'),
(2, 'pac1', 2, 'Completo', '2025-10-15', 'TT', 'GC', 'CT', 'CC', 'GT', 'TT'),
(3, 'pac3', 1, 'Completo', '2025-10-15', 'TT', 'GC', 'CT', 'CT', 'GG', 'TC'),
(4, 'pac4', 1, 'Pendiente', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 'pac5', 1, 'Pendiente', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, 'pac6', 1, 'Completo', '2025-10-15', 'TC', 'GG', 'CT', 'CT', 'GT', 'TT'),
(7, 'pac7', 1, 'Pendiente', '2025-10-16', 'TT', 'GC', 'CT', 'CT', 'GG', 'TC'),
(8, 'pac8', 1, 'Completo', '2025-10-16', 'TT', 'GC', 'CC', 'CT', 'GG', 'TC'),
(9, 'pac1', 1, 'Completo', '2025-10-16', 'CC', 'CC', 'TT', 'CC', 'TT', 'TC');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitos_paciente`
--

CREATE TABLE `habitos_paciente` (
  `itemformu` int(11) NOT NULL,
  `formulario_id` int(11) NOT NULL,
  `estado_consumo_tabaco` varchar(255) DEFAULT NULL,
  `cant_prom_tabaco` int(11) DEFAULT NULL,
  `ex_consumidor_tabaco` int(11) DEFAULT NULL,
  `estado_consumo_alcohol` varchar(255) DEFAULT NULL,
  `frecuencia_alcohol` varchar(255) DEFAULT NULL,
  `cantidad_alcohol` int(11) DEFAULT NULL,
  `anios_consumo_alcohol` int(11) DEFAULT NULL,
  `ex_consumidor_alcohol` int(11) DEFAULT NULL,
  `tiempo_tabaco` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `habitos_paciente`
--

INSERT INTO `habitos_paciente` (`itemformu`, `formulario_id`, `estado_consumo_tabaco`, `cant_prom_tabaco`, `ex_consumidor_tabaco`, `estado_consumo_alcohol`, `frecuencia_alcohol`, `cantidad_alcohol`, `anios_consumo_alcohol`, `ex_consumidor_alcohol`, `tiempo_tabaco`) VALUES
(4, 101, 'No fumador', NULL, NULL, 'Bebedor', 'Ocasional', 2, 10, NULL, NULL),
(4, 102, 'Fumador', 10, NULL, 'No bebedor', NULL, NULL, NULL, NULL, NULL),
(4, 103, 'Exfumador', 5, 5, 'Bebedor', 'Mensual', 3, 8, NULL, NULL),
(4, 201, 'No fumador', NULL, NULL, 'Exbebedor', NULL, NULL, 15, 2, NULL),
(4, 202, 'Fumador', 15, NULL, 'Bebedor', 'Semanal', 4, 12, NULL, NULL),
(4, 203, 'No fumador', NULL, NULL, 'No bebedor', NULL, NULL, NULL, NULL, NULL),
(4, 209, 'Exfumador', 8, 3, 'Bebedor', 'Diario', 2, 10, NULL, NULL),
(4, 213, 'No fumador', NULL, NULL, 'Bebedor', 'Ocasional', 1, 5, NULL, NULL),
(4, 217, 'Fumador', NULL, NULL, 'Bebedor', NULL, NULL, 20, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `histopatologia`
--

CREATE TABLE `histopatologia` (
  `itemformu` int(10) NOT NULL,
  `formulario_id` int(100) NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `estado_clinico` varchar(255) DEFAULT NULL,
  `locali_tumoral` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `histopatologia`
--

INSERT INTO `histopatologia` (`itemformu`, `formulario_id`, `tipo`, `estado_clinico`, `locali_tumoral`) VALUES
(1, 103, 'Intestinal', 'IIB', 'Antro'),
(1, 201, 'Mixto', 'IIB', 'Cuerpo'),
(1, 202, 'Intestinal', 'IIIA', 'Antro'),
(1, 209, 'Difuso', 'IIIA', 'Cardias'),
(1, 217, 'Difuso', 'IIIA', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ingresa_datos`
--

CREATE TABLE `ingresa_datos` (
  `id_ingreso` int(11) NOT NULL,
  `formulario_id` int(11) DEFAULT NULL,
  `miembro_id` int(11) DEFAULT NULL,
  `campo` varchar(255) NOT NULL,
  `valor_anterior` text DEFAULT NULL,
  `valor_nuevo` text DEFAULT NULL,
  `fecha_cambio` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ingresa_datos`
--

INSERT INTO `ingresa_datos` (`id_ingreso`, `formulario_id`, `miembro_id`, `campo`, `valor_anterior`, `valor_nuevo`, `fecha_cambio`) VALUES
(4, 201, 10, '', NULL, NULL, NULL),
(5, 202, 10, '', NULL, NULL, NULL),
(7, 209, 11, '', NULL, NULL, NULL),
(8, 213, 12, '', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `miembro_equipo`
--

CREATE TABLE `miembro_equipo` (
  `id_miembro` int(11) NOT NULL,
  `nombre_miembro` varchar(255) DEFAULT NULL,
  `correo_miembro` varchar(255) DEFAULT NULL,
  `clave` varchar(255) DEFAULT NULL,
  `rol_miembro` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `miembro_equipo`
--

INSERT INTO `miembro_equipo` (`id_miembro`, `nombre_miembro`, `correo_miembro`, `clave`, `rol_miembro`) VALUES
(10, 'María Alejandra Lavanderos Villagrán', 'malavanderos@example.com', '$2a$12$hse/tgNln0IV/IRUKlxnje7dproUuQ44mOYvhhNDenfFsyrrdA2Nu', 'Administrador'),
(11, 'Esteban Pardo', 'epardo@example.com', '$2a$12$HQHa/Y162evUDdHiFcnw3ec0Mt2lgz0Hg1CQI4I.n.aRM9Gz7I0XS', 'Investigador'),
(12, 'Paula Reyes', 'preyess@gmail.com', '$2a$12$bNXtNjj5YW3YwX4zKELNs.HBTsRcx50SbBCRPT0aGSRxqxAWDxEBy', 'Informatico'),
(15, 'Francisco Morales', 'francisco@genrisk.cl', '$2a$10$nNfWdJIgpkNwPZNDRSUibOLuaWXddybwqHG6JG/JXanR.Fle46wLa', 'Investigador'),
(16, 'César Aguirre', 'cesarAg@gmail.com', '$2a$10$xQgbpX8kLXPG5BkqtVB8yOCDezWyrjo1rfAGvanq2lBomUNN3Ctca', 'Reclutador'),
(17, 'Gonzalo Matus', 'matusgonzalo1544@gmail.com', '$2a$10$GtBEnZEA7aJsOqIZ491j1exv78WgEOgsO/6xDfa02VsSykEYUp5Yy', 'Informatico');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `muestra`
--

CREATE TABLE `muestra` (
  `paciente_id` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nro_muestra` int(11) NOT NULL,
  `tipo_muestra` varchar(255) NOT NULL,
  `fecha_muestra` date DEFAULT NULL,
  `estado_muestra` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `muestra`
--

INSERT INTO `muestra` (`paciente_id`, `nro_muestra`, `tipo_muestra`, `fecha_muestra`, `estado_muestra`) VALUES
('pac1', 1, 'ADN', '2025-10-01', 'Procesada'),
('pac1', 2, 'PCR', '2025-10-01', 'Pendiente'),
('pac2', 1, 'ADN', '2025-10-02', 'Procesada'),
('pac3', 1, 'PCR', '2025-10-01', 'Procesada'),
('pac4', 1, 'ADN', '2025-10-04', 'Pendiente'),
('pac5', 1, 'ADN', '2025-10-05', 'Procesada'),
('pac6', 1, 'PCR', '2025-10-03', 'Procesada'),
('pac7', 1, 'PCR', '2025-10-10', 'Procesada'),
('pac8', 1, 'ADN', '2025-10-11', 'Procesada');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

CREATE TABLE `paciente` (
  `id_paciente` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_paciente` varchar(255) DEFAULT NULL,
  `correo_paciente` varchar(255) DEFAULT NULL,
  `direccion_paciente` varchar(255) DEFAULT NULL,
  `tipo_paciente` varchar(255) DEFAULT NULL,
  `fecha_inclusion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`id_paciente`, `nombre_paciente`, `correo_paciente`, `direccion_paciente`, `tipo_paciente`, `fecha_inclusion`) VALUES
('CA0001', 'Paciente primero caso', 'evelyn@example.com', 'Av. O´higgins 1548', 'caso', '2025-03-02'),
('CA0002', 'Paciente segundo caso', 'evelyn@example.com', 'Av. O´higgins 1548', 'caso', NULL),
('CA0003', 'Juan Pérez (Prueba Caso)', 'juan.perez@test.cl', 'Calle Falsa 123', 'Caso', NULL),
('CA0004', 'Cristopher Arias', 'cristopher@example.com', 'Av. Ohiggins 1028', 'Caso', '2025-11-28'),
('CR0001', 'Evelyn Matus', 'evelyn@example.com', 'Av. O´higgins 1548', 'Control', NULL),
('CR0002', 'Paciente segundo control', 'evelyn@example.com', 'Av. O´higgins 1548', 'control', NULL),
('CR0003', 'Andrea Astorga', 'andrea@example.com', 'Av. Ohiggins 1028', 'Control', '2025-11-28'),
('CR0004', 'Alex Ortiz', 'alex@example.com', 'Los Copihues 458', 'Control', '2025-11-30'),
('pac1', 'Evelyn Matus', 'evelyn@example.com', 'Av. O´higgins 1548', 'Control', NULL),
('pac2', 'Margaret Subercaseaux', 'margaret@example.com', 'Av. Ecuador 799', 'Control', NULL),
('pac3', 'Elizabeth Hererra', 'elizabeth@example.com', 'Av. Brasil 1087', 'Caso', NULL),
('pac4', 'Isadora Matus De La Parra', 'isadora@example.com', 'Av. Collin 798', 'Caso', NULL),
('pac5', 'Michelle Carrere', 'michelle@example.com', 'Av. Argentina 1097', 'Caso', NULL),
('pac6', 'José María Matus', 'josemaria@example.com', 'Los Alerces 853', 'Control', NULL),
('pac7', 'Daniel Vega', 'daniel@example.com', 'Rosauro Acuña 255', 'Caso', NULL),
('pac8', 'Alexander Matus', 'alexander@example.com', 'Carrera 1096', 'Control', NULL),
('pac99', 'Paciente de Prueba Postman', 'pac99@correo.cl', 'Av. Postman 123', 'Control', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recuperacion_clave`
--

CREATE TABLE `recuperacion_clave` (
  `id_recuperacion` int(11) NOT NULL,
  `clave_momentanea` varchar(255) NOT NULL,
  `miembro_id` int(11) NOT NULL,
  `fecha_expiracion` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recuperacion_clave`
--

INSERT INTO `recuperacion_clave` (`id_recuperacion`, `clave_momentanea`, `miembro_id`, `fecha_expiracion`) VALUES
(1, '8b61a7c2-6126-4f63-926d-e2ef586fbdf5', 17, '2025-12-11 20:48:52'),
(8, 'd206a42c-d8b7-4816-8cb5-1ed2e1863896', 17, '2025-12-11 21:05:16'),
(9, '66fefcfc-b8ea-428b-83ce-7bba93db6a4a', 17, '2025-12-11 21:09:21'),
(10, '69a19ff7-ce64-47a7-b7d8-7eb7b0951e65', 17, '2025-12-11 21:12:18'),
(11, '29ec4dbc-5b9b-4a5e-a6ed-0db4ab115748', 17, '2025-12-11 21:18:14'),
(12, '33e20347-3bd8-4259-b3cb-3fcafc76afad', 17, '2025-12-11 21:27:56');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `datos_clinicos`
--
ALTER TABLE `datos_clinicos`
  ADD PRIMARY KEY (`itemformu`,`formulario_id`),
  ADD KEY `formulario_id` (`formulario_id`);

--
-- Indices de la tabla `datos_generales`
--
ALTER TABLE `datos_generales`
  ADD PRIMARY KEY (`itemformu`,`formulario_id`),
  ADD KEY `formulario_id` (`formulario_id`);

--
-- Indices de la tabla `dicot_conjunto`
--
ALTER TABLE `dicot_conjunto`
  ADD PRIMARY KEY (`id_dicotconjunto`);

--
-- Indices de la tabla `dicot_regla`
--
ALTER TABLE `dicot_regla`
  ADD PRIMARY KEY (`id_dicotregla`),
  ADD KEY `dicotconjunto_id` (`dicotconjunto_id`);

--
-- Indices de la tabla `dicot_valor`
--
ALTER TABLE `dicot_valor`
  ADD PRIMARY KEY (`id_dicotvalor`),
  ADD KEY `formulario_id` (`formulario_id`),
  ADD KEY `regladicot_id` (`regladicot_id`);

--
-- Indices de la tabla `fact_dietario_ambiental`
--
ALTER TABLE `fact_dietario_ambiental`
  ADD PRIMARY KEY (`itemformu`,`formulario_id`),
  ADD KEY `formulario_id` (`formulario_id`);

--
-- Indices de la tabla `formulario`
--
ALTER TABLE `formulario`
  ADD PRIMARY KEY (`id_formulario`),
  ADD KEY `fk_formulario_paciente` (`paciente_id`),
  ADD KEY `fk_formulario_miembro` (`miembro_id`);

--
-- Indices de la tabla `genotipificacion`
--
ALTER TABLE `genotipificacion`
  ADD PRIMARY KEY (`id_genotipificacion`),
  ADD KEY `fk_genotipificacion_muestra` (`paciente_id`,`muestra_nro`);

--
-- Indices de la tabla `habitos_paciente`
--
ALTER TABLE `habitos_paciente`
  ADD PRIMARY KEY (`itemformu`,`formulario_id`),
  ADD KEY `formulario_id` (`formulario_id`);

--
-- Indices de la tabla `histopatologia`
--
ALTER TABLE `histopatologia`
  ADD PRIMARY KEY (`formulario_id`,`itemformu`);

--
-- Indices de la tabla `ingresa_datos`
--
ALTER TABLE `ingresa_datos`
  ADD PRIMARY KEY (`id_ingreso`),
  ADD KEY `formulario_id` (`formulario_id`),
  ADD KEY `miembro_id` (`miembro_id`);

--
-- Indices de la tabla `miembro_equipo`
--
ALTER TABLE `miembro_equipo`
  ADD PRIMARY KEY (`id_miembro`);

--
-- Indices de la tabla `muestra`
--
ALTER TABLE `muestra`
  ADD PRIMARY KEY (`paciente_id`,`nro_muestra`);

--
-- Indices de la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`id_paciente`);

--
-- Indices de la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  ADD PRIMARY KEY (`id_recuperacion`),
  ADD UNIQUE KEY `clave_momentanea` (`clave_momentanea`),
  ADD KEY `ind_clave` (`clave_momentanea`),
  ADD KEY `ind_miembro` (`miembro_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `dicot_conjunto`
--
ALTER TABLE `dicot_conjunto`
  MODIFY `id_dicotconjunto` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `dicot_regla`
--
ALTER TABLE `dicot_regla`
  MODIFY `id_dicotregla` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT de la tabla `dicot_valor`
--
ALTER TABLE `dicot_valor`
  MODIFY `id_dicotvalor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=185;

--
-- AUTO_INCREMENT de la tabla `formulario`
--
ALTER TABLE `formulario`
  MODIFY `id_formulario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=219;

--
-- AUTO_INCREMENT de la tabla `genotipificacion`
--
ALTER TABLE `genotipificacion`
  MODIFY `id_genotipificacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `ingresa_datos`
--
ALTER TABLE `ingresa_datos`
  MODIFY `id_ingreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `miembro_equipo`
--
ALTER TABLE `miembro_equipo`
  MODIFY `id_miembro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  MODIFY `id_recuperacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `datos_clinicos`
--
ALTER TABLE `datos_clinicos`
  ADD CONSTRAINT `datos_clinicos_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `datos_generales`
--
ALTER TABLE `datos_generales`
  ADD CONSTRAINT `datos_generales_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `dicot_regla`
--
ALTER TABLE `dicot_regla`
  ADD CONSTRAINT `dicot_regla_ibfk_1` FOREIGN KEY (`dicotconjunto_id`) REFERENCES `dicot_conjunto` (`id_dicotconjunto`);

--
-- Filtros para la tabla `dicot_valor`
--
ALTER TABLE `dicot_valor`
  ADD CONSTRAINT `dicot_valor_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dicot_valor_ibfk_2` FOREIGN KEY (`regladicot_id`) REFERENCES `dicot_regla` (`id_dicotregla`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `fact_dietario_ambiental`
--
ALTER TABLE `fact_dietario_ambiental`
  ADD CONSTRAINT `fact_dietario_ambiental_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `formulario`
--
ALTER TABLE `formulario`
  ADD CONSTRAINT `fk_formulario_miembro` FOREIGN KEY (`miembro_id`) REFERENCES `miembro_equipo` (`id_miembro`),
  ADD CONSTRAINT `fk_formulario_paciente` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `genotipificacion`
--
ALTER TABLE `genotipificacion`
  ADD CONSTRAINT `fk_genotipificacion_muestra` FOREIGN KEY (`paciente_id`,`muestra_nro`) REFERENCES `muestra` (`paciente_id`, `nro_muestra`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `habitos_paciente`
--
ALTER TABLE `habitos_paciente`
  ADD CONSTRAINT `habitos_paciente_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `histopatologia`
--
ALTER TABLE `histopatologia`
  ADD CONSTRAINT `histopatologia_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ingresa_datos`
--
ALTER TABLE `ingresa_datos`
  ADD CONSTRAINT `ingresa_datos_ibfk_1` FOREIGN KEY (`formulario_id`) REFERENCES `formulario` (`id_formulario`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ingresa_datos_ibfk_2` FOREIGN KEY (`miembro_id`) REFERENCES `miembro_equipo` (`id_miembro`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `muestra`
--
ALTER TABLE `muestra`
  ADD CONSTRAINT `fk_muestra_paciente` FOREIGN KEY (`paciente_id`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `recuperacion_clave`
--
ALTER TABLE `recuperacion_clave`
  ADD CONSTRAINT `recuperacion_clave_ibfk_1` FOREIGN KEY (`miembro_id`) REFERENCES `miembro_equipo` (`id_miembro`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
