import * as React from "react";
import useApiFetch from "../utils/useApiFetch.tsx";
import { Income } from "../models/Income.ts";
import { Loan } from "../models/Loan.ts";
import "bootstrap"
import {useNavigate} from "react-router-dom";

const Historial: React.FC = () => {
    const navigate = useNavigate();
    const { data: incomes, loading: loadingIncomes, error: errorIncomes } = useApiFetch<Income[]>(
        `http://localhost:8080/bank/income/${localStorage.getItem("username")}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
            },
        }
    );

    const { data: loans, loading: loadingLoans, error: errorLoans } = useApiFetch<Loan[]>(
        `http://localhost:8080/bank/loan/${localStorage.getItem("username")}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
            },
        }
    );

    if (loadingIncomes || loadingLoans) return <p>Cargando...</p>;
    if (errorIncomes) return <p>Error al cargar ingresos: {errorIncomes}</p>;
    if (errorLoans) return <p>Error al cargar préstamos: {errorLoans}</p>;

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Historial</h1>
            <h2 className="mb-3">Ingresos</h2>
            <table className="table table-striped table-bordered border-dark">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Monto</th>
                        <th>Fecha de Pago</th>
                    </tr>
                </thead>
                <tbody>
                    {incomes && incomes.map((income) => (
                        <tr key={income.id}>
                            <td>{income.id}</td>
                            <td>{income.amount}€</td>
                            <td>{new Date(income.payment_date).toLocaleDateString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <h2 className="mt-4 mb-3">Préstamos</h2>
            <table className="table table-striped table-bordered border-dark">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Monto</th>
                        <th>Fecha de Inicio</th>
                        <th>Fecha de Finalizacion</th>
                        <th>Pagado</th>
                    </tr>
                </thead>
                <tbody>
                    {loans && loans.map((loan) => (
                        <tr key={loan.id}>
                            <td>{loan.id}</td>
                            <td>{loan.amount}€</td>
                            <td>{new Date(loan.startDate).toLocaleDateString()}</td>
                            <td>{new Date(loan.endDate).toLocaleDateString()}</td>
                            <td>{loan.status ? "Sí" : "No"}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button className="btn btn-danger" onClick={() => navigate(-1)}>Volver</button>
        </div>
    );
};

export default Historial;