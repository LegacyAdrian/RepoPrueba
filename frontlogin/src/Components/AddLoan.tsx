import * as React from "react";
import { useState } from "react";
import { Loan } from "../models/Loan";
import 'bootstrap/dist/css/bootstrap.min.css';

const AddLoan: React.FC = () => {
    const [loanData, setLoanData] = useState<Loan>({
        amount: 0,
        startDate: new Date(),
        endDate: new Date(),
        status: false,
        user: {
            name: "",
            password: ""
        },
    });
    const [showForm, setShowForm] = useState<boolean>(false);

    const handleAddLoan = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/bank/loan', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
                body: JSON.stringify({
                    amount: loanData.amount,
                    startDate: loanData.startDate.toISOString(),
                    endDate: loanData.endDate.toISOString(),
                    status: loanData.status,
                    user: { name: localStorage.getItem("username") }
                }),
            });

            if (!response.ok) {
                throw new Error('Error al añadir la deuda');
            }

            alert("Deuda agregada correctamente");
            setShowForm(false);
            setLoanData({
                amount: 0,
                startDate: new Date(),
                endDate: new Date(),
                status: false,
                user: {
                    name: "",
                    password: ""
                },
            });
        } catch (error) {
            alert("Actualmente tienes una deuda sin pagar");
            console.error('Actualmente tienes una deuda sin pagar', error);
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Agregar Deuda</h1>
            <button className="btn btn-primary mb-4" onClick={() => setShowForm(true)}>Agregar nueva deuda</button>
            {showForm && (
                <div className="card">
                    <div className="card-body">
                        <form onSubmit={handleAddLoan}>
                            <div className="mb-3">
                                <label className="form-label">Cantidad</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    value={loanData.amount}
                                    onChange={(e) => setLoanData({ ...loanData, amount: Number(e.target.value) })}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Fecha de Inicio</label>
                                <input
                                    type="date"
                                    className="form-control"
                                    value={loanData.startDate.toISOString().split('T')[0]}
                                    onChange={(e) => setLoanData({ ...loanData, startDate: new Date(e.target.value) })}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Fecha de Finalización</label>
                                <input
                                    type="date"
                                    className="form-control"
                                    value={loanData.endDate.toISOString().split('T')[0]}
                                    onChange={(e) => setLoanData({ ...loanData, endDate: new Date(e.target.value) })}
                                    required
                                />
                            </div>
                            <div className="mb-3 form-check">
                                <input
                                    type="checkbox"
                                    className="form-check-input"
                                    checked={loanData.status}
                                    onChange={(e) => setLoanData({ ...loanData, status: e.target.checked })}
                                />
                                <label className="form-check-label">Pagado</label>
                            </div>
                            <button type="submit" className="btn btn-primary">Añadir</button>
                        </form>
                        <button className="btn btn-secondary mt-2" onClick={() => setShowForm(false)}>Cancelar</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AddLoan;