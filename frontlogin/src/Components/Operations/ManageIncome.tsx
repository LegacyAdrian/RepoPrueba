import * as React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Income } from "../../models/Income";
import 'bootstrap/dist/css/bootstrap.min.css';

const ManageBalances: React.FC = () => {
    const [balances, setBalances] = useState<Income[]>([]);
    const [selectedBalance, setSelectedBalance] = useState<Income | null>(null);
    const [inputData, setInputData] = useState<Income>({
        amount: 0,
        payment_date: new Date(),
        user: {
            name: "",
            password: ""
        },
    });
    const [showForm, setShowForm] = useState<boolean>(false);
    const [formType, setFormType] = useState<string>("");
    const navigate = useNavigate();

    const username = localStorage.getItem("username");

    useEffect(() => {
        if (username) {
            const fetchBalances = async () => {
                try {
                    const response = await fetch(`http://localhost:8080/bank/income/${username}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                        },
                    });

                    if (response.ok) {
                        const data = await response.json();
                        setBalances(data);
                    } else {
                        console.error('Error fetching balances');
                    }
                } catch (error) {
                    console.error('Error fetching balances', error);
                }
            };

            fetchBalances();
        }
    }, [username]);

    const handleAddBalance = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/bank/income', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
                body: JSON.stringify({
                    amount: inputData.amount,
                    payment_date: inputData.payment_date.toISOString(),
                    user: { name: username }
                }),

            });

            if (!response.ok) {
                console.error(inputData);
                throw new Error('Error al añadir saldo');
            }

            const newBalance: Income = await response.json();
            setBalances([...balances, newBalance]);
            setShowForm(false);
            setInputData({ amount: 0, payment_date: new Date(), user: { name: "", password: "" } });
            alert("Saldo Agregado Correctamente")
        } catch (error) {
            console.error('Error al añadir saldo', error);
        }
    };

    const handleUpdateBalance = async (e: React.FormEvent) => {
        e.preventDefault();
        if (selectedBalance === null) return;

        try {
            const response = await fetch(`http://localhost:8080/bank/income/${selectedBalance.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
                body: JSON.stringify({
                    amount: inputData.amount,
                    payment_date: inputData.payment_date.toISOString(),
                    user: { name: username }
                }),
            });

            if (!response.ok) {
                throw new Error('Error al modificar saldo');
            }

            const updatedBalance: Income = await response.json();
            setBalances(balances.map(balance => balance.id === selectedBalance.id ? updatedBalance : balance));
            setShowForm(false);
            setInputData({ amount: 0, payment_date: new Date(), user: { name: "", password: "" } });
            alert("Saldo Actualizado Correctamente")
        } catch (error) {
            console.error('Error al modificar saldo', error);
        }
    };

    const handleDeleteBalance = async () => {
        if (selectedBalance === null) return;

        try {
            const response = await fetch(`http://localhost:8080/bank/income/${selectedBalance.id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
            });

            if (!response.ok) {
                throw new Error('Error al eliminar saldo');
            }
            setBalances(balances.filter(balance => balance.id !== selectedBalance.id));
            setSelectedBalance(null);
            alert("Saldo Modificado Correctamente")
        } catch (error) {
            console.error('Error al eliminar saldo', error);
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Gestionar Saldos</h1>
            <div className="balance-list mb-4">
                <h2>Mis Saldos</h2>
                <ul className="list-group">
                    {balances.map((balance, index) => (
                        <li
                            key={index}
                            onClick={() => setSelectedBalance(balance)}
                            className={`list-group-item ${selectedBalance === balance ? "active" : ""}`}
                        >
                            ID: {balance.id} / Monto: {balance.amount}€ / Fecha de pago: {balance.payment_date ? new Date(balance.payment_date).toLocaleDateString() : "Fecha no disponible"}
                        </li>
                    ))}
                </ul>
            </div>
            <div className="actions mb-4">
                {!showForm && (
                    <>
                        <button className="btn btn-primary me-2" onClick={() => { setShowForm(true); setFormType("add"); }}>Agregar nuevo saldo</button>
                        <button className="btn btn-secondary me-2" onClick={() => { setShowForm(true); setFormType("update"); }}>Modificar saldo</button>
                        <button className="btn btn-danger" onClick={handleDeleteBalance}>Eliminar saldo</button>
                    </>
                )}
            </div>
            {showForm && (
                <div className="form-container mb-4">
                    <form onSubmit={formType === "add" ? handleAddBalance : handleUpdateBalance}>
                        <div className="mb-3">
                            <label className="form-label">Cantidad</label>
                            <input
                                type="number"
                                className="form-control"
                                value={inputData.amount}
                                onChange={(e) => setInputData({ ...inputData, amount: Number(e.target.value) })}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Fecha de Pago</label>
                            <input
                                type="date"
                                className="form-control"
                                value={inputData.payment_date.toISOString().split('T')[0]}
                                onChange={(e) => setInputData({ ...inputData, payment_date: new Date(e.target.value) })}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-success me-2">{formType === "add" ? "Añadir" : "Modificar"}</button>
                        <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancelar</button>
                    </form>
                </div>
            )}
            <button className="btn btn-secondary" onClick={() => navigate(-1)}>Volver</button>
        </div>
    );
};

export default ManageBalances;