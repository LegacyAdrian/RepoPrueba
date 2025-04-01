import { useState } from "react";
import useApiFetch from "../../utils/useApiFetch";
import { User } from "../../models/User";
import 'bootstrap/dist/css/bootstrap.min.css';

const PayComponent: React.FC = () => {
    const [amount, setAmount] = useState<number>(0);
    const [selectedUser, setSelectedUser] = useState<string>("");

    const { data, loading, error } = useApiFetch<User[]>(
        `http://localhost:8080/bank/users`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
            },
        }
    );

    if (loading) return <p>Cargando...</p>;
    if (error) return <p>Error: {error}</p>;

    const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAmount(Number(e.target.value));
    };

    const handleUserChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedUser(e.target.value);
    };

    const handlePayTransaction = async () => {
        try {
            const response = await fetch(`http://localhost:8080/bank/user/pay?payerId=${localStorage.getItem("username")}&receiverName=${selectedUser}&amount=${amount}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
                },
            });

            if (!response.ok) {
                throw new Error('Error en la transacción');
            }

            alert("Transacción realizada con éxito");
        } catch (error) {
            console.error('Error en la transacción', error);
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="mb-4">Hacer Transferencia</h1>
            <div className="card">
                <div className="card-body">
                    <div className="mb-3">
                        <label htmlFor="amount" className="form-label">Cantidad:</label>
                        <input
                            type="number"
                            id="amount"
                            className="form-control"
                            value={amount}
                            onChange={handleAmountChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="user" className="form-label">Usuario:</label>
                        <select id="user" className="form-select" value={selectedUser} onChange={handleUserChange}>
                            <option value="">Seleccione un usuario</option>
                            {data && data.map((user: User) => (
                                <option key={user.id} value={user.name}>
                                    {user.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <button onClick={handlePayTransaction} className="btn btn-primary">Realizar Transacción</button>
                </div>
            </div>
        </div>
    );
};

export default PayComponent;