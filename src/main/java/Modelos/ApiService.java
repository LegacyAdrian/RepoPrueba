package Modelos;


import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface ApiService {


    // Endpoint para registrar un usuario
    @POST("/usuario")
    Call<Usuario> register(@Body Usuario usuario);

    // Endpoint para obtener un usuario
    @GET("/usuario/{id}")
    Call<Usuario> findById(@Path("id") Integer id);

    // Endpoint para iniciar sesión con nombre de usuario y contraseña
    @POST("/login")
    Call<Map<String, Object>> iniciarSesion(@Body Usuario usuario);

    @POST("/producto")
    Call<Producto> crearProducto(@Body Producto producto);

    @GET("/productos")
    Call<List<Producto>> obtenerProductos();

    @GET("/productos/{nombre}")
    Call<Producto> obtenerProductoPorNombre(@Path("nombre") String nombre);

    @PUT("/producto")
    Call<Producto> actualizarProducto(@Body Producto producto);

    @DELETE("producto/{id}")
    Call<Void> deleteProducto(@Path("id") Integer id);




}
