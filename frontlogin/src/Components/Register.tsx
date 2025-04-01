import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
// @ts-ignore
import '../css/Estilos.css';
import {User} from "../models/User.ts";
import * as React from "react";



const Register = () => {
    const [inputData, setInputData] = useState<User>({
        name : "",
        password: ""
    });

    const navigate = useNavigate()

    const handleRegister = async (e: React.FormEvent):Promise<void> => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify( inputData),
            });

            if (!response.ok) {
                throw new Error('Dato Erroneo');
            }

            alert('Registro exitoso');
            navigate("/login")

        } catch (error) {
            console.error('Error de autenticación', error);
        }
    };
    const onHandle = (e :  React.ChangeEvent<HTMLInputElement>)=> {
        const {name, value} = e.target;
        setInputData({
            ...inputData,
            [name]: value
        });
    }
    useEffect(() => {
        console.log(inputData)
    }, [inputData])

    return (

        <div className="cartel">
            <form onSubmit={handleRegister}>
                <h1>Sign In</h1>
                <label>Username</label>
                <input
                    name="name"
                    value={inputData.name}
                    type="text"
                    onChange={(event) : void => onHandle(event)}
                />
                <label>Password</label>
                <input
                    name="password"
                    value={inputData.password}
                    type="password"
                    onChange={(event):void => onHandle(event)}
                />
                <button type="submit">Sign In</button>
            </form>
        </div>

    );
};

export default Register;
