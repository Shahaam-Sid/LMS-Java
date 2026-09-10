// components/ProtectedRoute.jsx
import { Navigate } from "react-router-dom";
import { useUser } from "../hooks/useUser";

export default function ProtectedRoute({ children }) {
    const { user } = useUser();
    return user ? children : <Navigate to="/login" replace />;
}