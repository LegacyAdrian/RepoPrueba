package c.practica_final_interfaz;

import Modelos.ApiService;
import Modelos.Producto;
import Modelos.RetrofitProduct;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import retrofit2.Call;

import Modelos.ApiService;
import Modelos.RetrofitClient;
import Modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javafx.application.Platform;

import java.util.List;

public class AdministrarView {

    @FXML
    private ComboBox<String> CatProducto;

    @FXML
    private TextArea DesProducto;

    @FXML
    private Label IdSeleccionada;

    @FXML
    private ListView<String> ListaProductos;


    @FXML
    private TextField NombreProducto;

    @FXML
    private TextField PriceProducto;
    @FXML
    public void initialize() {
       CatProducto.getItems().addAll("Consumibles","Muebles","Accesorios","Ropa","Tecnologia");
       cargarProductos();
    }
    private void cargarProductos() {
        ApiService productoservice = RetrofitProduct.getApiService();
        Call<List<Producto>> call = productoservice.obtenerProductos();


        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();
                    Platform.runLater(() -> {
                        // Limpiar la ListView y agregar los nombres de los productos
                        ListaProductos.getItems().clear();
                        for (Producto producto : productos) {
                            ListaProductos.getItems().add(producto.getName());
                        }
                    });
                } else {
                    Platform.runLater(() -> showAlert("Error", "No se pudieron cargar los productos.", Alert.AlertType.ERROR));
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "Error al conectar con el servidor.", Alert.AlertType.ERROR));
            }
        });
    }
    @FXML
    void CargarProductoSeleccionado(MouseEvent event) {
        String nombreSeleccionado = ListaProductos.getSelectionModel().getSelectedItem();

        if (nombreSeleccionado != null) {
            ApiService productoservice = RetrofitProduct.getApiService();

            Call<Producto> call = productoservice.obtenerProductoPorNombre(nombreSeleccionado);


            call.enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call, Response<Producto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Producto producto = response.body();
                        Platform.runLater(() -> {
                            IdSeleccionada.setText(producto.getId().toString());
                            NombreProducto.setText(producto.getName());
                            PriceProducto.setText(String.valueOf(producto.getPrice()));
                            DesProducto.setText(producto.getDescription());
                            CatProducto.getSelectionModel().select(producto.getCategory());
                        });
                    } else {
                        Platform.runLater(() -> showAlert("Error", "No se pudo cargar el producto seleccionado.", Alert.AlertType.ERROR));
                    }
                }

                @Override
                public void onFailure(Call<Producto> call, Throwable t) {
                    Platform.runLater(() -> showAlert("Error", "Error al conectar con el servidor.", Alert.AlertType.ERROR));
                }
            });
        }
    }

    @FXML
    void Eliminar(MouseEvent event) {
        String nombreSeleccionado = ListaProductos.getSelectionModel().getSelectedItem();

        if (nombreSeleccionado == null) {
            showAlert("Error", "Seleccione un producto para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        // Obtener el ID del producto seleccionado
        ApiService productoservice = RetrofitProduct.getApiService();
        Call<Producto> call = productoservice.obtenerProductoPorNombre(nombreSeleccionado);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Producto producto = response.body();

                    // Eliminar el producto
                    Call<Void> deleteCall = productoservice.deleteProducto(producto.getId());
                    deleteCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                showAlert("Eliminación", "Producto eliminado con éxito.", Alert.AlertType.INFORMATION);
                                cargarProductos();  // Recargar la lista de productos
                            } else {
                                showAlert("Error", "No se pudo eliminar el producto.", Alert.AlertType.ERROR);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            showAlert("Error", "Error al conectar con el servidor.", Alert.AlertType.ERROR);
                        }
                    });
                } else {
                    showAlert("Error", "No se pudo cargar el producto seleccionado.", Alert.AlertType.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                showAlert("Error", "Error al conectar con el servidor.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    void Modificar(MouseEvent event) {
        String nombre = NombreProducto.getText();
        String precio = PriceProducto.getText();
        String descripcion = DesProducto.getText();
        String categoria = CatProducto.getSelectionModel().getSelectedItem();
        String idProducto = IdSeleccionada.getText();  // El ID del producto seleccionado

        if (nombre.isEmpty() || precio.isEmpty() || descripcion.isEmpty() || categoria.isEmpty()) {
            showAlert("Error", "Rellene todos los campos", Alert.AlertType.ERROR);
            return;
        } else if (!precio.matches("\\d+")) {
            showAlert("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        // Crear el objeto Producto con los nuevos datos
        Producto producto = new Producto(Integer.parseInt(idProducto),nombre, Integer.parseInt(precio), descripcion, categoria);

        ApiService productoservice = RetrofitProduct.getApiService();
        // Llamada a la API para actualizar el producto
        Call<Producto> call = productoservice.actualizarProducto(producto);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful()) {
                    showAlert("Modificación", "Producto modificado con éxito.", Alert.AlertType.INFORMATION);
                    cargarProductos();  // Recargar la lista de productos
                } else {
                    showAlert("Error", "No se pudo modificar el producto.", Alert.AlertType.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                showAlert("Error", "Error al conectar con el servidor.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    void Registrar(MouseEvent event) {
    String nombre = NombreProducto.getText();
    String precio = PriceProducto.getText();
    String descripcion = DesProducto.getText();
    String categoria = CatProducto.getSelectionModel().getSelectedItem();

    if (nombre.isEmpty()||precio.isEmpty()||descripcion.isEmpty()||categoria.isEmpty()) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText("Rellene todos los campos");
        alerta.showAndWait();
        return;
    }
    else if (!precio.matches("\\d+")) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText("El precio debe ser un número válido.");
        alerta.showAndWait();
        return;
    }
    else {
        Producto p = new Producto(nombre,Integer.parseInt(precio),descripcion,categoria);

        ApiService productoservice = RetrofitProduct.getApiService();
        //Llamada
        Call<Producto> call = productoservice.crearProducto(p);
        // Respuesta
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Registro", "Producto registrado con exito", Alert.AlertType.INFORMATION);
                        cargarProductos();
                    } else {
                        showAlert("Error", "No se pudo registrar el producto.", Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "Ocurrió un error en la conexión.", Alert.AlertType.ERROR));
            }
        });
   }
    }

    @FXML
    void Salir(MouseEvent event) {
        System.exit(0);
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


}
