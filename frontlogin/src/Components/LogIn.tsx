import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
// @ts-ignore
import '../css/Estilos.css';
import * as React from "react";
import {User} from "../models/User.ts";


const Login:React.FC = () => {

    const [inputData, setInputData] = useState<User>({
        name : "",
        password: ""
    });
    const navigate = useNavigate()

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(inputData),
            });

            if (!response.ok) {
                setInputData({
                    name: "",
                    password: ""
                })
                throw new Error('Credenciales inválidas');
            }

            const data = await response.json();
            localStorage.setItem('jwtToken', data.token);
            localStorage.setItem('username', inputData.name);
            alert('Login exitoso');
            navigate("/main")

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
            <h1>Log In</h1>
            <form onSubmit={handleLogin}>
                <label>Username</label>
                <input
                    name="name"
                    value={inputData.name}
                    type="text"
                    onChange={(event) => onHandle(event)}
                />
                <label>Password</label>
                <input
                    name="password"
                    value={inputData.password}
                    type="password"
                    onChange={(event) => onHandle(event)}
                />
                <button type="submit">Log In</button>
                <label onClick={()=>{navigate("/register")}}>If u dont have acount Sign in here!!</label>
            </form>
        </div>

    );
};

export default Login;
