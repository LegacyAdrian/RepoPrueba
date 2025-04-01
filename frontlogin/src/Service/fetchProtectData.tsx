export const fetchProtectedData = async (methodType: string, url: string): Promise<any> => {
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        console.log("No token found, please log in first.");
        return null;
    }

    try {
        const response = await fetch(url, {
            method: methodType,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            throw new Error('No autorizado o sesión expirada');
        }

        return await response.json();
    } catch (error) {
        console.error('Error al obtener datos protegidos', error);
        return null;
    }
};
