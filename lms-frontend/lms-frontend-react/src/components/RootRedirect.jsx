import { Navigate } from "react-router-dom";
import { useUser } from "../hooks/useUser";

export default function RootRedirect() {
    const { user } = useUser();

    return user ? <Navigate to="/dashboard" replace /> : <Navigate to="/door-step" replace />
}