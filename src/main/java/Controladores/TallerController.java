package Controladores;

import BDD.OrdenDAO;
import modelo.Orden;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.Notifications;
import utils.WhatsApp;
import java.time.LocalDate;
import java.util.Optional;

public class TallerController {

    @FXML
    private TableView<Orden> tablaOrdenes;
    @FXML
    private TableColumn<Orden, Integer> colId;
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
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEquipo;
    @FXML
    private DatePicker dateFechaIngreso;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private TextField txtPresupuesto;
    @FXML
    private TextArea txtFalla;

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

    private OrdenDAO ordenDAO;
    private ObservableList<Orden> listaOrdenesMaster;
    private FilteredList<Orden> listaOrdenesFiltrada;
    private Orden ordenSeleccionada;

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
        onNuevoClick();
    }

    private void configurarTabla() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        this.colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        this.colEquipo.setCellValueFactory(new PropertyValueFactory<>("equipo"));
        this.colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));

        tablaOrdenes.setItems(listaOrdenesFiltrada);
        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDatosOrden(newValue)
        );
    }

    private void configurarFiltroBusqueda() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaOrdenesFiltrada.setPredicate(orden -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (orden.getNombreCliente().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orden.getApellidoCliente() != null && orden.getApellidoCliente().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orden.getEquipo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (orden.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(orden.getId()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
    }

    private void poblarComboBoxes() {
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Recibido", "Diagnosticando", "Esperando Repuesto", "Listo para Retirar", "Entregado"
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
            txtNombre.setText(orden.getNombreCliente());
            txtApellido.setText(orden.getApellidoCliente());
            txtTelefono.setText(orden.getTelefono());
            txtEquipo.setText(orden.getEquipo());
            cmbEstado.setValue(orden.getEstado());
            txtFalla.setText(orden.getFallaReportada());
            txtPresupuesto.setText(String.valueOf(orden.getPresupuesto()));
            if (orden.getFechaIngreso() != null && !orden.getFechaIngreso().isEmpty()) {
                dateFechaIngreso.setValue(LocalDate.parse(orden.getFechaIngreso()));
            } else {
                dateFechaIngreso.setValue(LocalDate.now());
            }
        }
    }

    @FXML
    private void onGuardarClick() {
        double presupuesto = 0.0;
        try {
            presupuesto = Double.parseDouble(txtPresupuesto.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            presupuesto = 0.0;
        }

        if (ordenSeleccionada == null) {
            Orden nuevaOrden = new Orden();
            llenarOrdenDesdeFormulario(nuevaOrden);
            ordenDAO.agregarOrden(nuevaOrden);
            Notifications.create().title("Éxito").text("Nueva orden guardada.").showInformation();
        } else {
            llenarOrdenDesdeFormulario(ordenSeleccionada);
            ordenDAO.actualizarOrden(ordenSeleccionada);
            Notifications.create().title("Éxito").text("Orden " + ordenSeleccionada.getId() + " actualizada.").showInformation();
        }

        refrescarTabla();
        onNuevoClick();
    }

    private void llenarOrdenDesdeFormulario(Orden orden) {
        orden.setNombreCliente(txtNombre.getText());
        orden.setApellidoCliente(txtApellido.getText());
        orden.setTelefono(txtTelefono.getText());
        orden.setEquipo(txtEquipo.getText());
        orden.setFallaReportada(txtFalla.getText());
        orden.setEstado(cmbEstado.getValue());
        orden.setPresupuesto(Double.parseDouble(txtPresupuesto.getText().isEmpty() ? "0" : txtPresupuesto.getText()));
        if (dateFechaIngreso.getValue() != null) {
            orden.setFechaIngreso(dateFechaIngreso.getValue().toString());
        } else {
            orden.setFechaIngreso(LocalDate.now().toString());
        }
    }

    @FXML
    private void onNuevoClick() {
        ordenSeleccionada = null;
        limpiarCampos();
        tablaOrdenes.getSelectionModel().clearSelection();
        dateFechaIngreso.setValue(LocalDate.now());
        cmbEstado.setValue("Recibido");
        txtNombre.requestFocus();
    }

    @FXML
    private void onEliminarClick() {
        if (ordenSeleccionada == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("Eliminar Orden N° " + ordenSeleccionada.getId());
        alert.setContentText("¿Estás seguro que querés eliminar la orden de " + ordenSeleccionada.getNombreCliente() + "?");

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
                mensaje = "¡Hola " + nombre + "! Te informamos que el presupuesto para la reparación de tu " + ordenSeleccionada.getEquipo() + " es de $" + ordenSeleccionada.getPresupuesto() + ". Esperamos tu confirmación.";
                break;
            case "Aviso de Retiro":
                mensaje = "¡Hola " + nombre + "! Te avisamos que tu " + ordenSeleccionada.getEquipo() + " ya está reparado y listo para retirar.";
                break;
            case "Consulta sobre Falla":
                mensaje = "¡Hola " + nombre + "! Necesitamos hacerte una consulta sobre la falla de tu " + ordenSeleccionada.getEquipo() + ". Por favor, comunicate con nosotros.";
                break;
            default:
                mensaje = "¡Hola " + nombre + "!";
        }

        WhatsApp.enviarMensaje(ordenSeleccionada.getTelefono(), mensaje);
    }

    private void refrescarTabla() {
        listaOrdenesMaster.clear();
        listaOrdenesMaster.addAll(ordenDAO.getTodasLasOrdenes());
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEquipo.clear();
        txtFalla.clear();
        txtPresupuesto.clear();
        dateFechaIngreso.setValue(null);
        cmbEstado.setValue(null);
    }
}