import * as React from "react";
import "../css/mainstyles.css";
import { useNavigate } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';

const Operation: React.FC = () => {
    const navigate = useNavigate();

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Operaciones</h1>
            <div className="operation-container">
                <button className="btn btn-primary me-2" onClick={() => navigate("/operations/add")}>Agregar saldo</button>
                <button className="btn btn-secondary me-2" onClick={() => {navigate("/operations/pay")}}>Hacer Transferencia</button>
                <button className="btn btn-success me-2" onClick={() => {navigate("/operations/payloan")}}>Efectuar Pago</button>
                <button className="btn btn-danger" onClick={() => navigate(-1)}>Volver</button>
            </div>
        </div>
    );
}

export default Operation;