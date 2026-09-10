import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { useUser } from "../hooks/useUser"
import { setOnUnauhtorized } from "../api/axios"


export default function AuthInterceptorSetup() {
    const {logOut} = useUser();
    const navigate = useNavigate();

    useEffect(() => {
        setOnUnauhtorized(() => {
            logOut();
            navigate("/login", {replace: true});
        });
    }, [logOut, navigate]);
    return null;
}