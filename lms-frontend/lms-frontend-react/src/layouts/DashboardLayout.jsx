import { Outlet } from "react-router-dom";
import { useUser } from "../hooks/useUser";
import { useNavigate } from "react-router-dom";
import FloatingButton from "../components/FloatingButton";

export default function DashboardLayout() {

    const { logOut } = useUser();
    const navigate = useNavigate();

    function onClick() {
        logOut();
        navigate("/door-step", {replace: true});
    }

    return (
        <>
            <h1 className="m-4 font-montserrat text-3xl">Dashboard</h1>
            <Outlet />
            <FloatingButton text="Logout" onClick={onClick}/>
        </>
    );
}