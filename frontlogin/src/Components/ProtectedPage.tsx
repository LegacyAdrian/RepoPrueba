import {useEffect, useState} from "react";
import {fetchProtectedData} from "../Service/fetchProtectData.tsx";
import {useNavigate} from "react-router-dom";

const ProtectedPage = ()=>{
    const [data,setData] = useState<void>()
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(()=>{
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            navigate("/login");
            return;
        }
        const fetchData = async () => {
            try {
                const result = await fetchProtectedData();
                setData(result)
                setLoading(false)
            } catch (err : any) {
                setError(err.message)
                setLoading(false);
            }
        }
        fetchData();
    },[])
    if (loading) return <div>In Process...</div>
    if (error) return <div>Error : {error}</div>
    return(
        <div>
            <h1>Secure</h1>
            <p>Here is your Data</p>
            <pre>{JSON.stringify(data,null,2)}</pre>
        </div>
    )
}
export default ProtectedPage