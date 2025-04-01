import * as React from "react";
import { useState, useEffect } from "react";
import { Loan } from "../../models/Loan";
import 'bootstrap/dist/css/bootstrap.min.css';

const PayLoan: React.FC = () => {
    const [loan, setLoan] = useState<Loan | null>(null);
    const [paymentAmount, setPaymentAmount] = useState<number>(0);

    useEffect(() => {
        const fetchActiveLoan = async () => {
            try {
                const response = await fetch(`http://localhost:8080/bank/activeloan/${localStorage.getItem("username")}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setLoan(data[0]);
                    console.log(data);
                } else {
                    console.error('Error fetching active loan');
                }
            } catch (error) {
                console.error('Error fetching active loan', error);
            }
        };

        fetchActiveLoan();
    }, []);

    const handlePayment = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!loan) return;

        const newAmount = loan.amount - paymentAmount;
        const updatedLoan = { ...loan, amount: newAmount, status: newAmount <= 0 };

        try {
            const response = await fetch(`http://localhost:8080/bank/loan/${loan.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
                body: JSON.stringify(updatedLoan),
            });

            if (!response.ok) {
                throw new Error('Error updating loan');
            }

            if (newAmount <= 0) {
                setLoan(null);
                alert("Deuda pagada completamente");
            } else {
                setLoan(updatedLoan);
                alert("Pago realizado correctamente");
            }
        } catch (error) {
            console.error('Error updating loan', error);
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Pagar Deuda</h1>
            {loan ? (
                <div>
                    <h2 className="mb-3">Deuda Activa</h2>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Monto</th>
                                <th>Fecha de Inicio</th>
                                <th>Fecha de Finalización</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{loan.id}</td>
                                <td>{loan.amount}€</td>
                                <td>{new Date(loan.startDate).toLocaleDateString()}</td>
                                <td>{new Date(loan.endDate).toLocaleDateString()}</td>
                            </tr>
                        </tbody>
                    </table>
                    <form onSubmit={handlePayment} className="mt-4">
                        <div className="mb-3">
                            <label className="form-label">Monto a Pagar</label>
                            <input
                                type="number"
                                className="form-control"
                                value={paymentAmount}
                                onChange={(e) => setPaymentAmount(Number(e.target.value))}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary">Pagar</button>
                    </form>
                </div>
            ) : (
                <p>No tienes deudas activas</p>
            )}
        </div>
    );
};

export default PayLoan;