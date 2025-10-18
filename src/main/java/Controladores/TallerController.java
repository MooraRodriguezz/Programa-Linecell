package Controladores;

import BDD.OrdenDAO;
import modelo.HistorialEstado;
import modelo.Orden;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.Notifications;
import utils.WhatsApp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TallerController {

    @FXML
    private TableView<Orden> tablaOrdenes;
    @FXML
    private TableColumn<Orden, Integer> colId;
    @FXML
    private TableColumn<Orden, String> colNumOrden;
    @FXML
    private TableColumn<Orden, String> colNombre;
    @FXML
    private TableColumn<Orden, String> colTelefono;
    @FXML
    private TableColumn<Orden, String> colEquipo;
    @FXML
    private TableColumn<Orden, String> colEstado;
    @FXML
    private TableColumn<Orden, String> colFecha;

    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cmbFiltro;

    @FXML
    private TextField txtNumeroOrden;
    @FXML
    private DatePicker dateFechaIngreso;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<String> cmbTipoEquipo;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtNumeroSerie;
    @FXML
    private TextArea txtFalla;
    @FXML
    private TextArea txtObsPublicas;
    @FXML
    private TextArea txtObsPrivadas;
    @FXML
    private TextField txtPresupuesto;
    @FXML
    private TextField txtImporteFinal;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;

    @FXML
    private ComboBox<String> cmbPlantillasWsp;
    @FXML
    private Button btnWhatsApp;
    @FXML
    private ListView<HistorialEstado> listHistorial;

    private OrdenDAO ordenDAO;
    private ObservableList<Orden> listaOrdenesMaster;
    private FilteredList<Orden> listaOrdenesFiltrada;
    private Orden ordenSeleccionada;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        ordenDAO = new OrdenDAO();
        listaOrdenesMaster = FXCollections.observableArrayList();
        listaOrdenesFiltrada = new FilteredList<>(listaOrdenesMaster, p -> true);

        configurarTabla();
        configurarFiltroBusqueda();
        poblarComboBoxes();
        enlazarEstadosBotones();

        refrescarTabla();
        Platform.runLater(this::onNuevoClick);
    }

    private void configurarTabla() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNumOrden.setCellValueFactory(new PropertyValueFactory<>("numeroOrden"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        this.colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colEquipo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        this.colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoActual"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        tablaOrdenes.setItems(listaOrdenesFiltrada);
        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDatosOrden(newValue)
        );
    }

    private void configurarFiltroBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltro());
        cmbFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltro());
    }

    private void aplicarFiltro() {
        String filtroSeleccionado = cmbFiltro.getValue();
        String textoBusqueda = txtBuscar.getText();

        listaOrdenesFiltrada.setPredicate(orden -> {
            if (textoBusqueda == null || textoBusqueda.isEmpty() || filtroSeleccionado == null) {
                return true;
            }
            String lowerCaseFilter = textoBusqueda.toLowerCase();

            switch (filtroSeleccionado) {
                case "Nombre o Apellido":
                    String nombre = orden.getNombreCliente().toLowerCase();
                    String apellido = (orden.getApellidoCliente() != null) ? orden.getApellidoCliente().toLowerCase() : "";
                    String nombreCompleto = nombre + " " + apellido;

                    return (nombre.contains(lowerCaseFilter) ||
                            apellido.contains(lowerCaseFilter) ||
                            nombreCompleto.contains(lowerCaseFilter));
                case "Teléfono":
                    return orden.getTelefono().toLowerCase().contains(lowerCaseFilter);
                case "N° de Orden":
                    return (orden.getNumeroOrden() != null && orden.getNumeroOrden().toLowerCase().contains(lowerCaseFilter));
                default:
                    return true;
            }
        });
    }

    private void poblarComboBoxes() {
        cmbFiltro.setItems(FXCollections.observableArrayList(
                "Nombre o Apellido", "Teléfono", "N° de Orden"
        ));
        cmbFiltro.setValue("Nombre o Apellido");

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Ingresado", "Presupuestado", "Aprobado", "Reparado", "Entregado"
        ));
        cmbTipoEquipo.setItems(FXCollections.observableArrayList(
                "Celular", "Tablet", "Notebook", "PC de Escritorio", "Otro"
        ));
        cmbPlantillasWsp.setItems(FXCollections.observableArrayList(
                "Aviso de Presupuesto", "Aviso de Retiro", "Consulta sobre Falla"
        ));
    }

    private void enlazarEstadosBotones() {
        btnEliminar.disableProperty().bind(tablaOrdenes.getSelectionModel().selectedItemProperty().isNull());
        btnWhatsApp.disableProperty().bind(tablaOrdenes.getSelectionModel().selectedItemProperty().isNull());
    }

    private void mostrarDatosOrden(Orden orden) {
        if (orden != null) {
            ordenSeleccionada = orden;
            txtNumeroOrden.setText(orden.getNumeroOrden());
            dateFechaIngreso.setValue(LocalDate.parse(orden.getFechaIngreso(), formatter));
            cmbEstado.setValue(orden.getEstadoActual());
            txtNombre.setText(orden.getNombreCliente());
            txtApellido.setText(orden.getApellidoCliente());
            txtTelefono.setText(orden.getTelefono());
            cmbTipoEquipo.setValue(orden.getTipoEquipo());
            txtMarca.setText(orden.getMarca());
            txtModelo.setText(orden.getModelo());
            txtNumeroSerie.setText(orden.getNumeroSerie());
            txtFalla.setText(orden.getFallaReportada());
            txtObsPublicas.setText(orden.getObservacionesPublicas());
            txtObsPrivadas.setText(orden.getObservacionesPrivadas());
            txtPresupuesto.setText(String.valueOf(orden.getPresupuesto()));
            txtImporteFinal.setText(String.valueOf(orden.getImporteFinal()));

            refrescarHistorial(orden.getId());
        } else {
            limpiarCampos();
        }
    }

    private void refrescarHistorial(int ordenId) {
        listHistorial.setItems(FXCollections.observableArrayList(ordenDAO.getHistorialDeOrden(ordenId)));
    }

    @FXML
    private void onGuardarClick() {
        String estadoNuevo = cmbEstado.getValue();
        String fechaHoy = LocalDate.now().format(formatter);

        if (ordenSeleccionada == null) {
            Orden nuevaOrden = new Orden();
            llenarOrdenDesdeFormulario(nuevaOrden);
            nuevaOrden.setFechaIngreso(fechaHoy);
            nuevaOrden.setEstadoActual(estadoNuevo);

            ordenDAO.agregarOrden(nuevaOrden);
            Notifications.create().title("Éxito").text("Nueva orden guardada.").showInformation();

            refrescarTabla();
            onNuevoClick();
        } else {
            String estadoViejo = ordenSeleccionada.getEstadoActual();
            llenarOrdenDesdeFormulario(ordenSeleccionada);

            ordenDAO.actualizarOrden(ordenSeleccionada);

            if (estadoViejo != null && !estadoViejo.equals(estadoNuevo)) {
                ordenDAO.agregarEstadoHistorial(ordenSeleccionada.getId(), estadoNuevo, fechaHoy);
            }
            Notifications.create().title("Éxito").text("Orden " + ordenSeleccionada.getId() + " actualizada.").showInformation();

            refrescarTabla();
        }
    }

    private void llenarOrdenDesdeFormulario(Orden orden) {
        orden.setNumeroOrden(txtNumeroOrden.getText());
        orden.setNombreCliente(txtNombre.getText());
        orden.setApellidoCliente(txtApellido.getText());
        orden.setTelefono(txtTelefono.getText());
        orden.setTipoEquipo(cmbTipoEquipo.getValue());
        orden.setMarca(txtMarca.getText());
        orden.setModelo(txtModelo.getText());
        orden.setNumeroSerie(txtNumeroSerie.getText());
        orden.setFallaReportada(txtFalla.getText());
        orden.setObservacionesPublicas(txtObsPublicas.getText());
        orden.setObservacionesPrivadas(txtObsPrivadas.getText());
        orden.setPresupuesto(parseDouble(txtPresupuesto.getText()));
        orden.setImporteFinal(parseDouble(txtImporteFinal.getText()));
        orden.setEstadoActual(cmbEstado.getValue());
        if (dateFechaIngreso.getValue() != null) {
            orden.setFechaIngreso(dateFechaIngreso.getValue().format(formatter));
        } else {
            orden.setFechaIngreso(LocalDate.now().format(formatter));
        }
    }

    @FXML
    private void onNuevoClick() {
        ordenSeleccionada = null;
        limpiarCampos();
        tablaOrdenes.getSelectionModel().clearSelection();
        dateFechaIngreso.setValue(LocalDate.now());
        cmbEstado.setValue("Ingresado");
        cmbTipoEquipo.setValue(null);
        txtNombre.requestFocus();
    }

    @FXML
    private void onEliminarClick() {
        if (ordenSeleccionada == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("Eliminar Orden N° " + ordenSeleccionada.getNumeroOrden());
        alert.setContentText("¿Estás seguro? Esta acción no se puede deshacer y eliminará todo el historial de la orden.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ordenDAO.eliminarOrden(ordenSeleccionada.getId());
            Notifications.create().title("Éxito").text("Orden eliminada.").showInformation();
            refrescarTabla();
            onNuevoClick();
        }
    }

    @FXML
    private void onWhatsAppClick() {
        if (ordenSeleccionada == null || cmbPlantillasWsp.getValue() == null) {
            Notifications.create().title("Error").text("Seleccioná una orden y una plantilla de mensaje.").showWarning();
            return;
        }

        String plantilla = cmbPlantillasWsp.getValue();
        String mensaje = "";
        String nombre = ordenSeleccionada.getNombreCliente();

        switch (plantilla) {
            case "Aviso de Presupuesto":
                mensaje = "¡Hola " + nombre + "! Te informamos que el presupuesto para la reparación de tu " + ordenSeleccionada.getModelo() + " es de $" + ordenSeleccionada.getPresupuesto() + ". " + txtObsPublicas.getText();
                break;
            case "Aviso de Retiro":
                mensaje = "¡Hola " + nombre + "! Te avisamos que tu " + ordenSeleccionada.getModelo() + " ya está reparado y listo para retirar. El importe final es de $" + ordenSeleccionada.getImporteFinal() + ".";
                break;
            case "Consulta sobre Falla":
                mensaje = "¡Hola " + nombre + "! Necesitamos hacerte una consulta sobre la falla de tu " + ordenSeleccionada.getModelo() + ". " + txtObsPublicas.getText();
                break;
            default:
                mensaje = "¡Hola " + nombre + "!";
        }

        WhatsApp.enviarMensaje(ordenSeleccionada.getTelefono(), mensaje);
    }

    private void refrescarTabla() {
        int selectedId = -1;
        if (ordenSeleccionada != null) {
            selectedId = ordenSeleccionada.getId();
        }

        listaOrdenesMaster.clear();
        listaOrdenesMaster.addAll(ordenDAO.getTodasLasOrdenes());

        final int finalSelectedId = selectedId;

        if (finalSelectedId != -1) {
            Optional<Orden> ordenParaReseleccionar = listaOrdenesFiltrada.stream()
                    .filter(o -> o.getId() == finalSelectedId)
                    .findFirst();

            if (ordenParaReseleccionar.isPresent()) {
                tablaOrdenes.getSelectionModel().select(ordenParaReseleccionar.get());
                tablaOrdenes.scrollTo(ordenParaReseleccionar.get());
            } else {
                onNuevoClick();
            }
        }
    }


    private void limpiarCampos() {
        txtNumeroOrden.clear();
        dateFechaIngreso.setValue(null);
        cmbEstado.setValue(null);
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        cmbTipoEquipo.setValue(null);
        txtMarca.clear();
        txtModelo.clear();
        txtNumeroSerie.clear();
        txtFalla.clear();
        txtObsPublicas.clear();
        txtObsPrivadas.clear();
        txtPresupuesto.clear();
        txtImporteFinal.clear();
        listHistorial.setItems(null);
    }

    private double parseDouble(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", "."));
        } catch (NumberFormatException | NullPointerException e) {
            return 0.0;
        }
    }
}