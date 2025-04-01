import * as React from "react";
import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import useApiFetch from "../utils/useApiFetch.tsx";
import { useNavigate } from "react-router-dom";
import "bootstrap"


const Main: React.FC = () => {
    const [username, setUsername] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("jwtToken");
        if (token) {
            try {
                const decoded: any = jwtDecode(token);
                setUsername(decoded.sub);
            } catch (error) {
                console.error("Error al decodificar el token:", error);
            }
        }
    }, []);

    const { data, loading, error } = useApiFetch(
        `http://localhost:8080/bank/user/${localStorage.getItem("username")}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
            },
        }
    );

    if (loading) return <p>Cargando...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className="container mt-5">
            <div className="header mb-4">
                <h1>Bienvenido, {username}</h1>
                <img src="/Real_de_a_Occho-1759_reverso-trans-297x300.png" alt="Imagen de BANCO" className="img-thumbnail" />
            </div>
            <div className="content">
                <div className="data-box mb-4">
                    <h2>Datos</h2>
                    <ul className="list-group">
                        {data ? Object.entries(data).map(([key, value]) => (
                            <li key={key} className="list-group-item">{key}: {value}</li>
                        )) : <li className="list-group-item">No hay datos disponibles</li>}
                    </ul>
                </div>
                <div className="button-box">
                    <button className="btn btn-primary me-2" onClick={() => navigate("/operations")}>Operaciones</button>
                    <button className="btn btn-secondary me-2" onClick={() => navigate("/historial")}>Historial</button>
                    <button className="btn btn-success me-2" onClick={() => navigate("/addloan")}>Solicitar un Préstamo</button>
                    <button className="btn btn-secondary me-2" onClick={() => navigate(-1)}>Logout</button>
                </div>
            </div>
        </div>
    );
}

export default Main;