import { Navigate } from "react-router-dom";
import { useUser } from "../hooks/useUser";

export default function GuestOnlyRoute({ children }) {
    const { user } = useUser();
    return user ? <Navigate to="/dashboard" replace /> : children;
}