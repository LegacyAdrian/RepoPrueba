package c.practica_final_interfaz;

import Modelos.ApiService;
import Modelos.Producto;
import Modelos.RetrofitProduct;
import Modelos.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.net.ftp.FTPFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import javafx.scene.control.SelectionMode;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComprarView {

    @FXML
    private ListView<String> ListaProductos; // Lista de productos

    static Usuario useractual = new Usuario();

    @FXML
    public void initialize() {
        cargarProductos();
        ListaProductos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static void recibirUsuario(Usuario u) {
        useractual = u;
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
                        ListaProductos.getItems().clear();
                        for (Producto producto : productos) {
                            ListaProductos.getItems().add(producto.getName() );
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
    }

    @FXML
    void Registrar(MouseEvent event) {

        String fechaCompra = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double precioTotal = 0;


        for (String nombreProducto : ListaProductos.getSelectionModel().getSelectedItems()) {
            precioTotal += obtenerPrecioProducto(nombreProducto);
        }

        // Crear el registro de compra
        String registroCompra = "Fecha: " + fechaCompra + ", Productos comprados: " + ListaProductos.getSelectionModel().getSelectedItems().size() + ", Precio total: " + precioTotal + "€\n";

        // Conectar al servidor FTP
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(5000);
            ftpClient.setDataTimeout(5000);

            ftpClient.connect("localhost", 21);

            boolean login = ftpClient.login("user", "user");

            if (login) {
                ftpClient.changeWorkingDirectory("/compras");

                String nombreArchivo = useractual.getUser() + "_compras.txt";
                boolean archivoExiste = false;
                FTPFile[] archivos = ftpClient.listFiles();

                for (FTPFile archivo : archivos) {
                    if (archivo.getName().equals(nombreArchivo)) {
                        archivoExiste = true;
                        break;
                    }
                }

                if (archivoExiste) {
                    // Descargar el archivo
                    InputStream inputStream = ftpClient.retrieveFileStream(nombreArchivo);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder contenidoArchivo = new StringBuilder();
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        contenidoArchivo.append(linea).append("\n");
                    }
                    reader.close();

                    // Agregar el nuevo registro
                    contenidoArchivo.append(registroCompra);

                    // Subir el archivo
                    FileOutputStream outputStream = new FileOutputStream("archivo_temp.txt");
                    outputStream.write(contenidoArchivo.toString().getBytes());
                    outputStream.close();

                    FileInputStream fileInputStream = new FileInputStream("archivo_temp.txt");
                    ftpClient.storeFile(nombreArchivo, fileInputStream);
                    fileInputStream.close();

                    // Eliminar archivo temporal
                    new File("archivo_temp.txt").delete();
                } else {
                    // Si el archivo no existe, crear uno nuevo
                    FileWriter writer = new FileWriter("archivo_temp.txt");
                    writer.write(registroCompra);
                    writer.close();

                    FileInputStream fileInputStream = new FileInputStream("archivo_temp.txt");
                    ftpClient.storeFile(nombreArchivo, fileInputStream);
                    fileInputStream.close();

                    // Eliminar archivo temporal
                    new File("archivo_temp.txt").delete();
                }

                // Cerrar la conexión
                ftpClient.logout();
                ftpClient.disconnect();

                showAlert("Compra Registrada", "La compra ha sido registrada con éxito.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "No se pudo autenticar en el servidor FTP.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("Error", "Ocurrió un error al registrar la compra.", Alert.AlertType.ERROR);
            e.printStackTrace();
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

    private double obtenerPrecioProducto(String nombreProducto) {
        ApiService productoservice = RetrofitProduct.getApiService();
        Call<Producto> call = productoservice.obtenerProductoPorNombre(nombreProducto);

        try {
            Response<Producto> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Producto producto = response.body();
                return producto.getPrice();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
