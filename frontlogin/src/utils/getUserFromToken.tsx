import {jwtDecode} from "jwt-decode";

const getUserFromToken = (): string | null => {
    const token = localStorage.getItem("jwtToken");
    if (!token) return null;

    try {
        const decoded: any = jwtDecode(token);
        return decoded.sub; // "sub" es el nombre de usuario en el payload
    } catch (error) {
        console.error("Error al decodificar el token:", error);
        return null;
    }
};

export default getUserFromToken;