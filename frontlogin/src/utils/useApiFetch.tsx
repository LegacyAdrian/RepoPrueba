import { useState, useEffect } from "react";

const useApiFetch = (url:string, options = {}) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await fetch(url, options);
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                const result = await response.json();
                setData(result);
            } catch (err) {
                // @ts-ignore
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [url, JSON.stringify(options)]);

    return { data, loading, error };
};

export default useApiFetch;